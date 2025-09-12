package com.example.luyenthiblxmay.controlller;

import static com.example.luyenthiblxmay.utils.PasswordUtils.hashPassword;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.luyenthiblxmay.dao.UserDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.model.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserController {
    private final UserDao userDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public UserController(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
    }

    // Insert user
    public void insertUser(User user, InsertCallback callback) {
        executor.execute(() -> {
            try {
                long result = userDao.insertUser(user);
                if (callback != null) {
                    callback.onResult(result > 0, result > 0 ? "Đăng ký thành công!" : "Đăng ký thất bại!");
                }
            } catch (Exception e) {
                if (callback != null) callback.onResult(false, "Lỗi: " + e.getMessage());
            }
        });
    }

    // Check email tồn tại
    public void checkEmailExists(String email, ExistCallback callback) {
        executor.execute(() -> {
            try {
                boolean exists = userDao.isEmailExists(email);
                if (callback != null) callback.onResult(exists);
            } catch (Exception e) {
                if (callback != null) callback.onResult(false);
            }
        });
    }

    // Check phone tồn tại
    public void checkPhoneExists(String phone, ExistCallback callback) {
        executor.execute(() -> {
            try {
                boolean exists = userDao.isPhoneExists(phone);
                if (callback != null) callback.onResult(exists);
            } catch (Exception e) {
                if (callback != null) callback.onResult(false);
            }
        });
    }

    // Login user
    public void loginUser(String email, String password, LoginCallback callback) {
        executor.execute(() -> {
            try {
                User user = userDao.getUserByEmail(email);
                boolean success = user != null && hashPassword(password).equals(user.getPassword());
                if (callback != null) {
                    callback.onResult(success, user, success ? "Đăng nhập thành công!" : "Email hoặc mật khẩu không đúng!");
                }
            } catch (Exception e) {
                if (callback != null) callback.onResult(false, null, "Lỗi: " + e.getMessage());
            }
        });
    }

    public void loginAdmin(String email, String password, LoginCallback callback) {
        executor.execute(() -> {
            try {
                User admin = userDao.getAdminByEmail(email);
                boolean success = admin != null && hashPassword(password).equals(admin.getPassword());
                if (callback != null) {
                    callback.onResult(success, admin, success ? "Đăng nhập admin thành công!" : "Không phải tài khoản admin hoặc sai mật khẩu!");
                }
            } catch (Exception e) {
                if (callback != null) callback.onResult(false, null, "Lỗi: " + e.getMessage());
            }
        });
    }


    // Lấy tất cả user bằng LiveData
    public LiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> liveData = new MutableLiveData<>();
        executor.execute(() -> {
            try {
                List<User> users = userDao.getAllUsersDirect();
                liveData.postValue(users);
            } catch (Exception e) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    // Update user
    public void updateUser(User user, UpdateCallback callback) {
        executor.execute(() -> {
            try {
                userDao.updateUser(user);
                if (callback != null) callback.onResult(true, "Cập nhật thành công!");
            } catch (Exception e) {
                if (callback != null) callback.onResult(false, "Lỗi: " + e.getMessage());
            }
        });
    }

    // Delete user
    public void deleteUser(User user, DeleteCallback callback) {
        executor.execute(() -> {
            try {
                userDao.deleteUser(user);
                if (callback != null) callback.onResult(true, "Xóa thành công!");
            } catch (Exception e) {
                if (callback != null) callback.onResult(false, "Lỗi: " + e.getMessage());
            }
        });
    }

    // ==================== Callback Interfaces ====================
    public interface InsertCallback {
        void onResult(boolean success, String message);
    }

    public interface ExistCallback {
        void onResult(boolean exists);
    }

    public interface LoginCallback {
        void onResult(boolean success, User user, String message);
    }

    public interface UpdateCallback {
        void onResult(boolean success, String message);
    }

    public interface DeleteCallback {
        void onResult(boolean success, String message);
    }

    public interface UserListCallback {
        void onResult(List<User> users);
    }
}
