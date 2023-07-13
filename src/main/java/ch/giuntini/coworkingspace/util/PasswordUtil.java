package ch.giuntini.coworkingspace.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PasswordUtil {

    public static String[] hash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = generateSalt();
        return new String[]{hash(password, salt), HexUtil.toHexString(salt)};
    }

    public static String hash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                .generateSecret(new PBEKeySpec(password.toCharArray(), salt, 0xffff, 128))
                .getEncoded();

        return HexUtil.toHexString(hash);
    }

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
}
