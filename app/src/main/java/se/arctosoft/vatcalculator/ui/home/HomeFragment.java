package se.arctosoft.vatcalculator.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

import se.arctosoft.vatcalculator.R;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final int[] VAT_RATES = {15, 16, 18, 19, 20, 21, 25, 27};
    private HomeViewModel homeViewModel;

    private EditText eTValExcl, eTValVatRate;
    private TextView txtFinalPrice, txtFinalPriceResult, txtFinalVAT, txtFinalVATResult;
    private LinearLayout lLVatRates;
    private boolean dontUpdate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        eTValExcl = root.findViewById(R.id.eTValExcl);
        eTValVatRate = root.findViewById(R.id.eTValVatRate);
        txtFinalPrice = root.findViewById(R.id.txtFinalPrice);
        txtFinalPriceResult = root.findViewById(R.id.txtFinalPriceResult);
        txtFinalVAT = root.findViewById(R.id.txtFinalVAT);
        txtFinalVATResult = root.findViewById(R.id.txtFinalVATResult);
        lLVatRates = root.findViewById(R.id.lLVatRates);
        dontUpdate = false;

        attachVatRates();
        attachListeners();
        checkStoredValues();

        return root;
    }

    private void checkStoredValues() {
        dontUpdate = true;
        boolean set = false;
        Double priceExc = homeViewModel.getLiveValueExclVat();
        Integer vatRate = homeViewModel.getVatRate();

        if (priceExc != null && priceExc != 0) {
            eTValExcl.setText(String.valueOf(priceExc));
            set = true;
        }

        if (vatRate != null && vatRate != 0) {
            eTValVatRate.setText(String.valueOf(vatRate));
            set = true;
        }

        if (set && vatRate != null) {
            if (checkAndUpdateVatRates(vatRate)) {
                dontUpdate = false;
            }
        }
        dontUpdate = false;
        update();
    }

    private boolean checkAndUpdateVatRates(Integer vatRate) {
        boolean found = false;
        for (int i = 0; i < VAT_RATES.length; i++) {
            ((CardView) lLVatRates.getChildAt(i)).setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            if (VAT_RATES[i] == vatRate) {
                found = true;
                lLVatRates.getChildAt(i).performClick();
            }
        }
        return found;
    }

    private void attachVatRates() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (int vatRate : VAT_RATES) {
            CardView layout = (CardView) inflater.inflate(R.layout.vat_rate_item, lLVatRates, false);
            layout.setTag(vatRate);

            TextView textView = layout.findViewById(R.id.txtVatRate);
            textView.setText(getString(R.string.percentage_placeholder, vatRate));

            layout.setOnClickListener(v -> {
                for (int i = 0; i < VAT_RATES.length; i++) {
                    ((CardView) lLVatRates.getChildAt(i)).setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                if (v instanceof CardView) {
                    ((CardView) v).setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                eTValVatRate.setText(String.valueOf(v.getTag()));
            });
            lLVatRates.addView(layout);
        }
        lLVatRates.getChildAt(4).performClick();
        setEmpty();
    }

    private void attachListeners() {
        TextWatcher t = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!dontUpdate) {
                    update();
                }
            }
        };
        eTValVatRate.addTextChangedListener(t);
        eTValExcl.addTextChangedListener(t);
    }

    private void update() {
        double priceExclVat;
        Integer vatRate;
        String sPriceExclVat = eTValExcl.getText().toString();
        String sVatRate = eTValVatRate.getText().toString();
        if (sPriceExclVat.isEmpty() || sVatRate.isEmpty()) {
            setEmpty();
            try {
                int vat = Integer.parseInt(sVatRate);
                homeViewModel.setVatRate(vat);
                dontUpdate = true;
                checkAndUpdateVatRates(vat);
                dontUpdate = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else {
            try {
                if (sPriceExclVat.endsWith(".")) {
                    sPriceExclVat = sPriceExclVat.replace(".", "");
                }
                priceExclVat = Double.parseDouble(sPriceExclVat);
            } catch (Exception e) {
                e.printStackTrace();
                setEmpty();
                return;
            }
            try {
                vatRate = Integer.valueOf(sVatRate);
                dontUpdate = true;
                checkAndUpdateVatRates(vatRate);
                dontUpdate = false;
            } catch (Exception e) {
                e.printStackTrace();
                setEmpty();
                return;
            }
        }

        final double vatAmount = priceExclVat * (vatRate / 100.0);
        final double valueIncVat = priceExclVat + vatAmount;
        homeViewModel.setValueExclVat(priceExclVat);
        homeViewModel.setValueInclVat(valueIncVat);
        homeViewModel.setVatRate(vatRate);

        String finalPriceResult = removeTrailingZeros(String.format(Locale.ENGLISH, "%.5f", valueIncVat));
        String finalVATResult = removeTrailingZeros(String.format(Locale.ENGLISH, "%.5f", vatAmount));

        txtFinalPriceResult.setText(finalPriceResult);
        txtFinalPrice.setText(getString(R.string.home_total_price, vatRate));

        txtFinalVATResult.setText(finalVATResult);
        txtFinalVAT.setText(getString(R.string.home_total_vat));

        txtFinalPriceResult.setVisibility(View.VISIBLE);
        txtFinalPrice.setVisibility(View.VISIBLE);
        txtFinalVATResult.setVisibility(View.VISIBLE);
        txtFinalVAT.setVisibility(View.VISIBLE);
    }

    private String removeTrailingZeros(@NonNull String s) {
        return s.contains(".") ? s.replaceAll("0*$", "").replaceAll("\\.$", "") : s;
    }

    private void setEmpty() {
        txtFinalPriceResult.setVisibility(View.INVISIBLE);
        txtFinalPrice.setVisibility(View.INVISIBLE);
        txtFinalVATResult.setVisibility(View.INVISIBLE);
        txtFinalVAT.setVisibility(View.INVISIBLE);
    }

}
