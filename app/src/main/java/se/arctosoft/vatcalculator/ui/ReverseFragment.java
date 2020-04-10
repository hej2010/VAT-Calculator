package se.arctosoft.vatcalculator.ui;

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
import se.arctosoft.vatcalculator.ui.data.DataViewModel;

import static se.arctosoft.vatcalculator.ui.data.DataViewModel.VAT_RATES;

public class ReverseFragment extends Fragment {
    private static final String TAG = "ReverseFragment";
    private DataViewModel dataViewModel;

    private EditText eTValIncl, eTValVatRate;
    private TextView txtFinalPrice, txtFinalPriceResult, txtFinalVAT, txtFinalVATResult;
    private LinearLayout lLVatRates;
    private boolean dontUpdate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reverse, container, false);

        eTValIncl = root.findViewById(R.id.eTValIncl);
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
        Double priceIncl = dataViewModel.getLiveValueInclVat();
        Integer vatRate = dataViewModel.getVatRate();

        if (priceIncl != null && priceIncl != 0) {
            eTValIncl.setText(String.valueOf(priceIncl));
            set = true;
        }

        if (vatRate != null && vatRate != 0) {
            eTValVatRate.setText(String.valueOf(vatRate));
            set = true;
        }

        if (set && vatRate != null) {
            if (((SharedStuff.checkAndUpdateVatRates(vatRate, lLVatRates, getResources())))) {
                dontUpdate = false;
            }
        }
        dontUpdate = false;
        update();
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
        lLVatRates.getChildAt(DataViewModel.VAT_DEFAULT_POS).performClick();
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
        eTValIncl.addTextChangedListener(t);
    }

    private void update() {
        double priceInclVat;
        Integer vatRate;
        String sPriceInclVat = eTValIncl.getText().toString();
        String sVatRate = eTValVatRate.getText().toString();
        if (sPriceInclVat.isEmpty() || sVatRate.isEmpty()) {
            setEmpty();
            try {
                int vat = Integer.parseInt(sVatRate);
                dataViewModel.setVatRate(vat);
                dontUpdate = true;
                SharedStuff.checkAndUpdateVatRates(vat, lLVatRates, getResources());
                dontUpdate = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else {
            try {
                if (sPriceInclVat.endsWith(".")) {
                    sPriceInclVat = sPriceInclVat.replace(".", "");
                }
                priceInclVat = Double.parseDouble(sPriceInclVat);
            } catch (Exception e) {
                e.printStackTrace();
                setEmpty();
                return;
            }
            try {
                vatRate = Integer.valueOf(sVatRate);
                dontUpdate = true;
                SharedStuff.checkAndUpdateVatRates(vatRate, lLVatRates, getResources());
                dontUpdate = false;
            } catch (Exception e) {
                e.printStackTrace();
                setEmpty();
                return;
            }
        }

        final double valueExclVat = priceInclVat / (1 + vatRate / 100.0);
        final double vatAmount = priceInclVat - valueExclVat;

        dataViewModel.setValueExclVat(valueExclVat);
        dataViewModel.setValueInclVat(priceInclVat);
        dataViewModel.setVatRate(vatRate);

        String finalPriceResult = SharedStuff.removeTrailingZeros(String.format(Locale.ENGLISH, "%.5f", valueExclVat));
        String finalVATResult = SharedStuff.removeTrailingZeros(String.format(Locale.ENGLISH, "%.5f", vatAmount));

        txtFinalPriceResult.setText(finalPriceResult);
        txtFinalPrice.setText(getString(R.string.price_exclusive_vat_output_text, vatRate));

        txtFinalVATResult.setText(finalVATResult);
        txtFinalVAT.setText(getString(R.string.vat_amount));

        txtFinalPriceResult.setVisibility(View.VISIBLE);
        txtFinalPrice.setVisibility(View.VISIBLE);
        txtFinalVATResult.setVisibility(View.VISIBLE);
        txtFinalVAT.setVisibility(View.VISIBLE);
    }

    private void setEmpty() {
        txtFinalPriceResult.setVisibility(View.INVISIBLE);
        txtFinalPrice.setVisibility(View.INVISIBLE);
        txtFinalVATResult.setVisibility(View.INVISIBLE);
        txtFinalVAT.setVisibility(View.INVISIBLE);
    }

}
