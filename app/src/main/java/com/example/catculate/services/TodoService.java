package com.example.catculate.services;

import com.example.catculate.data.database.TodoDatabase;
import com.example.catculate.data.entity.Todo;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import timber.log.Timber;

//https://proandroiddev.com/exploring-rxjava-in-android-different-types-of-observables-f23b3c78aeb6
public class TodoService {

  TodoDatabase todoDatabase;

  public TodoService() {
    todoDatabase = TodoDatabase.getDatabase();
  }

  /**
   * Get all from the db.
   * @return the list of todoItem.
   */
  public void getAll(SingleObserver subscriber) {
    Timber.d("getAll");
    Single<List<Todo>> single = todoDatabase.todoDao().getAll();
    single.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  public void saveAll(List<Todo> list) {
    Timber.d("saveAll %s", list.size());
//    Observable.just(list)
//        .map((Func1<List<Todo>, Void>) todos -> {
//          todoDatabase.todoDao().insertAll(todos);
//          return null;
//        })
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe();
  }

  /**
   * Insert an item.
   */
  public void saveItem(Todo item, CompletableObserver subscriber) {
    Timber.d("saveItem");
    Completable.fromAction(() -> todoDatabase.todoDao().insertItem(item))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  public void deleteItem(Todo item, CompletableObserver subscriber) {
    Timber.d("deleteItem");
    Completable.fromAction(() -> todoDatabase.todoDao().deleteItem(item))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  public void updateItem(Todo item, CompletableObserver subscriber) {
    Timber.d("updateItem");
    Completable.fromAction(() -> todoDatabase.todoDao().updateItem(item))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  public void reset(CompletableObserver subscriber) {
    Timber.d("reset");
    Completable.fromAction(() -> todoDatabase.todoDao().reset(false, true))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  public void removeService() {
    Timber.d("removeService");
    TodoDatabase.destroyInstance();
  }
}
