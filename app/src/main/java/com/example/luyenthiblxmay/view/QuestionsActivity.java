package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.adapter.QuestionAdapter;
import com.example.luyenthiblxmay.controller.QuestionController;
import com.example.luyenthiblxmay.model.Question;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private QuestionController questionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Ánh xạ View
        recyclerView = findViewById(R.id.recyclerViewQuestionsByCategory);
        ImageView backButton = findViewById(R.id.backButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Nhận category từ Intent
        String category = getIntent().getStringExtra("category");

        // Khởi tạo Controller (dùng LiveData)
        questionController = new QuestionController(getApplication());

        // Khởi tạo adapter với danh sách rỗng
        adapter = new QuestionAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Quan sát dữ liệu từ Room qua LiveData
        if (category != null && !category.isEmpty()) {
            // Nếu có category → lấy câu hỏi theo category
            questionController.getQuestionsByCategory(category).observe(this, questions -> {
                adapter.setQuestions(questions);
            });
        } else {
            // Nếu không có category → load tất cả câu hỏi
            questionController.getAllQuestions().observe(this, questions -> {
                adapter.setQuestions(questions);
            });
        }
        // Nút quay lại
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Lưu tất cả câu hỏi hiện tại vào DB khi thoát
        if (adapter != null && adapter.getQuestions() != null) {
            for (Question q : adapter.getQuestions()) {
                questionController.updateQuestion(q);
            }
        }
    }
}
