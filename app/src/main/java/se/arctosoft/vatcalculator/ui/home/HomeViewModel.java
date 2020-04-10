package se.arctosoft.vatcalculator.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Double> valueExclVat, valueInclVat;
    private MutableLiveData<Integer> vatRate;

    public HomeViewModel() {
        valueExclVat = new MutableLiveData<>((double) 0);
        valueInclVat = new MutableLiveData<>((double) 0);
        vatRate = new MutableLiveData<>(20);
    }

    Double getLiveValueExclVat() {
        return valueExclVat.getValue();
    }

    void setValueExclVat(double valueExclVat) {
        this.valueExclVat.setValue(valueExclVat);
    }

    Double getValueInclVat() {
        return valueInclVat.getValue();
    }

    void setValueInclVat(double valueInclVat) {
        this.valueInclVat.setValue(valueInclVat);
    }

    Integer getVatRate() {
        return vatRate.getValue();
    }

    void setVatRate(int vatRate) {
        this.vatRate.setValue(vatRate);
    }
}