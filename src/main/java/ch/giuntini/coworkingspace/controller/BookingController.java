package ch.giuntini.coworkingspace.controller;

import java.util.List;

import javax.inject.Inject;
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

import ch.giuntini.coworkingspace.model.Booking;
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
    public List<Booking> getAllBookings() {
        return bookingService.findAllBookings();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get a single booking.", 
        description = "Returns a single booking by the given id."
    )
    public Response getBooking(@PathParam("id") Long id) {
       return bookingService.findByID(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Creates a new booking.", 
        description = "Creates a new booking and returns the created booking."
    )
    public Response createBooking(Booking booking) {
       return bookingService.createBooking(booking);
    }

    @PUT
    @Path("/booking/accept/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Accepts a single booking.", 
        description = "Accepts a single booking by the given id putting it's status to ACCEPTED."
    )
    public Response acceptBooking(@PathParam("id") Long id) {
       return bookingService.acceptBooking(id);
    }

    @PUT
    @Path("/booking/decline/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Declines a single booking.", 
        description = "Declines a single booking by the given id putting it's status to DECLINED."
    )
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
    public Response cancelBooking(@PathParam("id") Long id) {
       return bookingService.calcelBooking(id);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Deletes a single booking.", 
        description = "Deletes a single booking by the given id."
    )
    public Response deleteBooking(@PathParam("id") Long id) {
       return bookingService.deleteBooking(id);
    }
}
