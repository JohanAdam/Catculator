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
import com.example.catculate.data.dao.TodoDao;
import com.example.catculate.data.entity.Todo;

@Database(entities = {Todo.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {

  private static TodoDatabase INSTANCE;

  public abstract TodoDao todoDao();

  public static TodoDatabase getDatabase() {

    if (INSTANCE == null) {
      synchronized (TodoDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(App.getApp(),
              TodoDatabase.class,
              Constants.TODO_DB_NAME)
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
