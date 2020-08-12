package se.arctosoft.vatcalculator.ui;

import android.content.res.Resources;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.arctosoft.vatcalculator.R;
import se.arctosoft.vatcalculator.ui.adapter.VatAdapter;
import se.arctosoft.vatcalculator.ui.data.CountryVAT;

import static se.arctosoft.vatcalculator.ui.data.DataViewModel.VAT_RATES;

class SharedStuff {
    private static final String VAT_DATA = "{\"success\":true,\"rates\":{\"AT\":{\"country_name\":\"Austria\",\"standard_rate\":20,\"reduced_rates\":{\"foodstuffs\":10,\"books\":10,\"pharmaceuticals\":10,\"passenger transport\":10,\"newspapers\":10,\"admission to cultural events\":10,\"hotels\":10,\"admission to entertainment events\":10}},\"BE\":{\"country_name\":\"Belgium\",\"standard_rate\":21,\"reduced_rates\":{\"restaurants\":12,\"foodstuffs\":6,\"books\":6,\"water\":6,\"pharmaceuticals\":6,\"medical\":6,\"newspapers\":6,\"hotels\":6,\"admission to cultural events\":6,\"admission to entertainment events\":6}},\"BG\":{\"country_name\":\"Bulgaria\",\"standard_rate\":20,\"reduced_rates\":{\"hotels\":9}},\"HR\":{\"country_name\":\"Croatia\",\"standard_rate\":25,\"reduced_rates\":{\"hotels\":13,\"newspapers\":13}},\"CY\":{\"country_name\":\"Cyprus\",\"standard_rate\":19,\"reduced_rates\":{\"hotels\":9,\"restaurants\":9,\"foodstuffs\":5,\"books\":5,\"pharmaceuticals\":5,\"medical\":5,\"passenger transport\":5,\"newspapers\":5,\"admission to cultural events\":5,\"admission to entertainment events\":5,\"admission to sporting events\":5}},\"CZ\":{\"country_name\":\"Czech Republic\",\"standard_rate\":21,\"reduced_rates\":{\"foodstuffs\":15,\"medical\":10,\"pharmaceuticals\":10,\"passenger transport\":15,\"newspapers\":15,\"admission to cultural events\":15,\"admission to sporting events\":15,\"admission to entertainment events\":15,\"hotels\":15,\"books\":10,\"baby foodstuffs\":10}},\"DK\":{\"country_name\":\"Denmark\",\"standard_rate\":25,\"reduced_rates\":null},\"EE\":{\"country_name\":\"Estonia\",\"standard_rate\":20,\"reduced_rates\":{\"books\":9,\"pharmaceuticals\":9,\"medical\":9,\"hotels\":9}},\"FI\":{\"country_name\":\"Finland\",\"standard_rate\":24,\"reduced_rates\":{\"foodstuffs\":14,\"restaurants\":14,\"books\":10,\"pharmaceuticals\":10,\"passenger transport\":10,\"newspapers\":10,\"admission to cultural events\":10,\"admission to sporting events\":10,\"admission to entertainment events\":10,\"hotels\":10}},\"FR\":{\"country_name\":\"France\",\"standard_rate\":20,\"reduced_rates\":{\"pharmaceuticals\":2.1,\"passenger transport\":10,\"admission to cultural events\":10,\"admission to sporting events\":10,\"admission to entertainment events\":10,\"hotels\":10,\"accommodation\":10,\"restaurants\":10,\"medical\":5.5,\"foodstuffs\":5.5,\"e-books\":5.5,\"books\":5.5,\"newspapers\":2.1}},\"DE\":{\"country_name\":\"Germany\",\"standard_rate\":16,\"reduced_rates\":{\"foodstuffs\":5,\"books\":5,\"medical\":5,\"passenger transport\":5,\"newspapers\":5,\"admission to cultural events\":5,\"admission to entertainment events\":5,\"hotels\":5}},\"GR\":{\"country_name\":\"Greece\",\"standard_rate\":24,\"reduced_rates\":{\"foodstuffs\":13,\"pharmaceuticals\":13,\"medical\":13,\"admission to cultural events\":13,\"admission to sporting events\":13,\"admission to entertainment events\":13,\"books\":6.5,\"newspapers\":6.5,\"hotels\":6.5}},\"HU\":{\"country_name\":\"Hungary\",\"standard_rate\":27,\"reduced_rates\":{\"foodstuffs\":18,\"hotels\":18,\"books\":5,\"pharmaceuticals\":5,\"medical\":5}},\"IE\":{\"country_name\":\"Ireland\",\"standard_rate\":23,\"reduced_rates\":{\"medical\":13.5,\"newspapers\":9,\"admission to cultural events\":13.5,\"admission to sporting events\":0,\"admission to entertainment events\":13.5,\"hotels\":13.5,\"restaurants\":13.5,\"foodstuffs\":4.8,\"books\":0,\"childrens clothing\":0}},\"IT\":{\"country_name\":\"Italy\",\"standard_rate\":22,\"reduced_rates\":{\"pharmaceuticals\":10,\"passenger transport\":10,\"admission to cultural events\":10,\"admission to entertainment events\":10,\"hotels\":10,\"restaurants\":10,\"foodstuffs\":4,\"medical\":4,\"books\":4,\"e-books\":4}},\"LV\":{\"country_name\":\"Latvia\",\"standard_rate\":21,\"reduced_rates\":{\"books\":12,\"pharmaceuticals\":12,\"medical\":12,\"newspapers\":12,\"hotels\":12}},\"LT\":{\"country_name\":\"Lithuania\",\"standard_rate\":21,\"reduced_rates\":{\"books\":9,\"pharmaceuticals\":5,\"medical\":5}},\"LU\":{\"country_name\":\"Luxembourg\",\"standard_rate\":17,\"reduced_rates\":{\"wine\":14,\"domestic fuel\":14,\"advertising\":14,\"bikes\":8,\"domestic services\":8,\"books\":3,\"foodstuffs\":3,\"pharmaceuticals\":3,\"medical\":3,\"passenger transport\":3,\"newspapers\":3,\"admission to cultural events\":3,\"admission to sporting events\":3,\"admission to entertainment events\":3,\"hotels\":3,\"restaurants\":3,\"e-books\":3}},\"MT\":{\"country_name\":\"Malta\",\"standard_rate\":18,\"reduced_rates\":{\"hotels\":7,\"books\":5,\"medical\":5,\"newspapers\":5,\"admission to cultural events\":5,\"e-books\":5,\"foodstuffs\":0,\"pharmaceuticals\":0}},\"NL\":{\"country_name\":\"Netherlands\",\"standard_rate\":21,\"reduced_rates\":{\"foodstuffs\":9,\"books\":9,\"pharmaceuticals\":9,\"medical\":9,\"passenger transport\":9,\"admission to cultural events\":9,\"admission to entertainment events\":9,\"hotels\":9,\"accommodation\":9}},\"PL\":{\"country_name\":\"Poland\",\"standard_rate\":23,\"reduced_rates\":{\"pharmaceuticals\":8,\"medical\":8,\"passenger transport\":8,\"newspapers\":8,\"hotels\":8,\"restaurants\":8,\"admission to cultural events\":8,\"admission to sporting events\":8,\"admission to entertainment events\":8,\"foodstuffs\":5}},\"PT\":{\"country_name\":\"Portugal\",\"standard_rate\":23,\"reduced_rates\":{\"foodstuffs\":6,\"agricultural supplies\":13,\"books\":6,\"pharmaceuticals\":6,\"medical\":6,\"newspapers\":6,\"hotels\":6,\"passenger transport\":6}},\"RO\":{\"country_name\":\"Romania\",\"standard_rate\":19,\"reduced_rates\":{\"books\":9,\"pharmaceuticals\":9,\"medical\":9,\"newspapers\":9,\"admission to cultural events\":9,\"admission to entertainment events\":9,\"hotels\":9,\"foodstuffs\":9,\"social housing\":5}},\"SK\":{\"country_name\":\"Slovakia\",\"standard_rate\":20,\"reduced_rates\":{\"books\":10,\"foodstuffs\":10,\"medical\":10,\"pharmaceuticals\":10,\"admission to cultural events\":10,\"admission to entertainment events\":10}},\"SI\":{\"country_name\":\"Slovenia\",\"standard_rate\":22,\"reduced_rates\":{\"foodstuffs\":9.5,\"books\":9.5,\"pharmaceuticals\":9.5,\"medical\":9.5,\"newspapers\":9.5,\"admission to sporting events\":9.5,\"admission to cultural events\":9.5,\"admission to entertainment events\":9.5,\"hotels\":9.5}},\"ES\":{\"country_name\":\"Spain\",\"standard_rate\":21,\"reduced_rates\":{\"medical\":10,\"pharmaceuticals\":10,\"passenger transport\":10,\"admission to cultural events\":10,\"admission to sporting events\":10,\"admission to entertainment events\":10,\"foodstuffs\":4,\"newspapers\":4}},\"SE\":{\"country_name\":\"Sweden\",\"standard_rate\":25,\"reduced_rates\":{\"foodstuffs\":12,\"books\":6}},\"GB\":{\"country_name\":\"United Kingdom\",\"standard_rate\":20,\"reduced_rates\":{\"property renovations\":5,\"foodstuffs\":0,\"books\":0,\"pharmaceuticals\":0,\"medical\":0,\"passenger transport\":0,\"newspapers\":0,\"childrens clothing\":0}}}}";
    private static final String[] COUNTRY_CODES = {"AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IE", "IT", "LV", "LT",
            "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "GB"};
    // API Key: 4111154764beda200e7ea14ffac2dde7
    // http://apilayer.net/api/rate_list?access_key=4111154764beda200e7ea14ffac2dde7&format=0

    private static List<CountryVAT> countryVATS = null;

    static boolean checkAndUpdateVatRates(Integer vatRate, LinearLayout lLVatRates, Resources resources) {
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

    static String removeTrailingZeros(@NonNull String s) {
        return s.contains(".") ? s.replaceAll("0*$", "").replaceAll("\\.$", "") : s;
    }

    static void setSpinner(FragmentActivity activity, Spinner spinner) throws JSONException {
        VatAdapter adapter = new VatAdapter(activity, R.layout.spinner_item, getVatArray(activity), activity.getResources());
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private static List<CountryVAT> getVatArray(FragmentActivity activity) throws JSONException {
        if (countryVATS == null) {
            JSONObject o = new JSONObject(VAT_DATA);
            JSONObject rates = o.getJSONObject("rates");
            countryVATS = new ArrayList<>();

            for (String s : COUNTRY_CODES) {
                if (!rates.isNull(s)) {
                    try {
                        countryVATS.add(CountryVAT.fromJson(s, rates.getJSONObject(s), activity));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return countryVATS;
    }
}
