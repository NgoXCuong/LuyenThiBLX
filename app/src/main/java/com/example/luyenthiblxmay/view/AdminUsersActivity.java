package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.adapter.UserAdapter;
import com.example.luyenthiblxmay.controlller.UserController;
import com.example.luyenthiblxmay.model.User;

import java.util.List;

public class AdminUsersActivity extends AppCompatActivity {
    private UserController userController;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userController = new UserController(getApplication()); // fix Application
        userAdapter = new UserAdapter(this, userController);
        recyclerView.setAdapter(userAdapter);

        // Quan sát LiveData
        userController.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    userAdapter.setUsers(users);
                } else {
                    Toast.makeText(AdminUsersActivity.this, "Không tải được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnAddUser).setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_user, null);
        EditText edtName = view.findViewById(R.id.edtFullName);
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        EditText edtPhone = view.findViewById(R.id.edtPhone);
        EditText edtPassword = view.findViewById(R.id.edtPassword);
        CheckBox chkIsAdmin = view.findViewById(R.id.chkIsAdmin); // Lấy checkbox

        new AlertDialog.Builder(this)
                .setTitle("Thêm tài khoản")
                .setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();
                    boolean isAdmin = chkIsAdmin.isChecked(); // Lấy giá trị checkbox

                    if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty()) {
                        // Hash password trước khi lưu
                        String hashedPassword = com.example.luyenthiblxmay.utils.PasswordUtils.hashPassword(password);
                        User user = new User(name, email, phone, hashedPassword, isAdmin); // dùng constructor 5 tham số
                        userController.insertUser(user, (success, message) -> {
                            runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
                        });
                    } else {
                        Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
