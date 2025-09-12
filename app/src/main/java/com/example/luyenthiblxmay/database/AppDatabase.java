package com.example.luyenthiblxmay.database;

import static com.example.luyenthiblxmay.utils.PasswordUtils.hashPassword;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.luyenthiblxmay.dao.TipsDao;
import com.example.luyenthiblxmay.dao.UserDao;
import com.example.luyenthiblxmay.model.Tips;
import com.example.luyenthiblxmay.model.User;

import java.util.Date;
import java.util.concurrent.Executors;

@Database(
        entities = {User.class, Tips.class},
        version = 2,
        exportSchema = false
)
@TypeConverters({AppDatabase.Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract TipsDao tipsDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "user_database"
                            )
                            .addMigrations(MIGRATION_1_2)
                            .addCallback(new Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);

                                    // Luôn kiểm tra khi mở DB
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        UserDao userDao = INSTANCE.userDao();
                                        if (userDao.countUsersByEmail("admin@gmail.com") == 0) {
                                            User admin = new User();
                                            admin.setFullName("Admin");
                                            admin.setEmail("admin@gmail.com");
                                            admin.setPhone("0000000000");
                                            admin.setPassword(hashPassword("admin123"));
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
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }

    // Migration: chỉ tạo bảng tips, KHÔNG chèn admin ở đây
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `tips` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`title` TEXT, " +
                    "`content` TEXT)");
        }
    };
}
