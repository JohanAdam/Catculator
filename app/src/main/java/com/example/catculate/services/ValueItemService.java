package com.example.catculate.services;

import com.example.catculate.data.database.ValueItemDatabase;
import com.example.catculate.data.entity.ValueItem;
import com.google.gson.Gson;
import java.util.List;
import timber.log.Timber;

/**
 * Class that manages all the database for ValueItem.
 * Presenter will deal with this class without knowing about Database, Entity and DAO.
 */

public class ValueItemService {

  ValueItemDatabase valueItemDatabase;

  public ValueItemService() {
    valueItemDatabase = ValueItemDatabase.getDatabase();
  }

  /**
   * Get all from the list.
   * @return all list.
   */
  public List<ValueItem> getAll() {
    Timber.d("getAll");
    return valueItemDatabase.valueItemDao().getAll();
  }

  public void saveItemList(List<ValueItem> list) {
    Timber.d("saveItemList size " + list.size());
    Timber.d("saveItemList " + new Gson().toJson(list));
    valueItemDatabase.valueItemDao().insertItem(list);
  }

  /**
   * Save single item of list.
   * @param valueItem save an item.
   */
  public void saveItem(ValueItem valueItem) {
    Timber.d("saveItem " + new Gson().toJson(valueItem));
    valueItemDatabase.valueItemDao().insertItem(valueItem);
  }

  /**
   * Delete an item.
   */
  public void delete(ValueItem valueItem) {
    Timber.d("delete " + new Gson().toJson(valueItem));
    valueItemDatabase.valueItemDao().deleteItem(valueItem);
  }

  /**
   * Update an item.
   * @param valueItem updated obj model.
   */
  public void updateItem(ValueItem valueItem){
    Timber.d("updateItem : " + new Gson().toJson(valueItem));
    valueItemDatabase.valueItemDao().updateItem(valueItem);
  }

  public void removeService() {
    Timber.d("removeService");
    ValueItemDatabase.destroyInstance();
  }

}

