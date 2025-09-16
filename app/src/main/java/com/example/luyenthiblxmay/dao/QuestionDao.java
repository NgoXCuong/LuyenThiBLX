package com.example.luyenthiblxmay.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.Question;

import java.util.List;

//@Dao
//public interface QuestionDao {
//
//    @Insert
//    void insert(Question question);
//
//    @Update
//    void update(Question question);
//
//    @Delete
//    void delete(Question question);
//
//    @Query("DELETE FROM questions")
//    void deleteAll();
//
//    @Query("SELECT * FROM questions ORDER BY id ASC")
//    LiveData<List<Question>> getAllQuestions();
//
//    @Query("SELECT * FROM questions WHERE category = :category")
//    LiveData<List<Question>> getQuestionsByCategory(String category);
//
//    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT 25")
//    LiveData<List<Question>> getRandomQuestions();
//
//    @Query("UPDATE questions SET isAnswered = :answered WHERE id = :id")
//    void updateAnswered(int id, boolean answered);
//}

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question question);

    @Update
    void update(Question question);

    @Delete
    void delete(Question question);

    @Query("DELETE FROM questions")
    void deleteAll();

    @Query("SELECT * FROM questions ORDER BY id ASC")
    LiveData<List<Question>> getAllQuestions();

    @Query("SELECT * FROM questions WHERE category = :category")
    LiveData<List<Question>> getQuestionsByCategory(String category);

    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT 25")
    LiveData<List<Question>> getRandomQuestions();

    // ✅ Cập nhật tiến độ
    @Query("UPDATE questions SET isAnswered = :answered WHERE id = :id")
    void updateAnswered(int id, boolean answered);

    // ✅ Đếm số câu đã làm
    @Query("SELECT COUNT(*) FROM questions WHERE isAnswered = 1")
    LiveData<Integer> getAnsweredCount();

    // ✅ Đếm tổng số câu
    @Query("SELECT COUNT(*) FROM questions")
    LiveData<Integer> getTotalCount();

    // ✅ (Tuỳ chọn) Lấy danh sách câu đã làm
    @Query("SELECT * FROM questions WHERE isAnswered = 1")
    LiveData<List<Question>> getAnsweredQuestions();

    // ✅ (Tuỳ chọn) Lấy danh sách câu chưa làm
    @Query("SELECT * FROM questions WHERE isAnswered = 0")
    LiveData<List<Question>> getUnansweredQuestions();
}
