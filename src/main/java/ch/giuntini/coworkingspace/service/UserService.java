package ch.giuntini.coworkingspace.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import ch.giuntini.coworkingspace.model.User;
import ch.giuntini.coworkingspace.util.HexUtil;
import ch.giuntini.coworkingspace.util.JWTUtil;
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Can't hash Password")
                .build();
        }
        entityManager.persist(user);
        return Response.status(Response.Status.CREATED)
            .entity(user)
            .build();
    }

    public Response loginUser(String email, String password) {
        Optional<User> userOpt = findUserByEmail(email);
        String paramPassword = null;
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            try {
                paramPassword = PasswordUtil.hash(password, HexUtil.fromHexString(user.getSalt()));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Can't verify credentials")
                    .build();
            }
            if (Objects.equals(paramPassword, userOpt.get().getPassword())) {
                return Response.status(Response.Status.CREATED)
                    .header(HttpHeaders.AUTHORIZATION, JWTUtil.generateJWTForUser(user))
                    .build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    public List<User> findAllUsers() {
        return entityManager.createQuery("FROM application_user", User.class).getResultList();
    }

    @Transactional
    public Response updateUser(Long id, User user) {
        User foundUser = entityManager.find(User.class, id);
        if (foundUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } 

        if (user.getPassword() != null ) {
            try {
                user.setPassword(PasswordUtil.hash(user.getPassword(), HexUtil.fromHexString(foundUser.getSalt())));
                user.setSalt(foundUser.getSalt());
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Can't hash Password")
                    .build();
            }
        }

        entityManager.merge(user);
        return Response.ok().build();
    }

    @Transactional
    public Response deleteUser(Long id) {
        User foundUser = entityManager.find(User.class, id);
        if (foundUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        entityManager.remove(foundUser);
        return Response.ok().build();
    }
}
