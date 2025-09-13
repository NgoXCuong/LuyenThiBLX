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

public class ModuleActivity extends AppCompatActivity {

    private ProgressBar progressBook, progressEthics, progressDrive, progressSigns, progressSimulation;
    private TextView txtProgressBook, txtProgressEthics, txtProgressDrive, txtProgressSigns, txtProgressSimulation;
    private ImageView backButton;
    private QuestionController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        controller = new QuestionController(this);

        // Back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Progress bars
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

        // Card click listeners → mở QuestionsByCategoryActivity theo module
        findViewById(R.id.cardBook).setOnClickListener(v -> openQuestionActivity("KHÁI NIỆM VÀ QUY TẮC"));
        findViewById(R.id.cardEthics).setOnClickListener(v -> openQuestionActivity("Văn hóa và đạo đức lái xe"));
        findViewById(R.id.cardDrive).setOnClickListener(v -> openQuestionActivity("Kỹ thuật lái xe"));
        findViewById(R.id.cardSigns).setOnClickListener(v -> openQuestionActivity("Biển báo"));
        findViewById(R.id.cardSimulation).setOnClickListener(v -> openQuestionActivity("Sa hinh"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật progress động từng category
        updateCategoryProgress("KHÁI NIỆM VÀ QUY TẮC", progressBook, txtProgressBook);
        updateCategoryProgress("Văn hóa và đạo đức lái xe", progressEthics, txtProgressEthics);
        updateCategoryProgress("Kỹ thuật lái xe", progressDrive, txtProgressDrive);
        updateCategoryProgress("Biển báo", progressSigns, txtProgressSigns);
        updateCategoryProgress("Sa hinh", progressSimulation, txtProgressSimulation);
    }

    private void updateCategoryProgress(String category, ProgressBar progressBar, TextView textView) {
        controller.getQuestionsByCategory(category, questions -> {
            int done = 0;
            for (Question q : questions) {
                if (q.isAnswered()) done++;
            }

            final int totalQuestions = questions.size();
            final int doneCount = done;

            runOnUiThread(() -> {
                progressBar.setMax(totalQuestions);
                progressBar.setProgress(doneCount);
                textView.setText(doneCount + "/" + totalQuestions);
            });
        });
    }

    // ✅ Mở Activity hiển thị câu hỏi theo category
    private void openQuestionActivity(String category) {
        Intent intent = new Intent(this, QuestionsActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
