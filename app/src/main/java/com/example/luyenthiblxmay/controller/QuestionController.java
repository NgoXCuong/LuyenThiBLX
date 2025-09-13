package com.example.luyenthiblxmay.controller;

import android.content.Context;

import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.dao.QuestionDao;
import com.example.luyenthiblxmay.model.Question;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuestionController {
    private final QuestionDao questionDao;
    private final ExecutorService executorService;

    public interface Callback<T> {
        void onResult(T result);
    }

    public QuestionController(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        questionDao = db.questionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void addQuestion(Question question) {
        executorService.execute(() -> questionDao.insert(question));
    }

    public void updateQuestion(Question question) {
        executorService.execute(() -> questionDao.update(question));
    }

    public void deleteQuestion(Question question) {
        executorService.execute(() -> questionDao.delete(question));
    }

    public void deleteAllQuestions() {
        executorService.execute(questionDao::deleteAll);
    }

    // ✅ Lấy tất cả question (async)
    public void getAllQuestions(Callback<List<Question>> callback) {
        executorService.execute(() -> {
            List<Question> questions = questionDao.getAllQuestions();
            callback.onResult(questions);
        });
    }

    // ✅ Lấy question theo category (async)
    public void getQuestionsByCategory(String category, Callback<List<Question>> callback) {
        executorService.execute(() -> {
            List<Question> questions = questionDao.getQuestionsByCategory(category);
            callback.onResult(questions);
        });
    }
}
