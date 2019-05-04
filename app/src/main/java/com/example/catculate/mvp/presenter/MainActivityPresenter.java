package com.example.catculate.mvp.presenter;

import com.example.catculate.data.entity.Todo;
import com.example.catculate.mvp.view.activities.MainActivityContract.Presenter;
import com.example.catculate.mvp.view.activities.MainActivityContract.View;
import com.example.catculate.services.TodoService;
import com.example.catculate.services.ValueItemService;
import com.example.catculate.utils.SharedPreferencesManager;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class MainActivityPresenter implements Presenter {

  SharedPreferencesManager sharedPrefs;
  ValueItemService service;
  TodoService todoService;
  View view;

  public MainActivityPresenter() {
  }

  @Override
  public void setView(View view, SharedPreferencesManager sharedPreferencesManager,
      ValueItemService service, TodoService todoService) {
    this.view = view;
    this.sharedPrefs = sharedPreferencesManager;
    this.service = service;
    this.todoService = todoService;
  }

  @Override
  public void seedExampleData() {
    Timber.d("seedExampleData");

//    List<ValueItem> valueItemsList = new ArrayList<>();
//
//    ValueItem valueItem = new ValueItem();
//    ValueItem valueItem2 = new ValueItem();
//    ValueItem valueItem3 = new ValueItem();
//
//    valueItem.setDescrip("Nyan1");
//    valueItem.setSymbolic(Constants.SYMBOLIC_ADD);
//    valueItem.setValue(0001);
//
//    valueItem2.setDescrip("Nyan2");
//    valueItem2.setSymbolic(Constants.SYMBOLIC_MINUS);
//    valueItem2.setValue(0002);
//
//    valueItem3.setDescrip("Nyan3");
//    valueItem3.setSymbolic(Constants.SYMBOLIC_ADD);
//    valueItem3.setValue(0003);
//
//    valueItemsList.add(valueItem);
//    valueItemsList.add(valueItem2);
//    valueItemsList.add(valueItem3);
//
//    Timber.d("Size is " + valueItemsList.size());
//
//    service.saveItemList(valueItemsList);

    Todo todo = new Todo();
    Todo todo2 = new Todo();
    Todo todo3 = new Todo();

    todo.setDescrip("Desc 1");
    todo.setValue(100);
    todo.setChecked(true);

    todo2.setDescrip("Desc 2");
    todo2.setValue(200);
    todo2.setChecked(true);

    todo3.setDescrip("Desc 3");
    todo3.setValue(300);
    todo3.setChecked(true);

    List<Todo> list = new ArrayList<>();
    list.add(todo);
    list.add(todo2);
    list.add(todo3);

    todoService.saveAll(list);

  }
}
