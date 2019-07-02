package fr.rekeningrijders.service;

import fr.rekeningrijders.DateUtil;
import fr.rekeningrijders.DateUtil.DateRange;
import fr.rekeningrijders.ICallback2;
import fr.rekeningrijders.IExtract;
import fr.rekeningrijders.Pair;
import fr.rekeningrijders.doa.context.IInvoiceStorage;
import fr.rekeningrijders.doa.context.IVehicleStorage;
import fr.rekeningrijders.models.pojo.*;
import fr.rekeningrijders.system.Europe;
import fr.rekeningrijders.system.Rekenregistratie;
import fr.rekeningrijders.rest.dto.eu.EuInvoice;
import fr.rekeningrijders.rest.dto.eu.EuMovement;
import fr.rekeningrijders.rest.dto.eu.EuStep;
import lombok.NoArgsConstructor;
import lombok.var;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.hibernate.cfg.NotYetImplementedException;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

/**
 *
 * @author piet
 */
@Stateless
@NoArgsConstructor
public class InvoiceService
{
    @Inject
    private IInvoiceStorage invoiceStorage;
    
    @Inject
    private IVehicleStorage vehicleStorage;

    @Inject
    private Rekenregistratie rekenRegistratie;

    @Inject
    private ReverseGeoService reverseGeoService;

    @Inject
    private RateService rateService;

    @Inject
    private Europe europe;

    private Logger logger = Logger.getLogger(InvoiceService.class);

    public InvoiceService(
        IInvoiceStorage invoiceStorage,
        Rekenregistratie rekenRegistratie,
        ReverseGeoService reverseGeoService,
        RateService rateService,
        Europe europe)
    {
        this.invoiceStorage = invoiceStorage;
        this.rekenRegistratie = rekenRegistratie;
        this.reverseGeoService = reverseGeoService;
        this.rateService = rateService;
        this.europe = europe;
    }

    public void create(Invoice invoice)
    {
        invoiceStorage.create(invoice);
    }

    public Invoice getInvoice(long id)
    {
        return invoiceStorage.read(id);
    }

    public List<Invoice> getInvoices(short year, Month month)
    {
        return invoiceStorage.getAll(year, month);
    }

    public List<Invoice> getByOwner(String uuid)
    {
        return invoiceStorage.getByOwner(uuid);
    }

    public Invoice regenerateInvoice(long id)
    {
        // No need for an intermediary database step
        final boolean useDB = false;

        var rs = rateService;
        var rgs = reverseGeoService;
        var rr = rekenRegistratie;

        Invoice invoice = invoiceStorage.read(id);
        short year = invoice.getYear();
        Month month = invoice.getMonth();

        DateRange range = DateUtil.getStartEndDates(year, month);
        
        var bills = new ArrayList<Pair<Vehicle, Double>>();
        var vehicles = invoice.getVehicles();

        for (InvoiceVehicle vehicle : vehicles) {
            String trackerId = vehicle.getTrackerId();
            Iterable<Movement> movements = rr.getMovements(trackerId, range.start, range.end);
            InvoiceTrackerTotal total = generateTrackerTotal(year, month, trackerId, movements, useDB, rs, rgs, invoiceStorage);
            bills.add(new Pair<>(vehicle.getVehicle(), total.getTotal()));
        }

        Invoice newInvoice = generateInvoice(year, month, invoice.getOwnerId(), bills, useDB);

        invoice.update(newInvoice);
        invoiceStorage.update(invoice);
        return invoice;
    }

    public void generateInvoices(short year, Month month)
    {
        // Because there might be thousands of movements the total amount per vehicle is written to the
        // database as an intemediary step.
        final boolean useDB = true;

        var rs = rateService;
        var rgs = reverseGeoService;
        var rr = rekenRegistratie;

        DateRange range = DateUtil.getStartEndDates(year, month);
        
        // Create the total bill for all the movements a SINGLE trackerId (Vehicle) made in the month.
        var allMovements = rr.getMovementsTrackerGrouped(range.start, range.end);
        groupedExec(
            allMovements,
            Movement::getTrackerId,
            (trackerId, movements) -> generateTrackerTotal(year, month, trackerId, movements, useDB, rs, rgs, invoiceStorage));
            
        // Create an invoice for each owner based on all their vehicle bills in the month.
        var allBills = invoiceStorage.getVehicleBillsOwnerGrouped(year, month);
        groupedExec(
            allBills,
            b -> b.getItem1().getOwner().getId(),
            (ownerId, vehicles) -> generateInvoice(year, month, ownerId, vehicles, useDB));
        
        var foreignTotals = invoiceStorage.getForeingTrackerTotals(year, month);
        for (InvoiceTrackerTotal total : foreignTotals) {
            var movements = rr.getMovements(total.getTrackerId(), range.start, range.end);
            var luukMovement = new EuMovement(EuStep.transform(movements), total.getTotal());
            EuInvoice euInvoice = new EuInvoice(total.getTrackerId(), Arrays.asList(luukMovement), total.getTotal(), new Date().getTime());
            europe.sendInvoice(euInvoice);
        }
    }
    
    // Sum the totals of all vehicles from a single owner.
    @Transactional
    private Invoice generateInvoice(short year, Month month, long ownerId, Iterable<Pair<Vehicle, Double>> vehicles, boolean store)
    {
        double invoiceTotal = 0;
        List<InvoiceVehicle> vehicleList = new ArrayList<>();
        for (Pair<Vehicle, Double> vehicle : vehicles) {
            invoiceTotal += vehicle.getItem2();
            vehicleList.add(new InvoiceVehicle(vehicle.getItem1()));
        }

        invoiceTotal = Math.round(invoiceTotal * 100.) / 100.;
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), ownerId, year, month, invoiceTotal, vehicleList);
        if (store) {
            invoiceStorage.create(invoice);
        }
        return invoice;
    }
    

    private static final long TEN_MINUTES = 10L * 60 * 1000;
    private static final double TEN_KILOMETER = 10.;

    // Generate the total amount for a single trackerId (Vehicle).
    @Transactional
    private InvoiceTrackerTotal generateTrackerTotal(
        short year, Month month, String trackerId,
        Iterable<Movement> movements, boolean store,
        RateService rs, ReverseGeoService rgs, IInvoiceStorage invoiceStorage)
    {
        Iterator<Movement> iter = movements.iterator();
        
        if (!iter.hasNext()) {
            // This shouldn't be possible
            throw new NotYetImplementedException();
        }
        
        // Only look up new rate if the distance to the ratePoint is greater than 1km.
        double vehicleTotal = 0;
        double totalRideDistance = 0;
        Movement prev = iter.next();
        Movement ratePoint = prev;
        Movement rideStart = prev;
        Rate rate = getRate(prev, rs, rgs);

        while (iter.hasNext())
        {
            Movement movement = iter.next();
            double pointDistance = ReverseGeoService.distanceBetween(ratePoint, movement);
            if (pointDistance > 1.0) {
                ratePoint = movement;
                rate = getRate(ratePoint, rs, rgs);
            }
            double distance = ReverseGeoService.distanceBetween(prev, movement);
            vehicleTotal += rate.getPrice() * distance;

            double rideDistance = ReverseGeoService.distanceBetween(rideStart, movement);
            totalRideDistance += rideDistance;
            long rideTime = movement.getTimestamp() - rideStart.getTimestamp();
            if (store && (rideDistance > TEN_KILOMETER || rideTime > TEN_MINUTES)) {
                Ride ride = new Ride(rideStart, prev, year, month, totalRideDistance);
                invoiceStorage.createRide(ride);
                totalRideDistance = 0;
                rideStart = movement;
            }

            prev = movement;
        }
        Ride ride = new Ride(rideStart, prev, year, month, totalRideDistance);
        invoiceStorage.createRide(ride);

        InvoiceTrackerTotal total = new InvoiceTrackerTotal(trackerId, year, month, vehicleTotal);
        if (store) {
            invoiceStorage.storeTrackerBill(total);
        }
        return total;
    }
    
    private Rate getRate(Movement movement, RateService rateService, ReverseGeoService reversGeoService)
    {
        String province = reversGeoService.getProvinceOf(movement);
        if (province == null) {
            logger.warn("No province found for movement");
            return new Rate(.1);
        }
        
        Date timestamp = new Date(movement.getTimestamp());
        LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
        Rate rate = rateService.getCurrentRate(province, dateTime.toLocalTime());

        if (rate == null) {
            logger.warnv("No rate found for province {0}", province);
            return new Rate(.1);
        }
        return rate;
    }

    public List<Ride> getRides(String trackerId, short year, Month month)
    {
        return invoiceStorage.getRides(trackerId, year, month);
    }
    
    // Runs the callback with all the items in a group based on the groupBy
    // Note: Assumes the items iterable is grouped (or sorted) by the property extracted with groupBy
    // Note: Assumes the callback consumes the iterables to the end
    private static<T1, T2> void groupedExec(Iterable<T1> items, IExtract<T1, T2> groupBy, ICallback2<T2, Iterable<T1>> callback)
    {
        Iterator<T1> iter = items.iterator();
        if (!iter.hasNext()) {
            return;
        }

        Cursor<T1, T2> cursor = new Cursor<>();
        cursor.next = iter.next();
        
        do
        {
            cursor.tag = groupBy.extract(cursor.next);
            
            callback.run(cursor.tag, () -> new Iterator<T1>()
            {
                @Override
                public boolean hasNext() {
                    return cursor.next != null
                        && cursor.tag.equals(groupBy.extract(cursor.next));
                }
                
                @Override
                public T1 next() {
                    T1 result = cursor.next;
                    cursor.next = iter.hasNext() ? iter.next() : null;
                    return result;
                }
            });
        } while (cursor.next != null);
    }

    static class Cursor<T1, T2>
    {
        private T1 next;
        private T2 tag;
    }

    public boolean createEUInvoice(EuInvoice euInvoice)
    {
        Vehicle vehicle = vehicleStorage.findByTrackerId(euInvoice.getTrackerId());
        if (vehicle == null) {
            return false;
        }
        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date(euInvoice.getBilledDate()));
        
        List<InvoiceVehicle> vehicleList = new ArrayList<>();
        vehicleList.add(new InvoiceVehicle(vehicle));
        Invoice invoice = new Invoice(vehicle.getOwner().getId(), (short)calender.get(Calendar.YEAR), Month.of(calender.get(Calendar.MONTH) +1), euInvoice.getTotalPrice(), false, vehicleList);
        create(invoice);
        return true;
    }
}
