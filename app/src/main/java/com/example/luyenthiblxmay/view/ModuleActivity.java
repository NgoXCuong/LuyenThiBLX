package com.example.luyenthiblxmay.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.controller.QuestionController;
import com.example.luyenthiblxmay.model.Question;

import java.util.List;

public class ModuleActivity extends AppCompatActivity {
    private ProgressBar progressBook, progressEthics, progressDrive, progressSigns, progressSimulation;
    private TextView txtProgressBook, txtProgressEthics, txtProgressDrive, txtProgressSigns, txtProgressSimulation;
    private ImageView backButton;
    private QuestionController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        controller = new QuestionController(getApplication());

        // Back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Progress bars & TextViews
        progressBook = findViewById(R.id.progressBarBook);
        txtProgressBook = findViewById(R.id.txtProgressBook);

        progressEthics = findViewById(R.id.progressBarEthics);
        txtProgressEthics = findViewById(R.id.txtProgressEthics);

        progressDrive = findViewById(R.id.progressBarDrive);
        txtProgressDrive = findViewById(R.id.txtProgressDrive);

        progressSigns = findViewById(R.id.progressBarSigns);
        txtProgressSigns = findViewById(R.id.txtProgressSigns);

        progressSimulation = findViewById(R.id.progressBarSimulation);
        txtProgressSimulation = findViewById(R.id.txtProgressSimulation);

        // Quan sát LiveData cho từng category
        observeCategoryProgress("Khái niệm và quy tắc", progressBook, txtProgressBook);
        observeCategoryProgress("Văn hóa và đạo đức lái xe", progressEthics, txtProgressEthics);
        observeCategoryProgress("Kỹ thuật lái xe", progressDrive, txtProgressDrive);
        observeCategoryProgress("Biển báo", progressSigns, txtProgressSigns);
        observeCategoryProgress("Sa hình", progressSimulation, txtProgressSimulation);

        // Card click listeners → mở QuestionsActivity theo module
        findViewById(R.id.cardBook).setOnClickListener(v -> openQuestionActivity("Khái niệm và quy tắc"));
        findViewById(R.id.cardEthics).setOnClickListener(v -> openQuestionActivity("Văn hóa và đạo đức lái xe"));
        findViewById(R.id.cardDrive).setOnClickListener(v -> openQuestionActivity("Kỹ thuật lái xe"));
        findViewById(R.id.cardSigns).setOnClickListener(v -> openQuestionActivity("Biển báo"));
        findViewById(R.id.cardSimulation).setOnClickListener(v -> openQuestionActivity("Sa hình"));
    }

    // Quan sát LiveData, tự update progress khi dữ liệu thay đổi
    private void observeCategoryProgress(String category, ProgressBar progressBar, TextView textView) {
        controller.getQuestionsByCategory(category).observe(this, questions -> {
            int done = 0;
            for (Question q : questions) {
                if (q.isAnswered()) done++;
            }
            int total = questions.size();

            progressBar.setMax(total);   // max = tổng số câu
            progressBar.setProgress(done); // progress = số câu đã làm

            textView.setText(done + "/" + total); // hiển thị kiểu 15/30
        });
    }

    // Mở Activity hiển thị câu hỏi theo category
    private void openQuestionActivity(String category) {
        Intent intent = new Intent(this, QuestionsActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
