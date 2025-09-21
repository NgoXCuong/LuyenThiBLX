package com.example.luyenthiblxmay.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.luyenthiblxmay.dao.*;
import com.example.luyenthiblxmay.model.*;
import com.example.luyenthiblxmay.utils.OptionsConverter;
import com.example.luyenthiblxmay.utils.PasswordUtils;

import java.util.concurrent.Executors;

@Database(
        entities = {
                User.class,
                Tips.class,
                Question.class,
                ExamResult.class,
                ExamQuestion.class,
                UserQuestion.class,
                BienBao.class
        },
        version = 6,
        exportSchema = false
)
@TypeConverters({AppDatabase.Converters.class, OptionsConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract TipsDao tipsDao();
    public abstract QuestionDao questionDao();
    public abstract ExamResultDao examResultDao();
    public abstract ExamQuestionDao examQuestionDao();
    public abstract UserQuestionDao userQuestionDao();
    public abstract BienBaoDao bienBaoDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "app_thi_blx"
                            )
                            .createFromAsset("database/app_thi_blx.db") // 👈 Thêm dòng này để load DB có sẵn từ assets
                            .fallbackToDestructiveMigration() // Xóa DB cũ nếu verify schema fail
                            .addCallback(new Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        UserDao userDao = INSTANCE.userDao();
                                        if (userDao.countUsersByEmail("admin@gmail.com") == 0) {
                                            User admin = new User();
                                            admin.setFullName("Admin");
                                            admin.setEmail("admin@gmail.com");
                                            admin.setPhone("0000000000");
                                            admin.setPassword(PasswordUtils.hashPassword("admin123"));
                                            admin.setAdmin(true);
                                            userDao.insertUser(admin);
                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }

    // Converters
    public static class Converters {
        @androidx.room.TypeConverter
        public static java.util.Date fromTimestamp(Long value) {
            return value == null ? null : new java.util.Date(value);
        }

        @androidx.room.TypeConverter
        public static Long dateToTimestamp(java.util.Date date) {
            return date == null ? null : date.getTime();
        }
    }

    // ================= Migrations =================

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `tips` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`title` TEXT, " +
                    "`content` TEXT)");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `questions` (" +
                    "`id` INTEGER PRIMARY KEY NOT NULL, " +
                    "`question` TEXT, " +
                    "`options` TEXT, " +
                    "`answer` TEXT, " +
                    "`explanation` TEXT, " +
                    "`category` TEXT, " +
                    "`image` TEXT, " +
                    "`isAnswered` INTEGER NOT NULL DEFAULT 0, " +
                    "`selectedAnswer` TEXT)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // ExamResult
            database.execSQL("CREATE TABLE IF NOT EXISTS `exam_result` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`userId` INTEGER NOT NULL, " +
                    "`totalQuestions` INTEGER NOT NULL, " +
                    "`correctAnswers` INTEGER NOT NULL, " +
                    "`takenAt` INTEGER NOT NULL, " +
                    "`category` TEXT, " +
                    "FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_exam_result_userId` ON `exam_result`(`userId`)");

            // ExamQuestion
            database.execSQL("CREATE TABLE IF NOT EXISTS `exam_question` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`examId` INTEGER NOT NULL, " +
                    "`questionId` INTEGER NOT NULL, " +
                    "`selectedAnswer` TEXT, " +
                    "`isCorrect` INTEGER NOT NULL DEFAULT 0, " +
                    "FOREIGN KEY(`examId`) REFERENCES `exam_result`(`id`) ON DELETE CASCADE, " +
                    "FOREIGN KEY(`questionId`) REFERENCES `questions`(`id`) ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_exam_question_examId` ON `exam_question`(`examId`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_exam_question_questionId` ON `exam_question`(`questionId`)");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `user_question` (" +
                    "`userId` INTEGER NOT NULL, " +
                    "`questionId` INTEGER NOT NULL, " +
                    "`isAnswered` INTEGER NOT NULL DEFAULT 0, " +
                    "`selectedAnswer` TEXT, " +
                    "`isCorrect` INTEGER NOT NULL DEFAULT 0, " +
                    "`answeredAt` INTEGER, " +
                    "PRIMARY KEY(`userId`, `questionId`), " +
                    "FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON DELETE CASCADE, " +
                    "FOREIGN KEY(`questionId`) REFERENCES `questions`(`id`) ON DELETE CASCADE" +
                    ")");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_user_question_userId` ON `user_question`(`userId`)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_user_question_questionId` ON `user_question`(`questionId`)");
        }
    };



    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `bien_bao` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`ten_bien_bao` TEXT, " +
                    "`loai_bien_bao` TEXT, " +
                    "`hinh_anh` TEXT, " +
                    "`mo_ta` TEXT)");
        }
    };
}
