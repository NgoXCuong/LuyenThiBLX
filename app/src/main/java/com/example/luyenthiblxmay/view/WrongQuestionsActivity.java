package com.example.luyenthiblxmay.view;

import android.os.Bundle;
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

        questionList.addAll(WrongQuestionCache.getWrongQuestions());
        if (questionList.isEmpty()) {
            Toast.makeText(this, "Bạn không có câu sai nào!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new WrongQuestionAdapter(this, questionList);
        recyclerView.setAdapter(adapter);
    }
}
