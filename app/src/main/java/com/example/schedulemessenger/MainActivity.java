package com.example.schedulemessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schedulemessenger.databinding.ActivityMainBinding;
import com.example.schedulemessenger.databinding.FragmentDashBoardBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting the home screen (MainActivity), using ViewBinding
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View homeView = activityMainBinding.getRoot();
        setContentView(homeView);

    }
}