package fr.rekeningrijders.doa.context;

import fr.rekeningrijders.Pair;
import fr.rekeningrijders.models.pojo.Invoice;
import fr.rekeningrijders.models.pojo.InvoiceTrackerTotal;
import fr.rekeningrijders.models.pojo.Ride;
import fr.rekeningrijders.models.pojo.Vehicle;

import java.time.Month;
import java.util.List;

/**
 *
 * @author piet
 */
public interface IInvoiceStorage
{
    Invoice read(long id);
    void create(Invoice invoice);
    void update(Invoice invoice);
    List<Invoice> getAll(short year, Month month);
    Invoice getByUuid(String uuid);

    List<Invoice> getByOwner(String uuid);
    
    void storeTrackerBill(InvoiceTrackerTotal trackerBill);
    List<Pair<Vehicle, Double>> getVehicleBillsOwnerGrouped(short year, Month month);
    List<InvoiceTrackerTotal> getForeingTrackerTotals(short year, Month month);

    void createRide(Ride ride);
    List<Ride> getRides(String trackerId, short year, Month month);
}
