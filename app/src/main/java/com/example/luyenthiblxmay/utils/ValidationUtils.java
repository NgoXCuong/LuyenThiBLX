package com.example.luyenthiblxmay.utils;


public class ValidationUtils {
    /**
     * Kiểm tra email hợp lệ
     * @param email Email cần kiểm tra
     * @return true nếu email hợp lệ
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }

    /**
     * Kiểm tra số điện thoại Việt Nam hợp lệ
     * @param phone Số điện thoại cần kiểm tra
     * @return true nếu số điện thoại hợp lệ
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Format: 0xxxxxxxxx (10 số, bắt đầu bằng 03, 05, 07, 08, 09)
        String phonePattern = "^(0[3|5|7|8|9])+([0-9]{8})$";
        return phone.matches(phonePattern);
    }

    /**
     * Kiểm tra mật khẩu hợp lệ
     * @param password Mật khẩu cần kiểm tra
     * @return true nếu mật khẩu hợp lệ (ít nhất 6 ký tự)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Kiểm tra họ tên hợp lệ
     * @param fullName Họ tên cần kiểm tra
     * @return true nếu họ tên hợp lệ (ít nhất 2 ký tự)
     */
    public static boolean isValidFullName(String fullName) {
        return fullName != null && fullName.trim().length() >= 2;
    }

    /**
     * Đánh giá độ mạnh của mật khẩu
     * @param password Mật khẩu cần đánh giá
     * @return Độ mạnh: "Yếu", "Trung bình", "Mạnh"
     */
    public static String getPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return "Yếu";
        }

        int score = 0;

        // Kiểm tra độ dài
        if (password.length() >= 8) score++;

        // Kiểm tra chữ hoa
        if (password.matches(".*[A-Z].*")) score++;

        // Kiểm tra chữ thường
        if (password.matches(".*[a-z].*")) score++;

        // Kiểm tra số
        if (password.matches(".*[0-9].*")) score++;

        // Kiểm tra ký tự đặc biệt
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) score++;

        if (score <= 2) return "Yếu";
        if (score <= 3) return "Trung bình";
        return "Mạnh";
    }

    /**
     * Kiểm tra mật khẩu và xác nhận mật khẩu có khớp không
     * @param password Mật khẩu
     * @param confirmPassword Xác nhận mật khẩu
     * @return true nếu khớp
     */
    public static boolean isPasswordMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    /**
     * Loại bỏ khoảng trắng thừa trong chuỗi
     * @param input Chuỗi đầu vào
     * @return Chuỗi đã được làm sạch
     */
    public static String cleanString(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * Kiểm tra chuỗi có rỗng không
     * @param input Chuỗi cần kiểm tra
     * @return true nếu chuỗi rỗng hoặc null
     */
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
}