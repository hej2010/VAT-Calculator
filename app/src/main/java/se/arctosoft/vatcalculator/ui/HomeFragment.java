package se.arctosoft.vatcalculator.ui;

import static se.arctosoft.vatcalculator.ui.data.DataViewModel.VAT_RATES;

import android.content.Context;
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

import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

import se.arctosoft.vatcalculator.MainActivity;
import se.arctosoft.vatcalculator.R;
import se.arctosoft.vatcalculator.ui.data.DataViewModel;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private DataViewModel dataViewModel;

    private EditText eTValExcl, eTValVatRate;
    private TextView txtFinalPrice, txtFinalPriceResult, txtFinalVAT, txtFinalVATResult;
    private LinearLayout lLVatRates;

    private boolean dontUpdate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        eTValExcl = root.findViewById(R.id.eTValExcl);
        eTValVatRate = root.findViewById(R.id.eTValVatRate);
        txtFinalPrice = root.findViewById(R.id.txtFinalPrice);
        txtFinalPriceResult = root.findViewById(R.id.txtFinalPriceResult);
        txtFinalVAT = root.findViewById(R.id.txtFinalVAT);
        txtFinalVATResult = root.findViewById(R.id.txtFinalVATResult);
        lLVatRates = root.findViewById(R.id.lLVatRates);
        dontUpdate = false;

        attachVatRates(requireContext(), lLVatRates, eTValVatRate);
        setEmpty();
        attachListeners();
        checkStoredValues();

        return root;
    }

    private void checkStoredValues() {
        dontUpdate = true;
        boolean set = false;
        Double priceExc = dataViewModel.getLiveValueExclVat();
        Double vatRate = dataViewModel.getVatRate();

        if (priceExc != null && priceExc != 0) {
            eTValExcl.setText(String.valueOf(priceExc));
            set = true;
        }

        if (vatRate != null && vatRate != 0) {
            eTValVatRate.setText(String.valueOf(vatRate));
            set = true;
        }

        if (set && vatRate != null) {
            if (SharedStuff.checkAndUpdateVatRates(vatRate, lLVatRates, getResources())) {
                dontUpdate = false;
            }
        }
        dontUpdate = false;
        update();
    }

    public static void attachVatRates(Context context, LinearLayout lLVatRates, EditText eTValVatRate) {
        LayoutInflater inflater = LayoutInflater.from(context);
        for (double vatRate : VAT_RATES) {
            CardView layout = (CardView) inflater.inflate(R.layout.vat_rate_item, lLVatRates, false);
            layout.setTag(vatRate);

            TextView textView = layout.findViewById(R.id.txtVatRate);
            textView.setText(context.getString(R.string.percentage_placeholder, SharedStuff.doubleToString(vatRate)));

            layout.setOnClickListener(v -> {
                for (int i = 0; i < VAT_RATES.length; i++) {
                    ((MaterialCardView) lLVatRates.getChildAt(i)).setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                }
                if (v instanceof CardView) {
                    ((MaterialCardView) v).setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                }
                eTValVatRate.setText(String.valueOf(v.getTag()));
            });
            lLVatRates.addView(layout);
        }
        if (MainActivity.isSwedishLocale) {
            lLVatRates.getChildAt(DataViewModel.VAT_SE_POS).performClick();
        } else {
            lLVatRates.getChildAt(DataViewModel.VAT_DEFAULT_POS).performClick();
        }
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
        double priceExclVat, vatRate;
        String sPriceExclVat = eTValExcl.getText().toString();
        String sVatRate = eTValVatRate.getText().toString();
        if (sVatRate.endsWith(".") || sVatRate.endsWith(",")) {
            return;
        }
        if (sPriceExclVat.isEmpty() || sVatRate.isEmpty()) {
            setEmpty();
            try {
                double vat = Double.parseDouble(sVatRate);
                dataViewModel.setVatRate(vat);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else {
            try {
                if (sPriceExclVat.endsWith(".") || sPriceExclVat.endsWith(",")) {
                    sPriceExclVat = sPriceExclVat.replace(".", "");
                }
                priceExclVat = Double.parseDouble(sPriceExclVat);
            } catch (Exception e) {
                e.printStackTrace();
                setEmpty();
                return;
            }
            try {
                vatRate = Double.parseDouble(sVatRate);
            } catch (Exception e) {
                e.printStackTrace();
                setEmpty();
                return;
            }
        }

        final double vatAmount = priceExclVat * (vatRate / 100.0);
        final double valueIncVat = priceExclVat + vatAmount;
        dataViewModel.setValueExclVat(priceExclVat);
        dataViewModel.setValueInclVat(valueIncVat);
        dataViewModel.setVatRate(vatRate);

        String finalPriceResult = SharedStuff.removeTrailingZeros(String.format(Locale.ENGLISH, "%.5f", valueIncVat));
        String finalVATResult = SharedStuff.removeTrailingZeros(String.format(Locale.ENGLISH, "%.5f", vatAmount));

        txtFinalPriceResult.setText(finalPriceResult);
        txtFinalPrice.setText(getString(R.string.price_inclusive_vat_output_text, SharedStuff.doubleToString(vatRate)));

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
