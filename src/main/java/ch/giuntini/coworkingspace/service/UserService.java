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

import ch.giuntini.coworkingspace.model.CreatingUser;
import ch.giuntini.coworkingspace.model.UpdatingUser;
import ch.giuntini.coworkingspace.model.User;
import ch.giuntini.coworkingspace.model.UserRole;
import ch.giuntini.coworkingspace.util.HexUtil;
import ch.giuntini.coworkingspace.util.JWTUtil;
import ch.giuntini.coworkingspace.util.PasswordUtil;

@ApplicationScoped
public class UserService {
    @Inject
    private EntityManager entityManager;

    public Optional<User> findUserByEmail(String email) {
        return entityManager
                .createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    public Response registerUser(CreatingUser creatingUser) {
        if (findUserByEmail(creatingUser.getEmail()).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email already used").build();
        }
        User user = User.ofCreatingUser(creatingUser);
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
        user.setRole(UserRole.MEMBER);
        if (isFirstUserToRegister()) {
            user.setRole(UserRole.ADMIN);
        }
        entityManager.persist(user);
        return Response.status(Response.Status.CREATED)
            .entity(user)
            .build();
    }

    private boolean isFirstUserToRegister() {
        return findAllUsers().size() == 0;
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

    public Response findUserById(Long id) {
        User foundUser = entityManager.find(User.class, id);
        if (foundUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(foundUser).build();
    }

    @Transactional
    public Response updateUser(Long id, UpdatingUser updatingUser) {
        User foundUser = entityManager.find(User.class, id);
        if (foundUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } 

        if (updatingUser.getPassword() != null ) {
            try {
                updatingUser.setPassword(PasswordUtil.hash(updatingUser.getPassword(), HexUtil.fromHexString(foundUser.getSalt())));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Can't hash Password")
                    .build();
            }
        }

        foundUser = fill(foundUser, updatingUser);

        entityManager.merge(foundUser);
        return Response.ok().build();
    }

    private User fill(User user, UpdatingUser updatingUser) {
        if (updatingUser.getEmail() != null) user.setEmail(updatingUser.getEmail());
        if (updatingUser.getFirstName() != null) user.setFirstName(updatingUser.getFirstName());
        if (updatingUser.getLastName() != null) user.setLastName(updatingUser.getLastName());
        if (updatingUser.getPassword() != null) user.setPassword(updatingUser.getPassword());
        return user;
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
