package com.example.catculate.data.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.catculate.App;
import com.example.catculate.Constants;
import com.example.catculate.data.dao.SpendTagDao;
import com.example.catculate.data.entity.SpendTagEntity;

@Database(entities = {SpendTagEntity.class}, version = 2)
public abstract class SpendTagDatabase extends RoomDatabase {

  private static SpendTagDatabase INSTANCE;

  public abstract SpendTagDao spendTagDao();

  public static SpendTagDatabase getDatabase() {
    if (INSTANCE == null) {
     synchronized (SpendTagDatabase.class) {
      if (INSTANCE == null) {
        INSTANCE = Room.databaseBuilder(App.getApp(),
            SpendTagDatabase.class,
            Constants.SPEND_TAG_DB_NAME)
            .build();
      }
     }
    }
    return INSTANCE;
  }

  public static void destroyInstance() {
    INSTANCE = null;
  }

  @NonNull
  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
    return null;
  }

  @NonNull
  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return null;
  }
}
