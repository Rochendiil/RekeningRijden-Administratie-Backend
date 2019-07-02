package fr.rekeningrijders.doa.jpacontext;

import fr.rekeningrijders.doa.context.ITrackerStrorage;
import fr.rekeningrijders.models.pojo.Tracker;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JPATrackerContextTest
{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("RekAdmPUTest");
    private EntityManager entityManager;
    private EntityTransaction transaction;

    private ITrackerStrorage trackerStrorage;

    @Before
    public void before() {
        entityManager = emf.createEntityManager();
        trackerStrorage = new JPATrackerContext(entityManager);
        transaction = entityManager.getTransaction();
    }

    @After
    public void after(){

        //entityManager.close();
    }
    @Test
    public void create() {
        Tracker tracker = new Tracker("testtracker");
        transaction.begin();
        trackerStrorage.create(tracker);
        transaction.commit();
        Assert.assertNotNull(tracker.getTrackerId());
    }

    @Test
    public void read() {
        Tracker tracker = new Tracker("testtracker");
        transaction.begin();
        trackerStrorage.create(tracker);
        transaction.commit();
        Assert.assertNotNull(tracker.getTrackerId());
        transaction.begin();
        Tracker found = trackerStrorage.read(tracker.getTrackerId());
        transaction.commit();
        Assert.assertEquals(tracker, found);
    }

    @Test
    public void update() {
        Tracker tracker = new Tracker("testtracker");
        transaction.begin();
        trackerStrorage.create(tracker);
        transaction.commit();
        Assert.assertNotNull(tracker.getTrackerId());
        transaction.begin();
        Tracker found = trackerStrorage.read(tracker.getTrackerId());
        transaction.commit();
        Assert.assertTrue(found.isActive());
        tracker.setActive(false);
        transaction.begin();
        trackerStrorage.update(tracker);
        transaction.commit();
        transaction.begin();
        found = trackerStrorage.read(tracker.getTrackerId());
        transaction.commit();
        Assert.assertFalse(found.isActive());
    }

    @Test
    public void getAll() {
        for(int i = 0 ; i < 20 ;i++){
            Tracker tracker = new Tracker("testtracker" + i);
            transaction.begin();
            trackerStrorage.create(tracker);
            transaction.commit();
        }
        transaction.begin();
        List<Tracker> trackerList = trackerStrorage.getAll();
        transaction.commit();
        Assert.assertEquals(20, trackerList.size());
    }
}
