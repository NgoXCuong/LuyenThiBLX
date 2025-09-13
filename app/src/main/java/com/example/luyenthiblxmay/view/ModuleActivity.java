package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;

import java.util.ArrayList;
import java.util.List;

public class ModuleActivity extends AppCompatActivity {
    // ProgressBars
    private ProgressBar progressBook, progressEthics, progressDrive, progressSigns, progressSimulation;
    // TextViews hiển thị tiến độ
    private TextView txtProgressBook, txtProgressEthics, txtProgressDrive, txtProgressSigns, txtProgressSimulation;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Khởi tạo ProgressBars và TextViews
        progressBook = findViewById(R.id.progressBarBook);
        txtProgressBook = findViewById(R.id.txtProgressBook);

        progressEthics = findViewById(R.id.progressBarEthics);
        txtProgressEthics = findViewById(R.id.txtProgressEthics);

        progressDrive = findViewById(R.id.progressBarDrive);
        txtProgressDrive = findViewById(R.id.txtProgressDrive);

        progressSigns = findViewById(R.id.progressBarSigns);
        txtProgressSigns = findViewById(R.id.txtProgressSigns);

        progressSimulation = findViewById(R.id.progressBarSimulation);
        txtProgressSimulation = findViewById(R.id.txtProgressSimulation);

        // Giả lập dữ liệu tiến độ (có thể lấy từ DB hoặc SharedPreferences)
        int doneBook = 40, totalBook = 100;
        int doneEthics = 3, totalEthics = 10;
        int doneDrive = 5, totalDrive = 15;
        int doneSigns = 30, totalSigns = 90;
        int doneSimulation = 10, totalSimulation = 35;

        // Cập nhật progress
        updateProgress(progressBook, txtProgressBook, doneBook, totalBook);
        updateProgress(progressEthics, txtProgressEthics, doneEthics, totalEthics);
        updateProgress(progressDrive, txtProgressDrive, doneDrive, totalDrive);
        updateProgress(progressSigns, txtProgressSigns, doneSigns, totalSigns);
        updateProgress(progressSimulation, txtProgressSimulation, doneSimulation, totalSimulation);
    }

    private void updateProgress(ProgressBar progressBar, TextView textView, int done, int total) {
        progressBar.setMax(total);
        progressBar.setProgress(done);
        textView.setText(done + "/" + total);
    }
}