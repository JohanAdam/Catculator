package com.example.catculate.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.catculate.data.entity.SpendTagEntity;
import java.util.List;
import io.reactivex.Single;

@Dao
public interface SpendTagDao {

  //Show all tags.
  @Query("SELECT * FROM spend_tag_item ORDER BY tag_name DESC")
  Single<List<SpendTagEntity>> getAll();

  //Insert new tag.
  @Insert
  void insert(SpendTagEntity item);

  //Delete tag.
  @Delete
  void delete(SpendTagEntity item);

  //Update item.
  @Update
  void update(SpendTagEntity item);

}
