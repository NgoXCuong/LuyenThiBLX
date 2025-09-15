package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.adapter.WrongQuestionAdapter;
import com.example.luyenthiblxmay.controller.ExamController;
import com.example.luyenthiblxmay.model.Question;

import java.util.ArrayList;
import java.util.List;

public class WrongQuestionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WrongQuestionAdapter adapter;
    private List<Question> questionList = new ArrayList<>();
    private ExamController examController;
    private int examId; // int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_questions);

        examController = new ExamController(getApplication());

        examId = getIntent().getIntExtra("examId", -1);
        if (examId == -1) {
            Toast.makeText(this, "Không có examId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewWrongQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WrongQuestionAdapter(this, questionList);
        recyclerView.setAdapter(adapter);

        loadWrongQuestions();
    }

    private void loadWrongQuestions() {
        examController.getWrongQuestionsWithDetail(examId).observe(this, questions -> {
            questionList.clear();
            if (questions != null) questionList.addAll(questions);
            adapter.notifyDataSetChanged();
        });
    }
}
