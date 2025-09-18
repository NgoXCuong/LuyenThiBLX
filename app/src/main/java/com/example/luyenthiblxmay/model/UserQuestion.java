package com.example.luyenthiblxmay.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
        tableName = "user_question",
        primaryKeys = {"userId", "questionId"},
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Question.class, parentColumns = "id", childColumns = "questionId", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("userId"), @Index("questionId")}
)
public class UserQuestion {

    private int userId;
    private int questionId;
    private boolean isAnswered;
    private String selectedAnswer;
    private boolean isCorrect;
    private long answeredAt;

    public UserQuestion() {} // bắt buộc Room

    @Ignore
    public UserQuestion(int userId, int questionId, boolean isAnswered, String selectedAnswer, boolean isCorrect, long answeredAt) {
        this.userId = userId;
        this.questionId = questionId;
        this.isAnswered = isAnswered;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
        this.answeredAt = answeredAt;
    }

    // Getter & Setter
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

//    public String getCategory() { return category; }
//    public void setCategory(String category) { this.category = category; }

    public boolean isAnswered() { return isAnswered; }
    public void setAnswered(boolean answered) { this.isAnswered = answered; }

    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }

    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { this.isCorrect = correct; }

    public long getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(long answeredAt) { this.answeredAt = answeredAt; }
}
