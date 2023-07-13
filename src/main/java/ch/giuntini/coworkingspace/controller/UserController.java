package ch.giuntini.coworkingspace.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.giuntini.coworkingspace.model.Credentials;
import ch.giuntini.coworkingspace.model.User;
import ch.giuntini.coworkingspace.service.UserService;

@Path("/user")
@Tag(name = "Users", description = "Handling of users")
public class UserController {

    @Inject
    UserService userService;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Register/(create) a new user.", 
        description = "Registers a new user and returns status codes respectively."
    )
    public Response create(User user) {
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
    public Response login(Credentials credentials) {
       return userService.loginUser(credentials.email, credentials.password);
    }
}
