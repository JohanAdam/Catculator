package com.example.catculate.mvp.presenter;

import com.example.catculate.data.entity.SpendTagEntity;
import com.example.catculate.mvp.view.activities.SpendSettingActivityContract.Presenter;
import com.example.catculate.mvp.view.activities.SpendSettingActivityContract.View;
import com.example.catculate.services.SpendTagService;
import com.example.catculate.utils.SharedPreferencesManager;
import com.google.gson.Gson;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.List;
import timber.log.Timber;

public class SpendSettingPresenter implements Presenter {

  private View view;
  private SharedPreferencesManager sharedPreferencesManager;
  private SpendTagService databaseService;
  private CompositeDisposable disposable;

  public SpendSettingPresenter() {

  }

  @Override
  public void setView(View view, SharedPreferencesManager sharedPreferences,
      SpendTagService databaseService) {
    this.view = view;
    this.sharedPreferencesManager = sharedPreferences;
    this.databaseService = databaseService;
    this.disposable = new CompositeDisposable();
  }

  private boolean isViewAttached() {
    return view != null;
  }

  @Override
  public void getAllTags() {
    Timber.d("getAllTags");
    if (isViewAttached()) {
      view.showWait(null);
    }

    databaseService.getAllTags(new SingleObserver<List<SpendTagEntity>>() {
      @Override
      public void onSubscribe(Disposable d) {
        disposable.add(d);
      }

      @Override
      public void onSuccess(List<SpendTagEntity> spendTagList) {
        Timber.e("onComplete");
        if (isViewAttached()){
          view.removeWait();
          view.setTagList(spendTagList);
        }
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        if (isViewAttached()) {
          view.removeWait();
          view.showSnackbar(e.getMessage());
        }
      }
    });
  }

  @Override
  public void addDummyTags() {
    Timber.e("addDummyTags");

    SpendTagEntity item = new SpendTagEntity();
    item.setName("Catto Pretto");

    if (isViewAttached()) {
      view.showWait(null);
    }

    databaseService.addTag(item, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposable.add(d);
      }

      @Override
      public void onComplete() {
        Timber.e("onComplete");
        if (isViewAttached()){
          view.removeWait();
          getAllTags();
          view.showSnackbar("New Tag added!");
        }
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        if (isViewAttached()){
          view.removeWait();
          view.showSnackbar(e.getMessage());
        }
      }
    });
  }

  @Override
  public void addNewTag(SpendTagEntity comment) {
    Timber.d("addNewTag %s", new Gson().toJson(comment));

    if (isViewAttached()) {
      view.showWait(null);
    }

    databaseService.addTag(comment, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposable.add(d);
      }

      @Override
      public void onComplete() {
        Timber.e("onComplete");
        if (isViewAttached()) {
          view.removeWait();
          view.showSnackbar("New Tag added!");
          view.addNewTag(comment);
        }
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        if (isViewAttached()){
          view.removeWait();
          view.showSnackbar(e.getMessage());
        }
      }
    });

  }

  @Override
  public void deleteTag(SpendTagEntity item) {
    Timber.d("deleteTag");

    if (isViewAttached()) {
      view.showWait(null);
    }

    databaseService.deleteTag(item, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposable.add(d);
      }

      @Override
      public void onComplete() {
        Timber.e("onComplete");
        if (isViewAttached()) {
          view.removeWait();
          view.showSnackbar("Tag deleted!");
        }
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        if (isViewAttached()) {
          view.removeWait();
          view.showSnackbar(e.getMessage());
        }
      }
    });
  }

  @Override
  public void updateTag(SpendTagEntity comment) {
    Timber.d("updateTag");

    if (isViewAttached()) {
      view.showWait(null);
    }

    databaseService.updateTag(comment, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposable.add(d);
      }

      @Override
      public void onComplete() {
        Timber.e("onComplete");
        if (isViewAttached()){
          view.removeWait();
          view.showSnackbar("Tag updated!");
        }
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        if (isViewAttached()) {
          view.removeWait();
          view.showSnackbar(e.getMessage());
        }
      }
    });
  }

  @Override
  public void unSubscribe() {
    databaseService.removeService();
    disposable.dispose();
    view = null;
  }
}
