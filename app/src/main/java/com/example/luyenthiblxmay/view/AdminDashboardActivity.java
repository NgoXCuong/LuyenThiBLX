package com.example.luyenthiblxmay.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.luyenthiblxmay.MainActivity;
import com.example.luyenthiblxmay.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private ImageView logoutBtn;
    private CardView cardManageTips, cardManageUsers, cardManageQuestions, cardManageExams, cardManageSigns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Logout button
        logoutBtn = findViewById(R.id.adminLogoutBtn);
        logoutBtn.setOnClickListener(v -> {
            // Xóa dữ liệu login nếu có (SharedPreferences)
            getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();

            // Quay về LoginActivity
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // xóa toàn bộ stack cũ
            startActivity(intent);
            finish(); // kết thúc MainActivity
        });

        // Tham chiếu đến các CardView menu
        cardManageTips = findViewById(R.id.card_manage_tips);
        cardManageUsers = findViewById(R.id.card_manage_users);
        cardManageQuestions = findViewById(R.id.card_manage_questions);
        cardManageExams = findViewById(R.id.card_manage_exams);
        cardManageSigns = findViewById(R.id.card_manage_signs);

        // Click listener cho từng menu
        cardManageTips.setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, AdminTipsActivity.class))
        );

        cardManageUsers.setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, AdminUsersActivity.class))
        );

        cardManageQuestions.setOnClickListener(v ->
                startActivity(new Intent(AdminDashboardActivity.this, AdminQuestionsActivity.class))
        );
//
//        cardManageExams.setOnClickListener(v ->
//                startActivity(new Intent(AdminDashboardActivity.this, AdminExamsActivity.class))
//        );
//
//        cardManageSigns.setOnClickListener(v ->
//                startActivity(new Intent(AdminDashboardActivity.this, AdminSignsActivity.class))
//        );
    }
}
