//package com.example.luyenthiblxmay.view;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.luyenthiblxmay.controller.QuestionController;
//import com.example.luyenthiblxmay.view.LoginActivity;
//import com.example.luyenthiblxmay.R;
//import com.example.luyenthiblxmay.controller.UserQuestionController;
//import com.example.luyenthiblxmay.model.UserQuestion;
//
//public class ModuleActivity extends AppCompatActivity {
//
//    private int userId;
//    private UserQuestionController userQuestionController;
//    private QuestionController questionController;
//
//    private ProgressBar progressBook, progressEthics, progressDrive, progressSigns, progressSimulation;
//    private TextView txtProgressBook, txtProgressEthics, txtProgressDrive, txtProgressSigns, txtProgressSimulation;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_module);
//
//        // Lấy userId từ Intent hoặc SharedPreferences
//        userId = getIntent().getIntExtra("user_id", -1); // ✅ đúng
//        if(userId == -1){
//            userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
//                    .getInt("user_id", -1);
//        }
//
//        if (userId == -1) {
//            // Không có user hợp lệ → quay về Login
//            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//            return;
//        }
//
//        // Khởi tạo controller
//        questionController = new QuestionController(getApplication());
//        userQuestionController = new UserQuestionController(getApplication());
//
//        // Ánh xạ các ProgressBar và TextView
//        progressBook = findViewById(R.id.progressBarBook);
//        txtProgressBook = findViewById(R.id.txtProgressBook);
//        progressEthics = findViewById(R.id.progressBarEthics);
//        txtProgressEthics = findViewById(R.id.txtProgressEthics);
//        progressDrive = findViewById(R.id.progressBarDrive);
//        txtProgressDrive = findViewById(R.id.txtProgressDrive);
//        progressSigns = findViewById(R.id.progressBarSigns);
//        txtProgressSigns = findViewById(R.id.txtProgressSigns);
//        progressSimulation = findViewById(R.id.progressBarSimulation);
//        txtProgressSimulation = findViewById(R.id.txtProgressSimulation);
//
//        // Nút quay lại
//        ImageView backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(v -> finish());
//
//        // Quan sát tiến độ từng category
//        observeCategoryProgress("Khái niệm và quy tắc", progressBook, txtProgressBook);
//        observeCategoryProgress("Văn hóa và đạo đức lái xe", progressEthics, txtProgressEthics);
//        observeCategoryProgress("Kỹ thuật lái xe", progressDrive, txtProgressDrive);
//        observeCategoryProgress("Biển báo", progressSigns, txtProgressSigns);
//        observeCategoryProgress("Sa hình", progressSimulation, txtProgressSimulation);
//
//        // Click mở QuestionActivity theo category
//        findViewById(R.id.cardBook).setOnClickListener(v -> openQuestionActivity("Khái niệm và quy tắc"));
//        findViewById(R.id.cardEthics).setOnClickListener(v -> openQuestionActivity("Văn hóa và đạo đức lái xe"));
//        findViewById(R.id.cardDrive).setOnClickListener(v -> openQuestionActivity("Kỹ thuật lái xe"));
//        findViewById(R.id.cardSigns).setOnClickListener(v -> openQuestionActivity("Biển báo"));
//        findViewById(R.id.cardSimulation).setOnClickListener(v -> openQuestionActivity("Sa hình"));
//    }
//
//    private void observeCategoryProgress(String category, ProgressBar progressBar, TextView textView) {
//        // Lấy tổng số câu hỏi của category từ QuestionController
//        questionController.getQuestionsByCategory(category).observe(this, questions -> {
//            if (questions == null || questions.isEmpty()) {
//                progressBar.setMax(1);
//                progressBar.setProgress(0);
//                textView.setText("0/0");
//                return;
//            }
//
//            int total = questions.size(); // Tổng số câu hỏi
//            progressBar.setMax(total);
//
//            // Lấy số câu đã làm từ UserQuestionController
//            userQuestionController.getUserQuestionsByCategory(userId, category)
//                    .observe(this, userQuestions -> {
//                        int done = 0;
//                        if (userQuestions != null) {
//                            for (UserQuestion uq : userQuestions) {
//                                if (uq.isAnswered()) done++;
//                            }
//                        }
//                        progressBar.setProgress(done);
//                        textView.setText(done + "/" + total);
//                    });
//        });
//    }
//
//
//    /**
//     * Mở QuestionsActivity theo category và userId
//     */
//    private void openQuestionActivity(String category) {
//        Intent intent = new Intent(this, QuestionsActivity.class);
//        intent.putExtra("category", category);
//        intent.putExtra("user_id", userId); // luôn truyền userId hợp lệ
//        startActivity(intent);
//    }
//
//    /**
//     * Lưu câu trả lời vào DB
//     */
//    public void saveAnswer(int questionId, String selectedAnswer, boolean isCorrect) {
//        if (userId == -1) {
//            Toast.makeText(this, "User không hợp lệ, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        userQuestionController.saveAnswer(userId, questionId, selectedAnswer, isCorrect);
//    }
//}
package com.example.luyenthiblxmay.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.controller.QuestionController;
import com.example.luyenthiblxmay.controller.UserQuestionController;
import com.example.luyenthiblxmay.model.Question;
import com.example.luyenthiblxmay.model.UserQuestion;

import java.util.List;

public class ModuleActivity extends AppCompatActivity {

    private int userId;
    private UserQuestionController userQuestionController;
    private QuestionController questionController;

    private ProgressBar progressBook, progressEthics, progressDrive, progressSigns, progressSimulation;
    private TextView txtProgressBook, txtProgressEthics, txtProgressDrive, txtProgressSigns, txtProgressSimulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        // Lấy userId từ Intent hoặc SharedPreferences
        userId = getIntent().getIntExtra("user_id", -1);
        if (userId == -1) {
            userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);
        }

        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Khởi tạo controller
        userQuestionController = new UserQuestionController(getApplication());
        questionController = new QuestionController(getApplication());

        // Ánh xạ ProgressBar và TextView
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

        // Nút quay lại
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Click mở QuestionsActivity theo category
        findViewById(R.id.cardBook).setOnClickListener(v -> openQuestionActivity("Khái niệm và quy tắc"));
        findViewById(R.id.cardEthics).setOnClickListener(v -> openQuestionActivity("Văn hóa và đạo đức lái xe"));
        findViewById(R.id.cardDrive).setOnClickListener(v -> openQuestionActivity("Kỹ thuật lái xe"));
        findViewById(R.id.cardSigns).setOnClickListener(v -> openQuestionActivity("Biển báo"));
        findViewById(R.id.cardSimulation).setOnClickListener(v -> openQuestionActivity("Sa hình"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật tiến độ mỗi khi activity hiển thị
        updateAllCategoryProgress();
    }

    private void updateAllCategoryProgress() {
        updateCategoryProgress("Khái niệm và quy tắc", progressBook, txtProgressBook);
        updateCategoryProgress("Văn hóa và đạo đức lái xe", progressEthics, txtProgressEthics);
        updateCategoryProgress("Kỹ thuật lái xe", progressDrive, txtProgressDrive);
        updateCategoryProgress("Biển báo", progressSigns, txtProgressSigns);
        updateCategoryProgress("Sa hình", progressSimulation, txtProgressSimulation);
    }

    private void updateCategoryProgress(String category, ProgressBar progressBar, TextView textView) {
        // Quan sát tiến độ user
        userQuestionController.getUserQuestionsByCategory(userId, category).observe(this, userQuestions -> {
            int done = (userQuestions != null) ? (int) userQuestions.stream().filter(UserQuestion::isAnswered).count() : 0;

            // Lấy tổng số câu hỏi trong category
            questionController.getQuestionsByCategory(category).observe(this, questions -> {
                int total = (questions != null) ? questions.size() : 0;
                progressBar.setMax(Math.max(total, 1));
                progressBar.setProgress(done);
                textView.setText(done + "/" + total);
            });
        });
    }

    private void openQuestionActivity(String category) {
        Intent intent = new Intent(this, QuestionsActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
}
