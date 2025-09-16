package com.example.luyenthiblxmay.controller;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.luyenthiblxmay.dao.QuestionDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.model.Question;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuestionController {
    private final QuestionDao questionDao;
    private final ExecutorService executorService;

    public QuestionController(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        questionDao = db.questionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Thêm
    public void addQuestion(Question question) {
        executorService.execute(() -> questionDao.insert(question));
    }

    // Cập nhật
    public void updateQuestion(Question question) {
        executorService.execute(() -> questionDao.update(question));
    }

    // Xóa
    public void deleteQuestion(Question question) {
        executorService.execute(() -> questionDao.delete(question));
    }

    public void deleteAllQuestions() {
        executorService.execute(questionDao::deleteAll);
    }

    // Lấy tất cả câu hỏi (LiveData)
    public LiveData<List<Question>> getAllQuestions() {
        return questionDao.getAllQuestions();
    }

    // Lấy theo category
    public LiveData<List<Question>> getQuestionsByCategory(String category) {
        return questionDao.getQuestionsByCategory(category);
    }

    public void markQuestionAnswered(int id, boolean answered) {
        executorService.execute(() -> {
            questionDao.updateAnswered(id, answered);
        });
    }
}
