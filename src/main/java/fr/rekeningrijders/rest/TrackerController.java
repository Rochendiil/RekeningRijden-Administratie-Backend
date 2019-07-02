package fr.rekeningrijders.rest;

import fr.rekeningrijders.models.pojo.Tracker;
import fr.rekeningrijders.rest.dto.TrackerDTO;
import fr.rekeningrijders.security.JwtSecured;
import fr.rekeningrijders.service.TrackerService;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Stateless
@Path("/tracker")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@JwtSecured
@NoArgsConstructor
public class TrackerController
{

    @Context
    private SecurityContext securityContext;

    @Inject
    private TrackerService trackerService;

    @POST
    public TrackerDTO createTracker(@Valid TrackerDTO trackerDTO)
    {
        Tracker tracker = TrackerDTO.transform(trackerDTO);
        return new TrackerDTO(trackerService.create(tracker));
    }

    @PUT
    public void updateTracker(@Valid Tracker tracker)
    {
        trackerService.update(tracker);
    }

    @GET
    @Path("/all")
    public List<TrackerDTO> getTrackers()
    {
        return TrackerDTO.transform(trackerService.getAll());
    }

    @GET
    @Path("/search")
    //Wat moet dit gaan doen?
    public List<Tracker> searchTrackers(@QueryParam("term") String term)
    {
        throw new NotSupportedException();
    }
    
    @GET
    @Path("/{id}")
    public Tracker getTracker(@PathParam("id") String id)
    {
        return trackerService.find(id);
    }
}
