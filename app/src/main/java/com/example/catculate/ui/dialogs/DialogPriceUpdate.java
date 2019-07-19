package com.example.catculate.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.catculate.Constants;
import com.example.catculate.R;
import com.example.catculate.data.entity.ValueItem;

public class DialogPriceUpdate {

  private Context context;
  private ValueItem currentData;
  private Dialog dialog = null;
  private DialogPriceUpdateCallback callback;

  public DialogPriceUpdate(Context context, ValueItem item, DialogPriceUpdateCallback callback) {
    this.context = context;
    this.currentData = item;
    this.callback = callback;
  }

  public void show() {
    if (dialog == null) {

      final String desc = !TextUtils.isEmpty(currentData.getDescrip()) ? currentData.getDescrip() : "Nyan Nyan";
      final String price = !TextUtils.isEmpty(String.valueOf(currentData.getValue())) ? String.valueOf(currentData.getValue()) : "0";
      final int symbolic = currentData.getSymbolic();

      dialog = new Dialog(context, R.style.AppDialog);
      if (dialog.getWindow() != null) {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      }
      dialog.setContentView(R.layout.dialog_spending_new);
      dialog.setOnDismissListener(new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dd) {
          dialog = null;
        }
      });
      dialog.show();

      final EditText etDesc = dialog.findViewById(R.id.et_desc);
      final EditText etPrice = dialog.findViewById(R.id.et_price);
      ImageView ivCurrentSymbol = dialog.findViewById(R.id.iv_current_symbol);
      ImageButton btnPlus = dialog.findViewById(R.id.btn_submit_plus);
      ImageButton btnMinus = dialog.findViewById(R.id.btn_submit_minus);

      ivCurrentSymbol.setAlpha(0.4f);
      ivCurrentSymbol.setImageDrawable(context.getResources().getDrawable(symbolic == Constants.SYMBOLIC_ADD ? R.drawable.ic_plus : R.drawable.ic_remove));

      btnPlus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_plus));
      btnMinus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_remove));

      etDesc.setText(desc);
      etPrice.setText(price);

      btnPlus.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          onClickFunction(etDesc, etPrice, Constants.SYMBOLIC_ADD, callback);
        }
      });

      btnMinus.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          onClickFunction(etDesc, etPrice, Constants.SYMBOLIC_MINUS, callback);
        }
      });


    }

  }

  private void onClickFunction(EditText etDesc, EditText etPrice, int symbolic, DialogPriceUpdateCallback callback) {
    String desc = etDesc.getText().toString();
    String price = etPrice.getText().toString();

    if (!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(price)) {

      ValueItem item = new ValueItem();
      item.setId(currentData.getId());
      item.setDescrip(desc);
      item.setValue(Long.parseLong(price));
      item.setSymbolic(symbolic);

      if (dialog != null && dialog.isShowing()) {
        dialog.dismiss();
      }

      callback.onClick(item);

    } else {
      if (TextUtils.isEmpty(desc)) {
        etDesc.setError("Please enter description!");
      }

      if (TextUtils.isEmpty(price)) {
        etPrice.setError("Please enter price!");
      }
    }
  }

  public void remove() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
      dialog = null;
    }
  }

  public interface DialogPriceUpdateCallback {
    void onClick(ValueItem updatedData);
  }

}

