package com.example.luyenthiblxmay.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// UserQuestion.java
@Entity(
        tableName = "user_question",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Question.class, parentColumns = "id", childColumns = "questionId", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index("userId"),
                @Index("questionId")
        }
)
public class UserQuestion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int questionId;
    private boolean isAnswered;
    private String selectedAnswer;
    private long answeredAt;

    // getters và sette
    public UserQuestion() {
    }

    @Ignore
    public UserQuestion(int id, int userId, int questionId, boolean isAnswered, String selectedAnswer, long answeredAt) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.isAnswered = isAnswered;
        this.selectedAnswer = selectedAnswer;
        this.answeredAt = answeredAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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

    public long getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(long answeredAt) {
        this.answeredAt = answeredAt;
    }
}
