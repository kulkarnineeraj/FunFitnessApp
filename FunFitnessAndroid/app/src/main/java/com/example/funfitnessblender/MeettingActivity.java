package com.example.funfitnessblender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.funfitnessblender.databinding.ActivityMainBinding;
import com.example.funfitnessblender.databinding.ActivityMeettingBinding;

public class MeettingActivity extends AppCompatActivity {



    ActivityMeettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AddMeettingFragment());

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.addmeeting) {
                replaceFragment(new AddMeettingFragment());
            } else if (itemId == R.id.meetinglist) {
                replaceFragment(new MeettingListFragment());
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