package com.example.luyenthiblxmay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.example.luyenthiblxmay.controller.QuestionController;
import com.example.luyenthiblxmay.dao.UserDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.model.Question;
import com.example.luyenthiblxmay.model.User;
import com.example.luyenthiblxmay.view.BienBaoActivity;
import com.example.luyenthiblxmay.view.ExamTestActivity;
import com.example.luyenthiblxmay.view.LoginActivity;
import com.example.luyenthiblxmay.view.ModuleActivity;
import com.example.luyenthiblxmay.view.TipsActivity;
import com.example.luyenthiblxmay.view.WrongQuestionsActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private QuestionController questionController;
    private ProgressBar progressBar;
    private TextView tvProgressPercent, tvProgressDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Padding cho hệ thống Bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Lấy layout các module ---
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

        // --- Hiển thị thông tin user ---
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "app_thi_blx")
                .allowMainThreadQueries()
                .build();

        UserDao userDao = db.userDao();
        int userId = getIntent().getIntExtra("user_id", -1);
        TextView textView = findViewById(R.id.textViewFullName);

        if (userId != -1) {
            User user = userDao.getUserById(userId);
            textView.setText(user != null ? user.getFullName() : "User không tồn tại");
        } else {
            textView.setText("ID không hợp lệ");
        }

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
        questionController.getAllQuestions().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                if (questions == null || questions.isEmpty()) {
                    progressBar.setProgress(0);
                    tvProgressPercent.setText("0%");
                    tvProgressDetail.setText("Chưa có câu hỏi nào");
                    return;
                }

                int total = questions.size();
                int answered = 0;
                for (Question q : questions) {
                    if (q.isAnswered()) answered++;
                }

                int percent = (int) ((answered * 100.0f) / total);
                progressBar.setProgress(percent);
                tvProgressPercent.setText(percent + "%");
                tvProgressDetail.setText("Đã hoàn thành " + answered + "/" + total + " câu hỏi");
            }
        });
    }
}
