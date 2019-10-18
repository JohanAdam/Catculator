package com.example.catculate.services;

import com.example.catculate.Constants;
import com.example.catculate.data.database.ValueItemDatabase;
import com.example.catculate.data.entity.ValueItem;
import com.google.gson.Gson;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import timber.log.Timber;

/**
 * Class that manages all the database for ValueItem.
 * Presenter will deal with this class without knowing about Database, Entity and DAO.
 */

public class ValueItemService {

  private ValueItemDatabase valueItemDatabase;

  public ValueItemService() {
    valueItemDatabase = ValueItemDatabase.getDatabase();
  }

  /**
   * Get all from the list.
   * @return all list.
   */
  public void getAll(SingleObserver<List<ValueItem>> observer) {
    Timber.d("getAll");
    Single<List<ValueItem>> single = valueItemDatabase.valueItemDao().getAll();
    single.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public void saveItemList(List<ValueItem> list) {
    Timber.d("saveItemList size %s", list.size());
    Timber.d("saveItemList %s", new Gson().toJson(list));
    valueItemDatabase.valueItemDao().insertItem(list);
  }

  /**
   * Save single item of list.
   * @param valueItem save an item.
   */
  public void saveItem(ValueItem valueItem, CompletableObserver subscriber) {
    Timber.d("saveItem %s", new Gson().toJson(valueItem));
    Completable.fromAction(() -> valueItemDatabase.valueItemDao().insertItem(valueItem))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  /**
   * Delete an item.
   */
  public void delete(ValueItem valueItem, CompletableObserver subsciber) {
    Timber.d("delete %s", new Gson().toJson(valueItem));
    Completable.fromAction(() -> valueItemDatabase.valueItemDao().deleteItem(valueItem))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subsciber);
  }


  public void getTotal(GetSumCallback callback) {
    Timber.d("getTotal");
    getAddSum(new GetSumCallback() {
      @Override
      public void onComplete(long addSum) {
        getMinusSum(new GetSumCallback() {
          @Override
          public void onComplete(long minusSum) {
            long total = addSum - minusSum;
            callback.onComplete(total);
          }

          @Override
          public void onFailed(String message) {
            callback.onFailed(message);
          }
        });
      }

      @Override
      public void onFailed(String message) {
        callback.onFailed(message);
      }
    });
  }

  private void getAddSum(GetSumCallback callback) {
    Timber.d("getAddSum");
    //Get all the add item.
    Single<Long> single = valueItemDatabase.valueItemDao().getSum(Constants.SYMBOLIC_ADD);
    single.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Long>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onSuccess(Long addSum) {
            callback.onComplete(addSum);
          }

          @Override
          public void onError(Throwable e) {
            callback.onFailed(e.getMessage());
          }
        });
  }

  private void getMinusSum(GetSumCallback callback) {
    Timber.d("getMinusSum");
    Single<Long> single = valueItemDatabase.valueItemDao().getSum(Constants.SYMBOLIC_MINUS);
    single.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Long>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onSuccess(Long sum) {
            callback.onComplete(sum);
          }

          @Override
          public void onError(Throwable e) {
            callback.onFailed(e.getMessage());
          }
        });
  }

  /**
   * Update an item.
   * @param valueItem updated obj model.
   */
  public void updateItem(ValueItem valueItem, CompletableObserver subscriber){
    Timber.d("updateItem : %s", new Gson().toJson(valueItem));
    Completable.fromAction(() -> valueItemDatabase.valueItemDao().updateItem(valueItem))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  public void removeService() {
    Timber.d("removeService");
    ValueItemDatabase.destroyInstance();
  }

  public interface GetSumCallback {
    void onComplete(long sum);
    void onFailed(String message);
  }

}

