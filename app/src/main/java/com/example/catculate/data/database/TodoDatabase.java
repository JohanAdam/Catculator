package com.example.catculate.data.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;
import com.example.catculate.App;
import com.example.catculate.data.dao.TodoDao;
import com.example.catculate.data.entity.Todo;

@Database(entities = {Todo.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {

  private static TodoDatabase INSTANCE;

  public abstract TodoDao todoDao();

  public static TodoDatabase getDatabase() {
    if (INSTANCE == null) {
      INSTANCE = Room.databaseBuilder(App.getApp(),
          TodoDatabase.class,
          "todo-db")
          .allowMainThreadQueries()
          .build();
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
