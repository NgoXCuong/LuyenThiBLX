package com.example.luyenthiblxmay.view;

import static com.example.luyenthiblxmay.utils.PasswordUtils.hashPassword;

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

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.model.User;
import com.example.luyenthiblxmay.controller.UserController;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private CheckBox cbTerms;
    private Button btnRegister, btnBackToLogin;
    private ProgressBar progressBar;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        userController = new UserController(getApplication());

        btnRegister.setOnClickListener(v -> registerUser());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbTerms = findViewById(R.id.cbTerms);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Validate input
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Vui lòng nhập họ và tên");
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return;
        }
        if (TextUtils.isEmpty(phone) || phone.length() < 9) {
            etPhone.setError("Số điện thoại không hợp lệ");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Mật khẩu phải >= 6 ký tự");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu không khớp");
            return;
        }
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Bạn phải đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        // Kiểm tra email và phone đã tồn tại chưa
        userController.checkEmailExists(email, exists -> {
            if (exists) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnRegister.setEnabled(true);
                    etEmail.setError("Email đã được sử dụng");
                });
            } else {
                userController.checkPhoneExists(phone, phoneExists -> {
                    if (phoneExists) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setEnabled(true);
                            etPhone.setError("Số điện thoại đã được sử dụng");
                        });
                    } else {
                        // Tạo User mới
                        User newUser = new User();
                        newUser.setFullName(fullName);
                        newUser.setEmail(email);
                        newUser.setPhone(phone);
                        newUser.setPassword(hashPassword(password)); // Nếu muốn hash thì hash trước khi insert
                        newUser.setAdmin(false);

                        userController.insertUser(newUser, (success, message) -> runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            if (success) finish(); // Quay về login
                        }));
                    }
                });
            }
        });
    }
}