package com.example.luyenthiblxmay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.model.Question;

import java.util.ArrayList;
import java.util.List;

public class AdminQuestionsAdapter extends RecyclerView.Adapter<AdminQuestionsAdapter.QuestionViewHolder> {
    private List<Question> questions = new ArrayList<>();
    private OnEditListener onEditListener;
    private OnDeleteListener onDeleteListener;
    public interface OnEditListener {
        void onEdit(Question question);
    }

    public interface OnDeleteListener {
        void onDelete(Question question);
    }

    public void setOnEditListener(OnEditListener listener) {
        this.onEditListener = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.onDeleteListener = listener;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.tvQuestion.setText(question.getQuestion());
        holder.tvAnswer.setText("Đáp án đúng: " + question.getAnswer());

        holder.btnEdit.setOnClickListener(v -> {
            if (onEditListener != null) onEditListener.onEdit(question);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteListener != null) onDeleteListener.onDelete(question);
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvAnswer;
        Button btnEdit, btnDelete;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}