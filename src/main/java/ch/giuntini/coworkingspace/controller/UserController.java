package ch.giuntini.coworkingspace.controller;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.giuntini.coworkingspace.model.CreatingUser;
import ch.giuntini.coworkingspace.model.Credentials;
import ch.giuntini.coworkingspace.model.UpdatingUser;
import ch.giuntini.coworkingspace.model.User;
import ch.giuntini.coworkingspace.service.UserService;

@Path("/user")
@Tag(name = "Users", description = "Handling of users")
public class UserController {

    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Register/(create) a new user.", 
        description = "Registers a new user and returns status codes respectively."
    )
    @PermitAll
    public Response create(@Valid CreatingUser user) {
       return userService.registerUser(user);
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Login for users.", 
        description = "Checks the given credentials and if they match to a user in the database a " + 
        "JWT is created and sent to the client."
    )
    @PermitAll
    public Response login(Credentials credentials) {
       return userService.loginUser(credentials.email, credentials.password);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all users", 
        description = "Returns a list of all users."
    )
    @RolesAllowed({"Admin"})
    public List<User> getAllUsers() {
       return userService.findAllUsers();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get single users", 
        description = "Returns a user by a given id."
    )
    @RolesAllowed({"User", "Admin"})
    public Response getUserbyId(@PathParam("id") Long id) {
       return userService.findUserById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Updates a user ", 
        description = "Updates a user by a given id."
    )
    @RolesAllowed({"Admin"})
    public Response updateUser(@PathParam("id") Long id, @Valid UpdatingUser updatingUser) {
        return userService.updateUser(id, updatingUser);
    }    

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Deletes a user ", 
        description = "Deletes a user by a given id."
    )
    @RolesAllowed({"Admin"})
    public Response deleteUser(@PathParam("id") Long id) {
        return userService.deleteUser(id);
    }  
}
