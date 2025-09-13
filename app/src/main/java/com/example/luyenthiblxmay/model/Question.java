package com.example.luyenthiblxmay.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.luyenthiblxmay.utils.OptionsConverter;

import java.util.Map;

@Entity(tableName = "questions")
public class Question {

    @PrimaryKey
    private int id;

    private String question;

    @TypeConverters(OptionsConverter.class)
    private Map<String, String> options;

    private String answer;
    private String explanation;
    private String category;
    private String image;

    // Trạng thái người dùng
    private boolean isAnswered;
    private String selectedAnswer;

    public Question() {}

    // Constructor dùng trong code, Room sẽ bỏ qua
    @Ignore
    public Question(int id, String question, Map<String, String> options, String answer, String explanation, String category, String image, boolean isAnswered, String selectedAnswer) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.explanation = explanation;
        this.category = category;
        this.image = image;
        this.isAnswered = isAnswered;
        this.selectedAnswer = selectedAnswer;
    }

    // Getter & Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}
