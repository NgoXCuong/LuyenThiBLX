package com.example.luyenthiblxmay.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.ExamQuestion;

import java.util.List;

@Dao
public interface ExamQuestionDao {

    @Insert
    void insertExamQuestions(List<ExamQuestion> examQuestions);

    @Query("SELECT * FROM exam_question WHERE examId = :examId")
    LiveData<List<ExamQuestion>> getQuestionsByExamId(int examId);

    @Query("SELECT * FROM exam_question WHERE examId = :examId AND isCorrect = 0")
    LiveData<List<ExamQuestion>> getWrongQuestionsByExamId(int examId);
}
