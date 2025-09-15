package com.example.luyenthiblxmay.controller;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.luyenthiblxmay.dao.ExamQuestionDao;
import com.example.luyenthiblxmay.dao.ExamResultDao;
import com.example.luyenthiblxmay.dao.QuestionDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.model.ExamQuestion;
import com.example.luyenthiblxmay.model.ExamResult;
import com.example.luyenthiblxmay.model.Question;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExamController {
    private final ExamResultDao examResultDao;
    private final ExamQuestionDao examQuestionDao;
    private final QuestionDao questionDao;
    private final ExecutorService executorService;

    public ExamController(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        examResultDao = db.examResultDao();
        examQuestionDao = db.examQuestionDao();
        questionDao = db.questionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Thêm ExamResult (tạo bài thi)
    public void addExamResult(ExamResult examResult) {
        executorService.execute(() -> examResultDao.insertExamResult(examResult));
    }

    public long insertExamResult(ExamResult result) {
        return examResultDao.insertExamResult(result); // Trả về ID mới
    }


    // Cập nhật ExamResult (ví dụ cập nhật số câu đúng)
    public void updateExamResult(ExamResult examResult) {
        executorService.execute(() -> examResultDao.updateExamResult(examResult));
    }

    // Lấy ExamResult theo id
    public LiveData<ExamResult> getExamResultById(int id) {
        return examResultDao.getExamResultById(id);
    }

    // Lấy tất cả ExamResult của user
    public LiveData<List<ExamResult>> getAllExamResultsByUser(int userId) {
        return examResultDao.getAllExamResultsByUser(userId);
    }

    // Thêm danh sách ExamQuestion (câu trả lời trong bài thi)
    public void addExamQuestions(List<ExamQuestion> examQuestions) {
        executorService.execute(() -> examQuestionDao.insertExamQuestions(examQuestions));
    }

    // Lấy danh sách câu hỏi của bài thi theo examId
    public LiveData<List<ExamQuestion>> getQuestionsByExamId(int examId) {
        return examQuestionDao.getQuestionsByExamId(examId);
    }

    // Lấy danh sách câu sai của bài thi
    public LiveData<List<ExamQuestion>> getWrongQuestionsByExamId(int examId) {
        return examQuestionDao.getWrongQuestionsByExamId(examId);
    }

    // insertExamResult
//    public long insertExamResult(ExamResult examResult) {
//        final long[] id = new long[1];
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        service.submit(() -> id[0] = examResultDao.insertExamResult(examResult));
//        return id[0]; // lưu ý: chỉ dùng khi chạy thread đồng bộ
//    }

    // insertExamQuestions
    public void insertExamQuestions(List<ExamQuestion> examQuestions) {
        executorService.execute(() -> examQuestionDao.insertExamQuestions(examQuestions));
    }

    // getRandomQuestions
    public LiveData<List<Question>> getRandomQuestions() {
        return questionDao.getRandomQuestions();
    }
}
