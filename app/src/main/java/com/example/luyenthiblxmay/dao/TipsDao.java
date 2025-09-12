package com.example.luyenthiblxmay.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.Tips;

import java.util.List;

@Dao
public interface TipsDao {
    // Thêm một tip
    @Insert
    void insertTip(Tips tip);

    // Thêm nhiều tip cùng lúc
    @Insert
    void insertAllTips(List<Tips> tips);

    // Cập nhật tip
    @Update
    void updateTip(Tips tip);

    // Xóa tip
    @Delete
    void deleteTip(Tips tip);

    // Lấy tất cả tips -> Room tự động quan sát
    @Query("SELECT * FROM tips ORDER BY id DESC")
    LiveData<List<Tips>> getAllTips();

    // Lấy tip theo id
    @Query("SELECT * FROM tips WHERE id = :id LIMIT 1")
    LiveData<Tips> getTipById(int id);

    // Tìm kiếm theo tiêu đề
    @Query("SELECT * FROM tips WHERE title LIKE '%' || :keyword || '%'")
    LiveData<List<Tips>> searchTipsByTitle(String keyword);
}
