//package com.example.luyenthiblxmay.dao;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import com.example.luyenthiblxmay.model.UserQuestion;
//
//import java.util.List;
//
//@Dao
//public interface UserQuestionDao {
//
//    // Lấy danh sách câu hỏi của 1 user theo category (LiveData)
//    @Query("SELECT uq.* FROM user_question uq " +
//            "INNER JOIN Questions q ON uq.questionId = q.id " +
//            "WHERE uq.userId = :userId AND q.category = :category")
//    LiveData<List<UserQuestion>> getUserQuestionsByCategory(int userId, String category);
//
//    // Lấy danh sách câu hỏi đồng bộ (không LiveData) nếu cần
//    @Query("SELECT uq.* FROM user_question uq " +
//            "INNER JOIN Questions q ON uq.questionId = q.id " +
//            "WHERE uq.userId = :userId AND q.category = :category")
//    List<UserQuestion> getUserQuestionsByCategorySync(int userId, String category);
//
//    // Lấy 1 UserQuestion theo userId + questionId
//    @Query("SELECT * FROM user_question WHERE userId = :userId AND questionId = :questionId LIMIT 1")
//    LiveData<UserQuestion> getByUserAndQuestion(int userId, int questionId);
//
//    // Insert or update (onConflict = REPLACE)
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertOrUpdate(UserQuestion uq);
//}
//

package com.example.luyenthiblxmay.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.UserQuestion;

import java.util.List;

@Dao
public interface UserQuestionDao {

    // Lấy tất cả câu hỏi của user theo category (LiveData)
    @Query("SELECT uq.* FROM user_question uq " +
            "INNER JOIN questions q ON uq.questionId = q.id " +
            "WHERE uq.userId = :userId AND q.category = :category")
    LiveData<List<UserQuestion>> getUserQuestionsByCategory(int userId, String category);

    // Lấy tất cả câu hỏi đã trả lời (không theo category)
    @Query("SELECT * FROM user_question WHERE userId = :userId AND isAnswered = 1")
    LiveData<List<UserQuestion>> getAnsweredQuestionsByUser(int userId);

    // Insert với REPLACE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserQuestion userQuestion);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserQuestion> userQuestions);

    // Update userQuestion
    @Update
    void update(UserQuestion userQuestion);

    // Tổng số câu đúng theo category (LiveData)
    @Query("SELECT COUNT(*) FROM user_question uq " +
            "INNER JOIN questions q ON uq.questionId = q.id " +
            "WHERE uq.userId = :userId AND q.category = :category AND uq.isCorrect = 1")
    LiveData<Integer> getCorrectCountByUserAndCategory(int userId, String category);

    // Tổng số câu đã trả lời theo category (LiveData)
    @Query("SELECT COUNT(*) FROM user_question uq " +
            "INNER JOIN questions q ON uq.questionId = q.id " +
            "WHERE uq.userId = :userId AND q.category = :category AND uq.isAnswered = 1")
    LiveData<Integer> getAnsweredCountByUserAndCategory(int userId, String category);

    // Kiểm tra user đã trả lời câu hỏi chưa
    @Query("SELECT * FROM user_question WHERE userId = :userId AND questionId = :questionId LIMIT 1")
    LiveData<UserQuestion> getUserQuestionLive(int userId, int questionId);

    // Xóa toàn bộ record của user
    @Query("DELETE FROM user_question WHERE userId = :userId")
    void deleteAllByUser(int userId);

    @Query("SELECT * FROM user_question WHERE userId = :userId")
    LiveData<List<UserQuestion>> getUserQuestionsByUser(int userId);
}
