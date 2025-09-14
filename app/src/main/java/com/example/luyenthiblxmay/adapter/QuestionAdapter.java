package com.example.luyenthiblxmay.adapter;

import android.content.Context;
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
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private Context context;
    private List<Question> questions;

    public QuestionAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);

        holder.tvQuestion.setText((position + 1) + ". " + question.getQuestion());

        // Hiển thị ảnh nếu có
        if (question.getImage() != null && !question.getImage().isEmpty()) {
            int resId = context.getResources().getIdentifier(
                    question.getImage(), "drawable", context.getPackageName()
            );
            if (resId != 0) {
                holder.imgQuestion.setVisibility(View.VISIBLE);
                holder.imgQuestion.setImageResource(resId);
            } else {
                holder.imgQuestion.setVisibility(View.GONE);
            }
        } else {
            holder.imgQuestion.setVisibility(View.GONE);
        }

        // Set đáp án linh hoạt
        Map<String, String> options = question.getOptions();
        if (options != null) {
            // Option A
            if (options.containsKey("A") && !options.get("A").isEmpty()) {
                holder.rbA.setText("A. " + options.get("A"));
                holder.rbA.setVisibility(View.VISIBLE);
            } else {
                holder.rbA.setVisibility(View.GONE);
            }

            // Option B
            if (options.containsKey("B") && !options.get("B").isEmpty()) {
                holder.rbB.setText("B. " + options.get("B"));
                holder.rbB.setVisibility(View.VISIBLE);
            } else {
                holder.rbB.setVisibility(View.GONE);
            }

            // Option C
            if (options.containsKey("C") && !options.get("C").isEmpty()) {
                holder.rbC.setText("C. " + options.get("C"));
                holder.rbC.setVisibility(View.VISIBLE);
            } else {
                holder.rbC.setVisibility(View.GONE);
            }

            // Option D
            if (options.containsKey("D") && !options.get("D").isEmpty()) {
                holder.rbD.setText("D. " + options.get("D"));
                holder.rbD.setVisibility(View.VISIBLE);
            } else {
                holder.rbD.setVisibility(View.GONE);
            }
        }

        // Nếu đã chọn đáp án thì hiển thị lại
        if (question.getSelectedAnswer() != null) {
            switch (question.getSelectedAnswer()) {
                case "A":
                    if (holder.rbA.getVisibility() == View.VISIBLE) holder.radioGroup.check(holder.rbA.getId());
                    break;
                case "B":
                    if (holder.rbB.getVisibility() == View.VISIBLE) holder.radioGroup.check(holder.rbB.getId());
                    break;
                case "C":
                    if (holder.rbC.getVisibility() == View.VISIBLE) holder.radioGroup.check(holder.rbC.getId());
                    break;
                case "D":
                    if (holder.rbD.getVisibility() == View.VISIBLE) holder.radioGroup.check(holder.rbD.getId());
                    break;
            }
        } else {
            holder.radioGroup.clearCheck();
        }

        // Xử lý chọn đáp án
        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == holder.rbA.getId()) {
                question.setSelectedAnswer("A");
            } else if (checkedId == holder.rbB.getId()) {
                question.setSelectedAnswer("B");
            } else if (checkedId == holder.rbC.getId()) {
                question.setSelectedAnswer("C");
            } else if (checkedId == holder.rbD.getId()) {
                question.setSelectedAnswer("D");
            }
        });
    }


    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        ImageView imgQuestion;
        RadioGroup radioGroup;
        RadioButton rbA, rbB, rbC, rbD;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestionText);
            imgQuestion = itemView.findViewById(R.id.imgQuestion);
            radioGroup = itemView.findViewById(R.id.radioGroupOptions);
            rbA = itemView.findViewById(R.id.rbOptionA);
            rbB = itemView.findViewById(R.id.rbOptionB);
            rbC = itemView.findViewById(R.id.rbOptionC);
            rbD = itemView.findViewById(R.id.rbOptionD);
        }
    }
}