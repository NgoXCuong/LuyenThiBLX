package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.adapter.QuestionAdapter;
import com.example.luyenthiblxmay.controller.QuestionController;
import com.example.luyenthiblxmay.controller.UserQuestionController;
import com.example.luyenthiblxmay.model.UserQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private QuestionController questionController;
    private UserQuestionController userQuestionController;
    private int userId;
    private String category;

    private TextView tvProgress, tvCorrect;
    private ProgressBar progressBar;

    private final Map<Integer, UserQuestion> userProgressMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        recyclerView = findViewById(R.id.recyclerViewQuestionsByCategory);
        tvProgress = findViewById(R.id.tvProgress);
        tvCorrect = findViewById(R.id.tvCorrect);
        progressBar = findViewById(R.id.progressBar);
        ImageView backButton = findViewById(R.id.backButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ Intent
        category = getIntent().getStringExtra("category");
        userId = getIntent().getIntExtra("user_id", -1);

        if (userId == -1) {
            finish(); // Không có user hợp lệ → thoát
            return;
        }

        questionController = new QuestionController(getApplication());
        userQuestionController = new UserQuestionController(this);

        // Khởi tạo adapter
        adapter = new QuestionAdapter(
                this,
                new ArrayList<>(),
                userId,
                userProgressMap,
                this::updateProgress // chỉ gọi updateProgress() khi chọn đáp án
        );
        recyclerView.setAdapter(adapter);

        loadUserProgress();

        // Load câu hỏi
        if (category != null && !category.isEmpty()) {
            questionController.getQuestionsByCategory(category).observe(this, questions -> {
                adapter.setQuestions(questions, userProgressMap);
                updateProgress();
            });
        } else {
            questionController.getAllQuestions().observe(this, questions -> {
                adapter.setQuestions(questions, userProgressMap);
                updateProgress();
            });
        }

        backButton.setOnClickListener(v -> finish());
    }

    private void loadUserProgress() {
        userQuestionController.getUserQuestionsByCategory(userId, category != null ? category : "")
                .observe(this, userQuestions -> {
                    userProgressMap.clear();
                    for (UserQuestion uq : userQuestions) {
                        userProgressMap.put(uq.getQuestionId(), uq);
                    }
                    adapter.notifyDataSetChanged();
                    updateProgress();
                });
    }

    private void updateProgress() {
        int total = adapter.getItemCount();
        int answered = 0;
        int correct = 0;

        for (UserQuestion uq : userProgressMap.values()) {
            if (uq.isAnswered()) {
                answered++;
                if (uq.isCorrect()) correct++;
            }
        }

        tvProgress.setText("Đã làm: " + answered + " / " + total);
        tvCorrect.setText("Đúng: " + correct + " / " + total);
        progressBar.setMax(total);
        progressBar.setProgress(answered);
    }
}
