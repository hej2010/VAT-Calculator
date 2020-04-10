package se.arctosoft.vatcalculator.ui;

import android.content.res.Resources;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import se.arctosoft.vatcalculator.R;

import static se.arctosoft.vatcalculator.ui.DataViewModel.VAT_RATES;

public class SharedStuff {

    public static boolean checkAndUpdateVatRates(Integer vatRate, LinearLayout lLVatRates, Resources resources) {
        boolean found = false;
        for (int i = 0; i < VAT_RATES.length; i++) {
            ((CardView) lLVatRates.getChildAt(i)).setCardBackgroundColor(resources.getColor(R.color.colorWhite));
            if (VAT_RATES[i] == vatRate) {
                found = true;
                lLVatRates.getChildAt(i).performClick();
            }
        }
        return found;
    }

    public static String removeTrailingZeros(@NonNull String s) {
        return s.contains(".") ? s.replaceAll("0*$", "").replaceAll("\\.$", "") : s;
    }

}
