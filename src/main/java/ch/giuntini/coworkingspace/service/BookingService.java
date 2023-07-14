package ch.giuntini.coworkingspace.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import ch.giuntini.coworkingspace.model.Booking;
import ch.giuntini.coworkingspace.model.BookingStatus;
import ch.giuntini.coworkingspace.model.CreatingBooking;
import ch.giuntini.coworkingspace.model.UpdatingBooking;
import ch.giuntini.coworkingspace.model.User;
import ch.giuntini.coworkingspace.model.UserRole;

@ApplicationScoped
public class BookingService {
    
    @Inject
    private EntityManager entityManager;

    public List<Booking> findAllBookings() {
        return entityManager.createQuery("FROM application_user", Booking.class).getResultList();
    }

    public List<Booking> findMyBookings(SecurityContext ctx) {
        final Long id = Long.parseLong(ctx.getUserPrincipal().getName());

        return findAllBookings().stream().filter(booking -> booking.getId() == id).toList();
    }

    public Response findByID(Long id, SecurityContext ctx) {
        Booking foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (foundBooking.getBooker().getId() != Long.parseLong(ctx.getUserPrincipal().getName())) {
            if (foundBooking.getBooker().getRole() != UserRole.ADMIN) {
                return Response.status(Response.Status.FORBIDDEN).entity("Not owner of booking").build();
            }
        }

        return Response.ok().entity(foundBooking).build();
    }

    @Transactional
    public Response createBooking(CreatingBooking creatingBooking, SecurityContext ctx) {
        if (!creatingBooking.getStartTime().toLocalDate().isEqual(creatingBooking.getEndTime().toLocalDate())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not on the dame date").build();
        }
        if (creatingBooking.getStartTime().isBefore(LocalDateTime.now())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Booking in the past").build();
        }
        Booking booking = Booking.ofCreatingBooking(creatingBooking);
        booking.setStatus(BookingStatus.PENDING);
        booking.setBooker(entityManager.find(User.class, Long.parseLong(ctx.getUserPrincipal().getName())));

        entityManager.persist(booking);
        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    @Transactional
    public Response updateBooking(Long id, UpdatingBooking updatingBooking, SecurityContext ctx) {
        Booking foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } 
        if (foundBooking.getBooker().getId() != Long.parseLong(ctx.getUserPrincipal().getName())) {
            if (foundBooking.getBooker().getRole() != UserRole.ADMIN) {
                return Response.status(Response.Status.FORBIDDEN).entity("Not owner of booking").build();
            }
        }
        if (updatingBooking.getWishFeedback() != null && foundBooking.getBooker().getRole() != UserRole.ADMIN) {
            return Response.status(Response.Status.FORBIDDEN).entity("Can't add wish feedback as non admin").build();
        }
        Booking booking = fill(foundBooking,
                updatingBooking, 
                entityManager.find(User.class, Long.parseLong(ctx.getUserPrincipal().getName())).getRole());

        entityManager.merge(booking);
        return Response.ok().build();
    }

    private Booking fill(Booking booking, UpdatingBooking updatingBooking, UserRole role) {
        if (updatingBooking.getWish() != null) booking.setWish(updatingBooking.getWish());
        if (updatingBooking.getStartTime() != null) booking.setStartTime(updatingBooking.getStartTime());
        if (updatingBooking.getEndTime() != null) booking.setEndTime(updatingBooking.getEndTime());
        if (updatingBooking.getSection() != null) booking.setSection(updatingBooking.getSection());
        if (role == UserRole.ADMIN) {
            if (updatingBooking.getWishFeedback() != null) booking.setWishFeedback(updatingBooking.getWishFeedback());
        }
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
    public Response calcelBooking(Long id, SecurityContext ctx) {
        Booking foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (foundBooking.getStartTime().isAfter(LocalDateTime.now())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Booking in the Past").build();
        }
        if (foundBooking.getBooker().getId() != Long.parseLong(ctx.getUserPrincipal().getName())) {
            if (foundBooking.getBooker().getRole() != UserRole.ADMIN) {
                return Response.status(Response.Status.FORBIDDEN).entity("Not owner of booking").build();
            }
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
