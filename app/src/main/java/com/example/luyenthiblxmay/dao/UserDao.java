package com.example.luyenthiblxmay.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.User;

import java.util.List;

@Dao
public interface UserDao {

    // Insert user - trả về long (ID của record mới)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);

    // Update user
    @Update
    void updateUser(User user);

    // Delete user
    @Delete
    int delete(User user);

    // Delete tất cả users
    @Query("DELETE FROM users")
    void deleteAllUsers();

    // Lấy tất cả users
    @Query("SELECT * FROM users ORDER BY id DESC")
    List<User> getAllUsersDirect();

    // Lấy user theo email
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);


    // Lấy admin theo email
    @Query("SELECT * FROM users WHERE email = :email AND is_admin = 1 LIMIT 1")
    User getAdminByEmail(String email);

    // Lấy user theo phone
    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    User getUserByPhone(String phone);

    // Lấy user theo ID
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserById(int id);

    // Count user theo email
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int countUsersByEmail(String email);

    // Count user theo phone
    @Query("SELECT COUNT(*) FROM users WHERE phone = :phone")
    int countUsersByPhone(String phone);

    // Login user (normal user)
    @Query("SELECT * FROM users WHERE email = :email AND password = :password AND is_admin = 0 LIMIT 1")
    User loginUser(String email, String password);

    // Login admin
    @Query("SELECT * FROM users WHERE email = :email AND password = :password AND is_admin = 1 LIMIT 1")
    User loginAdmin(String email, String password);

    // Lấy tất cả admin
    @Query("SELECT * FROM users WHERE is_admin = 1")
    List<User> getAllAdminsDirect();


    // Lấy tất cả user bình thường
    @Query("SELECT * FROM users WHERE is_admin = 0 ORDER BY id DESC")
    List<User> getAllNormalUsersDirect();

    // Tìm kiếm user (email hoặc tên)
    @Query("SELECT * FROM users WHERE email LIKE :searchQuery OR full_name LIKE :searchQuery ORDER BY id DESC")
    List<User> searchUsersDirect(String searchQuery);

    // Kiểm tra email tồn tại
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    boolean isEmailExists(String email);

    // Kiểm tra phone tồn tại
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE phone = :phone)")
    boolean isPhoneExists(String phone);

}
