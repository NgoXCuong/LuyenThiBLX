package com.example.luyenthiblxmay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.model.Tips;

import java.util.ArrayList;
import java.util.List;

public class AdminTipsAdapter extends RecyclerView.Adapter<AdminTipsAdapter.TipViewHolder> {
    public interface OnTipActionListener {
        void onEdit(Tips tip);
        void onDelete(Tips tip);
    }

    private final Context context;
    private final OnTipActionListener listener;
    private List<Tips> tipsList = new ArrayList<>();

    public AdminTipsAdapter(Context context, OnTipActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setTipsList(List<Tips> list) {
        this.tipsList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_tip, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        Tips tip = tipsList.get(position);
        holder.tvTitle.setText(tip.getTitle());
        holder.tvContent.setText(tip.getContent());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(tip));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(tip));
    }

    @Override
    public int getItemCount() {
        return tipsList.size();
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        ImageButton btnEdit, btnDelete;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTipTitle);
            tvContent = itemView.findViewById(R.id.tvTipContent);
            btnEdit = itemView.findViewById(R.id.btnEditTip);
            btnDelete = itemView.findViewById(R.id.btnDeleteTip);
        }
    }
}
