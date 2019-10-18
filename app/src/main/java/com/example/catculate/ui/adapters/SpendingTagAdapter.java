package com.example.catculate.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.catculate.R;
import com.example.catculate.data.entity.SpendTagEntity;
import com.example.catculate.ui.activities.SpendSettingActivity;
import com.example.catculate.ui.adapters.SpendingTagAdapter.ViewHolder;
import com.google.gson.Gson;
import java.util.List;
import timber.log.Timber;

public class SpendingTagAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final SpendSettingActivity activity;
  private List<SpendTagEntity> valueItems;

  public SpendingTagAdapter(SpendSettingActivity activity, List<SpendTagEntity> responses) {
    this.activity = activity;
    this.valueItems = responses;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater
        .from(parent.getContext()).inflate(R.layout.list_item_tag, parent, false);
    return new SpendingTagAdapter.ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull final SpendingTagAdapter.ViewHolder holder, final int position) {
    final SpendTagEntity data = valueItems.get(holder.getAdapterPosition());
    holder.bind(data);

    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        activity.updateTag(holder.getAdapterPosition(), data);
      }
    });

    holder.btnClose.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        //Remove from db.
        activity.getPresenter().deleteTag(data);
        //Remove from list.
        valueItems.remove(holder.getAdapterPosition());
        //Remove from adapter.
        notifyItemRemoved(holder.getAdapterPosition());
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
  public void onViewDetachedFromWindow(@NonNull SpendingTagAdapter.ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    //for avoid fastscrolling animation
    holder.itemView.clearAnimation();
  }

  public void setNewItem(SpendTagEntity valueItem) {
    Timber.e("setNewItems");
    valueItems.add(0,valueItem);
    notifyItemInserted(0);
  }

  public void updateItem(int updatedPosition, SpendTagEntity updatedData) {
    Timber.d("updatedItem %s", new Gson().toJson(updatedData));
    //Updated in list.
    valueItems.set(updatedPosition, updatedData);
    //Update in view.
    notifyItemChanged(updatedPosition);
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    final TextView tvTagName;
    final ImageView btnClose;

    ViewHolder(View itemView) {
      super(itemView);
      tvTagName = itemView.findViewById(R.id.tv_tag);
      btnClose = itemView.findViewById(R.id.btn_close);
    }

    public void bind(SpendTagEntity data) {
      tvTagName.setText(data.getName());
    }
  }
}
