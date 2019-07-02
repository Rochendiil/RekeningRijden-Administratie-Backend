package fr.rekeningrijders.doa.jpacontext;

import fr.rekeningrijders.Pair;
import fr.rekeningrijders.doa.context.IInvoiceStorage;
import fr.rekeningrijders.models.pojo.Invoice;
import fr.rekeningrijders.models.pojo.InvoiceTrackerTotal;
import fr.rekeningrijders.models.pojo.Ride;
import fr.rekeningrijders.models.pojo.Vehicle;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * @author piet
 */
@Default
@Stateless
@NoArgsConstructor
public class JPAInvoiceContext implements IInvoiceStorage
{
    private static final String MONTH = "month";
    private static final String YEAR = "year";

    @PersistenceContext(unitName = "RekAdmPU")
    private EntityManager em;

    public JPAInvoiceContext(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Invoice read(long id)
    {
        return em.find(Invoice.class, id);
    }
    
    @Override
    public void create(Invoice invoice)
    {
        em.persist(invoice);
    }

    @Override
    public void update(Invoice invoice)
    {
        em.merge(invoice);
    }

    @Override
    public void storeTrackerBill(InvoiceTrackerTotal trackerBill)
    {
        em.persist(trackerBill);
    }

    @Override
    public Invoice getByUuid(String uuid)
    {
        try {
            return em.createQuery("SELECT i FROM Invoice i WHERE i.uuid=:uuid", Invoice.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Invoice> getByOwner(String uuid)
    {
        return em.createQuery(
            "SELECT i FROM Invoice i " +
            "JOIN Owner o ON o.id = i.ownerId " +
            "WHERE o.uuid = :owner", Invoice.class)
            .setParameter("owner", uuid)
            .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Vehicle, Double>> getVehicleBillsOwnerGrouped(short year, Month month)
    {
        return toBills(em.createQuery(
            "SELECT v, b.total FROM InvoiceTrackerTotal b, Vehicle v " +
            "WHERE v.tracker.trackerId=b.trackerId AND b.month=:month AND b.year=:year " +
            "ORDER BY v.owner.id")
            .setParameter(MONTH, month)
            .setParameter(YEAR, year)
            .getResultList());
    }

    @Override
    public List<InvoiceTrackerTotal> getForeingTrackerTotals(short year, Month month)
    {
        return em.createQuery(
            "SELECT b FROM InvoiceTrackerTotal b " +
            "WHERE b.month=:month AND b.year=:year AND " +
            "b.trackerId NOT LIKE 'FR_%'", InvoiceTrackerTotal.class)
            .setParameter(YEAR, year)
            .setParameter(MONTH, month)
            .getResultList();
    }

    private List<Pair<Vehicle, Double>> toBills(List<Object[]> list)
    {
        List<Pair<Vehicle, Double>> result = new ArrayList<>(list.size()); 
        for (Object[] row : list) {
            result.add(new Pair<>((Vehicle)row[0], (Double)row[1]));
        }
        return result;
    }

    @Override
    public List<Invoice> getAll(short year, Month month)
    {
        return em.createQuery("SELECT i FROM Invoice i WHERE i.year=:year AND i.month=:month", Invoice.class)
            .setParameter(MONTH, month)
            .setParameter(YEAR, year)
            .getResultList();
    }

    @Override
    public void createRide(Ride ride)
    {
        em.persist(ride);
    }

    @Override
    public List<Ride> getRides(String trackerId, short year, Month month)
    {
        return em.createQuery("SELECT r FROM Ride r WHERE r.trackerId=:tracker AND r.year=:year AND r.month=:month", Ride.class)
            .setParameter("tracker", trackerId)
            .setParameter(YEAR, year)
            .setParameter(MONTH, month)
            .getResultList();
    }
}
