package com.example.schedulemessenger.View;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.schedulemessenger.R;
import com.example.schedulemessenger.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting the home screen (MainActivity), using ViewBinding
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View homeView = activityMainBinding.getRoot();
        setContentView(homeView);

        NavController navController =Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNav, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.dashBoardFragment || destination.getId() == R.id.settingsFragment
                        || destination.getId() == R.id.historyFragment) {
                    activityMainBinding.bottomNav.setVisibility(View.VISIBLE);
                } else {
                    activityMainBinding.bottomNav.setVisibility(View.GONE);
                }
            }
        });
    }
}