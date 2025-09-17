package com.example.luyenthiblxmay.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.model.BienBao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AdminBienBaoAdapter extends RecyclerView.Adapter<AdminBienBaoAdapter.AdminBienBaoViewHolder> {
    private final Context context;
    private List<BienBao> bienBaoList;
    private final OnBienBaoActionListener listener;

    // Interface cho sự kiện chỉnh sửa / xóa
    public interface OnBienBaoActionListener {
        void onEdit(BienBao bienBao);
        void onDelete(BienBao bienBao);
    }

    public AdminBienBaoAdapter(Context context, List<BienBao> bienBaoList, OnBienBaoActionListener listener) {
        this.context = context;
        this.bienBaoList = bienBaoList;
        this.listener = listener;
    }

    public void setBienBaoList(List<BienBao> bienBaoList) {
        this.bienBaoList = bienBaoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminBienBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_bienbao, parent, false);
        return new AdminBienBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBienBaoViewHolder holder, int position) {
        BienBao bienBao = bienBaoList.get(position);

        holder.tvTenBienBao.setText(bienBao.getTenBienBao());
        holder.tvLoaiBienBao.setText(bienBao.getLoaiBienBao());
        holder.tvMoTa.setText(bienBao.getMoTa());

        // Load ảnh từ assets/bienbao/
        if (bienBao.getHinhAnh() != null && !bienBao.getHinhAnh().isEmpty()) {
            try {
                InputStream is = context.getAssets().open("bienbao/" + bienBao.getHinhAnh());
                Drawable drawable = Drawable.createFromStream(is, null);
                holder.imgBienBao.setImageDrawable(drawable);
                holder.imgBienBao.setVisibility(View.VISIBLE);
                is.close();
            } catch (IOException e) {
                holder.imgBienBao.setVisibility(View.GONE);
                e.printStackTrace();
            }
        } else {
            holder.imgBienBao.setVisibility(View.GONE);
        }

        // Xử lý nút sửa
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(bienBao);
            }
        });

        // Xử lý nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(bienBao);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bienBaoList != null ? bienBaoList.size() : 0;
    }

    public static class AdminBienBaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenBienBao, tvLoaiBienBao, tvMoTa;
        ImageView imgBienBao;
        ImageButton btnEdit, btnDelete;

        public AdminBienBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenBienBao = itemView.findViewById(R.id.tvTenBienBao);
            tvLoaiBienBao = itemView.findViewById(R.id.tvLoaiBienBao);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            imgBienBao = itemView.findViewById(R.id.imgBienBao);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
