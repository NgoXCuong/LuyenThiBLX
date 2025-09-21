package com.example.luyenthiblxmay.controller;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.luyenthiblxmay.dao.ExamQuestionDao;
import com.example.luyenthiblxmay.dao.ExamResultDao;
import com.example.luyenthiblxmay.dao.QuestionDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.model.ExamQuestion;
import com.example.luyenthiblxmay.model.ExamResult;
import com.example.luyenthiblxmay.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    // Thêm ExamResult trong background (không trả về id)
    public void addExamResult(ExamResult examResult) {
        executorService.execute(() -> examResultDao.insertExamResult(examResult));
    }

    // Cập nhật ExamResult
    public void updateExamResult(ExamResult examResult) {
        executorService.execute(() -> examResultDao.updateExamResult(examResult));
    }

    // Thêm ExamResult **đồng bộ** và trả về int id
    public int insertExamResultSync(ExamResult examResult) {
        try {
            Future<Long> future = executorService.submit(() ->
                    examResultDao.insertExamResult(examResult)
            );
            Long idLong = future.get(); // lấy Long từ Room
            if (idLong != null) {
                return idLong.intValue(); // convert sang int
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Thêm danh sách ExamQuestion
    public void insertExamQuestions(List<ExamQuestion> examQuestions) {
        executorService.execute(() -> examQuestionDao.insertExamQuestions(examQuestions));
    }

    // Lấy câu hỏi ngẫu nhiên
    public LiveData<List<Question>> getRandomQuestions() {
        return questionDao.getRandomQuestions();
    }


    public LiveData<List<Question>> getExamQuestions() {
        MediatorLiveData<List<Question>> result = new MediatorLiveData<>();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Question> examQuestions = new ArrayList<>();

            examQuestions.addAll(questionDao.getRandomByCategorySync("Khái niệm và quy tắc", 9));
            examQuestions.addAll(questionDao.getRandomByCategorySync("Biển báo", 8));
            examQuestions.addAll(questionDao.getRandomByCategorySync("Sa hình", 6));
            examQuestions.addAll(questionDao.getRandomByCategorySync("Kỹ thuật lái xe", 1));
            examQuestions.addAll(questionDao.getRandomByCategorySync("Văn hóa và đạo đức lái xe", 1));

            Collections.shuffle(examQuestions); // trộn câu hỏi

            new Handler(Looper.getMainLooper()).post(() -> result.setValue(examQuestions));
        });

        return result;
    }
}
