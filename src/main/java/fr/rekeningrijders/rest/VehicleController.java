package fr.rekeningrijders.rest;

import fr.rekeningrijders.models.pojo.Owner;
import fr.rekeningrijders.models.pojo.Tracker;
import fr.rekeningrijders.models.pojo.Vehicle;
import fr.rekeningrijders.rest.dto.VehicleDTO;
import fr.rekeningrijders.security.JwtSecured;
import fr.rekeningrijders.service.OwnerService;
import fr.rekeningrijders.service.TrackerService;
import fr.rekeningrijders.service.VehicleService;
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
@Path("/vehicle")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@JwtSecured
@NoArgsConstructor
public class VehicleController {

    @Context
    private SecurityContext securityContext;

    @Inject
    VehicleService vehicleService;

    @Inject
    TrackerService trackerService;

    @Inject
    OwnerService ownerService;

    @GET
    @Path("/{id}")
    public VehicleDTO get(@PathParam("id") long id) {
        return VehicleDTO.make(vehicleService.find(id));
    }

    @GET
    @Path("/byowner/{ownerId}")
    public List<VehicleDTO> byOwner(@PathParam("ownerId") long ownerId) {
        return VehicleDTO.transform(vehicleService.byOwner(ownerId));
    }

    @GET
    @Path("/all")
    public List<VehicleDTO> getAll() {
        return VehicleDTO.transform(vehicleService.getAll());
    }

    @GET
    @Path("/vehicle/tracker/{trackerId}")
    public VehicleDTO findByTrackerId(@PathParam("trackerId") String trackerId) {
        return VehicleDTO.make(vehicleService.findByTrackerId(trackerId));
    }

    @GET
    @Path("/vehicle/license/{license}")
    public VehicleDTO findVehicleByLicense(@PathParam("license") String license) {
        return VehicleDTO.make(vehicleService.findVehicleByLicense(license));
    }

    @POST
    public VehicleDTO store(@Valid VehicleDTO vehicleDTO) {
        Vehicle vehicle = toVehicle(vehicleDTO);
        return vehicle != null
                ? VehicleDTO.make(vehicleService.create(vehicle))
                : null;
    }

    @PUT
    public VehicleDTO update(@Valid VehicleDTO vehicleDTO) {
        Vehicle vehicle = toVehicle(vehicleDTO);
        return vehicle != null
                ? VehicleDTO.make(vehicleService.update(vehicle))
                : null;
    }

    private Vehicle toVehicle(VehicleDTO vehicleDTO) {
        Tracker tracker = null;
        if (vehicleDTO.getTrackerId() != null) {
            tracker = trackerService.find(vehicleDTO.getTrackerId());
        }
        Owner owner = ownerService.find(vehicleDTO.getOwnerId());
        if (owner == null) {
            return null;
        }
        return vehicleDTO.asVehicle(tracker, owner);
    }
}
