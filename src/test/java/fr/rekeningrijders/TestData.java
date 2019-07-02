package fr.rekeningrijders;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.collect.Lists;
import com.owlike.genson.Genson;

import fr.rekeningrijders.doa.context.IInvoiceStorage;
import fr.rekeningrijders.doa.context.IOwnerStorage;
import fr.rekeningrijders.doa.context.ITrackerStrorage;
import fr.rekeningrijders.doa.context.IVehicleStorage;
import fr.rekeningrijders.doa.jpacontext.JPAInvoiceContext;
import fr.rekeningrijders.doa.jpacontext.JPAOwnerContext;
import fr.rekeningrijders.doa.jpacontext.JPATrackerContext;
import fr.rekeningrijders.doa.jpacontext.JPAVehicleContext;
import fr.rekeningrijders.models.pojo.*;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.core.MediaType;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TestData
{

    private static final Owner[] owners = {
        new Owner("Harrie", "lastnaem","bsn", "Ergens", "Asten City", "3498FD", "6b0d1a86-65d8-4eb3-a106-721bb7583124"),
        new Owner("Gerrie","lastname", "bsn", "Nergens", "Helmond", "3461SB", "05afc6e1-d469-42d8-af00-856630f16ce2"),
    };
    private static final Tracker[] trackers = {
        new Tracker("FR_029c80b3-c99f-3ced-806c-effe3f341b12"),
        new Tracker("FR_000c843e-dd32-3cd5-8082-2233a67eabd8"),
        new Tracker("NL_994"),
    };
    private static final Vehicle[] vehicles = {
        new Vehicle("BWM", "Dikke", "ABCD", (short)2000, trackers[0], owners[0]),
        new Vehicle("Tesla", "S", "EFGH", (short)2004, trackers[1], owners[1]),
    };
    private static final Movement[] movements = {
        new Movement(1, 63.3696,  50.78657,   new Date(1546297200001L), trackers[0].getTrackerId()),
        new Movement(2, -6.77251, -133.9815,  new Date(1546297200010L), trackers[0].getTrackerId()),
        new Movement(3, 10.73449, -44.33012,  new Date(1546297200020L), trackers[0].getTrackerId()),
        new Movement(4, 14.23429, 131.78769,  new Date(1546297200043L), trackers[1].getTrackerId()),
        new Movement(5, 58.64456, -164.52293, new Date(1546297200054L), trackers[1].getTrackerId()),
        new Movement(6, 14.23429, 131.78769,  new Date(1546297200043L), trackers[2].getTrackerId()),
        new Movement(7, 58.64456, -164.52293, new Date(1546297200054L), trackers[2].getTrackerId()),
    };
    private static final Invoice[] invoices = {
        new Invoice("3333e671-d6cd-488b-b9aa-034c3fdb5247", 1, (short)2019, Month.JANUARY, 0.0, Lists.newArrayList(new InvoiceVehicle(vehicles[0]))),
    };

    private static WireMockServer wireMock;
    private static EntityManager entityManager;

    private static IInvoiceStorage invoiceStorage;
    private static IOwnerStorage ownerStorage;
    private static IVehicleStorage vehicleStorage;
    private static ITrackerStrorage trackerStorage;
    
    public static Invoice[] getInvoices() { return invoices; }
    public static IInvoiceStorage getInvoiceStorage() { return invoiceStorage; }
    public static IOwnerStorage getOwnerStorage() { return ownerStorage; }
    public static IVehicleStorage getVehicleStorage() { return vehicleStorage; }
    public static ITrackerStrorage getTrackerStorage() { return trackerStorage; }

    public static void setupEntityManager()
    {
        if (entityManager != null) {
            // Already created
            return;
        }

        entityManager = Persistence
            .createEntityManagerFactory("RekAdmPUTest")
            .createEntityManager();
        
        invoiceStorage = new JPAInvoiceContext(entityManager);
        ownerStorage = new JPAOwnerContext(entityManager);
        vehicleStorage = new JPAVehicleContext(entityManager);
        trackerStorage = new JPATrackerContext(entityManager);

        begin();
        for (Owner owner : owners) {
            ownerStorage.create(owner);
        }
        for (Tracker tracker : trackers) {
            trackerStorage.create(tracker);
        }
        for (Vehicle vehicle : vehicles) {
            vehicleStorage.create(vehicle);
        }
        for (Invoice invoice : invoices) {
            invoiceStorage.create(invoice);
        }
        commit();
    }

    public static void setupWiremock()
    {
        if (wireMock != null) {
            // Wiremock already created
            return;
        }
        wireMock = new WireMockServer(8089);
        
        Movement[] trackerMoves = Arrays.stream(movements)
            .filter(m -> m.getTrackerId().equals(trackers[0].getTrackerId()))
            .toArray(Movement[]::new);

        wireMock.stubFor(makeStub("/movements", movements, 0));
        wireMock.stubFor(makeStub("/movements", new Movement[0], 1));
        wireMock.stubFor(makeStub("/tracker/[^/]+/movements", trackerMoves, 0));
        wireMock.stubFor(makeStub("/tracker/[^/]+/movements", new Movement[0], 1));
        wireMock.start();
    }

    static MappingBuilder makeStub(String url, Movement[] response, int page)
    {
        Genson genson = new Genson();
        return WireMock.get(urlPathMatching(url))
            .withQueryParam("page", WireMock.matching(String.valueOf(page)))
            .willReturn(WireMock.aResponse()
                .withBody(genson.serialize(response))
                .withHeader("Content-Type", MediaType.APPLICATION_JSON));
    }

    public static void begin()
    {
        entityManager.getTransaction().begin();
    }

    public static void commit()
    {
        entityManager.getTransaction().commit();
    }
}
