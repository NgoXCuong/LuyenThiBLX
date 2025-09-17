package com.example.luyenthiblxmay.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.UserQuestion;

import java.util.List;

@Dao
public interface UserQuestionDao {
    // Thêm mới câu hỏi người dùng đã trả lời
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserQuestion userQuestion);

    // Cập nhật câu hỏi người dùng
    @Update
    void update(UserQuestion userQuestion);

    // Xóa câu hỏi người dùng
    @Delete
    void delete(UserQuestion userQuestion);
}
