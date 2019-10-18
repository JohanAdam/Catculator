package com.example.catculate.data.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "spend_tag_item")
public class SpendTagEntity implements Parcelable {

  @PrimaryKey(autoGenerate = true)
  @NonNull
  @ColumnInfo(name = "tag_id")
  private int id;

  @ColumnInfo(name = "tag_name")
  private String name;

  public SpendTagEntity() {

  }

  protected SpendTagEntity(Parcel in) {
    id = in.readInt();
    name = in.readString();
  }

  public static final Creator<SpendTagEntity> CREATOR = new Creator<SpendTagEntity>() {
    @Override
    public SpendTagEntity createFromParcel(Parcel in) {
      return new SpendTagEntity(in);
    }

    @Override
    public SpendTagEntity[] newArray(int size) {
      return new SpendTagEntity[size];
    }
  };

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(id);
    parcel.writeString(name);
  }
}
