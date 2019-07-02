package fr.rekeningrijders.doa.jpacontext;

import fr.rekeningrijders.doa.context.IOwnerStorage;
import fr.rekeningrijders.models.pojo.Owner;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Default
@Stateless
public class JPAOwnerContext implements IOwnerStorage {

    @PersistenceContext(unitName = "RekAdmPU")
    private EntityManager em;

    public JPAOwnerContext() {
    }

    public JPAOwnerContext(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Owner read(long id) {
        return em.find(Owner.class,id);
    }

    @Override
    public void create(Owner owner) {
        em.persist(owner);
    }

    @Override
    public void update(Owner owner) {
        em.merge(owner);
    }

    @Override
    public List<Owner> getAll() {
        return em.createQuery("SELECT o FROM Owner o", Owner.class)
            .getResultList();
    }

    @Override
    public List<Owner> findPartialName(String partialName) {
        TypedQuery<Owner> query = em.createQuery("SELECT o FROM Owner o WHERE concat(o.lastname, ', ' , o.firstname) LIKE :partialname", Owner.class)
            .setParameter("partialname", "%" + partialName + "%");
        return Collections.unmodifiableList( query.getResultList());
    }

    @Override
    public Owner getByBsn(String bsn)
    {
        try {
            return em.createQuery("SELECT o FROM Owner o WHERE o.bsn = :bsn", Owner.class)
                .setParameter("bsn", bsn)
                .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Owner getByUuid(String uuid)
    {
        try {
            return em.createQuery("SELECT o FROM Owner o WHERE o.uuid = :uuid", Owner.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
