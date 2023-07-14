package ch.giuntini.coworkingspace.service;

import java.time.LocalDateTime;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import ch.giuntini.coworkingspace.model.Booking;
import ch.giuntini.coworkingspace.model.BookingStatus;
import ch.giuntini.coworkingspace.model.CreatingBooking;
import ch.giuntini.coworkingspace.model.UpdatingBooking;

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
    public Response createBooking(CreatingBooking creatingBooking) {
        if (!creatingBooking.getStartTime().toLocalDate().isEqual(creatingBooking.getEndTime().toLocalDate())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not on the dame date").build();
        }
        Booking booking = Booking.ofCreatingBooking(creatingBooking);
        booking.setStatus(BookingStatus.PENDING);
        entityManager.persist(booking);
        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    @Transactional
    public Response updateBooking(Long id, UpdatingBooking updatingBooking) {
        Booking foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } 
        Booking booking = fill(foundBooking, updatingBooking);

        entityManager.merge(booking);
        return Response.ok().build();
    }

    private Booking fill(Booking booking, UpdatingBooking updatingBooking) {
        if (updatingBooking.getBooker() != null) booking.setBooker(updatingBooking.getBooker());
        if (updatingBooking.getStartTime() != null) booking.setStartTime(updatingBooking.getStartTime());
        if (updatingBooking.getEndTime() != null) booking.setEndTime(updatingBooking.getEndTime());
        if (updatingBooking.getSection() != null) booking.setSection(updatingBooking.getSection());
        if (updatingBooking.getBooker() != null) booking.setBooker(updatingBooking.getBooker());
        return booking;
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
