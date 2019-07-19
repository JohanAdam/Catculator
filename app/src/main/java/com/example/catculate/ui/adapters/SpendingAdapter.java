package com.example.catculate.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.catculate.Constants;
import com.example.catculate.R;
import com.example.catculate.data.entity.ValueItem;
import com.example.catculate.ui.fragments.SpendingFragment;
import com.google.gson.Gson;
import java.util.List;
import timber.log.Timber;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.ViewHolder> {

  private final SpendingFragment fragment;
  private List<ValueItem> valueItems;

  public SpendingAdapter(SpendingFragment activity, List<ValueItem> responses) {
    this.fragment = activity;
    this.valueItems = responses;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater
        .from(parent.getContext()).inflate(R.layout.list_item_spending, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
    final ValueItem data = valueItems.get(holder.getAdapterPosition());
    holder.bind(data);

    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        fragment.updateItem(holder.getAdapterPosition(), data);
      }
    });

    holder.itemView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        //Remove from db.
        fragment.getPresenter().deleteItem(data);
        //Remove from list.
        valueItems.remove(holder.getAdapterPosition());
        //Remove from adapter.
        notifyItemRemoved(holder.getAdapterPosition());
        return false;
      }
    });
  }

  @Override
  public int getItemCount() {
    if (valueItems != null) {
      return valueItems.size();
    }
    return 0;
  }

  @Override
  public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    //for avoid fastscrolling animation
    holder.itemView.clearAnimation();
  }

  public void setNewItem(ValueItem valueItem) {
    Timber.e("setNewItems");
    valueItems.add(0,valueItem);
    notifyItemInserted(0);
  }

  public void updateItem(int updatedPosition, ValueItem updatedData) {
    Timber.d("updatedItem " + new Gson().toJson(updatedData));
    //Updated in list.
    valueItems.set(updatedPosition, updatedData);
    //Update in view.
    notifyItemChanged(updatedPosition);
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    final TextView tvDesc;
    final TextView tvPrice;
    final ImageView ivSymbol;

    ViewHolder(View itemView) {
      super(itemView);
      tvPrice = itemView.findViewById(R.id.tv_value);
      tvDesc = itemView.findViewById(R.id.tv_desc);
      ivSymbol = itemView.findViewById(R.id.iv_symbol);
    }

    public void bind(ValueItem data) {
      tvPrice.setText(String.valueOf(data.getValue()));
      tvDesc.setText(data.getDescrip());
      if (data.getSymbolic() == Constants.SYMBOLIC_MINUS) {
        ivSymbol.setImageDrawable(fragment.getResources().getDrawable(R.drawable.ic_remove));
      } else if (data.getSymbolic() == Constants.SYMBOLIC_ADD) {
        ivSymbol.setImageDrawable(fragment.getResources().getDrawable(R.drawable.ic_plus));
      }
    }
  }
}
