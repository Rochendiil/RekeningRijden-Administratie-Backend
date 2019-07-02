package fr.rekeningrijders.rest;

import fr.rekeningrijders.models.pojo.Owner;
import fr.rekeningrijders.rest.dto.OwnerDTO;
import fr.rekeningrijders.security.JwtSecured;
import fr.rekeningrijders.service.OwnerService;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.Collections;
import java.util.List;

@Stateless
@Path("/owner")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@JwtSecured
@NoArgsConstructor
public class OwnerController
{

    @Context
    private SecurityContext securityContext;

    @Inject
    private OwnerService ownerService;

    @POST
    public OwnerDTO create(@Valid OwnerDTO ownerDTO)
    {
        Owner owner = OwnerDTO.transform(ownerDTO);
        ownerService.create(owner);
        return OwnerDTO.transform(owner);
    }

    @PUT
    public void update(@Valid OwnerDTO ownerDTO)
    {
        ownerService.update(OwnerDTO.transform(ownerDTO));
    }

    @GET
    public List<OwnerDTO> getAll()
    {
        return OwnerDTO.transform(ownerService.getAll());
    }

    @GET
    @Path("/{id}")
    public OwnerDTO getById(@PathParam("id") long id)
    {
        return OwnerDTO.transform(ownerService.find(id));
    }

    @GET
    @Path("search/{partialname}")
    public List<OwnerDTO> findByPartialName(@PathParam("partialname") String partial)
    {
        List<Owner> owners = ownerService.findPartialName(partial);
        if(!owners.isEmpty()) return OwnerDTO.transform(owners);
        else return Collections.emptyList();
    }

    @GET
    @Path("/uuid/{bsn}")
    public String getbsn(@PathParam("bsn") String bsn)
    {
        Owner owner = ownerService.getByBsn(bsn);
        return owner != null ? owner.getUuid() : null;
    }
    
    @GET
    @Path("/getbyuuid/{uuid}")
    public OwnerDTO getByUuid(@PathParam("uuid") String uuid){
        return OwnerDTO.transform(ownerService.findByUuid(uuid));
    }
}
