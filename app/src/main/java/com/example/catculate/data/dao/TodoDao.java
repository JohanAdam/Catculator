package com.example.catculate.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.catculate.data.entity.Todo;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface TodoDao {

  //Get all.
  @Query("SELECT * FROM todo ORDER BY id DESC")
  Single<List<Todo>> getAll();

  @Insert
  void insertAll(List<Todo> list);

  //Insert new item.
  @Insert
  void insertItem(Todo item);

  //Delete a item.
  @Delete
  void deleteItem(Todo item);

  //Update an item.
  @Update
  void updateItem(Todo item);

  //Reset all.
  @Query("UPDATE todo SET isCheck = :isCheckNew WHERE isCheck = :isCheckPrevious")
  void reset(boolean isCheckNew, boolean isCheckPrevious);
}
