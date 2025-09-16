//package com.example.luyenthiblxmay.adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.luyenthiblxmay.R;
//import com.example.luyenthiblxmay.model.Question;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import java.util.Map;
//
//public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
//
//    private Context context;
//    private List<Question> questions;
//
//
//    public QuestionAdapter(Context context, List<Question> questions) {
//        this.context = context;
//        this.questions = questions;
//    }
//
//    @NonNull
//    @Override
//    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
//        return new QuestionViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
//        Question question = questions.get(position);
//
//        holder.tvQuestion.setText((position + 1) + ". " + question.getQuestion());
//
//        // Hiển thị ảnh từ assets
//        if (question.getImage() != null && !question.getImage().isEmpty()) {
//            setImageFromAssets(holder.imgQuestion, question.getImage());
//        } else {
//            holder.imgQuestion.setVisibility(View.GONE);
//        }
//
//        // Set đáp án linh hoạt
//        Map<String, String> options = question.getOptions();
//        if (options != null) {
//            setOption(holder.rbA, options.get("A"), "A");
//            setOption(holder.rbB, options.get("B"), "B");
//            setOption(holder.rbC, options.get("C"), "C");
//            setOption(holder.rbD, options.get("D"), "D");
//        }
//
//        // Reset giải thích
//        holder.tvExplanation.setVisibility(View.GONE);
//
//        // Nếu đã chọn đáp án thì hiển thị lại
//        if (question.getSelectedAnswer() != null) {
//            checkAnswer(holder, question);
//        } else {
//            holder.radioGroup.clearCheck();
//        }
//
//        // Xử lý chọn đáp án
//        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            String selected = "";
//            if (checkedId == holder.rbA.getId()) selected = "A";
//            else if (checkedId == holder.rbB.getId()) selected = "B";
//            else if (checkedId == holder.rbC.getId()) selected = "C";
//            else if (checkedId == holder.rbD.getId()) selected = "D";
//
//            question.setSelectedAnswer(selected);
//            checkAnswer(holder, question);
//        });
//    }
//
//    // Hàm set option gọn hơn
//    private void setOption(RadioButton rb, String text, String prefix) {
//        if (text != null && !text.isEmpty()) {
//            rb.setText(prefix + ". " + text);
//            rb.setVisibility(View.VISIBLE);
//        } else {
//            rb.setVisibility(View.GONE);
//        }
//    }
//
//    // Kiểm tra đáp án
//    // Kiểm tra đáp án
//    private void checkAnswer(QuestionViewHolder holder, Question question) {
//        String selected = question.getSelectedAnswer();
//        if (selected == null) return;
//
//        // Tick lại đáp án
//        switch (selected) {
//            case "A": if (holder.rbA.getVisibility() == View.VISIBLE) holder.radioGroup.check(holder.rbA.getId()); break;
//            case "B": if (holder.rbB.getVisibility() == View.VISIBLE) holder.radioGroup.check(holder.rbB.getId()); break;
//            case "C": if (holder.rbC.getVisibility() == View.VISIBLE) holder.radioGroup.check(holder.rbC.getId()); break;
//            case "D": if (holder.rbD.getVisibility() == View.VISIBLE) holder.radioGroup.check(holder.rbD.getId()); break;
//        }
//
//        // Nếu chọn đúng → hiển thị giải thích
//        if (selected.equals(question.getAnswer())) {
//            holder.tvExplanation.setText("✔ Đúng! Giải thích: " + question.getExplanation());
//            holder.tvExplanation.setVisibility(View.VISIBLE);
//        } else {
//            holder.tvExplanation.setText("✘ Sai! Đáp án đúng là "
//                    + question.getAnswer() + ". "
//                    + question.getOptions().get(question.getAnswer()));
//            holder.tvExplanation.setVisibility(View.VISIBLE);
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return questions != null ? questions.size() : 0;
//    }
//
//    public void setQuestions(List<Question> questions) {
//        this.questions = questions;
//        notifyDataSetChanged();
//    }
//
//    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
//        TextView tvQuestion, tvExplanation;
//        ImageView imgQuestion;
//        RadioGroup radioGroup;
//        RadioButton rbA, rbB, rbC, rbD;
//
//        public QuestionViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvQuestion = itemView.findViewById(R.id.tvQuestionText);
//            imgQuestion = itemView.findViewById(R.id.imgQuestion);
//            radioGroup = itemView.findViewById(R.id.radioGroupOptions);
//            rbA = itemView.findViewById(R.id.rbOptionA);
//            rbB = itemView.findViewById(R.id.rbOptionB);
//            rbC = itemView.findViewById(R.id.rbOptionC);
//            rbD = itemView.findViewById(R.id.rbOptionD);
//            tvExplanation = itemView.findViewById(R.id.tvExplanation); // thêm dòng này
//        }
//    }
//
//    private void setImageFromAssets(ImageView imageView, String fileName) {
//        try {
//            InputStream inputStream = imageView.getContext().getAssets().open(fileName);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            imageView.setImageBitmap(bitmap);
//            imageView.setVisibility(View.VISIBLE); // quan trọng vì mặc định GONE
//            Log.e("ImageLoad", "Đa load được ảnh: " + fileName);
//            inputStream.close();
//        } catch (IOException e) {
//            Log.e("ImageLoad", "Không load được ảnh: " + fileName, e);
//            imageView.setVisibility(View.GONE);
//        }
//    }
//  CHUA CAP NHAT TIEN DO
//}

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

import com.bumptech.glide.Glide;
import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.controller.QuestionController;
import com.example.luyenthiblxmay.model.Question;

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
        if (question.getImage() != null && !question.getImage().isEmpty()) {
            holder.imgQuestion.setVisibility(View.VISIBLE);
            Glide.with(context).load(question.getImage()).into(holder.imgQuestion);
        } else {
            holder.imgQuestion.setVisibility(View.GONE);
        }

        // Các đáp án
        Map<String, String> options = question.getOptions();
        if (options != null) {
            holder.rbA.setText("A. " + options.get("A"));
            holder.rbB.setText("B. " + options.get("B"));
            holder.rbC.setText("C. " + options.get("C"));
            holder.rbD.setText("D. " + options.get("D"));
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
        switch (selected) {
            case "A": holder.rbA.setChecked(true); break;
            case "B": holder.rbB.setChecked(true); break;
            case "C": holder.rbC.setChecked(true); break;
            case "D": holder.rbD.setChecked(true); break;
        }

        // Hiển thị giải thích
        if (selected.equals(question.getAnswer())) {
            holder.tvExplanation.setText("✔ Đúng! " + question.getExplanation());
            holder.tvExplanation.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            String correctOption = question.getAnswer() + ". " + question.getOptions().get(question.getAnswer());
            holder.tvExplanation.setText("✘ Sai! Đáp án đúng: " + correctOption + "\n" + question.getExplanation());
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

