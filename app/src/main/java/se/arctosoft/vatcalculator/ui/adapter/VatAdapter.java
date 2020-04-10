package se.arctosoft.vatcalculator.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import se.arctosoft.vatcalculator.R;
import se.arctosoft.vatcalculator.ui.data.CountryVAT;

public class VatAdapter extends ArrayAdapter<CountryVAT> {
    private Context context;
    private List<CountryVAT> statuses;
    public Resources res;
    private LayoutInflater inflater;

    public VatAdapter(Context context, int textViewResourceId, List<CountryVAT> statuses, Resources resLocal) {
        super(context, textViewResourceId, statuses);
        this.context = context;
        this.statuses = statuses;
        this.res = resLocal;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_item, parent, false);
        ImageView imgFlag = row.findViewById(R.id.imgFlag);
        TextView txtName = row.findViewById(R.id.txtName);
        if (position == 0) {
            txtName.setText(context.getString(R.string.spinner_default_text));
            imgFlag.setImageDrawable(null);
        } else {
            CountryVAT country = statuses.get(position - 1);
            txtName.setText(country.getCountryName());
            imgFlag.setImageResource(country.getDrawableID());
        }

        return row;
    }
}
