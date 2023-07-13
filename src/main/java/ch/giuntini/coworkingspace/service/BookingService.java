package ch.giuntini.coworkingspace.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class BookingService {

    @Inject
    private EntityManager entityManager;

    public List<Booking> findAllBookings() {
        entityManager.createQuery("FROM application_user", User.class).getResultList();
    }

    public Response findByID(Long id) {
        Optional<Booking> foundBooking = entityManager.find(Booking.class, id);
        if (foundBooking.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return esponse.ok().entity(foundBooking.get()).build();
    }
}
