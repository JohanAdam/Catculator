package com.example.catculate.mvp.presenter;

import com.example.catculate.Constants;
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

      view.addListToView(list);

      long total = 0;
      if (list.size() > 0) {

        //Get the total.
        for (ValueItem item : list) {

          if (item.getSymbolic() == Constants.SYMBOLIC_ADD) {
            total =  total + item.getValue();
          } else {
            total = total - item.getValue();
          }

        }

        view.setTotal(total);

      } else {
        view.setTotal(0);
        view.showEmptyLayout();
      }
    }
  }

  @Override
  public void insertItem(String desc, String price, int symbolic, String total) {
    Timber.d("insertItem desc " + desc + " price " + price);

    ValueItem valueItem = new ValueItem();
    valueItem.setDescrip(desc);
    valueItem.setValue(Long.parseLong(price));
    valueItem.setSymbolic(symbolic);

    dbService.saveItem(valueItem);

    view.insertNewItem(valueItem);

    //Change total.
    long totalLng = Long.parseLong(total);
    if (symbolic == Constants.SYMBOLIC_ADD) {
      totalLng = totalLng + Long.parseLong(price);
    } else {
      totalLng = totalLng - Long.parseLong(price);
    }
    view.setTotal(totalLng);
  }

  @Override
  public void deleteItem(ValueItem data) {
    Timber.d("delete Item ");
    dbService.delete(data);
  }

  @Override
  public void unSubscribe() {
    view = null;
  }
}
