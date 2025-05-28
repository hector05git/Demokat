package com.example.demokat.controllers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ButtonsMenu.this);
        builder.setTitle(getString(R.string.alerta))
                .setMessage(getString(R.string.cerrar_sesion))
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ButtonsMenu.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                })
                .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        builder.create().show();

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