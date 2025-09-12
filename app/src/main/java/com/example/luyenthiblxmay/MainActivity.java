package com.example.luyenthiblxmay;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.luyenthiblxmay.view.TipsActivity;

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
//        LinearLayout layoutHocLyThuet = findViewById(R.id.layoutHocLyThuyet);


        layoutMeoHocTap.setOnClickListener(v -> {
            // Chuyển sang trang khác
            Intent intent = new Intent(MainActivity.this, TipsActivity.class);
            startActivity(intent);
        });
//
//        layoutHocLyThuet.setOnClickListener(v -> {
//            // Chuyển sang trang khác
//            Intent intent = new Intent(MainActivity.this, ModuleActivity.class);
//            startActivity(intent);
//        });




        TextView textView = findViewById(R.id.textViewFullName);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "driving_test_a1_database")
                .allowMainThreadQueries() // ⚠️ chỉ dùng cho test, thực tế nên dùng AsyncTask/Executor
                .build();

        UserDao userDao = db.userDao();

        User user = userDao.getUserById(1);

        if (user != null) {
            textView.setText(user.getFullName());
        }
    }
}