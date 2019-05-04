package com.example.catculate.mvp.presenter;

import com.example.catculate.data.entity.ValueItem;
import com.example.catculate.mvp.view.fragments.SpendFragmentContract.Presenter;
import com.example.catculate.mvp.view.fragments.SpendFragmentContract.View;
import com.example.catculate.services.ValueItemService;
import com.example.catculate.utils.SharedPreferencesManager;
import com.google.gson.Gson;
import java.util.List;
import timber.log.Timber;

public class SpendingPresenter implements Presenter {

  private View view;
  private SharedPreferencesManager sharedPrefs;
  private ValueItemService dbService;
//  private long currentTotal = 0;

  public SpendingPresenter(View view) {
    this.view = view;
  }

  @Override
  public void initPresenter(SharedPreferencesManager sharedPreferencesManager,
      ValueItemService service) {
    this.sharedPrefs = sharedPreferencesManager;
    this.dbService = service;
  }

  private boolean isViewAttached() {
    return view != null;
  }

  private long getTotal() {
    return dbService.getTotal();
  }

  @Override
  public void getList() {
    Timber.d("getList");

    view.showWait();

    List<ValueItem> list = dbService.getAll();

    view.removeWait();

    if (isViewAttached() && list != null) {
      view.addListToView(list);

      if (list.size() > 0) {
        view.setTotal(getTotal());
      } else {
        view.setTotal(0);
        view.showEmptyLayout();
      }
    }
  }

  @Override
  public void insertItem(String desc, String price, int symbolic) {
    Timber.d("insertItem desc " + desc + " price " + price);

    ValueItem valueItem = new ValueItem();
    valueItem.setDescrip(desc);
    valueItem.setValue(Long.parseLong(price));
    valueItem.setSymbolic(symbolic);

    dbService.saveItem(valueItem);

    view.insertNewItem(valueItem);

    //Change total.
    view.setTotal(getTotal());
  }

  @Override
  public void updateItem(ValueItem updatedData) {
    Timber.d("updateItem " + new Gson().toJson(updatedData));
    dbService.updateItem(updatedData);

    view.setTotal(getTotal());
  }

  @Override
  public void deleteItem(ValueItem data) {
    Timber.d("delete Item ");
    dbService.delete(data);
    //Update total.
    view.setTotal(getTotal());
  }

  @Override
  public void unSubscribe() {
    dbService.removeService();
    view = null;
  }
}
