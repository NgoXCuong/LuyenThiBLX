package com.example.luyenthiblxmay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.controller.UserController;
import com.example.luyenthiblxmay.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final Context context;
    private final UserController userController;
    private List<User> userList = new ArrayList<>();

    public UserAdapter(Context context, UserController userController) {
        this.context = context;
        this.userController = userController;
    }

    public void setUsers(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtName.setText(user.getFullName());
        holder.txtEmail.setText(user.getEmail());

        // Xóa user
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa tài khoản")
                    .setMessage("Bạn có chắc muốn xóa tài khoản này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        userController.deleteUser(user, (success, message) -> {
                            ((Activity) context).runOnUiThread(() -> {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                if (success) {
                                    userList.remove(position);
                                    notifyItemRemoved(position);
                                }
                            });
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // Sửa user
        holder.btnEdit.setOnClickListener(v -> {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_user, null);
            EditText edtName = view.findViewById(R.id.edtFullName);
            EditText edtEmail = view.findViewById(R.id.edtEmail);
            EditText edtPhone = view.findViewById(R.id.edtPhone);
            EditText edtPassword = view.findViewById(R.id.edtPassword);

            // Gán dữ liệu cũ
            edtName.setText(user.getFullName());
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());
            edtPassword.setText(user.getPassword());

            new AlertDialog.Builder(context)
                    .setTitle("Sửa tài khoản")
                    .setView(view)
                    .setPositiveButton("Cập nhật", (dialog, which) -> {
                        String name = edtName.getText().toString().trim();
                        String email = edtEmail.getText().toString().trim();
                        String phone = edtPhone.getText().toString().trim();
                        String password = edtPassword.getText().toString().trim();

                        if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty()) {
                            user.setFullName(name);
                            user.setEmail(email);
                            user.setPhone(phone);
                            user.setPassword(password);

                            userController.updateUser(user, (success, message) -> {
                                ((Activity) context).runOnUiThread(() -> {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    if (success) {
                                        notifyItemChanged(position);
                                    }
                                });
                            });
                        } else {
                            Toast.makeText(context, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail;
        ImageButton btnEdit, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtUserName);
            txtEmail = itemView.findViewById(R.id.txtUserEmail);
            btnEdit = itemView.findViewById(R.id.btnEditUser);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}
