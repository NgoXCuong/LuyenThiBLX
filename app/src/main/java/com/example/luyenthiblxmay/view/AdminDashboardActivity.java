package com.example.luyenthiblxmay.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luyenthiblxmay.R;

public class AdminDashboardActivity extends AppCompatActivity {
    private ListView listView;
    private String[] menuItems = {
            "Quản lý mẹo",
            "Quản lý tài khoản"
            // có thể thêm "Quản lý câu hỏi", "Thống kê" sau này
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        listView = findViewById(R.id.listViewAdminMenu);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                menuItems
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Quản lý mẹo
                        startActivity(new Intent(AdminDashboardActivity.this, AdminTipsActivity.class));
                        break;
                    case 1: // Quản lý tài khoản
                        startActivity(new Intent(AdminDashboardActivity.this, AdminUsersActivity.class));
                        break;
                }
            }
        });
    }
}