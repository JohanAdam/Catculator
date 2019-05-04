package com.example.catculate.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.example.catculate.R;
import com.example.catculate.data.entity.Todo;
import com.example.catculate.mvp.presenter.MainActivityPresenter;
import com.example.catculate.mvp.view.activities.MainActivityContract;
import com.example.catculate.mvp.view.activities.MainActivityContract.Presenter;
import com.example.catculate.root.BaseActivity;
import com.example.catculate.services.TodoService;
import com.example.catculate.services.ValueItemService;
import com.example.catculate.ui.fragments.CheckFragment;
import com.example.catculate.ui.fragments.DashboardFragment;
import com.example.catculate.ui.fragments.SpendingFragment;
import com.example.catculate.utils.SharedPreferencesManager;
import com.google.gson.Gson;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainActivityContract.View {

  @Inject
  SharedPreferencesManager sharedPreferencesManager;

  @BindView(R.id.toolbar_title)
  TextView toolbarTitle;
  @BindView(R.id.include)
  Toolbar toolbar;
  @BindView(R.id.bottomNavigation)
  BottomNavigationView bottomNavigation;

  ProgressDialog progressDialog = null;
  MaterialDialog infoDialog = null;
  MaterialDialog errorDialog = null;
  Fragment currentFragment;

  Presenter presenter;

  ValueItemService valueItemDbService;
  TodoService todoDbService;

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_main;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);

    presenter = new MainActivityPresenter();

    presenter.setView(this, sharedPreferencesManager, getValueItemDbService(), getTodoDbService());

    initView();

//    presenter.seedExampleData();
    List<Todo> list = new TodoService().getAll();
    Timber.d("Local list is size " + list.size());
    Timber.d("Local list is " + new Gson().toJson(list));

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  public SharedPreferencesManager getSharedPreferencesManager() {
    return sharedPreferencesManager;
  }

  public ValueItemService getValueItemDbService() {
    if (valueItemDbService == null) {
      valueItemDbService = new ValueItemService();
    }
    return valueItemDbService;
  }

  public TodoService getTodoDbService() {
    if (todoDbService == null) {
      todoDbService = new TodoService();
    }
    return todoDbService;
  }

  private void initView() {

    //setup toolbar
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      Window window = this.getWindow();
      window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    openFirstFragment();

    toolbarTitle.setText(currentFragment.getClass().getSimpleName());

    bottomNavigation.setOnNavigationItemSelectedListener(
        new OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_dashboard:
                currentFragment = new DashboardFragment();
                break;
              case R.id.action_spend:
                currentFragment = new SpendingFragment();
                break;
              case R.id.action_check:
                currentFragment = new CheckFragment();
                break;
            }
            if (currentFragment != null) {
              switchFragmentWithoutBackstack(currentFragment, currentFragment.getClass().getSimpleName());
            }
            return true;
          }
        }
    );
  }

  private void openFirstFragment() {
    currentFragment = new DashboardFragment();
    switchFragmentWithoutBackstack(currentFragment, currentFragment.getClass().getSimpleName());
  }

  //Show fragment without back button access
  public void switchFragmentWithoutBackstack(Fragment fragment, String tag) {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.main_container, fragment, tag).commit();
  }

  //Show fragment with back button access
  public void switchFragmentAddBackstack(Fragment fragment, String tag) {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.main_container, fragment, tag);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

  @Override
  public void onInfoDialog(int title, int message) {
    Timber.d("onInfoDialog");
    if (infoDialog == null) {
      infoDialog = new MaterialDialog.Builder(this)
          .title(title)
          .content(message)
          .onPositive(new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              Timber.d("Click Info Dialog");
            }
          })
          .dismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
              infoDialog = null;
            }
          })
          .show();
    }
  }

  @Override
  public void onErrorDialog(int title, int message, SingleButtonCallback callback) {
    Timber.d("onErrorDialog");
    if (errorDialog == null) {
      errorDialog = new MaterialDialog.Builder(this)
          .title(title)
          .content(message)
          .onPositive(callback)
          .dismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
              errorDialog = null;
            }
          })
          .show();
    }
  }

  @Override
  public void showWait(String message) {
    progressDialog = new ProgressDialog(this);
    if (!TextUtils.isEmpty(message)) {
      progressDialog.setMessage(message);
    } else {
      progressDialog.setMessage(getResources().getString(R.string.title_loading));
    }
    progressDialog.setCancelable(false);
    progressDialog.show();
  }

  @Override
  public void removeWait() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

}
