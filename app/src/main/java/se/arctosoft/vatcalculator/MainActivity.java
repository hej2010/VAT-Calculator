package se.arctosoft.vatcalculator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import se.arctosoft.vatcalculator.ui.AboutFragment;
import se.arctosoft.vatcalculator.ui.HomeFragment;
import se.arctosoft.vatcalculator.ui.ReverseFragment;

public class MainActivity extends AppCompatActivity {
    public static boolean isSwedishLocale;

    // TODO https://stackoverflow.com/questions/14810348/fragment-is-not-being-replaced-but-put-on-top-of-the-previous-one

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_reverse, R.id.navigation_about)
                .build();

        Locale current = ConfigurationCompat.getLocales(getResources().getConfiguration()).get(0);
        String country = current.getCountry();
        isSwedishLocale = country.equals("SE");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_reverse:
                    selectedFragment = new ReverseFragment();
                    break;
                case R.id.navigation_about:
                    selectedFragment = new AboutFragment();
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            assert selectedFragment != null;
            transaction.replace(R.id.nav_host_fragment, selectedFragment);
            //transaction.addToBackStack(null);
            transaction.commit();
            return true;
        });
        navView.setOnNavigationItemReselectedListener(item -> {
        });
    }

}
