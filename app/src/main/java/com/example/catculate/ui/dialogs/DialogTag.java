package com.example.catculate.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.example.catculate.R;
import com.example.catculate.data.entity.SpendTagEntity;
import com.example.catculate.ui.activities.SpendSettingActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import timber.log.Timber;

public class DialogTag {

  private SpendSettingActivity activity;
  private Dialog dialog = null;
  private DialogTagCallback callback;
  private OnDismissListener dismissListener;
  private SpendTagEntity item;

  public DialogTag(SpendSettingActivity activity, SpendTagEntity comment, OnDismissListener dismissListener, DialogTagCallback callback){
    this.activity = activity;
    if (comment == null) {
      this.item = new SpendTagEntity();
    } else {
      this.item = comment;
    }
    this.dismissListener = dismissListener;
    this.callback = callback;
  }

  public void show() {
    Timber.d("show");
    if (dialog == null) {
      dialog = new Dialog(activity, R.style.AppDialog);
      if (dialog.getWindow() != null) {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      }
      dialog.setContentView(R.layout.dialog_tags);
      dialog.setOnDismissListener(dismissListener);
      dialog.show();

      TextInputEditText etTag = dialog.findViewById(R.id.et_tag);
      if (!TextUtils.isEmpty(item.getName())) {
        etTag.setText(item.getName());
      }

      MaterialButton btnSubmit = dialog.findViewById(R.id.btn_submit);

      btnSubmit.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          if (!TextUtils.isEmpty(etTag.getText().toString())) {
            item.setName(etTag.getText().toString());
            callback.onClick(item);
            remove();
          } else {
            Toast.makeText(activity, "Mana tag nya??", Toast.LENGTH_LONG).show();
          }
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

  public interface DialogTagCallback {
    void onClick(SpendTagEntity item);
  }

}
