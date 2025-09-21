package com.example.luyenthiblxmay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.luyenthiblxmay.controller.QuestionController;
import com.example.luyenthiblxmay.controller.UserQuestionController;
import com.example.luyenthiblxmay.dao.UserDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.model.Question;
import com.example.luyenthiblxmay.model.User;
import com.example.luyenthiblxmay.model.UserQuestion;
import com.example.luyenthiblxmay.services.ReminderWorker;
import com.example.luyenthiblxmay.view.BienBaoActivity;
import com.example.luyenthiblxmay.view.ExamTestActivity;
import com.example.luyenthiblxmay.view.LoginActivity;
import com.example.luyenthiblxmay.view.ModuleActivity;
import com.example.luyenthiblxmay.view.TipsActivity;
import com.example.luyenthiblxmay.view.WrongQuestionsActivity;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView tvProgressPercent, tvProgressDetail, tvUserInfo;

    private QuestionController questionController;
    private UserQuestionController userQuestionController;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ✅ Xin quyền thông báo cho Android 13+
        requestNotificationPermission();

        // ✅ Lên lịch notification sau 30s
        OneTimeWorkRequest request =
                new OneTimeWorkRequest.Builder(ReminderWorker.class)
                        .setInitialDelay(864000, TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(this).enqueue(request);

        // Padding cho hệ thống Bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Layout các module
        LinearLayout layoutMeoHocTap = findViewById(R.id.layoutMeoHocTap);
        LinearLayout layoutHocLyThuyet = findViewById(R.id.layoutHocLyThuyet);
        LinearLayout layoutThiThu = findViewById(R.id.layoutThithu);
        LinearLayout layoutCauSai = findViewById(R.id.layoutCauSai);
        LinearLayout layoutBienBao = findViewById(R.id.layoutBienBao);

        layoutMeoHocTap.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TipsActivity.class)));
        layoutHocLyThuyet.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ModuleActivity.class)));
        layoutThiThu.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ExamTestActivity.class)));
        layoutCauSai.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WrongQuestionsActivity.class)));
        layoutBienBao.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BienBaoActivity.class)));

        // --- Lấy userId ---
        userId = getIntent().getIntExtra("user_id", -1);
        if (userId == -1) {
            userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    .getInt("user_id", -1);
        }

        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // 👉 Thêm vào đây
        scheduleDailyReminder();

        // --- Khởi tạo DB và lấy thông tin user ---
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "app_thi_blx")
                .allowMainThreadQueries()
                .build();

        UserDao userDao = db.userDao();
        User user = userDao.getUserById(userId);

        tvUserInfo = findViewById(R.id.textViewFullName);
        if (user != null) {
            tvUserInfo.setText("ID: " + user.getId() + " - " + user.getFullName());
        } else {
            tvUserInfo.setText("User không tồn tại");
        }

        // --- Logout ---
        ImageView logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // --- Progress học tập ---
        progressBar = findViewById(R.id.progressBar);
        tvProgressPercent = findViewById(R.id.tvProgressPercent);
        tvProgressDetail = findViewById(R.id.tvProgressDetail);

        questionController = new QuestionController(getApplication());
        userQuestionController = new UserQuestionController(getApplication());
    }

    // 👇 Thêm cuối class MainActivity (ngoài onCreate, onResume)
    private void scheduleDailyReminder() {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 16);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        long initialDelay = calendar.getTimeInMillis() - now;

        Constraints constraints = new Constraints.Builder().build();

        PeriodicWorkRequest reminderRequest =
                new PeriodicWorkRequest.Builder(ReminderWorker.class, 24, TimeUnit.HOURS)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "daily_study_reminder",
                ExistingPeriodicWorkPolicy.REPLACE,
                reminderRequest
        );
    }

    private void requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // ✅ Được cấp quyền, bạn có thể gửi notification
                Toast.makeText(this, "Đã cho phép thông báo", Toast.LENGTH_SHORT).show();
            } else {
                // ❌ User từ chối quyền, có thể hiển thị Toast giải thích
                Toast.makeText(this, "Bạn đã từ chối quyền thông báo. Một số tính năng có thể không hoạt động.", Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Quan sát tiến độ của user khi activity hiển thị lại
        userQuestionController.getUserQuestionsByUser(userId)
                .observe(this, this::updateTotalProgress);
    }

    private void updateTotalProgress(List<UserQuestion> userQuestions) {
        questionController.getAllQuestions().observe(this, questions -> {
            if (questions == null || questions.isEmpty()) {
                progressBar.setProgress(0);
                tvProgressPercent.setText("0%");
                tvProgressDetail.setText("Chưa có câu hỏi nào");
                return;
            }

            int total = questions.size();
            int answered = 0;

            for (Question q : questions) {
                for (UserQuestion uq : userQuestions) {
                    if (uq.getQuestionId() == q.getId() && uq.isAnswered()) {
                        answered++;
                        break;
                    }
                }
            }

            int percent = (int) ((answered * 100.0f) / total);
            progressBar.setProgress(percent);
            tvProgressPercent.setText(percent + "%");
            tvProgressDetail.setText("Đã hoàn thành " + answered + "/" + total + " câu hỏi");
        });
    }
}
