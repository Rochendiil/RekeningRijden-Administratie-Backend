package fr.rekeningrijders.doa.jpacontext;

import fr.rekeningrijders.doa.context.IRatesStorage;
import fr.rekeningrijders.models.pojo.Rate;
import fr.rekeningrijders.models.pojo.Zone;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Stateless
@NoArgsConstructor
public class JPARatesContext implements IRatesStorage
{
    @PersistenceContext(unitName = "RekAdmPU")
    private EntityManager em;

    @Override
    public List<Zone> readZones()
    {
        return Collections.unmodifiableList(em.createQuery("SELECT z FROM Zone z", Zone.class).getResultList());
    }

    @Override
    public void writeZone(Zone zone)
    {
        em.persist(zone);
    }

    @Override
    public Zone readZone(long id)
    {
        return em.find(Zone.class, id);
    }

    @Override
    public Rate readRate(long id) {
        return em.find(Rate.class, id);
    }

    @Override
    public void updateZone(Zone zone)
    {
        em.persist(zone);
    }

    @Override
    public List<Rate> getCustomRates(long id)
    {
        return em.createQuery("SELECT r FROM Rate r WHERE r.zone.id = :id", Rate.class)
            .setParameter("id", id)
            .getResultList();
    }

    @Override
    public void writeCustomRate(Rate rate)
    {
        em.persist(rate);
    }

    @Override
    public Zone getZonebyName(String zoneName)
    {
        List<Zone> zone = em.createQuery("SELECT z from Zone z WHERE z.name = :name", Zone.class)
            .setParameter("name", zoneName)
            .setMaxResults(1)
            .getResultList();
        return !zone.isEmpty() ? zone.get(0) : null;
    }

    @Override
    public void removeCustomRate(Rate rate) {
        em.createQuery("DELETE FROM Rate r WHERE r.id = :id").setParameter("id", rate.getId()).executeUpdate();
    }
}
