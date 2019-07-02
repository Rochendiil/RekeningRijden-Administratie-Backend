package fr.rekeningrijders.rest;

import fr.rekeningrijders.models.pojo.Rate;
import fr.rekeningrijders.rest.dto.RateDTO;
import fr.rekeningrijders.rest.dto.ZoneDTO;
import fr.rekeningrijders.security.JwtSecured;
import fr.rekeningrijders.service.RateService;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Stateless
@Path("/rate")
@Produces({ MediaType.APPLICATION_JSON + "; charset=ISO-8859-1"})
@Consumes({ MediaType.APPLICATION_JSON })
@JwtSecured
@NoArgsConstructor
public class RateController {

    @Inject
    private RateService rateService;

    @GET
    @Path("/zones")
    public List<ZoneDTO> getZones(){
        return ZoneDTO.transform(rateService.getZones());
    }

    @GET
    @Path("/zone/{id}")
    public ZoneDTO getZone(@PathParam("id") long id){
        return ZoneDTO.transform(rateService.getZone(id));
    }

    @POST
    @Path("/zone/{id}/basicrate/{rate}")
    public void setBasicRate(@PathParam("id") long id, @PathParam("rate") double rate){
        rateService.setBasicRate(id,rate);
    }

    @POST
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("/zone/{id}/customrate")
    public Response setCustomRate(@PathParam("id") long id, RateDTO rateDTO){
        Rate rate = RateDTO.transform(rateDTO);
        rate.setZone(rateService.getZone(rateDTO.getZoneId()));
        if(!rateService.setCustomRate(id, rate)){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else{
            return Response.ok().build();
        }
    }

    @GET
    @Path("/zone/{id}/customrate")
    public List<Rate> getCustomRate(long id) {
        return rateService.getCustomRates(id);
    }

    @GET
    @Path("/zone/{id}/customrates")
    public List<RateDTO> getCustomRates(@PathParam("id") long id) {
        return RateDTO.transform(rateService.getCustomRates(id));
    }
    @DELETE
    @Path("/{id}")
    public void removeCustomRate(@PathParam("id") long id){
        rateService.removeCustomRate(id);
    }
}
