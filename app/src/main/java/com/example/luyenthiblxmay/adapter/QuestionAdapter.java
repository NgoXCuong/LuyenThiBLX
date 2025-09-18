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

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.controller.UserQuestionController;
import com.example.luyenthiblxmay.model.Question;
import com.example.luyenthiblxmay.model.UserQuestion;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    public interface ProgressUpdateListener {
        void onProgressUpdated();
    }

    private final Context context;
    private List<Question> questions;
    private final int userId;
    private final UserQuestionController userQuestionController;
    private Map<Integer, UserQuestion> userProgressMap;
    private final ProgressUpdateListener progressListener;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public QuestionAdapter(Context context, List<Question> questions, int userId,
                           Map<Integer, UserQuestion> userProgressMap,
                           ProgressUpdateListener progressListener) {
        this.context = context;
        this.questions = questions;
        this.userId = userId;
        this.userQuestionController = new UserQuestionController(context);
        this.userProgressMap = userProgressMap;
        this.progressListener = progressListener;
    }

    public void setQuestions(List<Question> questions, Map<Integer, UserQuestion> progressMap) {
        this.questions = questions;
        this.userProgressMap = progressMap;
        notifyDataSetChanged();
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
        holder.tvQuestionText.setText("Câu " + (position + 1) + ": " + question.getQuestion());

        // Hiển thị ảnh nếu có
        if (question.getImage() != null && !question.getImage().trim().isEmpty()) {
            holder.imgQuestion.setVisibility(View.VISIBLE);
            try (InputStream is = context.getAssets().open(question.getImage())) {
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                holder.imgQuestion.setImageBitmap(bitmap);
            } catch (IOException e) {
                holder.imgQuestion.setVisibility(View.GONE);
            }
        } else {
            holder.imgQuestion.setVisibility(View.GONE);
        }

        // Hiển thị đáp án
        holder.rbA.setVisibility(View.GONE);
        holder.rbB.setVisibility(View.GONE);
        holder.rbC.setVisibility(View.GONE);
        holder.rbD.setVisibility(View.GONE);
        if (question.getOptions() != null) {
            if (question.getOptions().containsKey("A")) { holder.rbA.setText("A. " + question.getOptions().get("A")); holder.rbA.setVisibility(View.VISIBLE);}
            if (question.getOptions().containsKey("B")) { holder.rbB.setText("B. " + question.getOptions().get("B")); holder.rbB.setVisibility(View.VISIBLE);}
            if (question.getOptions().containsKey("C")) { holder.rbC.setText("C. " + question.getOptions().get("C")); holder.rbC.setVisibility(View.VISIBLE);}
            if (question.getOptions().containsKey("D")) { holder.rbD.setText("D. " + question.getOptions().get("D")); holder.rbD.setVisibility(View.VISIBLE);}
        }

        // Reset radioGroup
        holder.radioGroup.setOnCheckedChangeListener(null);
        holder.radioGroup.clearCheck();
        holder.tvExplanation.setVisibility(View.GONE);

        // Load trạng thái câu trả lời từ snapshot
        UserQuestion uq = userProgressMap != null ? userProgressMap.get(question.getId()) : null;
        if (uq != null && uq.isAnswered()) {
            question.setAnswered(true);
            question.setSelectedAnswer(uq.getSelectedAnswer());
            checkAnswer(holder, question);
        }

        // Xử lý chọn đáp án
        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String sel = null;
            if (checkedId == holder.rbA.getId()) sel = "A";
            else if (checkedId == holder.rbB.getId()) sel = "B";
            else if (checkedId == holder.rbC.getId()) sel = "C";
            else if (checkedId == holder.rbD.getId()) sel = "D";

            if (sel != null) {
                final String selectedAnswer = sel;
                question.setSelectedAnswer(selectedAnswer);
                question.setAnswered(true);

                boolean isCorrect = selectedAnswer.equals(question.getAnswer());

                // Lưu vào database
                executor.execute(() -> userQuestionController.saveAnswer(
                        userId,
                        question.getId(),
                        selectedAnswer,
                        isCorrect
                ));

                // Cập nhật snapshot
                if (userProgressMap != null) {
                    UserQuestion newUq = new UserQuestion(
                            userId,
                            question.getId(),
                            true,
                            selectedAnswer,
                            isCorrect,
                            System.currentTimeMillis()
                    );
                    userProgressMap.put(question.getId(), newUq);
                }

                checkAnswer(holder, question);

                if (progressListener != null) progressListener.onProgressUpdated();
            }
        });

    }

    private void checkAnswer(QuestionViewHolder holder, Question question) {
        String selected = question.getSelectedAnswer();
        if (selected == null) return;

        holder.rbA.setChecked("A".equals(selected));
        holder.rbB.setChecked("B".equals(selected));
        holder.rbC.setChecked("C".equals(selected));
        holder.rbD.setChecked("D".equals(selected));

        if (selected.equals(question.getAnswer())) {
            holder.tvExplanation.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.tvExplanation.setText("✔ Đúng! " + (question.getExplanation() != null ? question.getExplanation() : ""));
        } else {
            String correctOption = question.getAnswer();
            if (question.getOptions() != null && question.getOptions().get(correctOption) != null) {
                correctOption += ". " + question.getOptions().get(correctOption);
            }
            holder.tvExplanation.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.tvExplanation.setText("✘ Sai! Đáp án đúng: " + correctOption + "\n" +
                    (question.getExplanation() != null ? question.getExplanation() : ""));
        }

        holder.tvExplanation.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionText, tvExplanation;
        ImageView imgQuestion;
        RadioGroup radioGroup;
        RadioButton rbA, rbB, rbC, rbD;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            tvExplanation = itemView.findViewById(R.id.tvExplanation);
            imgQuestion = itemView.findViewById(R.id.imgQuestion);
            radioGroup = itemView.findViewById(R.id.radioGroupOptions);
            rbA = itemView.findViewById(R.id.rbOptionA);
            rbB = itemView.findViewById(R.id.rbOptionB);
            rbC = itemView.findViewById(R.id.rbOptionC);
            rbD = itemView.findViewById(R.id.rbOptionD);
        }
    }
}
