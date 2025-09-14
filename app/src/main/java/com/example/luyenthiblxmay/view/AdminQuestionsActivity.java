package com.example.luyenthiblxmay.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luyenthiblxmay.R;
import com.example.luyenthiblxmay.adapter.AdminQuestionsAdapter;
import com.example.luyenthiblxmay.controller.QuestionController;
import com.example.luyenthiblxmay.model.Question;

import java.util.HashMap;
import java.util.Map;

public class AdminQuestionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton btnAddQuestion;
    private AdminQuestionsAdapter adapter;
    private QuestionController questionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_questions);

        recyclerView = findViewById(R.id.recyclerViewQuestions);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);

        // Nút back
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminQuestionsAdapter();
        recyclerView.setAdapter(adapter);

        // Khởi tạo controller kiểu LiveData
        questionController = new QuestionController(getApplication());

        // Quan sát LiveData, RecyclerView tự cập nhật
        questionController.getAllQuestions().observe(this, questions -> {
            adapter.setQuestions(questions);
        });

        // Listener sửa
        adapter.setOnEditListener(this::showAddEditDialog);

        // Listener xóa
        adapter.setOnDeleteListener(question -> questionController.deleteQuestion(question));

        // Thêm mới
        btnAddQuestion.setOnClickListener(v -> showAddEditDialog(null));
    }

    private void showAddEditDialog(@Nullable Question question) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_add_edit_question, null);

        EditText etQuestion = view.findViewById(R.id.etQuestion);
        EditText etOptionA = view.findViewById(R.id.etOptionA);
        EditText etOptionB = view.findViewById(R.id.etOptionB);
        EditText etOptionC = view.findViewById(R.id.etOptionC);
        EditText etOptionD = view.findViewById(R.id.etOptionD);
        EditText etAnswer = view.findViewById(R.id.etAnswer);
        EditText etExplanation = view.findViewById(R.id.etExplanation);
        EditText etCategory = view.findViewById(R.id.etCategory);
        EditText etImage = view.findViewById(R.id.etImage);

        if (question != null) {
            etQuestion.setText(question.getQuestion());
            if (question.getOptions() != null) {
                etOptionA.setText(question.getOptions().get("A"));
                etOptionB.setText(question.getOptions().get("B"));
                etOptionC.setText(question.getOptions().get("C"));
                etOptionD.setText(question.getOptions().get("D"));
            }
            etAnswer.setText(question.getAnswer());
            etExplanation.setText(question.getExplanation());
            etCategory.setText(question.getCategory());
            etImage.setText(question.getImage());
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(question == null ? "Thêm câu hỏi" : "Sửa câu hỏi")
                .setView(view)
                .setPositiveButton("Lưu", (d, which) -> {
                    String qText = etQuestion.getText().toString().trim();
                    String ans = etAnswer.getText().toString().trim();
                    String explain = etExplanation.getText().toString().trim();
                    String category = etCategory.getText().toString().trim();
                    String image = etImage.getText().toString().trim();

                    Map<String, String> options = new HashMap<>();
                    options.put("A", etOptionA.getText().toString().trim());
                    options.put("B", etOptionB.getText().toString().trim());
                    options.put("C", etOptionC.getText().toString().trim());
                    options.put("D", etOptionD.getText().toString().trim());

                    if (question == null) {
                        Question newQ = new Question();
                        newQ.setQuestion(qText);
                        newQ.setOptions(options);
                        newQ.setAnswer(ans);
                        newQ.setExplanation(explain);
                        newQ.setCategory(category);
                        newQ.setImage(image);
                        newQ.setAnswered(false);
                        newQ.setSelectedAnswer(null);

                        questionController.addQuestion(newQ);
                    } else {
                        question.setQuestion(qText);
                        question.setOptions(options);
                        question.setAnswer(ans);
                        question.setExplanation(explain);
                        question.setCategory(category);
                        question.setImage(image);

                        questionController.updateQuestion(question);
                    }
                    // Không cần gọi loadQuestions(), LiveData tự cập nhật
                })
                .setNegativeButton("Hủy", null)
                .create();

        dialog.show();
    }
}
