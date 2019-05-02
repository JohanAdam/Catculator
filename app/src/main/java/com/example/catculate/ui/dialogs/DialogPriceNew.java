package com.example.catculate.ui.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.catculate.Constants;
import com.example.catculate.R;
import timber.log.Timber;

public class DialogPriceNew {

  private Context context;
  private int symbolic;
  private MaterialDialog dialog = null;
  private DialogPriceNewCallback callback;

  public DialogPriceNew(Context context, int symbolic, DialogPriceNewCallback callback) {
    this.context = context;
    this.symbolic = symbolic;
    this.callback = callback;
  }

  public void show() {

    if (dialog == null) {

      dialog = new MaterialDialog.Builder(context)
          .customView(R.layout.dialog_add_minus, true)
          .dismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dd) {
              dialog = null;
            }
          })
          .showListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dd) {
              Timber.d("onShow");

              final EditText etDesc = (EditText) dialog.findViewById(R.id.et_desc);
              final EditText etPrice = (EditText) dialog.findViewById(R.id.et_price);
              ImageButton btnSubmit = (ImageButton) dialog.findViewById(R.id.btn_submit);

              btnSubmit.setBackground(symbolic == Constants.SYMBOLIC_ADD ?
                  context.getResources().getDrawable(R.drawable.button_background_plus) :
                  context.getResources().getDrawable(R.drawable.button_background_minus));

              btnSubmit.setImageDrawable(symbolic == Constants.SYMBOLIC_ADD ?
                  context.getResources().getDrawable(R.drawable.ic_plus) :
                  context.getResources().getDrawable(R.drawable.ic_remove));

              btnSubmit.setOnClickListener(new OnClickListener() {
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

            }
          })
          .show();

    }

  }

  public void remove() {
    if (dialog == null && dialog.isShowing()) {
      dialog.dismiss();
      dialog = null;
    }
  }

  public interface DialogPriceNewCallback {
    void onClick(String desc, String price, int type);
  }

}
