package com.example.luyenthiblxmay.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.UserLearningProgress;

import java.util.List;

@Dao
public interface UserLearningProgressDao {

    @Insert
    void insert(UserLearningProgress progress);

    @Update
    void update(UserLearningProgress progress);

    // Lấy tiến độ học của user theo category
    @Query("SELECT * FROM user_learning_progress WHERE userId = :userId AND category = :category LIMIT 1")
    UserLearningProgress getProgressByUserAndCategory(int userId, String category);

    // Lấy tất cả tiến độ học của user
    @Query("SELECT * FROM user_learning_progress WHERE userId = :userId")
    List<UserLearningProgress> getAllProgressByUser(int userId);
}
