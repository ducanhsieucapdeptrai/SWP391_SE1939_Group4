package utils;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets; // 👈 dùng để chuẩn hoá encoding

public class HashUtil {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8)); // 👈 UTF-8 theo chuẩn
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
