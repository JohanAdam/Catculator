package com.example.catculate.mvp.view.fragments;

import com.example.catculate.data.entity.ValueItem;
import com.example.catculate.services.ValueItemService;
import com.example.catculate.utils.SharedPreferencesManager;
import java.util.List;

public interface SpendFragmentContract {

  interface View {

    void showWait();

    void removeWait();

    void showErrorLayout();

    void removeErrorLayout();

    void showEmptyLayout();

    void removeEmptyLayout();

    void addListToView(List<ValueItem> list);

    void insertNewItem(ValueItem valueItem);
  }

  interface Presenter {

    void initPresenter(SharedPreferencesManager sharedPreferencesManager, ValueItemService service);

    void getList();

    void insertItem(String desc, String price, int symbolic);

    void unSubscribe();
  }

}
