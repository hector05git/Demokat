package com.example.demokat.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.demokat.R;
import com.example.demokat.adapters.AdapterPersonalizado;

public class MainMenu extends AppCompatActivity {
    private ListView listView;
    public static String instrumento;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = findViewById(R.id.listRec);

        crearLista();



    }



    public void crearLista() {
        int[] images = {R.drawable.dino_click, R.drawable.dino_click, R.drawable.dino_click, R.drawable.dino_click, R.drawable.dino_click};
        String[] titulos = {"Guitarra", "Voz", "Batería", "Bajo", "Notas"};


        AdapterPersonalizado adapter = new AdapterPersonalizado(this, images, titulos);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (titulos[position].equals("Guitarra")) {
                    instrumento = "Guitarra";
                    Intent intent = new Intent(MainMenu.this, InstrumentMenu.class);
                    startActivity(intent);
                }
                if (titulos[position].equals("Voz")) {
                    instrumento = "Voz";
                    Intent intent = new Intent(MainMenu.this, InstrumentMenu.class);
                    startActivity(intent);
                }
                if (titulos[position].equals("Batería")) {
                    instrumento = "Batería";
                    Intent intent = new Intent(MainMenu.this, InstrumentMenu.class);
                    startActivity(intent);
                }
                if (titulos[position].equals("Bajo")) {
                    instrumento = "Bajo";
                    Intent intent = new Intent(MainMenu.this, InstrumentMenu.class);
                    startActivity(intent);
                }
                if (titulos[position].equals("Notas")) {
                    instrumento = "Notas";
                    Intent intent = new Intent(MainMenu.this, InstrumentMenu.class);
                    startActivity(intent);
                }
            }
        });


    }

    public static String getInstrumento() {
        return instrumento;
    }
}