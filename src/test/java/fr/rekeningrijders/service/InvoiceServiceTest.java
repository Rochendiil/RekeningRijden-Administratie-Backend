package fr.rekeningrijders.service;

import fr.rekeningrijders.TestData;
import fr.rekeningrijders.models.pojo.Invoice;
import fr.rekeningrijders.models.pojo.Movement;
import fr.rekeningrijders.models.pojo.Rate;
import fr.rekeningrijders.rest.dto.eu.EuInvoice;
import fr.rekeningrijders.system.Europe;
import fr.rekeningrijders.system.Rekenregistratie;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class InvoiceServiceTest
{
    private static InvoiceService invoiceService;
    
    private short year = (short)2019;
    private Month month = Month.JANUARY;

    @BeforeClass
    public static void setUp() throws Exception
    {
        Rekenregistratie.enableDebug();
        TestData.setupEntityManager();
        TestData.setupWiremock();
        
        Rekenregistratie rekenRegistratie = new Rekenregistratie();
        ReverseGeoService reverseGeoService = new ReverseGeoService()
        {
            @Override
            public String getProvinceOf(Movement movement)
            {
                return "";
            }
        };

        RateService rateService = new RateService()
        {
            @Override
            public Rate getCurrentRate(String zoneName, LocalTime localTime)
            {
                return new Rate(.3);
            }
        };
        Europe europe = new Europe()
        {
            @Override
            protected void putInvoice(EuInvoice invoice, String url)
            {
                System.out.println("Sending invoice to: "+url);
            }
        };

        invoiceService = new InvoiceService(TestData.getInvoiceStorage(), rekenRegistratie, reverseGeoService, rateService, europe);   
    }
    
    @Test
    public void testGenerateInvoices() throws Exception
    {
        TestData.begin();
        invoiceService.generateInvoices(year, month);
        List<Invoice> invoices = invoiceService.getInvoices(year, month);
        TestData.commit();
        assertTrue("Invoices not generated", invoices.size() >= 2);
    }

    @Test
    public void testRegenerateInvoices() throws Exception
    {
        Invoice original = TestData.getInvoices()[0];
        double originalTotal = original.getTotalAmount();

        TestData.begin();
        Invoice result = invoiceService.regenerateInvoice(original.getId());
        TestData.commit();
        assertNotEquals(result.getTotalAmount(), originalTotal);
    }
}
