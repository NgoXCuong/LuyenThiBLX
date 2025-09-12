package com.example.luyenthiblxmay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.model.Tips;

import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {
    private final Context context;
    private List<Tips> tipsList;

    public TipsAdapter(Context context, List<Tips> tipsList) {
        this.context = context;
        this.tipsList = tipsList;
    }

    public void setTipsList(List<Tips> tipsList) {
        this.tipsList = tipsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tip, parent, false);
        return new TipsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
        Tips tip = tipsList.get(position);
        holder.tvTitle.setText(tip.getTitle());
        holder.tvContent.setText(tip.getContent());
    }

    @Override
    public int getItemCount() {
        return tipsList != null ? tipsList.size() : 0;
    }

    public static class TipsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;

        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTipTitle);
            tvContent = itemView.findViewById(R.id.tvTipContent);
        }
    }
}