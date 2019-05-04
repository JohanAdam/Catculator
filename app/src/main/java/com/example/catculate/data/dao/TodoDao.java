package com.example.catculate.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.catculate.data.entity.Todo;
import java.util.List;

@Dao
public interface TodoDao {

  //Get all.
  @Query("SELECT * FROM todo ORDER BY id DESC")
  List<Todo> getAll();

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
