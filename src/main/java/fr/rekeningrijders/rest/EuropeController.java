package fr.rekeningrijders.rest;

import fr.rekeningrijders.models.pojo.FuelType;
import fr.rekeningrijders.rest.dto.TrackerDTO;
import fr.rekeningrijders.rest.dto.eu.EuInvoice;
import fr.rekeningrijders.rest.dto.eu.EuTracker;
import fr.rekeningrijders.service.InvoiceService;
import fr.rekeningrijders.service.TrackerService;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@Path("/europe")
@NoArgsConstructor
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class EuropeController
{
    @Inject
    private TrackerService trackerService;

    @Inject
    private InvoiceService invoiceService;

    private Logger logger = Logger.getLogger(EuropeController.class);

    @GET
    @Path("/trackers")
    public List<EuTracker> getTrackers(@QueryParam("limit") @DefaultValue("10") int limit)
    {

        List<EuTracker> euTrackers = new ArrayList<>();
        for (TrackerDTO tracker: TrackerDTO.transform(trackerService.getFrenchCars(limit))) {
            euTrackers.add(new EuTracker(tracker.getTrackerId()));
        }
        return euTrackers;
    }

    @POST
    @Path("/invoices")
    public Response postInvoice(EuInvoice euInvoice)
    {
        logger.info(euInvoice);

        boolean result = invoiceService.createEUInvoice(euInvoice);
        return result
            ? Response.ok().build()
            : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{trackerid}/fueltype")
    public String getFuelType(@PathParam("trackerid") String trackerId)
    {
        byte type = trackerService.getFuelTypeByTracker(trackerId);
        switch (type){
            case FuelType.BENZINE: return "BENZINE";
            case FuelType.DIESEL:  return "DIESEL";
            default: return "OTHER";
        }
    }
}
