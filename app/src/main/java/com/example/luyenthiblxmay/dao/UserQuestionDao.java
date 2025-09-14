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

    // Xóa tất cả
    @Query("DELETE FROM user_question")
    void deleteAll();

    // Lấy tất cả câu hỏi người dùng
    @Query("SELECT * FROM user_question")
    LiveData<List<UserQuestion>> getAll();

    // Lấy tất cả câu hỏi của 1 user
    @Query("SELECT * FROM user_question WHERE userId = :userId")
    LiveData<List<UserQuestion>> getByUserId(int userId);

    // Lấy tất cả câu trả lời sai của user
    @Query("SELECT * FROM user_question WHERE userId = :userId AND isAnswered = 1 AND selectedAnswer != (SELECT answer FROM questions WHERE questions.id = user_question.questionId)")
    LiveData<List<UserQuestion>> getWrongAnswersByUser(int userId);

    // Lấy 1 câu hỏi theo user và questionId
    @Query("SELECT * FROM user_question WHERE userId = :userId AND questionId = :questionId LIMIT 1")
    UserQuestion getByUserAndQuestion(int userId, int questionId);
}
