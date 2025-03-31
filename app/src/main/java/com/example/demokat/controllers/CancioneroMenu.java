package com.example.demokat.controllers;

import static com.example.demokat.controllers.MainMenu.instrumento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;
import com.example.demokat.adapters.AdapterPersonalizado2;
import com.example.demokat.database.UsuarioDAO;

import java.util.List;

public class CancioneroMenu extends AppCompatActivity {
    ListView  list_Rec;
    ListView list_Cancion;
    public static String cancion_titulo;
    SearchView searchView;

    public static String getCancion_titulo() {
        return cancion_titulo;
    }

    UsuarioDAO usuarioDAO = MainActivity.getUsuarioDAO();
    private int user_id = MainActivity.getUser_id();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cancionero_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        list_Cancion = findViewById(R.id.listCanciones);
        crearLista2();
        searchView = findViewById(R.id.search_id);
        searchView.setIconified(false);  // Deshabilita el comportamiento de "iconificación" de la lupa.
        searchView.setFocusable(true);   // Asegura que pueda recibir foco.
        searchView.setClickable(true);   // Asegura que sea clicable en toda su extensión.
        searchView.setFocusableInTouchMode(true);  // Asegura que se pueda enfocar al tocar.
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());

    }


    public void nuevaCancion(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText finalInput = new EditText(this);
        builder.setTitle("Nueva Canción")
                .setMessage("Escriba un titulo para su canción")
                .setView(finalInput)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = finalInput.getText().toString().trim();
                        if (!userInput.isEmpty()) {
                            int i = usuarioDAO.checkCancion(user_id, userInput);
                            if (i == 0) {
                                Toast.makeText(CancioneroMenu.this, "Ya tienes una canción con ese título", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(CancioneroMenu.this, "Nueva Canción: " + userInput, Toast.LENGTH_SHORT).show();
                                usuarioDAO.insertCancion(userInput,user_id);
                                crearLista2();

                            }


                        } else {

                            Toast.makeText(CancioneroMenu.this, "No se ingresó texto", Toast.LENGTH_SHORT).show();
                        }
                    }
                })

                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Simplemente cerrar el diálogo
                    }
                });

        // Mostrar el AlertDialog
        builder.create().show();

    }


    public void crearLista2(){


        String[] titulos=  usuarioDAO.selectAllTitulosCanciones(user_id);
        int [] images = new int[titulos.length];

        for (int i = 0; i < titulos.length; i++) {
            images[i] = R.drawable.dino_click;
        }

        AdapterPersonalizado2 adapter2 = new AdapterPersonalizado2(this, images, titulos);
        list_Cancion.setAdapter(adapter2);
        list_Cancion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cancion_titulo = titulos[position];
                    Intent intent = new Intent(CancioneroMenu.this, Canciones_Menu.class);
                    startActivity(intent);



            }
        });

        list_Cancion.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CancioneroMenu.this);
                builder.setTitle("¡Alerta!")
                        .setMessage("¿Deseas eliminar esta cancion?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CancioneroMenu.this, "Canción eliminada", Toast.LENGTH_SHORT).show();
                            crearLista2();


                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                builder.create().show();

                return true;
            } });
    }


}