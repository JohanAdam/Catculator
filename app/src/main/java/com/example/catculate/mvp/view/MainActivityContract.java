package com.example.catculate.mvp.view;

import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.example.catculate.services.ValueItemService;
import com.example.catculate.utils.SharedPreferencesManager;

public interface MainActivityContract {

  interface View {
    void onInfoDialog(int title, int message);
    void onErrorDialog(int title, int message, SingleButtonCallback callback);
    void showWait(String message);
    void removeWait();
  }

  interface Presenter {
    void setView(View view, SharedPreferencesManager sharedPreferencesManager, ValueItemService service);
    void seedExampleData();
  }

}
