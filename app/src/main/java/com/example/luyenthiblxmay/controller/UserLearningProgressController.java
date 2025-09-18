//package com.example.luyenthiblxmay.controller;
//
//import android.app.Application;
//
//import androidx.annotation.Nullable;
//
//import com.example.luyenthiblxmay.dao.UserLearningProgressDao;
//import com.example.luyenthiblxmay.database.AppDatabase;
//import com.example.luyenthiblxmay.model.UserLearningProgress;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class UserLearningProgressController {
//
//    private final UserLearningProgressDao progressDao;
//    private final ExecutorService executorService;
//
//    public UserLearningProgressController(Application application) {
//        AppDatabase db = AppDatabase.getDatabase(application);
//        progressDao = db.userLearningProgressDao();
//        executorService = Executors.newSingleThreadExecutor();
//    }
//
//    // Callback interface
//    public interface Callback<T> {
//        void onResult(@Nullable T result);
//    }
//
//    // Lấy tiến độ học của user theo category
//    public void getByUserAndCategory(int userId, String category, Callback<UserLearningProgress> callback) {
//        executorService.execute(() -> {
//            UserLearningProgress progress = progressDao.getProgressByUserAndCategory(userId, category);
//            callback.onResult(progress);
//        });
//    }
//
//    // Lấy tất cả tiến độ học của user
//    public void getAllByUser(int userId, Callback<List<UserLearningProgress>> callback) {
//        executorService.execute(() -> {
//            List<UserLearningProgress> list = progressDao.getAllProgressByUser(userId);
//            callback.onResult(list);
//        });
//    }
//
//    // Insert hoặc update
//    public void insertOrUpdate(UserLearningProgress progress) {
//        executorService.execute(() -> {
//            UserLearningProgress existing = progressDao.getProgressByUserAndCategory(progress.getUserId(), progress.getCategory());
//            if (existing != null) {
//                existing.setLastQuestionId(progress.getLastQuestionId());
//                existing.setUpdatedAt(progress.getUpdatedAt());
//                progressDao.update(existing);
//            } else {
//                progressDao.insert(progress);
//            }
//        });
//    }
//}
