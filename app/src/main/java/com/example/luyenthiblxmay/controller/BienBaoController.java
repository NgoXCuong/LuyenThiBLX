package com.example.luyenthiblxmay.controller;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.luyenthiblxmay.dao.BienBaoDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.model.BienBao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BienBaoController {
    private final BienBaoDao bienBaoDao;
    private final ExecutorService executorService;

    public BienBaoController(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        bienBaoDao = db.bienBaoDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Thêm 1 biển báo
    public void insertBienBao(BienBao bienBao) {
        executorService.execute(() -> bienBaoDao.insertBienBao(bienBao));
    }

    // Cập nhật
    public void updateBienBao(BienBao bienBao) {
        executorService.execute(() -> bienBaoDao.updateBienBao(bienBao));
    }

    // Xóa 1 biển báo
    public void deleteBienBao(BienBao bienBao) {
        executorService.execute(() -> bienBaoDao.deleteBienBao(bienBao));
    }

    // Lấy tất cả
    public LiveData<List<BienBao>> getAllBienBao() {
        return bienBaoDao.getAllBienBao();
    }

    // Lấy theo loại
    public LiveData<List<BienBao>> getBienBaoByLoai(String loai) {
        return bienBaoDao.getBienBaoByLoai(loai);
    }
}
