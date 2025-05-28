package com.example.demokat.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;

public class ButtonsMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buttons_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRec), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        finishAffinity();//cierra la app
    }

    public void logout(View view) {
        Intent intent = new Intent(ButtonsMenu.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void goToCancionero(View view){
        Intent intent = new Intent(this, CancioneroMenu.class);
        startActivity(intent);
    }

    public void goToMetro(View view){
        Intent intent = new Intent(this, Metronome.class);
        startActivity(intent);
    }
    public void goToMainMenu(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}