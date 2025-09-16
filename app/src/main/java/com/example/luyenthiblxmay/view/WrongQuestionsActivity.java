package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.adapter.WrongQuestionAdapter;
import com.example.luyenthiblxmay.model.Question;
import com.example.luyenthiblxmay.utils.WrongQuestionCache;

import java.util.ArrayList;
import java.util.List;

public class WrongQuestionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WrongQuestionAdapter adapter;
    private List<Question> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_questions);

        recyclerView = findViewById(R.id.recyclerViewWrongQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Nút back
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // TextView thông báo
        TextView tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        questionList.addAll(WrongQuestionCache.getWrongQuestions());

        if (questionList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            adapter = new WrongQuestionAdapter(this, questionList);
            recyclerView.setAdapter(adapter);
            tvEmptyMessage.setVisibility(View.GONE);
        }
    }
}
