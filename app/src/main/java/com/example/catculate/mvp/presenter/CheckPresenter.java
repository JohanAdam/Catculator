package com.example.catculate.mvp.presenter;

import com.example.catculate.Constants;
import com.example.catculate.data.entity.Todo;
import com.example.catculate.mvp.view.fragments.CheckFragmentContract.Presenter;
import com.example.catculate.mvp.view.fragments.CheckFragmentContract.View;
import com.example.catculate.services.TodoService;
import com.example.catculate.utils.SharedPreferencesManager;
import java.util.ArrayList;
import java.util.List;

public class CheckPresenter implements Presenter {

  private int state = Constants.STATE_COMPLETED;
  private View view;
  private SharedPreferencesManager sharedPrefManager;
  private TodoService dbService;

  public CheckPresenter(View view) {
    this.view = view;
  }

  @Override
  public void initPresenter(SharedPreferencesManager sharedPreferencesManager,
      TodoService service) {
    this.sharedPrefManager = sharedPreferencesManager;
    this.dbService = service;
  }

  private boolean isViewAttached() {
    return view != null;
  }

  @Override
  public void getAll(int state) {

    view.showWait();

    List<Todo> list = dbService.getAll();

    view.removeWait();

    if (isViewAttached() && list != null) {
      if (list.size() == 0) {
        view.showEmptyLayout();
      }

      List<Todo> unCompletedList = new ArrayList<>();
      List<Todo> completedList = new ArrayList<>();
      for (Todo item : list) {

        if (item.isChecked()) {
          completedList.add(item);
        } else {
          unCompletedList.add(item);
        }

      }

      if (state == Constants.STATE_COMPLETED) {
        view.addListCompletedToView(completedList, unCompletedList.size());
      } else {
        view.addListUncompletedToView(unCompletedList, completedList.size());
      }
    }

  }

  @Override
  public void insertItem(Todo item) {

    //Save in db.
    dbService.saveItem(item);

    //Insert in adapter.
    view.insertNewItem(item);
  }

  @Override
  public void updateItem(Todo item) {
    //Update in db.
    dbService.updateItem(item);
  }

  @Override
  public void deleteItem(Todo item) {
    dbService.deleteItem(item);
  }

  @Override
  public void reset(int state) {
    dbService.reset();
    getAll(state);
  }

  @Override
  public void unSubscribe() {
    dbService.removeService();
    view = null;
  }
}
