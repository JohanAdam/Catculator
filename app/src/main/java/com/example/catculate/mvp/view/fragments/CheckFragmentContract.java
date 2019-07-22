package com.example.catculate.mvp.view.fragments;

import com.example.catculate.data.entity.Todo;
import com.example.catculate.services.TodoService;
import com.example.catculate.utils.SharedPreferencesManager;
import java.util.List;

public interface CheckFragmentContract {

  interface View {

    void showWait();

    void removeWait();

    void showLoading();

    void removeLoading();

    void showEmptyLayout();

    void removeEmptyLayout();

    void showToast(String msg);

    void addListUncompletedToView(List<Todo> unCompletedList,
        int completedTotal);

    void addListCompletedToView(List<Todo> completedList, int inCompletedTotal);

    void insertNewItem(Todo todo);
  }

  interface Presenter {

    void initPresenter(SharedPreferencesManager sharedPreferencesManager, TodoService service);

    void getAll(int state);

    void insertItem(Todo item);

    void updateItem(Todo item);

    void deleteItem(Todo item);

    void unSubscribe();

    void reset();
  }

  interface GetAllTodoCallback {
    void onCompleted(List<Todo> result);
  }

}
