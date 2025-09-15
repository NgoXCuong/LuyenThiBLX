package com.example.luyenthiblxmay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.luyenthiblxmay.dao.UserDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.model.User;
import com.example.luyenthiblxmay.view.ExamTestActivity;
import com.example.luyenthiblxmay.view.LoginActivity;
import com.example.luyenthiblxmay.view.ModuleActivity;
import com.example.luyenthiblxmay.view.TipsActivity;
import com.example.luyenthiblxmay.view.WrongQuestionsActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Gán ID đúng cho layout gốc
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LinearLayout layoutMeoHocTap = findViewById(R.id.layoutMeoHocTap);
        LinearLayout layoutHocLyThuyet = findViewById(R.id.layoutHocLyThuyet);
        LinearLayout layoutThiThu = findViewById(R.id.layoutThithu);
        LinearLayout layoutCauSai = findViewById(R.id.layoutCauSai);


        layoutMeoHocTap.setOnClickListener(v -> {
            // Chuyển sang trang khác
            Intent intent = new Intent(MainActivity.this, TipsActivity.class);
            startActivity(intent);
        });

        layoutHocLyThuyet.setOnClickListener(v -> {
            // Chuyển sang trang khác
            Intent intent = new Intent(MainActivity.this, ModuleActivity.class);
            startActivity(intent);
        });

        layoutThiThu.setOnClickListener(v -> {
            // Chuyển sang trang khác
            Intent intent = new Intent(MainActivity.this, ExamTestActivity.class);
            startActivity(intent);
        });

        layoutCauSai.setOnClickListener(v -> {
            // Chuyển sang trang khác
            Intent intent = new Intent(MainActivity.this, WrongQuestionsActivity.class);
            startActivity(intent);
        });


        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "app_thi_blx")
                .allowMainThreadQueries() // ⚠️ chỉ dùng cho test, thực tế nên dùng AsyncTask/Executor
                .build();

        UserDao userDao = db.userDao();
        int userId = getIntent().getIntExtra("user_id", -1); // mặc định -1 nếu không có
        TextView textView = findViewById(R.id.textViewFullName);

        if (userId != -1) {
            User user = userDao.getUserById(userId);
            if (user != null) {
                textView.setText(user.getFullName());
            } else {
                textView.setText("User không tồn tại");
            }
        } else {
            textView.setText("ID không hợp lệ");
        }

        ImageView logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            // Xóa dữ liệu login nếu có (SharedPreferences)
            getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();

            // Quay về LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // xóa toàn bộ stack cũ
            startActivity(intent);
            finish(); // kết thúc MainActivity
        });


    }
}