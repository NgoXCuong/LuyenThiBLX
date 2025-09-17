package com.example.luyenthiblxmay.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "bien_bao")
public class BienBao {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "ten_bien_bao")
    private String tenBienBao;   // Tên biển báo
    @ColumnInfo(name = "loai_bien_bao")
    private String loaiBienBao;  // Loại biển báo (cấm, nguy hiểm, chỉ dẫn...)
    @ColumnInfo(name = "mo_ta")
    private String moTa;         // Nội dung mô tả chi tiết biển báo
    @ColumnInfo(name = "hinh_anh")
    private String hinhAnh;      // Đường dẫn hoặc URL tới hình ảnh biển báo
    public  BienBao(String tenBienBao, String loaiBienBao, String moTa, String hinhAnh){
        this.tenBienBao = tenBienBao;
        this.loaiBienBao = loaiBienBao;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenBienBao() { return tenBienBao; }
    public void setTenBienBao(String tenBienBao) { this.tenBienBao = tenBienBao; }
    public String getLoaiBienBao() { return loaiBienBao; }
    public void setLoaiBienBao(String loaiBienBao) { this.loaiBienBao = loaiBienBao; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
}
