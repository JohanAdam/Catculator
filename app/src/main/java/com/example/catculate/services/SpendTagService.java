package com.example.catculate.services;

import androidx.annotation.NonNull;
import com.example.catculate.data.database.SpendTagDatabase;
import com.example.catculate.data.entity.SpendTagEntity;
import com.google.gson.Gson;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import timber.log.Timber;
public class SpendTagService {

  private SpendTagDatabase spendTagDatabase;

  public SpendTagService(){
    spendTagDatabase = SpendTagDatabase.getDatabase();
  }

  public void getAllTags(SingleObserver<List<SpendTagEntity>> observer) {
    Timber.d("getAllTags");
    Single<List<SpendTagEntity>> single = spendTagDatabase.spendTagDao().getAll();
    single.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public void addTag(@NonNull SpendTagEntity item, CompletableObserver subscriber) {
    Timber.d("saveItem %s", new Gson().toJson(item));
    Completable.fromAction(() -> spendTagDatabase.spendTagDao().insert(item))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  public void deleteTag(@NonNull SpendTagEntity item, CompletableObserver subscriber) {
    Timber.d("deleteTag %s", new Gson().toJson(item));
    Completable.fromAction(() -> spendTagDatabase.spendTagDao().delete(item))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  public void updateTag(@NonNull SpendTagEntity item, CompletableObserver subscriber) {
    Timber.d("updateTag %s", new Gson().toJson(item));
    Completable.fromAction(() -> spendTagDatabase.spendTagDao().update(item))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  public void removeService() {
    Timber.e("removeService");
    SpendTagDatabase.destroyInstance();
  }

}
