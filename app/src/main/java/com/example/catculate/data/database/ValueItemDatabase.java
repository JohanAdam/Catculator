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
import com.example.catculate.data.dao.ValueItemDao;
import com.example.catculate.data.entity.ValueItem;

@Database(entities = {ValueItem.class}, version = 1)
public abstract class ValueItemDatabase extends RoomDatabase {

  private static ValueItemDatabase INSTANCE;

  public abstract ValueItemDao valueItemDao();

  //  public static ValueItemDatabase getDatabase() {
//    if (INSTANCE == null) {
//      INSTANCE = Room.databaseBuilder(App.getApp(),
//          ValueItemDatabase.class,
//          Constants.SPEND_DB_NAME)
//          .allowMainThreadQueries()
//          .build();
//    }
//    return INSTANCE;
//  }
  public static ValueItemDatabase getDatabase() {
    if (INSTANCE == null) {
      //Basically, it will make a thread and wait..
      //If for some reason if this method is being call two times, this will help the second
      //calling to wait for the first calling to finish first to avoid running the same thing two times.
      synchronized (ValueItemDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(App.getApp(),
              ValueItemDatabase.class,
              Constants.SPEND_DB_NAME)
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
