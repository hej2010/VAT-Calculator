package se.arctosoft.vatcalculator.ui.reverse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReverseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReverseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}