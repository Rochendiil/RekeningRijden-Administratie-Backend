package fr.rekeningrijders.doa.jpacontext;

import fr.rekeningrijders.doa.context.ITrackerStrorage;
import fr.rekeningrijders.models.pojo.Tracker;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Default
@Stateless
@NoArgsConstructor
public class JPATrackerContext implements ITrackerStrorage
{
    @PersistenceContext(unitName = "RekAdmPU")
    private EntityManager entityManager;

    public JPATrackerContext(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(Tracker tracker) {
        entityManager.persist(tracker);
    }

    @Override
    public Tracker read(String id) {
        return entityManager.find(Tracker.class,id);
    }

    @Override
    public void update(Tracker tracker) {
        entityManager.merge(tracker);
    }

    @Override
    public List<Tracker> getAll()
    {
        return entityManager
            .createQuery("SELECT t FROM Tracker t", Tracker.class)
            .getResultList();
    }

    @Override
    public List<Tracker> byOwner(long ownerId)
    {
        return entityManager
            .createQuery("SELECT v.tracker FROM Vehicle v WHERE v.owner.id = :id", Tracker.class)
            .setParameter("id", ownerId)
            .getResultList();
    }

    @Override
    public List<Tracker> getFrenchCars(int limit)
    {
        return entityManager
            .createQuery("SELECT t FROM Tracker t WHERE t.trackerId LIKE 'FR_%'", Tracker.class)
            .setMaxResults(limit)
            .getResultList();
    }

    @Override
    public byte getFuelTypeByTracker(String trackerId) {
        return entityManager.createQuery("SELECT v.fuelType FROM Vehicle v WHERE v.tracker.trackerId = :trackerId", Byte.class)
                .setParameter("trackerId", trackerId)
                .getSingleResult();
    }
}
