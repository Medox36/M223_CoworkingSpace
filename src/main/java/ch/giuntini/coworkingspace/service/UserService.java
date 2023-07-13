package ch.giuntini.coworkingspace.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import ch.giuntini.coworkingspace.model.User;
import ch.giuntini.coworkingspace.util.PasswordUtil;

@ApplicationScoped
public class UserService {
    @Inject
    private EntityManager entityManager;

    public Optional<User> findUserByEmail(String email) {
        return entityManager
                .createNamedQuery("ApplicationUser.findByEmail", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    public Response registerUser(User user) {
        String pw = user.getPassword();
        try {
            String[] ps = PasswordUtil.hash(pw);
            user.setPassword(ps[0]);
            user.setSalt(ps[1]);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Can't hash Password").build();
        }
        entityManager.persist(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    
}
