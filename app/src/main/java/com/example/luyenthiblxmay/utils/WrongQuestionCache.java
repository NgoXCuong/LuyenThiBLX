package com.example.luyenthiblxmay.utils;

import com.example.luyenthiblxmay.model.Question;

import java.util.ArrayList;
import java.util.List;

public class WrongQuestionCache {
    private static List<Question> wrongQuestions = new ArrayList<>();

    public static void clear() {
        wrongQuestions.clear();
    }

    // 👉 thêm hàm addWrongQuestion
    public static void addWrongQuestion(Question q) {
        if (q != null && !wrongQuestions.contains(q)) {
            wrongQuestions.add(q);
        }
    }

    public static List<Question> getWrongQuestions() {
        return new ArrayList<>(wrongQuestions);
    }
}
