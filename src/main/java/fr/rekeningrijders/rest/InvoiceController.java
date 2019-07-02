package fr.rekeningrijders.rest;

import fr.rekeningrijders.rest.dto.InvoiceDTO;
import fr.rekeningrijders.rest.dto.RideDTO;
import fr.rekeningrijders.security.JwtSecured;
import fr.rekeningrijders.service.InvoiceService;
import fr.rekeningrijders.service.ReverseGeoService;
import lombok.NoArgsConstructor;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.time.Month;
import java.util.Date;
import java.util.List;

@Path("/invoice")
@Produces({ MediaType.APPLICATION_JSON })
@Stateless
@JwtSecured
@NoArgsConstructor
public class InvoiceController
{

    private Logger logger = Logger.getLogger(InvoiceController.class);

    @Inject
    private InvoiceService invoiceService;

    @Inject
    private ReverseGeoService reverseGeoService;

    @GET
    @Path("/all")
    public List<InvoiceDTO> getInvoices(
        @QueryParam("year") short year,
        @QueryParam("month") short monthNumber)
    {
        Month month = Month.of(monthNumber);
        return InvoiceDTO.create(invoiceService.getInvoices(year, month));
    }

    @GET
    @Path("/{id}")
    public InvoiceDTO getInvoice(@PathParam("id") long id)
    {
        return InvoiceDTO.create(invoiceService.getInvoice(id));
    }

    @GET
    @Path("/by_owner/{uuid}")
    public List<InvoiceDTO> getOnwerInvoices(@PathParam("uuid") String uuid)
    {
        return InvoiceDTO.create(invoiceService.getByOwner(uuid));
    }
    
    @GET
    @Path("/{id}/regenerate")
    public InvoiceDTO regenerate(@PathParam("id") long id)
    {
        return InvoiceDTO.create(invoiceService.regenerateInvoice(id));
    }

    @GET
    @Path("/generate")
    public void generate(
        @QueryParam("year") short year,
        @QueryParam("month") short monthNumber)
    {
        logger.info("started");
        Date start = new Date();
        Month month = Month.of(monthNumber);
        invoiceService.generateInvoices(year, month);
        Date end = new Date();
        logger.info("started at: " + start + " finished at: " + end);
    }

    @GET
    @Path("/rides")
    public List<RideDTO> getRides(
        @QueryParam("tracker") String trackerId,
        @QueryParam("year") short year,
        @QueryParam("month") short monthNumber)
    {
        Month month = Month.of(monthNumber);
        return RideDTO.transform(invoiceService.getRides(trackerId, year, month), reverseGeoService);
    }
}
