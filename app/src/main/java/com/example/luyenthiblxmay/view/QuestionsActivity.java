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

        recyclerView = findViewById(R.id.recyclerViewQuestionsByCategory);
        ImageView backButton = findViewById(R.id.backButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Nhận category từ Intent
        String category = getIntent().getStringExtra("category");

        questionController = new QuestionController(this);

        // Adapter khởi tạo với list rỗng
        adapter = new QuestionAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Lấy danh sách câu hỏi theo category (async)
        questionController.getQuestionsByCategory(category, result -> {
            runOnUiThread(() -> {
                adapter.setQuestions(result);
                adapter.notifyDataSetChanged();
            });
        });

        backButton.setOnClickListener(v -> finish());
    }
}
