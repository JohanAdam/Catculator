package com.example.catculate.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.example.catculate.Constants;
import com.example.catculate.R;
import com.example.catculate.data.entity.ValueItem;
import com.example.catculate.mvp.presenter.SpendingPresenter;
import com.example.catculate.mvp.view.fragments.SpendFragmentContract;
import com.example.catculate.mvp.view.fragments.SpendFragmentContract.Presenter;
import com.example.catculate.ui.activities.MainActivity;
import com.example.catculate.ui.adapters.SpendingAdapter;
import com.example.catculate.ui.dialogs.DialogPriceNew;
import com.example.catculate.ui.dialogs.DialogPriceNew.DialogPriceNewCallback;
import com.example.catculate.utils.RecyclerViewAnimation;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingFragment extends Fragment implements SpendFragmentContract.View {

  @BindView(R.id.tv_total)
  TextView tvTotal;
  @BindView(R.id.main_rv)
  RecyclerView recyclerView;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;
  Unbinder unbinder;

  View rootView;
  MainActivity activity;
  Presenter presenter;

  public SpendingFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof MainActivity) {
      this.activity = (MainActivity) context;
    }
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_spending, container, false);
    unbinder = ButterKnife.bind(this, rootView);

    initView();

    presenter = new SpendingPresenter(this);

    presenter.initPresenter(activity.getSharedPreferencesManager(), activity.getDbService());

    presenter.getList();

    return rootView;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void initView() {

    recyclerView.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    recyclerView.setLayoutManager(linearLayoutManager);

  }

  @Override
  public void showWait() {
    progressBar.setVisibility(View.VISIBLE);
    recyclerView.setVisibility(View.INVISIBLE);
  }

  @Override
  public void removeWait() {
    progressBar.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
  }

  //TODO Make error layout.
  @Override
  public void showErrorLayout() {
  }

  @Override
  public void removeErrorLayout() {
  }

  //TODO Make empty layout.
  @Override
  public void showEmptyLayout() {
  }

  @Override
  public void removeEmptyLayout() {
  }

  SpendingAdapter mAdapter;
  @Override
  public void addListToView(List<ValueItem> list) {
    recyclerView.setVisibility(View.VISIBLE);
    mAdapter = new SpendingAdapter(activity, list);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mAdapter);
    new RecyclerViewAnimation().rerunLayoutAnimation(recyclerView);
  }

  @Override
  public void insertNewItem(ValueItem valueItem) {
    Timber.d("insertNewItem");
    mAdapter.setNewItem(valueItem);
    recyclerView.scrollToPosition(0);
  }

  @OnClick({R.id.btn_plus, R.id.btn_minus})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_plus:
        showDialog(Constants.SYMBOLIC_ADD);
        break;
      case R.id.btn_minus:
        showDialog(Constants.SYMBOLIC_MINUS);
        break;
    }
  }

  DialogPriceNew dialogPrice;
  private void showDialog(final int type) {
    dialogPrice = new DialogPriceNew(activity, type, new DialogPriceNewCallback() {
      @Override
      public void onClick(String desc, String price, int type) {
        presenter.insertItem(desc, price, type);
      }
    });

    dialogPrice.show();
  }

}
