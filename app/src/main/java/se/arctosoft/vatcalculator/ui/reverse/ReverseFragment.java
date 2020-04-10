package se.arctosoft.vatcalculator.ui.reverse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import se.arctosoft.vatcalculator.R;

public class ReverseFragment extends Fragment {
    private ReverseViewModel reverseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        reverseViewModel = new ViewModelProvider(this).get(ReverseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reverse, container, false);
        /*final TextView textView = root.findViewById(R.id.txtTitle);
        reverseViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}
