package com.example.luyenthiblxmay.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class PasswordUtils {

    private static final String ALGORITHM = "SHA-256";
    private static final String CHARSET = "UTF-8";

    /**
     * Mã hóa mật khẩu sử dụng SHA-256
     * @param password Mật khẩu gốc
     * @return Mật khẩu đã được mã hóa
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = md.digest(password.getBytes(CHARSET));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: trả về mật khẩu gốc nếu mã hóa thất bại
            return password;
        }
    }

    /**
     * Mã hóa mật khẩu với salt
     * @param password Mật khẩu gốc
     * @param salt Salt để tăng bảo mật
     * @return Mật khẩu đã được mã hóa với salt
     */
    public static String hashPasswordWithSalt(String password, String salt) {
        return hashPassword(password + salt);
    }

    /**
     * Xác minh mật khẩu
     * @param password Mật khẩu người dùng nhập
     * @param hashedPassword Mật khẩu đã mã hóa trong database
     * @return true nếu mật khẩu đúng
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }
        return hashPassword(password).equals(hashedPassword);
    }

    /**
     * Xác minh mật khẩu với salt
     * @param password Mật khẩu người dùng nhập
     * @param hashedPassword Mật khẩu đã mã hóa trong database
     * @param salt Salt đã sử dụng khi mã hóa
     * @return true nếu mật khẩu đúng
     */
    public static boolean verifyPasswordWithSalt(String password, String hashedPassword, String salt) {
        if (password == null || hashedPassword == null || salt == null) {
            return false;
        }
        return hashPasswordWithSalt(password, salt).equals(hashedPassword);
    }

    /**
     * Tạo salt ngẫu nhiên
     * @return Salt ngẫu nhiên
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    /**
     * Chuyển đổi byte array thành hex string
     * @param bytes Byte array
     * @return Hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     <<<<<<< HEAD
     * Tạo mật khẩu ngẫu nhiên * @param length Độ dài mật khẩu
     =======
     * Tạo mật khẩu ngẫu nhiên
     * @param length Độ dài mật khẩu
    >>>>>>> 83f4733889382973411d52ad4721366bcfcaf921
     * @return Mật khẩu ngẫu nhiên
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
