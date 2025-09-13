package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.adapter.AdminTipsAdapter;
import com.example.luyenthiblxmay.controller.TipsController;
import com.example.luyenthiblxmay.model.Tips;

import java.util.List;

public class AdminTipsActivity extends AppCompatActivity {
    private TipsController tipsController;
    private RecyclerView recyclerView;
    private AdminTipsAdapter adapter;
    private ImageButton btnAddTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tips);

        tipsController = new TipsController(getApplication());
        recyclerView = findViewById(R.id.recyclerViewAdminTips);
        btnAddTip = findViewById(R.id.btnAddTip);

        // Nút back
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        adapter = new AdminTipsAdapter(this, new AdminTipsAdapter.OnTipActionListener() {
            @Override
            public void onEdit(Tips tip) {
                showEditDialog(tip);
            }

            @Override
            public void onDelete(Tips tip) {
                tipsController.deleteTip(tip);
                Toast.makeText(AdminTipsActivity.this, "Đã xóa mẹo", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Quan sát dữ liệu
        tipsController.getAllTips().observe(this, new Observer<List<Tips>>() {
            @Override
            public void onChanged(List<Tips> tips) {
                adapter.setTipsList(tips);
            }
        });

        // Nút thêm mẹo
        btnAddTip.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tip, null);
        EditText edtTitle = view.findViewById(R.id.edtTipTitle);
        EditText edtContent = view.findViewById(R.id.edtTipContent);

        new AlertDialog.Builder(this)
                .setTitle("Thêm mẹo mới")
                .setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String title = edtTitle.getText().toString().trim();
                    String content = edtContent.getText().toString().trim();
                    if (!title.isEmpty() && !content.isEmpty()) {
                        tipsController.insertTip(new Tips(title, content));
                    } else {
                        Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditDialog(Tips tip) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tip, null);
        EditText edtTitle = view.findViewById(R.id.edtTipTitle);
        EditText edtContent = view.findViewById(R.id.edtTipContent);

        edtTitle.setText(tip.getTitle());
        edtContent.setText(tip.getContent());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Sửa mẹo")
                .setView(view)
                .setPositiveButton("Cập nhật", null) // để null, lát nữa setOnClickListener thủ công
                .setNegativeButton("Hủy", (d, w) -> d.dismiss())
                .create();

        dialog.setOnShowListener(d -> {
            Button btnUpdate = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnUpdate.setOnClickListener(v -> {
                String title = edtTitle.getText().toString().trim();
                String content = edtContent.getText().toString().trim();

                if (!title.isEmpty() && !content.isEmpty()) {
                    tip.setTitle(title);
                    tip.setContent(content);
                    tipsController.updateTip(tip);
                    dialog.dismiss(); // chỉ đóng khi hợp lệ
                } else {
                    Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
}