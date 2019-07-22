package com.example.catculate.mvp.presenter;

import com.example.catculate.data.entity.ValueItem;
import com.example.catculate.mvp.view.fragments.SpendFragmentContract.Presenter;
import com.example.catculate.mvp.view.fragments.SpendFragmentContract.View;
import com.example.catculate.services.ValueItemService;
import com.example.catculate.services.ValueItemService.GetSumCallback;
import com.example.catculate.utils.SharedPreferencesManager;
import com.google.gson.Gson;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.List;
import timber.log.Timber;

public class SpendingPresenter implements Presenter {

  private View view;
  private SharedPreferencesManager sharedPrefs;
  private ValueItemService dbService;
  private CompositeDisposable disposables;
//  private long currentTotal = 0;

  public SpendingPresenter(View view) {
    this.view = view;
  }

  @Override
  public void initPresenter(SharedPreferencesManager sharedPreferencesManager,
      ValueItemService service) {
    this.sharedPrefs = sharedPreferencesManager;
    this.dbService = service;
    this.disposables = new CompositeDisposable();
  }

  private boolean isViewAttached() {
    return view != null;
  }

  private void updateTotal() {
    dbService.getTotal(new GetSumCallback() {
      @Override
      public void onComplete(long sum) {
        view.setTotal(String.valueOf(sum));
      }

      @Override
      public void onFailed(String message) {
        view.setTotal("--");
        view.showSnackbar(message);
      }
    });
  }

  @Override
  public void getList() {
    Timber.d("getList");

    view.showWait();

    dbService.getAll(new SingleObserver<List<ValueItem>>() {
      @Override
      public void onSubscribe(Disposable d) {
        Timber.d("onSubscribe");
        disposables.add(d);
      }

      @Override
      public void onSuccess(List<ValueItem> valueItems) {
        Timber.d("onSuccess");
        view.removeWait();

        if (isViewAttached() && valueItems != null) {
          view.addListToView(valueItems);

          if (valueItems.size() > 0) {
            updateTotal();
          } else {
            view.setTotal("0");
            view.showEmptyLayout();
          }
        }
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        view.removeWait();
        view.showSnackbar(e.getMessage());
      }
    });
  }

  @Override
  public void insertItem(String desc, String price, int symbolic) {
    Timber.d("insertItem desc " + desc + " price " + price);

    view.showLoadingDialog();

    ValueItem valueItem = new ValueItem();
    valueItem.setDescrip(desc);
    valueItem.setValue(Long.parseLong(price));
    valueItem.setSymbolic(symbolic);

    dbService.saveItem(valueItem, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposables.add(d);
      }

      @Override
      public void onComplete() {
        view.removeLoadingDialog();

        view.insertNewItem(valueItem);

        //Change total.
        updateTotal();
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
        view.removeLoadingDialog();
        view.showSnackbar(e.getMessage());
      }
    });
  }

  @Override
  public void updateItem(ValueItem updatedData) {
    Timber.d("updateItem %s", new Gson().toJson(updatedData));

    view.showLoadingDialog();

    dbService.updateItem(updatedData, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposables.add(d);
      }

      @Override
      public void onComplete() {
        view.removeLoadingDialog();
        updateTotal();
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
        view.removeLoadingDialog();
        view.showSnackbar(e.getMessage());
      }
    });
  }

  @Override
  public void deleteItem(ValueItem data) {
    Timber.d("delete Item ");

    view.showLoadingDialog();

    dbService.delete(data, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposables.add(d);
      }

      @Override
      public void onComplete() {
        view.removeLoadingDialog();

        //Update total.
        updateTotal();

        view.showSnackbar("Item deleted!");
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
        view.removeLoadingDialog();
        view.showSnackbar(e.getMessage());
      }
    });
  }

  @Override
  public void unSubscribe() {
    dbService.removeService();
    disposables.dispose();
    view = null;
  }
}
