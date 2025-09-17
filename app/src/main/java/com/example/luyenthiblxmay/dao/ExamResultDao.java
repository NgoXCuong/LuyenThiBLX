package com.example.luyenthiblxmay.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.ExamResult;

import java.util.List;

@Dao
public interface ExamResultDao {
    @Insert
//    long insertExamResult(ExamResult examResult);
    long insertExamResult(ExamResult result);

    @Update
    void updateExamResult(ExamResult examResult);

    @Query("SELECT * FROM exam_result WHERE id = :id")
    LiveData<ExamResult> getExamResultById(int id);

    // ✅ Lấy tất cả bài thi của user, sắp xếp theo thời gian mới nhất
    @Query("SELECT * FROM exam_result WHERE userId = :userId ORDER BY takenAt DESC")
    LiveData<List<ExamResult>> getAllExamResultsByUser(int userId);
}
