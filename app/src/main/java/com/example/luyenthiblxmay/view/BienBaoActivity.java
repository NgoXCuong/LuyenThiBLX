package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.adapter.BienBaoAdapter;
import com.example.luyenthiblxmay.controller.BienBaoController;
import com.example.luyenthiblxmay.model.BienBao;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class BienBaoActivity extends AppCompatActivity {
    private BienBaoController bienBaoController;
    private RecyclerView recyclerBienBao;
    private BienBaoAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bien_bao);

        bienBaoController = new BienBaoController(getApplication());
        recyclerBienBao = findViewById(R.id.recyclerBienBao);
        tabLayout = findViewById(R.id.tabLayout);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        adapter = new BienBaoAdapter(this, null);
        recyclerBienBao.setLayoutManager(new LinearLayoutManager(this));
        recyclerBienBao.setAdapter(adapter);

        // Load mặc định "Cấm"
        bienBaoController.getBienBaoByLoai("Cam").observe(this,
                bienBaos -> adapter.setBienBaoList(bienBaos));

        setupTabs();
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Biển báo cấm"));
        tabLayout.addTab(tabLayout.newTab().setText("Biển báo nguy hiểm"));
        tabLayout.addTab(tabLayout.newTab().setText("Biển chỉ dẫn"));
        tabLayout.addTab(tabLayout.newTab().setText("Biển hiệu lệnh"));
        tabLayout.addTab(tabLayout.newTab().setText("Biển báo phụ"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String loai;
                switch (tab.getPosition()) {
                    case 0: loai = "Cam"; break;
                    case 1: loai = "Nguy hiem"; break;
                    case 2: loai = "Chi dan"; break;
                    case 3: loai = "Hieu lenh"; break;
                    case 4: loai = "Phu"; break;
                    default: loai = ""; break;
                }

                bienBaoController.getBienBaoByLoai(loai)
                        .observe(BienBaoActivity.this,
                                bienBaos -> adapter.setBienBaoList(bienBaos));
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
}
