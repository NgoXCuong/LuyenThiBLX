package com.example.luyenthiblxmay.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.luyenthiblxmay.model.ExamQuestion;
import com.example.luyenthiblxmay.model.Question;

import java.util.List;

@Dao
public interface ExamQuestionDao {

    @Insert
    void insertExamQuestions(List<ExamQuestion> examQuestions);

    // Lấy tất cả câu hỏi của 1 bài thi
    @Query("SELECT * FROM exam_question WHERE examId = :examId")
    LiveData<List<ExamQuestion>> getQuestionsByExamId(int examId);

    // Lấy danh sách câu sai (chỉ thông tin trong bảng ExamQuestion)
    @Query("SELECT * FROM exam_question WHERE examId = :examId AND isCorrect = 0")
    LiveData<List<ExamQuestion>> getWrongQuestionsByExamId(int examId);

    // Lấy câu hỏi chi tiết bị sai, join với bảng Question
    @Query("SELECT q.* FROM exam_question AS eq " +
            "INNER JOIN questions AS q ON eq.questionId = q.id " +
            "WHERE eq.examId = :examId AND eq.isCorrect = 0")
    LiveData<List<Question>> getWrongQuestionsWithDetail(int examId);

    // Đồng bộ (cho Controller)
    @Query("SELECT q.* FROM exam_question AS eq " +
            "INNER JOIN questions AS q ON eq.questionId = q.id " +
            "WHERE eq.examId = :examId AND eq.isCorrect = 0")
    List<Question> getWrongQuestionsWithDetailSync(int examId);
}
