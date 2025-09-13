package com.example.luyenthiblxmay.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.luyenthiblxmay.dao.QuestionDao;
import com.example.luyenthiblxmay.dao.TipsDao;
import com.example.luyenthiblxmay.dao.UserDao;
import com.example.luyenthiblxmay.model.Question;
import com.example.luyenthiblxmay.model.Tips;
import com.example.luyenthiblxmay.model.User;
import com.example.luyenthiblxmay.utils.OptionsConverter;
import com.example.luyenthiblxmay.utils.PasswordUtils;

import java.util.concurrent.Executors;

@Database(
        entities = {User.class, Tips.class, Question.class},
        version = 3, // tăng version lên 3
        exportSchema = false
)
@TypeConverters({AppDatabase.Converters.class, OptionsConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract TipsDao tipsDao();
    public abstract QuestionDao questionDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "user_database"
                            )
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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

    // Converters để Room lưu Date
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

    // Migration 1->2: tạo bảng tips
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `tips` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`title` TEXT, " +
                    "`content` TEXT)");
        }
    };

    // Migration 2->3: tạo bảng questions
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `questions` (" +
                    "`id` INTEGER PRIMARY KEY NOT NULL, " +
                    "`question` TEXT, " +
                    "`options` TEXT, " + // lưu Map dưới dạng JSON String
                    "`answer` TEXT, " +
                    "`explanation` TEXT, " +
                    "`category` TEXT, " +
                    "`image` TEXT, " +
                    "`isAnswered` INTEGER NOT NULL DEFAULT 0, " +
                    "`selectedAnswer` TEXT)");
        }
    };
}
