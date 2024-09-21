package com.example.googlebus;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    boolean doubletap = false;
    BottomNavigationView bottomNavigationView;

    SharedPreferences preferences; // used to store the temp data
    SharedPreferences.Editor editor; //used to put or edit the temp data

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));


        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        editor = preferences.edit();

        boolean firstTime = preferences.getBoolean("isFirstTime", true);

        if (firstTime) {

            welcome();
        }

        bottomNavigationView = findViewById(R.id.homeBottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.HomeMenuBottomNavigation);
    }

    private void welcome() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("Google Bus");
        ad.setMessage("Welcome to Google Bus ");
        ad.setPositiveButton("Thank You",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
        editor.putBoolean("isFirstTime", false).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuHomeLogout) {
            lagout();
        }
        else if (item.getItemId() == R.id.homeMenuMyProfile) {
            Intent intent = new Intent(HomeActivity.this,Account_Profile_Activity.class);
            startActivity(intent);
        } 
        return true;
    }

    private void lagout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("Google Bus");
        ad.setMessage("Are you sure you want to logout ?");
        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                editor.putBoolean("isLogin", false).commit();
                startActivity(i);
            }
        }).create().show();
    }

    HomeFragment homeFragment = new HomeFragment();
    BookingsFragment bookingsFragment = new BookingsFragment();
    NotificationFragment notificationFragment = new NotificationFragment();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.HomeMenuBottomNavigation) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, homeFragment).commit();

        } else if (item.getItemId() == R.id.BookingMenuBottomNavigation) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, bookingsFragment).commit();

        } else if (item.getItemId() == R.id.NotificationMenuBottomNavigation) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, notificationFragment).commit();

        }
        return false;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (doubletap) {
            finishAffinity();
        } else {
            Toast.makeText(HomeActivity.this, "Press Again to Exit App", Toast.LENGTH_SHORT).show();
            doubletap = true;
            Handler h = new Handler();
            h.postDelayed(new Runnable() {

                @Override

                public void run() {
                    doubletap = false;
                }

            }, 2000);
        }

    }
}
