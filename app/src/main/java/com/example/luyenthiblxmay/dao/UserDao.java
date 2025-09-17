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
    int deleteUser(User user);

    // Lấy tất cả users
    @Query("SELECT * FROM users ORDER BY id DESC")
    List<User> getAllUsersDirect();

    // Lấy user theo email
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    // Lấy user theo ID
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserById(int id);

    // Count user theo email
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int countUsersByEmail(String email);

    // Kiểm tra email tồn tại
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    boolean isEmailExists(String email);

    // Kiểm tra phone tồn tại
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE phone = :phone)")
    boolean isPhoneExists(String phone);

}
