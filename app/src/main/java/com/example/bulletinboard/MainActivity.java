package com.example.bulletinboard;//package com.example.bulletinboard;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.Menu;
//
//import com.example.bulletinboard.ui.post.PostFragment;
//import com.example.bulletinboard.ui.home.HomeFragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.android.material.navigation.NavigationView;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.bulletinboard.databinding.ActivityMainBinding;
//
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class MainActivity extends AppCompatActivity {
//
//    private AppBarConfiguration mAppBarConfiguration;
//    private ActivityMainBinding binding;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
////        DrawerLayout drawer = binding.drawerLayout;
////        NavigationView navigationView = binding.navView;
////        // Passing each menu ID as a set of Ids because each
////        // menu should be considered as top level destinations.
////        mAppBarConfiguration = new AppBarConfiguration.Builder(
////                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
////                .setOpenableLayout(drawer)
////                .build();
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
////        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
////        NavigationUI.setupWithNavController(navigationView, navController);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.nav_home) {
//                Fragment fragment = new HomeFragment(); // Replace with the appropriate fragment
//                transaction.replace(R.id.nav_host_fragment_content_main, fragment);
//                transaction.commit();
//                return true;
//            } else if (item.getItemId() == R.id.nav_post) {
//                Fragment fragment = new PostFragment(); // Replace with the appropriate fragment
//                transaction.replace(R.id.nav_host_fragment_content_main, fragment);
//                transaction.commit();
//                return true;
//            }
//            return true;
//        });
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//}

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bulletinboard.ui.post.PostFragment;
import com.example.bulletinboard.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    // Define your fragments
    private Fragment fragmentHome;
    private Fragment fragmentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize fragments
        fragmentHome = new HomeFragment();
        fragmentPost = new PostFragment();

        // Initialize the AppBar (Toolbar)
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the AppBarLayout
        appBarLayout = findViewById(R.id.app_bar_layout);

        // Initialize the BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the default fragment
        setFragment(fragmentHome);

        // Handle item selection from the BottomNavigationView
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

    // Helper method to set the fragment
    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
