package com.example.catculate.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.example.catculate.R;
import com.example.catculate.data.entity.Todo;
import com.example.catculate.ui.fragments.CheckFragment;
import com.google.gson.Gson;
import java.util.List;
import timber.log.Timber;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

  private final CheckFragment fragment;
  private List<Todo> todoList;
  private int state;
  private boolean onBind;

  public TodoAdapter(CheckFragment activity, List<Todo> responses, int state) {
    this.fragment = activity;
    this.todoList = responses;
    this.state = state;
  }

  @NonNull
  @Override
  public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater
        .from(parent.getContext()).inflate(R.layout.list_item_check, parent, false);
    return new TodoAdapter.ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull final TodoAdapter.ViewHolder holder, final int position) {
    final Todo data = todoList.get(holder.getAdapterPosition());
    holder.bind(data);

    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
//        fragment.updateItem(holder.getAdapterPosition(), data);
        fragment.showDialogUpdate(data, holder.getAdapterPosition());
      }
    });

    holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!onBind) {
          //Edit checked value.
          data.setChecked(isChecked);
          //Update.
          fragment.getPresenter().updateItem(data);
          //Remove from list.
          todoList.remove(holder.getAdapterPosition());
          //Remove from adapter.
          notifyItemRemoved(holder.getAdapterPosition());


          long newTotal = Long.parseLong(fragment.tvBottomTotal.getText().toString()) + 1;
          fragment.tvBottomTotal.setText(String.valueOf(newTotal));
        }
      }
    });

    holder.itemView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
//        //Remove from db.
        fragment.getPresenter().deleteItem(data);
        //Remove from list.
        todoList.remove(holder.getAdapterPosition());
        //Remove from adapter.
        notifyItemRemoved(holder.getAdapterPosition());
        return false;
      }
    });
  }

  @Override
  public int getItemCount() {
    if (todoList != null) {
      return todoList.size();
    }
    return 0;
  }

  @Override
  public void onViewDetachedFromWindow(@NonNull TodoAdapter.ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    //for avoid fastscrolling animation
    holder.itemView.clearAnimation();
  }

  public void setNewItem(Todo todo) {
    Timber.e("setNewItems");
    todoList.add(0, todo);
    notifyItemInserted(0);
  }

  public void updateItem(int updatedPosition, Todo updatedData) {
    Timber.d("updatedItem %s", new Gson().toJson(updatedData));
    //Updated in list.
    todoList.set(updatedPosition, updatedData);
    //Update in view.
    notifyItemChanged(updatedPosition);
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    final TextView tvDesc;
    final TextView tvPrice;
    final CheckBox checkBox;

    ViewHolder(View itemView) {
      super(itemView);
      tvPrice = itemView.findViewById(R.id.tv_price);
      tvDesc = itemView.findViewById(R.id.tv_desc);
      checkBox = itemView.findViewById(R.id.checkBox);
    }

    void bind(final Todo data) {
      tvDesc.setText(data.getDescrip());
      tvPrice.setText(String.valueOf(data.getValue()));

      onBind = true;
      checkBox.setChecked(data.isChecked());
      onBind = false;
    }
  }
}
