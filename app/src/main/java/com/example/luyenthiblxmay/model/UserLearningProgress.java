package com.example.luyenthiblxmay.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_learning_progress")
public class UserLearningProgress {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String category; // chủ đề
    private int lastQuestionId; // câu cuối cùng đã học
    private long updatedAt;

    // getters và setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getLastQuestionId() { return lastQuestionId; }
    public void setLastQuestionId(int lastQuestionId) { this.lastQuestionId = lastQuestionId; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
