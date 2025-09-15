package com.example.luyenthiblxmay.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.luyenthiblxmay.MainActivity;
import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.controller.ExamController;
import com.example.luyenthiblxmay.model.ExamQuestion;
import com.example.luyenthiblxmay.model.ExamResult;
import com.example.luyenthiblxmay.model.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamTestActivity extends AppCompatActivity {
    private TextView tvQuestionNumber, tvQuestion;
    private RadioGroup rgOptions;
    private Button btnNext, btnSubmit, btnRetryWrong;
    private ExamController examController;
    private List<Question> questionList = new ArrayList<>();
    private List<ExamQuestion> examAnswers = new ArrayList<>();
    private int currentIndex = 0;
    private int currentExamId; // dùng int
    private int currentUserId = 1;
    private int correctCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_test);

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnRetryWrong = findViewById(R.id.btnRetryWrong);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        examController = new ExamController(getApplication());

        // Tạo ExamResult và load câu hỏi
        new Thread(() -> {
            ExamResult examResult = new ExamResult();
            examResult.setUserId(currentUserId);
            examResult.setCategory("A1");
            examResult.setTotalQuestions(25);
            examResult.setCorrectAnswers(0);
            examResult.setTakenAt(System.currentTimeMillis());

            currentExamId = (int) examController.insertExamResultSync(examResult); // lưu int

            runOnUiThread(() -> {
                examController.getRandomQuestions().observe(ExamTestActivity.this, questions -> {
                    if (questions != null && !questions.isEmpty()) {
                        questionList = questions;
                        showQuestion(currentIndex);
                    }
                });
            });
        }).start();

        btnNext.setOnClickListener(v -> {
            saveAnswer();
            currentIndex++;
            if (currentIndex < questionList.size()) showQuestion(currentIndex);
            else finishExam();
        });

        btnSubmit.setOnClickListener(v -> {
            saveAnswer();
            finishExam();
        });

        btnRetryWrong.setOnClickListener(v -> {
            Intent intent = new Intent(this, WrongQuestionsActivity.class);
            intent.putExtra("examId", currentExamId);
            startActivity(intent);
        });
    }

    private void showQuestion(int index) {
        Question q = questionList.get(index);

        tvQuestionNumber.setText("Câu " + (index + 1) + "/" + questionList.size());
        tvQuestion.setText(q.getQuestion());

        ImageView imgQuestion = findViewById(R.id.imgQuestion);
        if (q.getImage() != null && !q.getImage().isEmpty()) {
            try (InputStream is = getAssets().open(q.getImage())) {
                Drawable drawable = Drawable.createFromStream(is, null);
                imgQuestion.setImageDrawable(drawable);
                imgQuestion.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                imgQuestion.setVisibility(View.GONE);
                e.printStackTrace();
            }
        } else {
            imgQuestion.setVisibility(View.GONE);
        }

        RadioButton rbA = findViewById(R.id.rbOptionA);
        RadioButton rbB = findViewById(R.id.rbOptionB);
        RadioButton rbC = findViewById(R.id.rbOptionC);
        RadioButton rbD = findViewById(R.id.rbOptionD);

        rbA.setVisibility(View.GONE);
        rbB.setVisibility(View.GONE);
        rbC.setVisibility(View.GONE);
        rbD.setVisibility(View.GONE);

        Map<String, String> options = q.getOptions();
        if (options != null) {
            if (options.get("A") != null) { rbA.setText("A. " + options.get("A")); rbA.setVisibility(View.VISIBLE);}
            if (options.get("B") != null) { rbB.setText("B. " + options.get("B")); rbB.setVisibility(View.VISIBLE);}
            if (options.get("C") != null) { rbC.setText("C. " + options.get("C")); rbC.setVisibility(View.VISIBLE);}
            if (options.get("D") != null) { rbD.setText("D. " + options.get("D")); rbD.setVisibility(View.VISIBLE);}
        }

        rgOptions.clearCheck();
    }

    private void saveAnswer() {
        if (currentIndex >= questionList.size()) return;

        Question q = questionList.get(currentIndex);
        int selectedId = rgOptions.getCheckedRadioButtonId();
        String selected = "";
        if (selectedId == R.id.rbOptionA) selected = "A";
        else if (selectedId == R.id.rbOptionB) selected = "B";
        else if (selectedId == R.id.rbOptionC) selected = "C";
        else if (selectedId == R.id.rbOptionD) selected = "D";

        boolean correct = selected.equals(q.getAnswer());
        if (correct) correctCount++;

        ExamQuestion eq = new ExamQuestion();
        eq.setExamId(currentExamId); // int
        eq.setQuestionId(q.getId());
        eq.setSelectedAnswer(selected);
        eq.setCorrect(correct);

        examAnswers.add(eq);
    }


    private void finishExam() {
        runOnUiThread(this::showResultDialog);

<<<<<<< Updated upstream
        // Lưu kết quả vào DB
        new Thread(() -> {
            try {
                // Tạo ExamResult
                ExamResult result = new ExamResult();
=======
        new Thread(() -> {
            try {
                examController.insertExamQuestions(examAnswers);

                ExamResult result = new ExamResult();
                result.setId(currentExamId);
>>>>>>> Stashed changes
                result.setUserId(currentUserId);
                result.setTotalQuestions(examAnswers.size());
                result.setCorrectAnswers(correctCount);
                result.setTakenAt(System.currentTimeMillis());
                result.setCategory("A1");

<<<<<<< Updated upstream
                // Insert ExamResult -> lấy ID mới
                long newExamId = examController.insertExamResult(result);

                // Gán examId cho tất cả câu trả lời
                for (ExamQuestion answer : examAnswers) {
                    answer.setExamId((int)newExamId);
                }

                // Insert câu trả lời
                examController.insertExamQuestions(examAnswers);
=======
                examController.updateExamResult(result);
>>>>>>> Stashed changes

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

<<<<<<< Updated upstream


    private void showResultDialog() {
        try {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Kết quả bài thi")
                    .setMessage("Bạn trả lời đúng: " + correctCount + "/" + examAnswers.size())
                    .setCancelable(false)
                    .setPositiveButton("Thoát", (d, which) -> {
                        Intent intent = new Intent(ExamTestActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // Chỉ finish ở đây
                    })
                    .setNegativeButton("Xem lại", (d, which) -> d.dismiss())
                    .create();

            Log.d("ExamFinish", "correctCount=" + correctCount + ", total=" + examAnswers.size());
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


=======
    private void showResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả bài thi");
        builder.setMessage("Bạn trả lời đúng: " + correctCount + "/" + examAnswers.size());

        builder.setPositiveButton("Thoát", (dialog, which) -> {
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        });

        builder.setNegativeButton("Làm lại câu sai", (dialog, which) -> {
            Intent intent = new Intent(this, WrongQuestionsActivity.class);
            intent.putExtra("examId", currentExamId);
            startActivity(intent);
        });

        builder.setCancelable(false);
        builder.show();
    }
>>>>>>> Stashed changes
}
