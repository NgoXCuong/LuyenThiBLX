package com.example.luyenthiblxmay.controller;

import android.content.Context;

import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.dao.QuestionDao;
import com.example.luyenthiblxmay.model.Question;

import java.util.List;

public class QuestionController {

    private QuestionDao questionDao;

    public QuestionController(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        questionDao = db.questionDao();
    }

    /**
     * Lấy tất cả câu hỏi
     */
    public List<Question> getAllQuestions() {
        return questionDao.getAllQuestions();
    }

    /**
     * Lấy câu hỏi theo category
     */
    public List<Question> getQuestionsByCategory(String category) {
        return questionDao.getQuestionsByCategory(category);
    }

    /**
     * Cập nhật trạng thái đã trả lời và câu trả lời người dùng
     */
    public void updateAnswer(int questionId, String selectedAnswer, boolean isAnswered) {
        Question question = questionDao.getQuestionById(questionId);
        if (question != null) {
            question.setSelectedAnswer(selectedAnswer);
            question.setAnswered(isAnswered);
            questionDao.updateQuestion(question);
        }
    }

    /**
     * Tính tiến trình học tập (số câu trả lời / tổng câu) theo category
     */
    public int getProgressByCategory(String category) {
        List<Question> questions = questionDao.getQuestionsByCategory(category);
        if (questions == null || questions.isEmpty()) return 0;

        int answeredCount = 0;
        for (Question q : questions) {
            if (q.isAnswered()) answeredCount++;
        }

        // Trả về phần trăm
        return (int) ((answeredCount * 100.0) / questions.size());
    }
}

