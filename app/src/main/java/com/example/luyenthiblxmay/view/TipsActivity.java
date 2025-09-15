package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.adapter.TipsAdapter;
import com.example.luyenthiblxmay.controller.TipsController;
import com.example.luyenthiblxmay.model.Tips;

import java.util.ArrayList;
import java.util.List;

public class TipsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMeo;
    private TipsAdapter adapter;
    private TipsController tipsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        recyclerViewMeo = findViewById(R.id.recyclerViewMeo);
        recyclerViewMeo.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TipsAdapter(this, new ArrayList<>());
        recyclerViewMeo.setAdapter(adapter);

        tipsController = new TipsController(getApplication());

        // Quan sát LiveData từ Controller
        tipsController.getAllTips().observe(this, tips -> {
            if (tips != null) {
                adapter.setTipsList(tips);
            }
        });

        // Nút back
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // ⚡️ Nếu database trống thì thêm dữ liệu mẫu
        addSampleTipsIfEmpty();
    }

    private void addSampleTipsIfEmpty() {
        tipsController.getAllTips().observe(this, tips -> {
            if (tips == null || tips.isEmpty()) {
                List<Tips> sample = new ArrayList<>();
                tipsController.insertAll(sample);
            }
        });
    }
}