package fr.rekeningrijders.arquillian;


import fr.rekeningrijders.doa.context.ITrackerStrorage;

import fr.rekeningrijders.models.pojo.Invoice;

import fr.rekeningrijders.models.pojo.LoginInfo;
import fr.rekeningrijders.rest.*;
import fr.rekeningrijders.rest.dto.RateDTO;
import fr.rekeningrijders.rest.dto.UserDTO;
import fr.rekeningrijders.rest.dto.ZoneDTO;

import fr.rekeningrijders.rest.dto.eu.EuInvoice;
import fr.rekeningrijders.rest.dto.eu.EuMovement;
import fr.rekeningrijders.rest.dto.eu.EuStep;
import fr.rekeningrijders.system.Rekenregistratie;
import lombok.var;

import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.extension.rest.client.Header;
import org.jboss.arquillian.extension.rest.client.Headers;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Date;

import java.util.Calendar;

import java.util.List;


@RunWith(Arquillian.class)
public class arqTest extends BaseArqTest {


    @Test
    @InSequence(1)
    public void loginTest(@ArquillianResteasyResource("rest/user") UserController webTarget) {
        LoginInfo loginInfo = new LoginInfo("luuk@luuk.nl", "luuk");
        Response response = webTarget.login(loginInfo);
        UserDTO user = (UserDTO) response.getEntity();
        Assert.assertEquals("luuk@luuk.nl", user.getUsername());
    }

    @Headers(@Header(name = AUTHORIZATION, value = TOKEN))
    @Test
    @InSequence(2)
    public void getAllVehicles(@ArquillianResteasyResource("rest/vehicle") VehicleController webTarget) {
        List<?> vehicles = webTarget.getAll();
        Assert.assertEquals(1000, vehicles.size());
    }

    @Headers(@Header(name = AUTHORIZATION, value = TOKEN))
    @Test
    @InSequence(3)
    public void getAllZones(@ArquillianResteasyResource("rest/rate") RateController rateController) {
        List<ZoneDTO> zones = rateController.getZones();
        Assert.assertEquals(21, zones.size());
    }

    @Headers(@Header(name = AUTHORIZATION, value = TOKEN))
    @Test
    @InSequence(4)
    public void setZoneBasicRate(@ArquillianResteasyResource("rest/rate") RateController rateController) {
        List<ZoneDTO> zones = rateController.getZones();
        double basicRate = zones.get(0).getBasicRate();
        Assert.assertEquals(basicRate, 0.25, 0.001);
        rateController.setBasicRate(zones.get(0).getId(), 0.30);
        zones = rateController.getZones();
        double changedRate = zones.get(0).getBasicRate();
        Assert.assertNotSame(basicRate, changedRate);
        Assert.assertEquals(0.30, changedRate, 0.001);
    }

    @Headers(@Header(name = AUTHORIZATION, value = TOKEN))
    @Test
    @InSequence(5)
    public void setZoneCustomRate(@ArquillianResteasyResource("rest/rate") RateController rateController) {
        List<ZoneDTO> zones = rateController.getZones();
        ZoneDTO zone = zones.get(0);
        List<RateDTO> customRates = rateController.getCustomRates(zone.getId());
        Assert.assertEquals(0, customRates.size());
        RateDTO rateDTO = new RateDTO(0, 1.25, 8, 15, 9, 15, zone.getId());
        rateController.setCustomRate(zone.getId(), rateDTO);
        // ZoneDTO getzone = rateController.getZone(zone.getId());
        List<RateDTO> getcustomRates = rateController.getCustomRates(zone.getId());
        Assert.assertEquals(1, getcustomRates.size());
        RateDTO getRate = getcustomRates.get(0);
        Assert.assertEquals(rateDTO.getFromHour(), getRate.getFromHour());
        Assert.assertEquals(rateDTO.getFromMinute(), getRate.getFromMinute());
        Assert.assertEquals(rateDTO.getUntilHour(), getRate.getUntilHour());
        Assert.assertEquals(rateDTO.getUntilMinute(), getRate.getUntilMinute());
        Assert.assertEquals(rateDTO.getPrice(), getRate.getPrice(), 0.001);
    }

    @Headers(@Header(name = AUTHORIZATION, value = TOKEN))
    @Test
    @InSequence(6)
    public void OverlappingRatesTest(@ArquillianResteasyResource("rest/rate") RateController rateController) {
        List<ZoneDTO> zones = rateController.getZones();
        ZoneDTO zone = zones.get(0);
        RateDTO rateDTO = new RateDTO(0, 1.25, 8, 15, 9, 15, zone.getId());
        rateController.setCustomRate(zone.getId(), rateDTO);
        //should fail since it's in the range of the first rate
        RateDTO rateDTO2 = new RateDTO(0, 2.00, 7, 20, 8, 20, zone.getId());
        Response response = rateController.setCustomRate(zone.getId(), rateDTO2);
        Assert.assertEquals(400, response.getStatus());
        zone = rateController.getZone(zone.getId());
        List<RateDTO> customRates = rateController.getCustomRates(zone.getId());
        Assert.assertEquals(1, customRates.size());
        //should work
        RateDTO rateDTO3 = new RateDTO(0, 2.00, 20, 20, 21, 20, zone.getId());
        response = rateController.setCustomRate(zone.getId(), rateDTO3);
        Assert.assertEquals(200, response.getStatus());

        RateDTO rateDTO11 = new RateDTO(0, 2.00, 20, 20, 21, 20, zone.getId());
        response = rateController.setCustomRate(zone.getId(), rateDTO11);
        Assert.assertEquals(400, response.getStatus());

        RateDTO rateDTO12 = new RateDTO(0, 2.00, 19, 20, 22, 20, zone.getId());
        response = rateController.setCustomRate(zone.getId(), rateDTO12);
        Assert.assertEquals(400, response.getStatus());

        //should fail
        RateDTO rateDTO4 = new RateDTO(0, 2.00, 20, 10, 22, 20, zone.getId());
        response = rateController.setCustomRate(zone.getId(), rateDTO4);
        Assert.assertEquals(400, response.getStatus());
        zone = rateController.getZone(zone.getId());
        customRates = rateController.getCustomRates(zone.getId());
        Assert.assertEquals(2, customRates.size());

        //should fail
        RateDTO rateDTO5 = new RateDTO(0, 2.00, 20, 11, 20, 50, zone.getId());
        response = rateController.setCustomRate(zone.getId(), rateDTO5);
        Assert.assertEquals(400, response.getStatus());
        zone = rateController.getZone(zone.getId());
        customRates = rateController.getCustomRates(zone.getId());
        Assert.assertEquals(2, customRates.size());

        //should fail
        RateDTO rateDTO6 = new RateDTO(0, 2.00, 20, 20, 21, 21, zone.getId());
        response = rateController.setCustomRate(zone.getId(), rateDTO6);
        Assert.assertEquals(400, response.getStatus());
        zone = rateController.getZone(zone.getId());
        customRates = rateController.getCustomRates(zone.getId());
        Assert.assertEquals(2, customRates.size());
        //should fail
        RateDTO rateDTO7 = new RateDTO(0, 2.00, 20, 20, 21, 20, zone.getId());
        response = rateController.setCustomRate(zone.getId(), rateDTO7);
        Assert.assertEquals(400, response.getStatus());
        zone = rateController.getZone(zone.getId());
        customRates = rateController.getCustomRates(zone.getId());
        Assert.assertEquals(2, customRates.size());

        rateController.removeCustomRate(customRates.get(1).getId());
        customRates = rateController.getCustomRates(zone.getId());
        Assert.assertEquals(1, customRates.size());
    }

    @Headers(@Header(name = AUTHORIZATION, value = TOKEN))
    @Test
    @InSequence(7)
    public void InvoiceGenerateTest (@ArquillianResteasyResource("rest/invoice") InvoiceController invoiceController)
    {
        Rekenregistratie.enableDebug();

        Calendar calender = Calendar.getInstance();
        short year = (short) calender.get(Calendar.YEAR);
        short month = (short) (calender.get(Calendar.MONTH) + 1);

        invoiceController.generate(year, month);
        var invoices = invoiceController.getInvoices(year, month);
        Assert.assertTrue("No invoices generated", invoices.size() > 0);
    }

    @Headers(@Header(name = AUTHORIZATION, value = TOKEN))
    @Test
    @InSequence(8)
    public void InvoiceRegenerateTest(@ArquillianResteasyResource("rest/invoice") InvoiceController invoiceController)
    {
        Rekenregistratie.enableDebug();
        
        Invoice testInvoice = invoiceController.getInvoices((short)2019, (short)1).get(0).asInvoice();
        Invoice result = invoiceController.regenerate(testInvoice.getId()).asInvoice();
        Assert.assertTrue("Regeneration failed", result.getTotalAmount() != testInvoice.getTotalAmount());
    }


    @Inject
    private ITrackerStrorage trackerStrorage;

    @Headers(@Header(name = AUTHORIZATION, value = TOKEN))
    @Test
    @InSequence(9)
    public void EuropeInvoiceTest(@ArquillianResteasyResource("rest/europe") EuropeController europeController) {

        List<EuMovement> movements = new ArrayList<>();
        List<EuStep> steps = new ArrayList<>();

        steps.add(new EuStep(63.3696, 50.78657, 1546297200001L));
        steps.add(new EuStep(-6.77251, -133.9815, 1546297200010L));
        steps.add(new EuStep(10.73449, -44.33012, 1546297200020L));
        steps.add(new EuStep(14.23429, 131.78769, 1546297200043L));
        steps.add(new EuStep(58.64456, -164.52293, 1546297200054L));
        movements.add(new EuMovement(steps, 10));
        EuInvoice invoice = new EuInvoice(trackerStrorage.getAll().get(10).getTrackerId(), movements, 10.0, new Date().getTime());
        Response r = europeController.postInvoice(invoice);
        Assert.assertEquals(200, r.getStatus());
    }

}
