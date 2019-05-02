package com.example.catculate.mvp.presenter;

import com.example.catculate.data.entity.ValueItem;
import com.example.catculate.mvp.view.fragments.SpendFragmentContract.Presenter;
import com.example.catculate.mvp.view.fragments.SpendFragmentContract.View;
import com.example.catculate.services.ValueItemService;
import com.example.catculate.utils.SharedPreferencesManager;
import java.util.List;
import timber.log.Timber;

public class SpendingPresenter implements Presenter {

  private View view;
  private SharedPreferencesManager sharedPrefs;
  private ValueItemService dbService;

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

  @Override
  public void getList() {
    Timber.d("getList");

    view.showWait();

    List<ValueItem> list = dbService.getAll();

    if (isViewAttached() && list != null) {
      view.removeWait();

      if (list.size() > 0) {
        view.addListToView(list);
      } else {
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
  }

  @Override
  public void unSubscribe() {
    view = null;
  }
}
