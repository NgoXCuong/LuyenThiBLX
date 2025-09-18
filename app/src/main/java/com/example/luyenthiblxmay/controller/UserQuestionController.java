package com.example.luyenthiblxmay.controller;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.luyenthiblxmay.dao.QuestionDao;
import com.example.luyenthiblxmay.dao.UserDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.dao.UserQuestionDao;
import com.example.luyenthiblxmay.model.Question;
import com.example.luyenthiblxmay.model.User;
import com.example.luyenthiblxmay.model.UserQuestion;

import java.util.List;

public class UserQuestionController {

    private final UserQuestionDao userQuestionDao;
    private final UserDao userDao;
    private final QuestionDao questionDao;

    public UserQuestionController(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "app_thi_blx")
                .allowMainThreadQueries() // Demo, thực tế nên dùng Async
                .build();
        userQuestionDao = db.userQuestionDao();
        userDao = db.userDao();          // thêm dòng này
        questionDao = db.questionDao();  // thêm dòng này
    }


    // Lưu câu trả lời của user (REPLACE)
    public void saveAnswer(int userId, int questionId, String selectedAnswer, boolean isCorrect) {
        new Thread(() -> {
            User user = userDao.getUserById(userId);
            Question question = questionDao.getQuestionById(questionId);
            if (user == null) {
                Log.e("UserQuestionController", "Cannot save answer: userId=" + userId + " not found!");
                return;
            }
            if (question == null) {
                Log.e("UserQuestionController", "Cannot save answer: questionId=" + questionId + " not found!");
                return;
            }

            try {
                UserQuestion uq = new UserQuestion(userId, questionId, true, selectedAnswer, isCorrect, System.currentTimeMillis());
                userQuestionDao.insert(uq);
                Log.d("UserQuestionController", "Saved answer: userId=" + userId + ", questionId=" + questionId + ", answer=" + selectedAnswer);
            } catch (Exception e) {
                Log.e("UserQuestionController", "Insert failed", e);
            }
        }).start();
    }



    // Lấy tất cả câu hỏi của user theo category (LiveData)
    public LiveData<List<UserQuestion>> getUserQuestionsByCategory(int userId, String category) {
        return userQuestionDao.getUserQuestionsByCategory(userId, category);
    }

    // Lấy tổng số câu đã trả lời đúng theo category (LiveData)
    public LiveData<Integer> getCorrectCount(int userId, String category) {
        return userQuestionDao.getCorrectCountByUserAndCategory(userId, category);
    }

    // Lấy tổng số câu đã trả lời theo category (LiveData)
    public LiveData<Integer> getAnsweredCount(int userId, String category) {
        return userQuestionDao.getAnsweredCountByUserAndCategory(userId, category);
    }

    // Kiểm tra user đã trả lời câu hỏi chưa (LiveData)
    public LiveData<UserQuestion> getUserQuestionLive(int userId, int questionId) {
        return userQuestionDao.getUserQuestionLive(userId, questionId);
    }

    // Xóa toàn bộ tiến độ user
    public void resetProgress(int userId) {
        userQuestionDao.deleteAllByUser(userId);
    }
    // Trong UserQuestionController.java
    public LiveData<List<UserQuestion>> getUserQuestionsByUser(int userId) {
        return userQuestionDao.getUserQuestionsByUser(userId);
    }

}
