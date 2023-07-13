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
        entityManager.ntityManager.createQuery("FROM application_user", User.class).getResultList();
    }
}
