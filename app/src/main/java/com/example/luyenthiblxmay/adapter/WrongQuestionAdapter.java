package com.example.luyenthiblxmay.adapter;

import android.content.Context;
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

import java.util.List;

public class WrongQuestionAdapter extends RecyclerView.Adapter<WrongQuestionAdapter.ViewHolder> {

    private final List<Question> questions;
    private final Context context;

    public WrongQuestionAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_wrong_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questions.get(position);

        // Nội dung câu hỏi
        holder.tvQuestion.setText(question.getQuestion());

        // Hình minh họa (nếu có)
        if (question.getImage() != null && !question.getImage().isEmpty()) {
            holder.imgQuestion.setVisibility(View.VISIBLE);
            // Nếu dùng Glide/Picasso:
            // Glide.with(context).load(question.getImage()).into(holder.imgQuestion);
        } else {
            holder.imgQuestion.setVisibility(View.GONE);
        }

        // Các đáp án
        holder.rbOptionA.setText("A. " + question.getOptions().get("A"));
        holder.rbOptionB.setText("B. " + question.getOptions().get("B"));
        holder.rbOptionC.setText("C. " + question.getOptions().get("C"));
        holder.rbOptionD.setText("D. " + question.getOptions().get("D"));

        // Hiển thị đáp án đúng & đánh dấu sai (tùy chọn)
        String correct = question.getAnswer();
        holder.rbOptionA.setEnabled(false);
        holder.rbOptionB.setEnabled(false);
        holder.rbOptionC.setEnabled(false);
        holder.rbOptionD.setEnabled(false);

        switch (correct) {
            case "A":
                holder.rbOptionA.setTextColor(Color.GREEN);
                break;
            case "B":
                holder.rbOptionB.setTextColor(Color.GREEN);
                break;
            case "C":
                holder.rbOptionC.setTextColor(Color.GREEN);
                break;
            case "D":
                holder.rbOptionD.setTextColor(Color.GREEN);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        ImageView imgQuestion;
        RadioButton rbOptionA, rbOptionB, rbOptionC, rbOptionD;
        RadioGroup rgOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            imgQuestion = itemView.findViewById(R.id.imgQuestion);
            rgOptions = itemView.findViewById(R.id.rgOptions);
            rbOptionA = itemView.findViewById(R.id.rbOptionA);
            rbOptionB = itemView.findViewById(R.id.rbOptionB);
            rbOptionC = itemView.findViewById(R.id.rbOptionC);
            rbOptionD = itemView.findViewById(R.id.rbOptionD);
        }
    }
}

