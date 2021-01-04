package se.arctosoft.vatcalculator.ui.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import se.arctosoft.vatcalculator.MainActivity;

public class DataViewModel extends ViewModel {
    public static final int[] VAT_RATES = {15, 16, 18, 19, 20, 21, 22, 25, 27};
    public static final int VAT_DEFAULT_POS = 4;
    public static final int VAT_SE_POS = 7;
    private final MutableLiveData<Double> valueExclVat, valueInclVat;
    private final MutableLiveData<Integer> vatRate;

    public DataViewModel() {
        valueExclVat = new MutableLiveData<>((double) 0);
        valueInclVat = new MutableLiveData<>((double) 0);
        vatRate = new MutableLiveData<>(MainActivity.isSwedishLocale ? VAT_RATES[VAT_SE_POS] : VAT_RATES[VAT_DEFAULT_POS]);
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