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
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.example.catculate.R;
import com.example.catculate.data.entity.Todo;

public class DialogTodo {

  private Context context;
  private MaterialDialog dialog = null;
  private Todo todo;
  private DialogTodoCallback callback;

  public DialogTodo(Context context, Todo item, DialogTodoCallback callback) {
    this.context = context;
    this.todo = item;
    this.callback = callback;
  }

  public void show() {

    if (dialog == null) {

      dialog = new Builder(context)
          .customView(R.layout.dialog_todo, true)
          .dismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dd) {
              dialog = null;
            }
          })
          .showListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dd) {

              final EditText etDesc = (EditText) dialog.findViewById(R.id.et_desc);
              final EditText etPrice = (EditText) dialog.findViewById(R.id.et_price);
              ImageButton btnSubmit = (ImageButton) dialog.findViewById(R.id.btn_submit);

              btnSubmit.setImageDrawable(context.getResources().getDrawable(
                  todo == null ? R.drawable.ic_plus : R.drawable.ic_edit
              ));

              if (todo != null) {
                etDesc.setText(todo.getDescrip());
                etPrice.setText(String.valueOf(todo.getValue()));
              }

              btnSubmit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                  String desc = etDesc.getText().toString();
                  String price = etPrice.getText().toString();

                  if (!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(price)) {

                    if (dialog != null && dialog.isShowing()) {
                      dialog.dismiss();
                    }

                    if (todo == null) {
                      todo = new Todo();
                      todo.setChecked(false);
                    }
                    todo.setDescrip(desc);
                    todo.setValue(Long.parseLong(price));

                    callback.onClick(todo);

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
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
      dialog = null;
    }
  }

  public interface DialogTodoCallback {
    void onClick(Todo todo);
  }

}
