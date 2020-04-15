package com.example.first_firebase.Activities.ui.singout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SingOutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SingOutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SingOut fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}