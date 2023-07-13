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
        summary = "Get all bookings", 
        description = "Returns a list of all bookings."
    )
    public List<Booking> getAllBookings() {
        return bookingService.findAllBookings();
    }
    
}
