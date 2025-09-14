package com.example.luyenthiblxmay.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luyenthiblxmay.MainActivity;
import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.controller.ExamController;
import com.example.luyenthiblxmay.model.ExamQuestion;
import com.example.luyenthiblxmay.model.ExamResult;
import com.example.luyenthiblxmay.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamTestActivity extends AppCompatActivity {

    private TextView tvQuestionNumber, tvQuestion;
    private RadioGroup rgOptions;
    private Button btnNext;

    private ExamController examController;
    private List<Question> questionList = new ArrayList<>();
    private List<ExamQuestion> examAnswers = new ArrayList<>();

    private int currentIndex = 0;
    private long currentExamId;
    private int currentUserId = 1; // user hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_test);

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        btnNext = findViewById(R.id.btnNext);

        // Nút back
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        examController = new ExamController(getApplication());

        // 1️⃣ Tạo ExamResult mới
        ExamResult examResult = new ExamResult();
        examResult.setUserId(currentUserId);
        examResult.setCategory("A1");
        examResult.setTotalQuestions(25);
        examResult.setCorrectAnswers(0);
        examResult.setTakenAt(System.currentTimeMillis());

        new Thread(() -> currentExamId = examController.insertExamResult(examResult)).start();

        // 2️⃣ Lấy 25 câu hỏi ngẫu nhiên
        examController.getRandomQuestions().observe(this, questions -> {
            if (questions != null && !questions.isEmpty()) {
                questionList = questions;
                showQuestion(currentIndex);
            }
        });

        // 3️⃣ Nút Next
        btnNext.setOnClickListener(v -> {
            saveAnswer();
            currentIndex++;
            if (currentIndex < questionList.size()) {
                showQuestion(currentIndex);
            } else {
                finishExam();
            }
        });
    }

    private void showQuestion(int index) {
        Question q = questionList.get(index);
        tvQuestionNumber.setText("Câu " + (index + 1) + "/" + questionList.size());
        tvQuestion.setText(q.getQuestion());

        Map<String, String> options = q.getOptions();
        ((RadioButton) findViewById(R.id.rbOptionA)).setText("A. " + options.get("A"));
        ((RadioButton) findViewById(R.id.rbOptionB)).setText("B. " + options.get("B"));
        ((RadioButton) findViewById(R.id.rbOptionC)).setText("C. " + options.get("C"));
        ((RadioButton) findViewById(R.id.rbOptionD)).setText("D. " + options.get("D"));

        rgOptions.clearCheck();
    }

    private void saveAnswer() {
        Question q = questionList.get(currentIndex);
        int selectedId = rgOptions.getCheckedRadioButtonId();
        String selected = "";
        if (selectedId == R.id.rbOptionA) selected = "A";
        else if (selectedId == R.id.rbOptionB) selected = "B";
        else if (selectedId == R.id.rbOptionC) selected = "C";
        else if (selectedId == R.id.rbOptionD) selected = "D";

        boolean correct = selected.equals(q.getAnswer());

        ExamQuestion eq = new ExamQuestion();
        eq.setExamId((int) currentExamId);
        eq.setQuestionId(q.getId());
        eq.setSelectedAnswer(selected);
        eq.setCorrect(correct);

        examAnswers.add(eq);
    }

    private void finishExam() {
        new Thread(() -> {
            // Lưu tất cả câu trả lời
            examController.insertExamQuestions(examAnswers);

            // Tính số câu đúng
            int correctCount = 0;
            for (ExamQuestion eq : examAnswers) {
                if (eq.isCorrect()) correctCount++;
            }

            // Cập nhật kết quả vào ExamResult
            ExamResult result = new ExamResult();
            result.setId((int) currentExamId);
            result.setUserId(currentUserId);
            result.setTotalQuestions(examAnswers.size());
            result.setCorrectAnswers(correctCount);
            result.setTakenAt(System.currentTimeMillis());
            result.setCategory("A1");

            examController.updateExamResult(result);

            int finalCorrectCount = correctCount;

            runOnUiThread(() -> {
                Toast.makeText(this,
                        "Kết thúc bài thi. Đúng: " + finalCorrectCount + "/" + examAnswers.size(),
                        Toast.LENGTH_LONG).show();

                // 👇 Quay về MainActivity
                Intent intent = new Intent(ExamTestActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // đóng ExamTestActivity
            });
        }).start();
    }


}
