package com.example.catculate.mvp.presenter;

import com.example.catculate.Constants;
import com.example.catculate.data.entity.Todo;
import com.example.catculate.mvp.view.fragments.CheckFragmentContract.Presenter;
import com.example.catculate.mvp.view.fragments.CheckFragmentContract.View;
import com.example.catculate.services.TodoService;
import com.example.catculate.utils.SharedPreferencesManager;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class CheckPresenter implements Presenter {

  private View view;
  private SharedPreferencesManager sharedPrefManager;
  private TodoService dbService;
  private CompositeDisposable disposables;

  private int currentState;

  public CheckPresenter(View view) {
    this.view = view;
  }

  @Override
  public void initPresenter(SharedPreferencesManager sharedPreferencesManager,
      TodoService service) {
    this.sharedPrefManager = sharedPreferencesManager;
    this.dbService = service;
    this.disposables = new CompositeDisposable();
  }

  private boolean isViewAttached() {
    return view != null;
  }

  @Override
  public void getAll(int state) {
    Timber.d("GetAll Todo List");

    view.showWait();

    this.currentState = state;

    dbService.getAll(
        new SingleObserver<List<Todo>>() {
          @Override
          public void onSubscribe(Disposable d) {
            Timber.d("onSubscribe");
            // add it to a CompositeDisposable.
            disposables.add(d);
          }

          @Override
          public void onSuccess(List<Todo> result) {
            // update the UI.
            Timber.d("onSuccess");
            if (isViewAttached() && result != null) {
              view.removeWait();

              if (result.size() == 0) {
                view.showEmptyLayout();
              }

              List<Todo> unCompletedList = new ArrayList<>();
              List<Todo> completedList = new ArrayList<>();
              for (Todo item : result) {

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
          public void onError(Throwable e) {
            // show an error message.

            e.printStackTrace();
            view.removeWait();
            view.showEmptyLayout();
            view.showToast(e.getMessage());
          }
        });
  }

  @Override
  public void insertItem(Todo item) {
    Timber.d("insertItem ");
    view.showLoading();

    //Save in db.
    dbService.saveItem(item, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
         disposables.add(d);
      }

      @Override
      public void onComplete() {
        Timber.d("onSuccess");
        view.removeLoading();

        //Insert in adapter.
        //If current state is incomplete, add item to view.
        if (currentState == Constants.STATE_INCOMPLETED) {
          //This is because , new item added is always incompleted.
          view.insertNewItem(item);
        }
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        view.removeLoading();
        view.showToast(e.getMessage());
      }
    });
  }

  @Override
  public void updateItem(Todo item) {
    Timber.d("updateItem");
    //Update in db.
    view.showLoading();

    dbService.updateItem(item, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposables.add(d);
      }

      @Override
      public void onComplete() {
        Timber.d("onComplete");
        view.removeLoading();
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        view.removeLoading();
        view.showToast(e.getMessage());
      }
    });
  }

  @Override
  public void deleteItem(Todo item) {
    Timber.d("Delete item");
    view.showLoading();

    dbService.deleteItem(item, new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposables.add(d);
      }

      @Override
      public void onComplete() {
        Timber.d("onCompleted");
        view.removeLoading();
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        view.removeLoading();
        view.showToast(e.getMessage());
      }
    });
  }

  @Override
  public void reset() {
    Timber.d("Reset");
    view.showLoading();

    dbService.reset(new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        disposables.add(d);
      }

      @Override
      public void onComplete() {
        Timber.d("onCompleted");
        view.removeLoading();

        //Reset all to incomplete, and show incompleted list.
        getAll(Constants.STATE_INCOMPLETED);
      }

      @Override
      public void onError(Throwable e) {
        Timber.e("onError");
        e.printStackTrace();
        view.removeLoading();
        view.showToast(e.getMessage());
      }
    });
  }

  @Override
  public void unSubscribe() {
    disposables.dispose();
    dbService.removeService();
    view = null;
  }
}
