package com.example.catculate.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import com.example.catculate.R;

public class DialogLoading {

  private Dialog dialog = null;
  private Context context;

  public DialogLoading(Context context) {
    this.context = context;
  }

  public void show() {

    if (dialog == null) {

      dialog = new Dialog(context, R.style.AppDialog);
      if (dialog.getWindow() != null) {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      }
      dialog.setContentView(R.layout.dialog_loading);
      dialog.setOnDismissListener(new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dd) {
          dialog = null;
        }
      });
      dialog.show();
    }
  }

  public void dismiss() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }

}
