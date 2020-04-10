package se.arctosoft.vatcalculator.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import se.arctosoft.vatcalculator.R;
import se.arctosoft.vatcalculator.ui.data.CountryVAT;

public class AboutFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "AboutFragment";

    private TextView txtStandardRate, txtReducedRates;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        root.findViewById(R.id.btnRate).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=se.arctosoft.vatcalculator"))));

        txtStandardRate = root.findViewById(R.id.txtStandardRate);
        txtReducedRates = root.findViewById(R.id.txtReducedRates);

        hideRates();
        setupSpinner(root);
        return root;
    }

    private void hideRates() {
        txtStandardRate.setVisibility(View.GONE);
        txtReducedRates.setVisibility(View.GONE);
    }

    private void showRates() {
        txtStandardRate.setVisibility(View.VISIBLE);
        txtReducedRates.setVisibility(View.VISIBLE);
    }

    private void setupSpinner(View root) {
        Spinner spinner = root.findViewById(R.id.spinner);
        try {
            SharedStuff.setSpinner(requireActivity(), spinner);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            hideRates();
        } else {
            CountryVAT c = (CountryVAT) parent.getItemAtPosition(position);

            txtStandardRate.setText(getString(R.string.spinner_item_standard_rate, c.getStandardRate()));

            StringBuilder sb = new StringBuilder();
            for (Pair<String, Double> p : c.getReducedRates()) {
                sb.append(p.first).append(": ").append(p.second).append("%\n");
            }
            txtReducedRates.setText(getString(R.string.spinner_item_reduced_rates, sb.toString()));
            showRates();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        hideRates();
    }

}
