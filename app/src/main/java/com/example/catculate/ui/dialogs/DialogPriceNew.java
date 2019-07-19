package com.example.catculate.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.catculate.Constants;
import com.example.catculate.R;
import com.example.catculate.ui.activities.MainActivity;
import com.example.catculate.utils.Utils;

public class DialogPriceNew {

  private MainActivity context;
  private int symbolic;
  private Dialog dialog = null;
  private DialogPriceNewCallback callback;

  public DialogPriceNew(MainActivity context, int symbolic, DialogPriceNewCallback callback) {
    this.context = context;
    this.symbolic = symbolic;
    this.callback = callback;
  }

  public void show() {

    if (dialog == null) {

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

      ivCurrentSymbol.setVisibility(View.GONE);
      btnPlus.setVisibility(View.GONE);

      btnMinus.setBackground(symbolic == Constants.SYMBOLIC_ADD ?
          context.getResources().getDrawable(R.drawable.button_background_plus) :
          context.getResources().getDrawable(R.drawable.button_background_minus));

      btnMinus.setImageDrawable(symbolic == Constants.SYMBOLIC_ADD ?
          context.getResources().getDrawable(R.drawable.ic_plus) :
          context.getResources().getDrawable(R.drawable.ic_remove));

      btnMinus.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

          String desc = etDesc.getText().toString();
          String price = etPrice.getText().toString();

          if (!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(price)) {

            if (dialog != null && dialog.isShowing()) {
              dialog.dismiss();
            }

            callback.onClick(desc, price, symbolic);

          } else {
            if (TextUtils.isEmpty(desc)) {
              etDesc.setError("Please enter description!");
            }

            if (TextUtils.isEmpty(price)) {
              etPrice.setError("Please enter price!");
            }
          }
        }
      });

      etPrice.setOnKeyListener(new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
          // If the event is a key-down event on the "enter" button
          if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
              (keyCode == KeyEvent.KEYCODE_ENTER)) {

            // Perform action on key press
            new Utils(context).hideKeyboard(etPrice);
            return true;
          }
          return false;
        }
      });
    }

  }

  public void remove() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
      dialog = null;
    }
  }

  public interface DialogPriceNewCallback {
    void onClick(String desc, String price, int type);
  }

}
