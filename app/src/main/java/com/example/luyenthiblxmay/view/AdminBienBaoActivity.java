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
import com.example.luyenthiblxmay.adapter.AdminBienBaoAdapter;
import com.example.luyenthiblxmay.controller.BienBaoController;
import com.example.luyenthiblxmay.model.BienBao;

import java.util.List;

public class AdminBienBaoActivity extends AppCompatActivity {
    private BienBaoController bienBaoController;
    private RecyclerView recyclerView;
    private AdminBienBaoAdapter adapter;
    private ImageButton btnAddBienBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bien_bao);

        bienBaoController = new BienBaoController(getApplication());
        recyclerView = findViewById(R.id.recyclerViewAdminBienBao);
        btnAddBienBao = findViewById(R.id.btnAddBienBao);

        // Nút back
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        adapter = new AdminBienBaoAdapter(this, null, new AdminBienBaoAdapter.OnBienBaoActionListener() {
            @Override
            public void onEdit(BienBao bienBao) {
                showEditDialog(bienBao);
            }

            @Override
            public void onDelete(BienBao bienBao) {
                bienBaoController.deleteBienBao(bienBao);
                Toast.makeText(AdminBienBaoActivity.this, "Đã xóa biển báo", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Quan sát dữ liệu
        bienBaoController.getAllBienBao().observe(this, new Observer<List<BienBao>>() {
            @Override
            public void onChanged(List<BienBao> bienBaos) {
                adapter.setBienBaoList(bienBaos);
            }
        });

        // Nút thêm biển báo
        btnAddBienBao.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bienbao, null);
        EditText edtTen = view.findViewById(R.id.edtTenBienBao);
        EditText edtLoai = view.findViewById(R.id.edtLoaiBienBao);
        EditText edtMoTa = view.findViewById(R.id.edtMoTaBienBao);
        EditText edtHinhAnh = view.findViewById(R.id.edtHinhAnhBienBao); // tên file ảnh trong assets/bienbao/

        new AlertDialog.Builder(this)
                .setTitle("Thêm biển báo mới")
                .setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String ten = edtTen.getText().toString().trim();
                    String loai = edtLoai.getText().toString().trim();
                    String moTa = edtMoTa.getText().toString().trim();
                    String hinhAnh = edtHinhAnh.getText().toString().trim();

                    if (!ten.isEmpty() && !loai.isEmpty() && !moTa.isEmpty()) {
                        BienBao bienBao = new BienBao(ten, loai, moTa, hinhAnh);
                        bienBaoController.insertBienBao(bienBao);
                    } else {
                        Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditDialog(BienBao bienBao) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bienbao, null);
        EditText edtTen = view.findViewById(R.id.edtTenBienBao);
        EditText edtLoai = view.findViewById(R.id.edtLoaiBienBao);
        EditText edtMoTa = view.findViewById(R.id.edtMoTaBienBao);
        EditText edtHinhAnh = view.findViewById(R.id.edtHinhAnhBienBao);

        edtTen.setText(bienBao.getTenBienBao());
        edtLoai.setText(bienBao.getLoaiBienBao());
        edtMoTa.setText(bienBao.getMoTa());
        edtHinhAnh.setText(bienBao.getHinhAnh());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Sửa biển báo")
                .setView(view)
                .setPositiveButton("Cập nhật", null)
                .setNegativeButton("Hủy", (d, w) -> d.dismiss())
                .create();

        dialog.setOnShowListener(d -> {
            Button btnUpdate = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnUpdate.setOnClickListener(v -> {
                String ten = edtTen.getText().toString().trim();
                String loai = edtLoai.getText().toString().trim();
                String moTa = edtMoTa.getText().toString().trim();
                String hinhAnh = edtHinhAnh.getText().toString().trim();

                if (!ten.isEmpty() && !loai.isEmpty() && !moTa.isEmpty()) {
                    bienBao.setTenBienBao(ten);
                    bienBao.setLoaiBienBao(loai);
                    bienBao.setMoTa(moTa);
                    bienBao.setHinhAnh(hinhAnh);
                    bienBaoController.updateBienBao(bienBao);
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show();
    }
}
