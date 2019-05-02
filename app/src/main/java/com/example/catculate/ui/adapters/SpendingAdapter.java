package com.example.catculate.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.catculate.Constants;
import com.example.catculate.R;
import com.example.catculate.data.entity.ValueItem;
import com.example.catculate.ui.activities.MainActivity;
import java.util.List;
import timber.log.Timber;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.ViewHolder> {

  private final MainActivity mContext;
  private List<ValueItem> valueItems;

  public SpendingAdapter(MainActivity activity, List<ValueItem> responses) {
    this.mContext = activity;
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
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final ValueItem data = valueItems.get(holder.getAdapterPosition());
    holder.bind(data);

    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

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
        ivSymbol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_remove));
      } else if (data.getSymbolic() == Constants.SYMBOLIC_ADD) {
        ivSymbol.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_plus));
      }
    }
  }
}
