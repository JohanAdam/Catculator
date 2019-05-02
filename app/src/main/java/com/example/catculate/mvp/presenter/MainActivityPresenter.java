package com.example.catculate.mvp.presenter;

import com.example.catculate.data.entity.ValueItem;
import com.example.catculate.mvp.view.MainActivityContract.Presenter;
import com.example.catculate.mvp.view.MainActivityContract.View;
import com.example.catculate.services.ValueItemService;
import com.example.catculate.utils.SharedPreferencesManager;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class MainActivityPresenter implements Presenter {

  SharedPreferencesManager sharedPrefs;
  ValueItemService service;
  View view;

  public MainActivityPresenter() {
  }

  @Override
  public void setView(View view, SharedPreferencesManager sharedPreferencesManager,
      ValueItemService service) {
    this.view = view;
    this.sharedPrefs = sharedPreferencesManager;
    this.service = service;
  }

  @Override
  public void seedExampleData() {
    Timber.d("seedExampleData");

    List<ValueItem> valueItemsList = new ArrayList<>();

    ValueItem valueItem = new ValueItem();
    ValueItem valueItem2 = new ValueItem();
    ValueItem valueItem3 = new ValueItem();

    valueItem.setDescrip("Nyan1");
    valueItem.setSymbolic(0);
    valueItem.setValue(0001);

    valueItem2.setDescrip("Nyan2");
    valueItem2.setSymbolic(2);
    valueItem2.setValue(0002);

    valueItem3.setDescrip("Nyan3");
    valueItem3.setSymbolic(3);
    valueItem3.setValue(0003);

    valueItemsList.add(valueItem);
    valueItemsList.add(valueItem2);
    valueItemsList.add(valueItem3);

    Timber.d("Size is " + valueItemsList.size());

    service.saveItemList(valueItemsList);
    }
}
