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

    // Thêm nhiều biển báo cùng lúc
    @Insert
    void insertAllBienBao(List<BienBao> bienBaoList);

    // Cập nhật biển báo
    @Update
    void updateBienBao(BienBao bienBao);

    // Xóa một biển báo
    @Delete
    void deleteBienBao(BienBao bienBao);

    // Xóa tất cả biển báo
    @Query("DELETE FROM bien_bao")
    void deleteAllBienBao();

    // Lấy tất cả biển báo
    @Query("SELECT * FROM bien_bao")
    LiveData<List<BienBao>> getAllBienBao();

    // Lấy biển báo theo id
    @Query("SELECT * FROM bien_bao WHERE id = :id LIMIT 1")
    LiveData<BienBao> getBienBaoById(int id);

    // Tìm biển báo theo loại
    @Query("SELECT * FROM bien_bao WHERE loai_bien_bao = :loai")
    LiveData<List<BienBao>> getBienBaoByLoai(String loai);

    // Tìm biển báo theo tên (gần đúng)
    @Query("SELECT * FROM bien_bao WHERE ten_bien_bao LIKE '%' || :keyword || '%'")
    LiveData<List<BienBao>> searchBienBaoByName(String keyword);
}
