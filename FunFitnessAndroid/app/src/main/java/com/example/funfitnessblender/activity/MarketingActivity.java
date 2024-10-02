package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.funfitnessblender.AddMarketingFragment;
import com.example.funfitnessblender.MarketingListFragment;
import com.example.funfitnessblender.R;
import com.example.funfitnessblender.databinding.ActivityMainBinding;
import com.example.funfitnessblender.databinding.ActivityMarketingBinding;
import com.example.funfitnessblender.databinding.ActivityMeettingBinding;

public class MarketingActivity extends AppCompatActivity {



    ActivityMarketingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarketingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AddMarketingFragment());

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.addmarketing) {
                replaceFragment(new AddMarketingFragment());
            } else if (itemId == R.id.marketinglist) {
                replaceFragment(new MarketingListFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}