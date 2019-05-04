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
import com.example.catculate.data.entity.Todo;
import com.example.catculate.mvp.presenter.CheckPresenter;
import com.example.catculate.mvp.view.fragments.CheckFragmentContract;
import com.example.catculate.mvp.view.fragments.CheckFragmentContract.Presenter;
import com.example.catculate.ui.activities.MainActivity;
import com.example.catculate.ui.adapters.TodoAdapter;
import com.example.catculate.ui.dialogs.DialogTodo;
import com.example.catculate.ui.dialogs.DialogTodo.DialogTodoCallback;
import com.example.catculate.utils.RecyclerViewAnimation;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment implements CheckFragmentContract.View {

  View rootView;
  @BindView(R.id.rv_main)
  RecyclerView rvMain;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;
  @BindView(R.id.title)
  TextView tvTitle;
  @BindView(R.id.tv_bottom_title)
  TextView tvTitleBottom;
  @BindView(R.id.tv_bottom_total)
  public TextView tvBottomTotal;
  Unbinder unbinder;

  MainActivity activity;
  Presenter presenter;
  TodoAdapter mAdapter;
  DialogTodo dialogTodo;

  public CheckFragment() {
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
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_check, container, false);
    unbinder = ButterKnife.bind(this, rootView);

    initView();

    presenter = new CheckPresenter(this);

    presenter.initPresenter(activity.getSharedPreferencesManager(), activity.getTodoDbService());

    presenter.getAll(Constants.STATE_INCOMPLETED);

    return rootView;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  public Presenter getPresenter() {
    return presenter;
  }

  private void initView() {

    rvMain.setHasFixedSize(true);
    rvMain.setLayoutManager(new LinearLayoutManager(activity));
  }

  @Override
  public void showWait() {
    progressBar.setVisibility(View.VISIBLE);
    rvMain.setVisibility(View.GONE);
  }

  @Override
  public void removeWait() {
    progressBar.setVisibility(View.GONE);
    rvMain.setVisibility(View.VISIBLE);
  }

  //TODO make empty layout.
  @Override
  public void showEmptyLayout() {

  }

  @Override
  public void removeEmptyLayout() {

  }

  @Override
  public void addListUncompletedToView(
      List<Todo> unCompletedList,
      int completedListSize) {
    rvMain.setVisibility(View.VISIBLE);
    mAdapter = new TodoAdapter(this, unCompletedList, Constants.STATE_INCOMPLETED);
    rvMain.setItemAnimator(new DefaultItemAnimator());
    rvMain.setAdapter(mAdapter);
    new RecyclerViewAnimation().rerunLayoutAnimation(rvMain);

    tvTitleBottom.setText(getResources().getString(R.string.title_completed));
    tvBottomTotal.setText(String.valueOf(completedListSize));

    tvTitle.setText(getResources().getString(R.string.title_uncompleted));
  }

  @Override
  public void addListCompletedToView(List<Todo> completedList, int inCompletedTotal) {
    rvMain.setVisibility(View.VISIBLE);
    mAdapter = new TodoAdapter(this, completedList, Constants.STATE_COMPLETED);
    rvMain.setItemAnimator(new DefaultItemAnimator());
    rvMain.setAdapter(mAdapter);
    new RecyclerViewAnimation().rerunLayoutAnimation(rvMain);

    tvTitleBottom.setText(getResources().getString(R.string.title_uncompleted));
    tvBottomTotal.setText(String.valueOf(inCompletedTotal));

    tvTitle.setText(getResources().getString(R.string.title_completed));
  }

  @Override
  public void insertNewItem(Todo todo) {
    mAdapter.setNewItem(todo);
    rvMain.scrollToPosition(0);
  }

  @OnClick({R.id.bottom_layout, R.id.btn_reset, R.id.btn_add})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.bottom_layout:
        presenter.getAll(tvTitle.getText().equals(getResources().getString(R.string.title_completed)) ? Constants.STATE_INCOMPLETED : Constants.STATE_COMPLETED);
        break;
      case R.id.btn_reset:
        presenter.reset(Constants.STATE_INCOMPLETED);
        break;
      case R.id.btn_add:
        showDialogNew();
        break;
    }
  }

  private void showDialogNew() {
    dialogTodo = new DialogTodo(activity, null, new DialogTodoCallback() {
      @Override
      public void onClick(Todo todo) {
        presenter.insertItem(todo);
      }
    });
    dialogTodo.show();
  }

  public void showDialogUpdate(Todo todo, final int position) {
    dialogTodo = new DialogTodo(activity, todo, new DialogTodoCallback() {
      @Override
      public void onClick(Todo todo) {
        presenter.updateItem(todo);
        mAdapter.updateItem(position, todo);
      }
    });
    dialogTodo.show();
  }

}
