package fr.rekeningrijders.doa.jpacontext;

import fr.rekeningrijders.doa.context.IOwnerStorage;
import fr.rekeningrijders.models.pojo.Owner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JPAOwnerContextTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("RekAdmPUTest");
    private EntityManager entityManager;
    private EntityTransaction transaction;
    private IOwnerStorage ownerStorage;

    @Before
    public void before() {
        entityManager = emf.createEntityManager();
        ownerStorage = new JPAOwnerContext(entityManager);
        transaction = entityManager.getTransaction();
    }

    @After
    public void after(){

        //entityManager.close();
    }

    @Test
    public void read() {
    }

    @Test
    public void create() {
        Owner owner = new Owner("firsttest", "lastnametest", "bsn","adrresstest", "citytest", "zipcode", "b06faf2f-f3a4-465f-9618-ecb616aeee78");
        transaction.begin();
        ownerStorage.create(owner);
        transaction.commit();
        assertNotNull(owner.getId());
    }

    @Test
    public void update() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void findPartialName() {
        Owner owner = new Owner("firsttest", "lastnametest", "bsn","adrresstest", "citytest", "zipcode", "dd253c3f-9eb1-427d-875f-c8430449b088");
        transaction.begin();
        ownerStorage.create(owner);
        transaction.commit();
        assertNotNull(owner.getId());
        transaction.begin();
        List<Owner> found = ownerStorage.findPartialName("last");
        transaction.commit();
        assertEquals(1 ,found.size());
        transaction.begin();
        found = ownerStorage.findPartialName("lastnametest, firsttest");
        transaction.commit();
        assertEquals(1 ,found.size());
    }
}
