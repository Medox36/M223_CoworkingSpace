package ch.giuntini.coworkingspace.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import ch.giuntini.coworkingspace.model.Booking;
import ch.giuntini.coworkingspace.model.BookingStatus;

@ApplicationScoped
public class BookingService {

    @Inject
    private EntityManager entityManager;

    public List<Booking> findAllBookings() {
        return entityManager.createQuery("FROM application_user", Booking.class).getResultList();
    }

    public Response findByID(Long id) {
        Booking foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(foundBooking).build();
    }

    @Transactional
    public Response createBooking(Booking booking) {
        entityManager.persist(booking);
        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    @Transactional
    public Response acceptBooking(Long id) {
        Booking foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (foundBooking.getStatus() == BookingStatus.ACCEPTED) {
            return Response.status(Response.Status.NOT_MODIFIED).entity("Already accepted").build();
        }

        foundBooking.setStatus(BookingStatus.ACCEPTED);
        return Response.ok().build();
    }
    
    @Transactional
    public Response declineBooking(Long id) {
        Booking foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (foundBooking.getStatus() == BookingStatus.REJECTED) {
            return Response.status(Response.Status.NOT_MODIFIED).entity("Already declined").build();
        }

        foundBooking.setStatus(BookingStatus.REJECTED);
        return Response.ok().build();
    }

    @Transactional
    public Response calcelBooking(Long id) {
        Booking foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (foundBooking.getStartTime().isAfter(LocalDateTime.now())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Booking in the Past").build();
        }

        foundBooking.setStatus(BookingStatus.CANCELED);
        return Response.ok().build();
    }

    @Transactional
    public Response deleteBooking(Long id) {
        Booking foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        entityManager.remove(foundBooking);
        return Response.ok().build();
    }
}
