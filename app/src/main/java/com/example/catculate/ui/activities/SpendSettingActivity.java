package com.example.catculate.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.catculate.R;
import com.example.catculate.data.entity.SpendTagEntity;
import com.example.catculate.mvp.presenter.SpendSettingPresenter;
import com.example.catculate.mvp.view.activities.SpendSettingActivityContract.Presenter;
import com.example.catculate.mvp.view.activities.SpendSettingActivityContract.View;
import com.example.catculate.root.BaseActivity;
import com.example.catculate.services.SpendTagService;
import com.example.catculate.ui.adapters.SpendingTagAdapter;
import com.example.catculate.ui.dialogs.DialogTag;
import com.example.catculate.ui.dialogs.DialogTag.DialogTagCallback;
import com.example.catculate.utils.RecyclerViewAnimation;
import com.example.catculate.utils.SharedPreferencesManager;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class SpendSettingActivity extends BaseActivity implements View {

  @Inject
  SharedPreferencesManager sharedPreferencesManager;

  @BindView(R.id.toolbar_title)
  TextView toolbarTitle;
  @BindView(R.id.include)
  Toolbar toolbar;
  @BindView(R.id.rv)
  RecyclerView recyclerView;
  @BindView(R.id.middleLayout)
  CoordinatorLayout middleLayout;

  private ProgressDialog progressDialog = null;
  private DialogTag dialogTag = null;
  private SpendTagService spendTagService;
  private SpendingTagAdapter mAdapter;

  private Presenter presenter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);

    presenter = new SpendSettingPresenter();
    presenter.setView(this, sharedPreferencesManager, getSpendTagDbService());

    initView();
  }

  @Override
  protected int getLayoutResourceId() {
    return R.layout.activity_spend_setting;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    presenter.unSubscribe();
  }

  public Presenter getPresenter() {
    return presenter;
  }

  public SpendTagService getSpendTagDbService() {
    if (spendTagService == null) {
      spendTagService = new SpendTagService();
    }
    return spendTagService;
  }

  private void initView() {
    //Setup toolbar.
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    toolbar.setNavigationOnClickListener(new OnClickListener() {
      @Override
      public void onClick(android.view.View view) {
        onBackPressed();
      }
    });

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      Window window = this.getWindow();
      window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    toolbarTitle.setText("TAGS");

    recyclerView.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(linearLayoutManager);

    presenter.getAllTags();
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

  @Override
  public void showDialogTag(int position, SpendTagEntity item) {
    Timber.d("showDialogTag");
    if (dialogTag == null) {
      dialogTag = new DialogTag(this, item, new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
          dialogTag = null;
        }
      }, new DialogTagCallback() {
        @Override
        public void onClick(SpendTagEntity tag) {
          if (item == null) {
            presenter.addNewTag(tag);
          } else {
            presenter.updateTag(tag);
            mAdapter.updateItem(position, tag);
          }
        }
      });
      dialogTag.show();
    }
  }

  @Override
  public void addNewTag(SpendTagEntity item) {
    Timber.d("addNewTag");
    mAdapter.setNewItem(item);
    recyclerView.scrollToPosition(0);
  }

  @Override
  public void updateTag(int position, SpendTagEntity item) {
    Timber.d("updateTag");
    showDialogTag(position, item);
  }

  @Override
  public void setTagList(List<SpendTagEntity> spendTagList) {
    recyclerView.setVisibility(android.view.View.VISIBLE);
    mAdapter = new SpendingTagAdapter(this, spendTagList);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mAdapter);
    new RecyclerViewAnimation().rerunLayoutAnimation(recyclerView);
  }

  @Override
  public void showSnackbar(String message) {
    Snackbar.make(middleLayout, message, Snackbar.LENGTH_SHORT).show();
  }

  @OnClick(R.id.fab_add)
  public void onViewClicked() {
    showDialogTag(0, null);
  }
}
