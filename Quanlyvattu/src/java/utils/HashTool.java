package utils;

/**
 *
 * @author Lenovo
 */
import utils.HashUtil;

public class HashTool {
    public static void main(String[] args) {
        String input = "123456";
        String hashed = HashUtil.hashPassword(input);
        System.out.println("SHA-256: " + hashed);
    }
}