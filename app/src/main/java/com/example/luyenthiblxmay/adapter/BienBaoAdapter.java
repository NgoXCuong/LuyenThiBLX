package com.example.luyenthiblxmay.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.model.BienBao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BienBaoAdapter extends RecyclerView.Adapter<BienBaoAdapter.BienBaoViewHolder> {

    private static final String TAG = "BienBaoAdapter";
    private final Context context;
    private List<BienBao> bienBaoList;

    public BienBaoAdapter(Context context, List<BienBao> bienBaoList) {
        this.context = context;
        this.bienBaoList = bienBaoList;
    }

    public void setBienBaoList(List<BienBao> bienBaoList) {
        this.bienBaoList = bienBaoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BienBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bienbao, parent, false);
        return new BienBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BienBaoViewHolder holder, int position) {
        BienBao bienBao = bienBaoList.get(position);

        holder.tvTenBienBao.setText(bienBao.getTenBienBao());
        holder.tvLoaiBienBao.setText(bienBao.getLoaiBienBao());
        holder.tvMoTa.setText(bienBao.getMoTa());

        // Hiển thị ảnh từ assets/bienbao/
        if (bienBao.getHinhAnh() != null && !bienBao.getHinhAnh().isEmpty()) {
            Log.d(TAG, "Đang load ảnh cho " + bienBao.getTenBienBao() +
                    " | File: " + bienBao.getHinhAnh());
            setImageFromAssets(holder.imgBienBao, bienBao.getHinhAnh());
        } else {
            Log.w(TAG, "BienBao không có ảnh: " + bienBao.getTenBienBao());
            holder.imgBienBao.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bienBaoList != null ? bienBaoList.size() : 0;
    }

    // Hàm tiện ích load ảnh từ assets/bienbao/
    private void setImageFromAssets(ImageView imageView, String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            Log.w(TAG, "⚠️ Tên file rỗng, hiển thị placeholder");
            imageView.setImageResource(R.drawable.ic_placeholder);
            return;
        }

        // Xóa "bienbao/" nếu đã có sẵn trong DB
        String cleanFileName = fileName.replace("bienbao/", "");

        // Nếu thiếu đuôi file thì thêm mặc định
        if (!cleanFileName.endsWith(".jpg") && !cleanFileName.endsWith(".png")) {
            cleanFileName += ".jpg";
        }

        String filePath = "bienbao/" + cleanFileName;

        try (InputStream is = imageView.getContext().getAssets().open(filePath)) {
            Drawable drawable = Drawable.createFromStream(is, null);
            imageView.setImageDrawable(drawable);
            imageView.setVisibility(View.VISIBLE);
            Log.d(TAG, "✅ Load thành công: " + filePath);
        } catch (IOException e) {
            Log.e(TAG, "❌ Không tìm thấy ảnh: " + filePath, e);
            imageView.setImageResource(R.drawable.ic_placeholder);
            imageView.setVisibility(View.VISIBLE);
        }
    }


    public static class BienBaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenBienBao, tvLoaiBienBao, tvMoTa;
        ImageView imgBienBao;

        public BienBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenBienBao = itemView.findViewById(R.id.tvTenBienBao);
            tvLoaiBienBao = itemView.findViewById(R.id.tvLoaiBienBao);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            imgBienBao = itemView.findViewById(R.id.imgBienBao);
        }
    }
}

