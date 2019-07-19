package com.example.catculate.root;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutResourceId());
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  protected abstract int getLayoutResourceId();
}
