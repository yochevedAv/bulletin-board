package com.example.bulletinboard;//package com.example.bulletinboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bulletinboard.ui.home.HomeFragment;
import com.example.bulletinboard.ui.post.PostFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    private Fragment fragmentHome;
    private Fragment fragmentPost;

    private String myLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragmentHome = new HomeFragment();
        fragmentPost = new PostFragment();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.app_bar_layout);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setFragment(fragmentHome);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.navigation_home) {
                        setFragment(fragmentHome);
                        return true;
                    } else if (itemId == R.id.navigation_post) {
                        setFragment(fragmentPost);
                        return true;
                    }
                    return false;
                }
        );
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
