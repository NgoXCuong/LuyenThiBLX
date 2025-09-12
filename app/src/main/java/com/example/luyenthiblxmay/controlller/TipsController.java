package com.example.luyenthiblxmay.controlller;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.luyenthiblxmay.dao.TipsDao;
import com.example.luyenthiblxmay.database.AppDatabase;
import com.example.luyenthiblxmay.model.Tips;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TipsController {
    private final TipsDao tipsDao;
    private final ExecutorService executorService;

    public TipsController(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        tipsDao = db.tipsDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Thêm
    public void insertTip(Tips tip) {
        executorService.execute(() -> tipsDao.insertTip(tip));
    }

    public void insertAll(List<Tips> tips) {
        executorService.execute(() -> tipsDao.insertAllTips(tips));
    }

    // Cập nhật
    public void updateTip(Tips tip) {
        executorService.execute(() -> tipsDao.updateTip(tip));
    }

    // Xóa
    public void deleteTip(Tips tip) {
        executorService.execute(() -> tipsDao.deleteTip(tip));
    }

    // Lấy toàn bộ (Room quản lý LiveData, auto update)
    public LiveData<List<Tips>> getAllTips() {
        return tipsDao.getAllTips();
    }

    public LiveData<Tips> getTipById(int id) {
        return tipsDao.getTipById(id);
    }

    public LiveData<List<Tips>> searchTipsByTitle(String keyword) {
        return tipsDao.searchTipsByTitle(keyword);
    }
}

