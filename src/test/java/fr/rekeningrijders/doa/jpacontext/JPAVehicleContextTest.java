package fr.rekeningrijders.doa.jpacontext;

import fr.rekeningrijders.doa.context.IVehicleStorage;
import fr.rekeningrijders.models.pojo.Owner;
import fr.rekeningrijders.models.pojo.Tracker;
import fr.rekeningrijders.models.pojo.Vehicle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class JPAVehicleContextTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("RekAdmPUTest");
    private EntityManager entityManager;
    private EntityTransaction transaction;

    private IVehicleStorage vehicleStorage;
    
    private Owner user;
    private Tracker tracker;

    @Before
    public void before()
    {
        entityManager = emf.createEntityManager();
        vehicleStorage = new JPAVehicleContext(entityManager);
        
        user = new Owner("firsttest", "lastnametest", "bsn","adrresstest", "citytest", "zipcode", "a97ff690-41cc-4b49-af8d-27996f496a6a");
        tracker = new Tracker("FR_ASjkdnasjkfdsdkf");
        
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        entityManager.persist(tracker);
        transaction.commit();
    }
    
    @After
    public void after()
    {
        //entityManager.close();
    }

    @Test
    public void read()
    {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle("BMW", "A3", "LicenseTest", (short) 1992, tracker, user));
        transaction.begin();
        vehicleStorage.create(vehicles.get(0));
        transaction.commit();
        transaction.begin();
        Vehicle found = vehicleStorage.find(vehicles.get(0).getId());
        transaction.commit();
        Assert.assertEquals(vehicles.get(0), found);
    }

    @Test
    public void create()
    {
        Vehicle vehicle = new Vehicle("BMW", "A3", "LicenseTest", (short) 1992, tracker, user);
        transaction.begin();
        vehicleStorage.create(vehicle);
        transaction.commit();
        Assert.assertNotNull(vehicle.getId());
    }

    @Test
    public void update()
    {
        Vehicle vehicle = new Vehicle("BMW", "A3", "LicenseTest", (short) 1992, tracker, user);
        transaction.begin();
        vehicleStorage.create(vehicle);
        transaction.commit();
        transaction.begin();
        Vehicle found = vehicleStorage.find(vehicle.getId());
        transaction.commit();
        Assert.assertEquals(vehicle, found);
        transaction.begin();
        vehicle.setBuildYear((short) 1990);
        vehicleStorage.update(vehicle);
        transaction.commit();
        transaction.begin();
        found = vehicleStorage.find(vehicle.getId());
        transaction.commit();
        Assert.assertEquals((short) 1990,found.getBuildYear());
    }

    @Test
    public void getAll()
    {
        for(int i =0 ; i < 10 ; i++)
        {
            Vehicle vehicle = new Vehicle("BMW" + i, "A3", "LicenseTest", (short)1992, tracker, user);
            transaction.begin();
            vehicleStorage.create(vehicle);
            transaction.commit();
        }
        transaction.begin();
        List<Vehicle> vehicles = vehicleStorage.getAll();
        transaction.commit();
        Assert.assertEquals(10, vehicles.size());
    }

    @Test
    public void byOwner()
    {
        for(int i = 0; i < 3; i++)
        {
            Vehicle vehicle = new Vehicle("BMW" + i, "A3", "ByOwnerTest", (short)1992, tracker, user);
            transaction.begin();
            vehicleStorage.create(vehicle);
            transaction.commit();
        }
        transaction.begin();
        List<Vehicle> vehicles = vehicleStorage.byOwner(user.getId());
        transaction.commit();
        Assert.assertEquals(3, vehicles.size());
    }
}
