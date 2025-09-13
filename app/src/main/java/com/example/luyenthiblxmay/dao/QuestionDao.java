package com.example.luyenthiblxmay.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.Question;

import java.util.List;

@Dao
public interface QuestionDao {

    // Thêm câu hỏi mới hoặc cập nhật nếu trùng id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(Question question);

    // Cập nhật câu hỏi (ví dụ update trạng thái trả lời)
    @Update
    void updateQuestion(Question question);

    // Lấy tất cả câu hỏi
    @Query("SELECT * FROM questions")
    List<Question> getAllQuestions();

    // Lấy câu hỏi theo category
    @Query("SELECT * FROM questions WHERE category = :category")
    List<Question> getQuestionsByCategory(String category);

    // Đếm số câu đã trả lời trong category
    @Query("SELECT COUNT(*) FROM questions WHERE category = :category AND isAnswered = 1")
    int countAnsweredQuestions(String category);

    // Đếm tổng số câu trong category
    @Query("SELECT COUNT(*) FROM questions WHERE category = :category")
    int countTotalQuestions(String category);

    // Lấy câu hỏi theo id
    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    Question getQuestionById(int id);
}