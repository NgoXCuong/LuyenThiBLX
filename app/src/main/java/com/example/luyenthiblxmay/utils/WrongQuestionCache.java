package com.example.luyenthiblxmay.utils;

import com.example.luyenthiblxmay.model.Question;

import java.util.ArrayList;
import java.util.List;

public class WrongQuestionCache {
    private static List<Question> wrongQuestions = new ArrayList<>();

    public static void clear() {
        wrongQuestions.clear();
    }

    public static void addQuestion(Question q) {
        wrongQuestions.add(q);
    }

    // 👉 thêm hàm addWrongQuestion
    public static void addWrongQuestion(Question q) {
        if (q != null && !wrongQuestions.contains(q)) {
            wrongQuestions.add(q);
        }
    }

    public static void addAll(List<Question> questions) {
        if (questions != null) {
            for (Question q : questions) {
                addWrongQuestion(q); // dùng hàm vừa viết để tránh trùng
            }
        }
    }

    public static List<Question> getWrongQuestions() {
        return new ArrayList<>(wrongQuestions);
    }
}
