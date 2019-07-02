package fr.rekeningrijders.rest;

import fr.rekeningrijders.models.pojo.LoginInfo;
import fr.rekeningrijders.rest.dto.UserDTO;
import fr.rekeningrijders.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserService userService;

    @POST
    @Path("/login")
    public Response login(LoginInfo loginInfo){
        return Response.ok(UserDTO.transform(userService.login(loginInfo))).build();
    }
}
