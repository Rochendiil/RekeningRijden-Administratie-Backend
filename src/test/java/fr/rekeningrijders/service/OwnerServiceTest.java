package fr.rekeningrijders.service;

import fr.rekeningrijders.doa.context.IOwnerStorage;
import fr.rekeningrijders.doa.jpacontext.JPAOwnerContext;
import fr.rekeningrijders.models.pojo.Owner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OwnerServiceTest
{
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private OwnerService ownerService;

    @Before
    public void beforeEach()
    {
        entityManager = Persistence
            .createEntityManagerFactory("RekAdmPUTest")
            .createEntityManager();
        transaction = entityManager.getTransaction();
        IOwnerStorage context = new JPAOwnerContext(entityManager);
        ownerService = new OwnerService(context);
    }

    @Test
    public void createOwnerTest()
    {
        //long id, String firstname, String lastname, String address, String city, String zipcode
        transaction.begin();
        Owner owner = new Owner("firstnameTest", "lastnameTest", "bsn","addressTest", "cityTest","zipcodeTest", "d8846537-1e2e-44a1-9033-f0ac43892c48");
        ownerService.create(owner);
        transaction.commit();
        Assert.assertNotNull(owner.getId());
    }

    @Test
    public void updateOwnerTest()
    {
        transaction.begin();
        Owner owner = new Owner("firstnameTest", "lastnameTest","bsn" ,"addressTest", "cityTest","zipcodeTest", "3c621c73-e08f-4ba3-9555-e3040a616f1d");
        ownerService.create(owner);
        transaction.commit();
        Assert.assertNotNull(owner.getId());
        long id = owner.getId();
        owner.setFirstname("changedtest");
        owner.setZipcode("changedtest");
        transaction.begin();
        ownerService.update(owner);
        transaction.commit();
        transaction.begin();
        owner = ownerService.find(id);
        Assert.assertEquals("changedtest", owner.getFirstname());
        Assert.assertEquals("changedtest", owner.getZipcode());
    }

}
