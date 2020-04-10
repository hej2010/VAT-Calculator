package se.arctosoft.vatcalculator.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {
    public static final int[] VAT_RATES = {15, 16, 18, 19, 20, 21, 22, 25, 27};
    public static final int VAT_DEFAULT_POS = 4;
    private MutableLiveData<Double> valueExclVat, valueInclVat;
    private MutableLiveData<Integer> vatRate;

    public DataViewModel() {
        valueExclVat = new MutableLiveData<>((double) 0);
        valueInclVat = new MutableLiveData<>((double) 0);
        vatRate = new MutableLiveData<>(20);
    }

    public Double getLiveValueExclVat() {
        return valueExclVat.getValue();
    }

    public void setValueExclVat(double valueExclVat) {
        this.valueExclVat.setValue(valueExclVat);
    }

    public Double getLiveValueInclVat() {
        return valueInclVat.getValue();
    }

    public void setValueInclVat(double valueInclVat) {
        this.valueInclVat.setValue(valueInclVat);
    }

    public Integer getVatRate() {
        return vatRate.getValue();
    }

    public void setVatRate(int vatRate) {
        this.vatRate.setValue(vatRate);
    }
}