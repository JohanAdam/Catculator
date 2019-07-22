package com.example.catculate.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.catculate.data.entity.ValueItem;
import io.reactivex.Single;
import java.util.List;

/**
 * DAO
 *
 * It defines the method that access the database.
 * Annotations are user to bind SQL with each method
 * declared in DAO.
 */

@Dao
public interface ValueItemDao {

  //Show all valueItems.
  @Query("SELECT * FROM valueitem ORDER BY id DESC")
  Single<List<ValueItem>> getAll();

  //Insert all item.
  @Insert
  void insertItem(List<ValueItem> valueItemList);

  //Insert item.
  @Insert
  void insertItem(ValueItem valueItem);

  //Delete item.
  @Delete
  void deleteItem(ValueItem valueItem);

  //Update item
  @Update
  void updateItem(ValueItem valueItem);

  //Update by query.
//  @Query("UPDATE valueitem SET description = :newDesc, value = :newValue, symbolic = :newSymbol WHERE id = :id")
//  void updateItem(int id, String newDesc, String newValue, int newSymbol);

  //Get sum total.
  @Query("SELECT SUM(value) FROM valueitem WHERE symbolic = :symbol")
  Single<Long> getSum(int symbol);

}
