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
}
