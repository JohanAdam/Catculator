package com.example.catculate.services;

import com.example.catculate.data.database.TodoDatabase;
import com.example.catculate.data.entity.Todo;
import java.util.List;
import timber.log.Timber;

public class TodoService {

  TodoDatabase todoDatabase;

  public TodoService() {
    todoDatabase = TodoDatabase.getDatabase();
  }

  /**
   * Get all from the db.
   */
  public List<Todo> getAll() {
    Timber.d("getAll");
    return todoDatabase.todoDao().getAll();
  }

  public void saveAll(List<Todo> list) {
    Timber.d("saveAll %s", list.size());
    todoDatabase.todoDao().insertAll(list);
  }

  /**
   * Insert an item.
   */
  public void saveItem(Todo item) {
    Timber.d("saveItem");
    todoDatabase.todoDao().insertItem(item);
  }

  public void deleteItem(Todo item) {
    Timber.d("deleteItem");
    todoDatabase.todoDao().deleteItem(item);
  }

  public void updateItem(Todo item) {
    Timber.d("updateItem");
    todoDatabase.todoDao().updateItem(item);
  }

  public void reset() {
    Timber.d("reset");
    todoDatabase.todoDao().reset(false, true);
//    return getAll();
  }

  public void removeService() {
    Timber.d("removeService");
    TodoDatabase.destroyInstance();
  }

}
