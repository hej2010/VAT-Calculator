package se.arctosoft.vatcalculator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

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
        String country = current == null ? "" : current.getCountry();
        isSwedishLocale = country.equals("SE");

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController(); //Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /*navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_reverse) {
                selectedFragment = new ReverseFragment();
            } else if (itemId == R.id.navigation_about) {
                selectedFragment = new AboutFragment();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            assert selectedFragment != null;
            transaction.replace(R.id.nav_host_fragment, selectedFragment);
            //transaction.addToBackStack(null);
            transaction.commit();
            return true;
        });*/
        /*navView.setOnNavigationItemReselectedListener(item -> {
        });*/
    }

}
