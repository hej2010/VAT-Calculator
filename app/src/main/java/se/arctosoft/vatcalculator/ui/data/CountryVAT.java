package se.arctosoft.vatcalculator.ui.data;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import se.arctosoft.vatcalculator.BuildConfig;

public class CountryVAT {
    private static final String TAG = "CountryVAT";
    private final String countryName;
    private final double standardRate;
    private final int drawableID;
    private final List<Pair<String, Double>> reducedRates;

    private CountryVAT(String countryName, double standardRate, int drawableID, List<Pair<String, Double>> reducedRates) {
        this.countryName = countryName;
        this.standardRate = standardRate;
        this.drawableID = drawableID;
        this.reducedRates = reducedRates;
        //Log.e(TAG, "CountryVAT: " + countryName + ", " + standardRate + ", " + Arrays.toString(reducedRates.toArray()));
    }

    public static CountryVAT fromJson(@NonNull String countryCode, @NonNull JSONObject o, @NonNull Context context) throws JSONException {
        int drawableID = context.getResources().getIdentifier(countryCode.toLowerCase(Locale.ENGLISH), "drawable", BuildConfig.APPLICATION_ID);
        String countryName = o.getString("country_name");
        double standardRate = o.getDouble("standard_rate");
        List<Pair<String, Double>> reducedRates = new ArrayList<>();

        if (!o.isNull("reduced_rates")) {
            JSONObject rr = o.getJSONObject("reduced_rates");
            for (Iterator<String> it = rr.keys(); it.hasNext(); ) {
                String key = it.next();
                //Log.e("FromJson", "fromJson: " + key);
                try {
                    reducedRates.add(new Pair<>(key, rr.getDouble(key)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new CountryVAT(countryName, standardRate, drawableID, reducedRates);
    }

    public String getCountryName() {
        return countryName;
    }

    public double getStandardRate() {
        return standardRate;
    }

    public List<Pair<String, Double>> getReducedRates() {
        return reducedRates;
    }

    public int getDrawableID() {
        return drawableID;
    }

    @NonNull
    @Override
    public String toString() {
        return countryName;
    }
}
