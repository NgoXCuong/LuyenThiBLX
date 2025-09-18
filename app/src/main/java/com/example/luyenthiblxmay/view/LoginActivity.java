package com.example.luyenthiblxmay.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luyenthiblxmay.MainActivity;
import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.controller.UserController;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private CheckBox cbRememberMe;
    private ProgressBar progressBar;
    private TextView tvForgotPassword;

    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        userController = new UserController(getApplication());

        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v -> {
            // Chuyển sang RegisterActivity
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng quên mật khẩu chưa được triển khai", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        progressBar = findViewById(R.id.progressBar);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Validate input
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // Login user
        userController.loginUser(email, password, (success, user, message) -> runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);

            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            if (success && user != null) {
                // Lưu vào SharedPreferences
                getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit()
                        .putInt("user_id", user.getId())
                        .apply();

                Intent intent;
                if (user.isAdmin()) {
                    intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                }
                intent.putExtra("user_id", user.getId());
                startActivity(intent);
                finish();
            }

        }));
    }
}