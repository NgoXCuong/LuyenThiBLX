package com.example.luyenthiblxmay.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.example.luyenthiblxmay.utils.WrongQuestionCache;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamTestActivity extends AppCompatActivity {
    private TextView tvQuestionNumber, tvQuestion, tvTimer;
    private RadioGroup rgOptions;
    private RadioButton rbA, rbB, rbC, rbD;
    private Button btnNext, btnSubmit;
    private ImageView imgQuestion;

    private ExamController examController;
    private List<Question> questionList = new ArrayList<>();
    private List<ExamQuestion> examAnswers = new ArrayList<>();
    private int currentIndex = 0;
    private int currentExamId;
    private int currentUserId = 1;
    private int correctCount = 0;

    private CountDownTimer countDownTimer;
    private static final long EXAM_DURATION = 19 * 60 * 1000; // 19 phút
    private boolean warningTriggered = false; // cảnh báo 1 phút cuối

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_test);

        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvTimer = findViewById(R.id.tvTimer);
        rgOptions = findViewById(R.id.rgOptions);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgQuestion = findViewById(R.id.imgQuestion);

        rbA = findViewById(R.id.rbOptionA);
        rbB = findViewById(R.id.rbOptionB);
        rbC = findViewById(R.id.rbOptionC);
        rbD = findViewById(R.id.rbOptionD);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        examController = new ExamController(getApplication());
        WrongQuestionCache.clear();

        startTimer(); // 🚀 bắt đầu đếm ngược

        // Load câu hỏi
        new Thread(() -> {
            ExamResult examResult = new ExamResult();
            examResult.setUserId(currentUserId);
            examResult.setCategory("A1");
            examResult.setTotalQuestions(25);
            examResult.setCorrectAnswers(0);
            examResult.setTakenAt(System.currentTimeMillis());

            currentExamId = examController.insertExamResultSync(examResult);

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
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(EXAM_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;

                // 1 phút cuối đổi màu đỏ
                if (millisUntilFinished <= 60000 && !warningTriggered) {
                    tvTimer.setTextColor(Color.RED);
                    warningTriggered = true;
                }

                tvTimer.setText(String.format("⏰ %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                finishExam(); // hết giờ tự nộp
            }
        }.start();
    }

    private void showQuestion(int index) {
        Question q = questionList.get(index);

        tvQuestionNumber.setText("Câu " + (index + 1) + "/" + questionList.size());
        tvQuestion.setText(q.getQuestion());

        if (q.getImage() != null && !q.getImage().isEmpty()) {
            try (InputStream is = getAssets().open(q.getImage())) {
                Drawable drawable = Drawable.createFromStream(is, null);
                imgQuestion.setImageDrawable(drawable);
                imgQuestion.setVisibility(ImageView.VISIBLE);
            } catch (IOException e) {
                imgQuestion.setVisibility(ImageView.GONE);
                e.printStackTrace();
            }
        } else {
            imgQuestion.setVisibility(ImageView.GONE);
        }

        rbA.setVisibility(RadioButton.GONE);
        rbB.setVisibility(RadioButton.GONE);
        rbC.setVisibility(RadioButton.GONE);
        rbD.setVisibility(RadioButton.GONE);

        Map<String, String> options = q.getOptions();
        if (options != null) {
            if (options.containsKey("A") && options.get("A") != null && !options.get("A").trim().isEmpty()) {
                rbA.setText("A. " + options.get("A"));
                rbA.setVisibility(RadioButton.VISIBLE);
            }
            if (options.containsKey("B") && options.get("B") != null && !options.get("B").trim().isEmpty()) {
                rbB.setText("B. " + options.get("B"));
                rbB.setVisibility(RadioButton.VISIBLE);
            }
            if (options.containsKey("C") && options.get("C") != null && !options.get("C").trim().isEmpty()) {
                rbC.setText("C. " + options.get("C"));
                rbC.setVisibility(RadioButton.VISIBLE);
            }
            if (options.containsKey("D") && options.get("D") != null && !options.get("D").trim().isEmpty()) {
                rbD.setText("D. " + options.get("D"));
                rbD.setVisibility(RadioButton.VISIBLE);
            }
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
        eq.setExamId(currentExamId);
        eq.setQuestionId(q.getId());
        eq.setSelectedAnswer(selected);
        eq.setCorrect(correct);

        examAnswers.add(eq);

        if (!correct) {
            WrongQuestionCache.addWrongQuestion(q);
        }
    }

    private void finishExam() {
        if (countDownTimer != null) countDownTimer.cancel();
        runOnUiThread(this::showResultDialog);

        new Thread(() -> {
            try {
                examController.insertExamQuestions(examAnswers);

                ExamResult result = new ExamResult();
                result.setId(currentExamId);
                result.setUserId(currentUserId);
                result.setTotalQuestions(examAnswers.size());
                result.setCorrectAnswers(correctCount);
                result.setTakenAt(System.currentTimeMillis());
                result.setCategory("A1");

                examController.updateExamResult(result);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả bài thi");
        builder.setMessage("Bạn trả lời đúng: " + correctCount + "/" + examAnswers.size());

        builder.setPositiveButton("Thoát", (dialog, which) -> {
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        });

        builder.setNegativeButton("Xem câu sai", (dialog, which) -> {
            Intent intent = new Intent(this, WrongQuestionsActivity.class);
            startActivity(intent);
        });

        builder.setCancelable(false);
        builder.show();
    }
}
