package ch.giuntini.coworkingspace.util;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import ch.giuntini.coworkingspace.model.User;
import ch.giuntini.coworkingspace.model.UserRole;
import io.smallrye.jwt.build.Jwt;

public class JWTUtil {

    public static String generateJWTForUser(User user) {
        Set<String> groups = new HashSet<String>();

        UserRole userRole = user.getRole();
        if (userRole == UserRole.MEMBER || userRole == UserRole.ADMIN) {
            groups.add("User");
        }
        if (userRole == UserRole.ADMIN) {
            groups.add("Admin");
        }

        return Jwt.issuer("https://jwtissuer.zli.example.com/")
                .upn(String.valueOf(user.getId()))
                .groups(groups)
                .expiresIn(Duration.ofHours(24))
                .sign();
    }
}
