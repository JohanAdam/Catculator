package com.example.catculate.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Entity
 *
 * It represents data for a single table row.
 * Room provide construction of entity using annotations.
 */

@Entity(tableName = "todo")
public class Todo {

  @PrimaryKey(autoGenerate = true)
  @NonNull
  int id;

  @ColumnInfo(name = "description")
  String descrip;

  @ColumnInfo(name = "isCheck")
  boolean isChecked;

  @ColumnInfo(name = "value")
  long value;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDescrip() {
    return descrip;
  }

  public void setDescrip(String descrip) {
    this.descrip = descrip;
  }

  public boolean isChecked() {
    return isChecked;
  }

  public void setChecked(boolean checked) {
    isChecked = checked;
  }

  public long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }
}
