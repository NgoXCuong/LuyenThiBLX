package com.example.luyenthiblxmay.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.model.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class WrongQuestionAdapter extends RecyclerView.Adapter<WrongQuestionAdapter.ViewHolder> {
    private Context context;
    private List<Question> questions;

    public WrongQuestionAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wrong_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question q = questions.get(position);

        holder.tvQuestion.setText(q.getQuestion());

        // Hiển thị ảnh nếu có
        if (q.getImage() != null && !q.getImage().isEmpty()) {
            holder.imgQuestion.setVisibility(View.VISIBLE);
            try {
                // q.getImage() ví dụ: "bienbao/camdi.png"
                InputStream inputStream = context.getAssets().open(q.getImage());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                holder.imgQuestion.setImageBitmap(bitmap);
                inputStream.close();
            } catch (IOException e) {
                holder.imgQuestion.setVisibility(View.GONE);
                e.printStackTrace();
            }
        } else {
            holder.imgQuestion.setVisibility(View.GONE);
        }

        // Ẩn tất cả đáp án trước
        holder.rbA.setVisibility(View.GONE);
        holder.rbB.setVisibility(View.GONE);
        holder.rbC.setVisibility(View.GONE);
        holder.rbD.setVisibility(View.GONE);

        // Hiển thị đáp án nếu có
        Map<String, String> options = q.getOptions();
        if (options != null) {
            if (options.containsKey("A") && options.get("A") != null && !options.get("A").trim().isEmpty()) {
                holder.rbA.setText("A. " + options.get("A"));
                holder.rbA.setVisibility(View.VISIBLE);
            }
            if (options.containsKey("B") && options.get("B") != null && !options.get("B").trim().isEmpty()) {
                holder.rbB.setText("B. " + options.get("B"));
                holder.rbB.setVisibility(View.VISIBLE);
            }
            if (options.containsKey("C") && options.get("C") != null && !options.get("C").trim().isEmpty()) {
                holder.rbC.setText("C. " + options.get("C"));
                holder.rbC.setVisibility(View.VISIBLE);
            }
            if (options.containsKey("D") && options.get("D") != null && !options.get("D").trim().isEmpty()) {
                holder.rbD.setText("D. " + options.get("D"));
                holder.rbD.setVisibility(View.VISIBLE);
            }
        }

        // Reset lại RadioGroup và kết quả khi bind
        holder.rgOptions.clearCheck();
        holder.tvResult.setVisibility(View.GONE);

        // Lắng nghe chọn đáp án
        holder.rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            String selected = "";
            if (checkedId == holder.rbA.getId()) selected = "A";
            else if (checkedId == holder.rbB.getId()) selected = "B";
            else if (checkedId == holder.rbC.getId()) selected = "C";
            else if (checkedId == holder.rbD.getId()) selected = "D";

            if (!selected.isEmpty()) {
                if (selected.equalsIgnoreCase(q.getAnswer())) {
                    holder.tvResult.setTextColor(Color.parseColor("#388E3C"));
                    String explanation = (q.getExplanation() != null && !q.getExplanation().trim().isEmpty())
                            ? "\n\n💡 " + q.getExplanation() : "";
                    holder.tvResult.setText("✅ Chính xác!" + explanation);
                } else {
                    holder.tvResult.setTextColor(Color.parseColor("#D32F2F"));
                    String explanation = (q.getExplanation() != null && !q.getExplanation().trim().isEmpty())
                            ? "\n\n💡 " + q.getExplanation() : "";
                    holder.tvResult.setText("❌ Sai! Đáp án đúng là: " + q.getAnswer() + explanation);
                }
                holder.tvResult.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvResult;
        RadioGroup rgOptions;
        RadioButton rbA, rbB, rbC, rbD;
        ImageView imgQuestion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            rgOptions = itemView.findViewById(R.id.rgOptions);
            rbA = itemView.findViewById(R.id.rbOptionA);
            rbB = itemView.findViewById(R.id.rbOptionB);
            rbC = itemView.findViewById(R.id.rbOptionC);
            rbD = itemView.findViewById(R.id.rbOptionD);
            tvResult = itemView.findViewById(R.id.tvResult);
            imgQuestion = itemView.findViewById(R.id.imgQuestion);
        }
    }
}
