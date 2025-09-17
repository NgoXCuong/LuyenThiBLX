package com.example.luyenthiblxmay.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.controller.QuestionController;
import com.example.luyenthiblxmay.model.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final Context context;
    private List<Question> questions;
    private final QuestionController questionController;

    public QuestionAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
        this.questionController = new QuestionController(((android.app.Application) context.getApplicationContext()));
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    public List<Question> getQuestions() {
        return questions;
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

        // Nội dung câu hỏi
        holder.tvQuestionText.setText("Câu " + (position + 1) + ": " + question.getQuestion());

        // Ảnh minh họa
        if (question.getImage() != null && !question.getImage().trim().isEmpty()) {
            holder.imgQuestion.setVisibility(View.VISIBLE);
            try {
                InputStream inputStream = context.getAssets().open(question.getImage());
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

        // Ẩn tất cả option trước
        holder.rbA.setVisibility(View.GONE);
        holder.rbB.setVisibility(View.GONE);
        holder.rbC.setVisibility(View.GONE);
        holder.rbD.setVisibility(View.GONE);

        // Các đáp án
        Map<String, String> options = question.getOptions();
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

        // Reset RadioGroup
        holder.radioGroup.setOnCheckedChangeListener(null);
        holder.radioGroup.clearCheck();
        holder.tvExplanation.setVisibility(View.GONE);

        // Nếu đã chọn đáp án trước đó → đánh dấu + hiện giải thích
        if (question.getSelectedAnswer() != null) {
            checkAnswer(holder, question);
        }

        // Khi user chọn đáp án mới
        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selected = null;
            if (checkedId == holder.rbA.getId()) selected = "A";
            else if (checkedId == holder.rbB.getId()) selected = "B";
            else if (checkedId == holder.rbC.getId()) selected = "C";
            else if (checkedId == holder.rbD.getId()) selected = "D";

            if (selected != null) {
                question.setSelectedAnswer(selected);
                question.setAnswered(true);

                // Lưu tiến độ
                questionController.updateQuestion(question);

                // Hiển thị giải thích
                checkAnswer(holder, question);
            }
        });
    }

    // Hiển thị giải thích
    private void checkAnswer(QuestionViewHolder holder, Question question) {
        String selected = question.getSelectedAnswer();
        if (selected == null) return;

        // Tick đáp án đã chọn
        if ("A".equals(selected)) holder.rbA.setChecked(true);
        else if ("B".equals(selected)) holder.rbB.setChecked(true);
        else if ("C".equals(selected)) holder.rbC.setChecked(true);
        else if ("D".equals(selected)) holder.rbD.setChecked(true);

        // Hiển thị giải thích
        if (selected.equals(question.getAnswer())) {
            String explanation = (question.getExplanation() != null && !question.getExplanation().trim().isEmpty())
                    ? "✔ Đúng! " + question.getExplanation()
                    : "✔ Đúng!";
            holder.tvExplanation.setText(explanation);
            holder.tvExplanation.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            String correctOption = question.getAnswer();
            if (question.getOptions() != null && question.getOptions().get(correctOption) != null) {
                correctOption += ". " + question.getOptions().get(correctOption);
            }
            String explanation = (question.getExplanation() != null && !question.getExplanation().trim().isEmpty())
                    ? "\n💡 " + question.getExplanation()
                    : "";
            holder.tvExplanation.setText("✘ Sai! Đáp án đúng: " + correctOption + explanation);
            holder.tvExplanation.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        holder.tvExplanation.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return (questions != null) ? questions.size() : 0;
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionText, tvExplanation;
        ImageView imgQuestion;
        RadioGroup radioGroup;
        RadioButton rbA, rbB, rbC, rbD;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            imgQuestion = itemView.findViewById(R.id.imgQuestion);
            radioGroup = itemView.findViewById(R.id.radioGroupOptions);
            rbA = itemView.findViewById(R.id.rbOptionA);
            rbB = itemView.findViewById(R.id.rbOptionB);
            rbC = itemView.findViewById(R.id.rbOptionC);
            rbD = itemView.findViewById(R.id.rbOptionD);
            tvExplanation = itemView.findViewById(R.id.tvExplanation);
        }
    }
}

