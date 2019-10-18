package com.example.catculate.mvp.view.activities;

import com.example.catculate.data.entity.SpendTagEntity;
import com.example.catculate.services.SpendTagService;
import com.example.catculate.utils.SharedPreferencesManager;
import java.util.List;

public interface SpendSettingActivityContract {

  interface View {

    void showWait(String message);

    void removeWait();

    void showDialogTag(int position, SpendTagEntity item);

    void addNewTag(SpendTagEntity item);

    void updateTag(int position, SpendTagEntity item);

    void setTagList(List<SpendTagEntity> spendTagList);

    void showSnackbar(String message);
  }

  interface Presenter {

    void setView(View view, SharedPreferencesManager sharedPreferences, SpendTagService databaseService);

    void getAllTags();

    void deleteTag(SpendTagEntity item);

    void addDummyTags();

    void addNewTag(SpendTagEntity comment);

    void updateTag(SpendTagEntity comment);

    void unSubscribe();

  }

}
