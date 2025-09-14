package com.example.luyenthiblxmay.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "exam_question",
        foreignKeys = {
                @ForeignKey(entity = ExamResult.class,
                        parentColumns = "id",
                        childColumns = "examId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Question.class,
                        parentColumns = "id",
                        childColumns = "questionId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("examId"), @Index("questionId")}) // ✅ Thêm index cho các foreign key
public class ExamQuestion {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int examId;
    private int questionId;
    private String selectedAnswer;
    private boolean isCorrect;

    // getters và setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }

    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
}
