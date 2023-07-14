package ch.giuntini.coworkingspace.controller;

import java.util.List;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.giuntini.coworkingspace.model.Booking;
import ch.giuntini.coworkingspace.model.CreatingBooking;
import ch.giuntini.coworkingspace.model.UpdatingBooking;
import ch.giuntini.coworkingspace.service.BookingService;

@Path("/booking")
@Tag(name = "Booking", description = "Handling of bookings")
public class BookingController {

    @Inject
    private BookingService bookingService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all bookings.", 
        description = "Returns a list of all bookings."
    )
    @RolesAllowed({"Admin"})
    public List<Booking> getAllBookings() {
        return bookingService.findAllBookings();
    }

    @GET
    @Path("/my")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get all bookings owned by the user.", 
        description = "Returns a list of all bookings owned identified by the JWT."
    )
    @RolesAllowed({"User", "Admin"})
    public List<Booking> getMyBookings(@Context SecurityContext ctx) {
        return bookingService.findMyBookings(ctx);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get a single booking.", 
        description = "Returns a single booking by the given id."
    )
    @RolesAllowed({"User", "Admin"})
    public Response getBooking(@PathParam("id") Long id, @Context SecurityContext ctx) {
       return bookingService.findByID(id, ctx);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Creates a new booking.", 
        description = "Creates a new booking and returns the created booking."
    )
    @RolesAllowed({"User", "Admin"})
    public Response createBooking(@Valid CreatingBooking creatingBooking, @Context SecurityContext ctx) {
       return bookingService.createBooking(creatingBooking, ctx);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Updates a booking.", 
        description = "Updates a booking by a given id."
    )
    @RolesAllowed({"User", "Admin"})
    public Response updateBooking(@PathParam("id") Long id, @Valid UpdatingBooking updatingBooking, @Context SecurityContext ctx) {
       return bookingService.updateBooking(id, updatingBooking, ctx);
    }

    @PUT
    @Path("/accept/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Accepts a single booking.", 
        description = "Accepts a single booking by the given id putting it's status to ACCEPTED."
    )
    @RolesAllowed({"Admin"})
    public Response acceptBooking(@PathParam("id") Long id) {
       return bookingService.acceptBooking(id);
    }

    @PUT
    @Path("/decline/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Declines a single booking.", 
        description = "Declines a single booking by the given id putting it's status to DECLINED."
    )
    @RolesAllowed({"Admin"})
    public Response declineBooking(@PathParam("id") Long id) {
       return bookingService.declineBooking(id);
    }

    @DELETE
    @Path("/cancel/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Cancel a single booking.", 
        description = "Cancels a single booking by the given id putting it's status to CANCELED."
    )
    @RolesAllowed({"User", "Admin"})
    public Response cancelBooking(@PathParam("id") Long id, @Context SecurityContext ctx) {
       return bookingService.calcelBooking(id, ctx);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Deletes a single booking.", 
        description = "Deletes a single booking by the given id."
    )
    @RolesAllowed({"Admin"})
    public Response deleteBooking(@PathParam("id") Long id) {
       return bookingService.deleteBooking(id);
    }
}
