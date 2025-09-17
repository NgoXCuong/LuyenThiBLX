package com.example.luyenthiblxmay.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.luyenthiblxmay.model.BienBao;

import java.util.List;

@Dao
public interface BienBaoDao {
    // Thêm một biển báo
    @Insert
    void insertBienBao(BienBao bienBao);

    // Cập nhật biển báo
    @Update
    void updateBienBao(BienBao bienBao);

    // Xóa một biển báo
    @Delete
    void deleteBienBao(BienBao bienBao);

    // Lấy tất cả biển báo
    @Query("SELECT * FROM bien_bao")
    LiveData<List<BienBao>> getAllBienBao();

    // Tìm biển báo theo loại
    @Query("SELECT * FROM bien_bao WHERE loai_bien_bao = :loai")
    LiveData<List<BienBao>> getBienBaoByLoai(String loai);
}
