package fr.rekeningrijders.doa.jpacontext;

import fr.rekeningrijders.doa.context.IVehicleStorage;
import fr.rekeningrijders.models.pojo.Vehicle;
import lombok.var;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Default
@Stateless
public class JPAVehicleContext implements IVehicleStorage {

    @PersistenceContext(unitName = "RekAdmPU")
    private EntityManager em;

    public JPAVehicleContext() {
    }

    public JPAVehicleContext(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(Vehicle vehicle) {
        em.persist(vehicle);
    }

    @Override
    public Vehicle find(long id) {
        return em.find(Vehicle.class, id);
    }

    @Override
    public void update(Vehicle vehicle) {
        em.merge(vehicle);
    }

    @Override
    public List<Vehicle> getAll() {
        return em
                .createQuery("SELECT v FROM Vehicle v ", Vehicle.class)
                .getResultList();
    }

    @Override
    public List<Vehicle> byOwner(long ownerId) {
        return Collections.unmodifiableList(
                em.createQuery("SELECT v FROM Vehicle v WHERE v.owner.id=:id", Vehicle.class)
                        .setParameter("id", ownerId)
                        .getResultList());
    }

    @Override
    public Vehicle findByTrackerId(String trackerId)
    {
        var result = em.createQuery("SELECT V FROM Vehicle V WHERE v.tracker.trackerId = :trackerId", Vehicle.class)
            .setParameter("trackerId", trackerId)
            .setMaxResults(1)
            .getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }

    @Override
    public Vehicle findVehicleByLicense(String license)
    {
        var result = em.createQuery("SELECT V FROM Vehicle V WHERE v.licenseNumber = :license", Vehicle.class)
            .setParameter("license", license)
            .setMaxResults(1)
            .getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }
}
