package com.example.catculate.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * Entity
 *
 * It represents data for a single table row.
 * Room provide construction of entity using annotations.
 */

@Entity(tableName = "valueitem")
public class ValueItem implements Parcelable {

  @PrimaryKey(autoGenerate = true)
  @NonNull
  int id;

  @ColumnInfo(name = "description")
  String descrip;

  @ColumnInfo(name = "symbolic")
  int symbolic;

  @ColumnInfo(name = "value")
  long value;

  public ValueItem() {

  }

  protected ValueItem(Parcel in) {
    id = in.readInt();
    descrip = in.readString();
    symbolic = in.readInt();
    value = in.readLong();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(descrip);
    dest.writeInt(symbolic);
    dest.writeLong(value);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ValueItem> CREATOR = new Creator<ValueItem>() {
    @Override
    public ValueItem createFromParcel(Parcel in) {
      return new ValueItem(in);
    }

    @Override
    public ValueItem[] newArray(int size) {
      return new ValueItem[size];
    }
  };

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

  public int getSymbolic() {
    return symbolic;
  }

  public void setSymbolic(int symbolic) {
    this.symbolic = symbolic;
  }

  public long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }
}
