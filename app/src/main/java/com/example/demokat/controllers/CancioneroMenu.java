package com.example.demokat.controllers;

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
import com.example.demokat.database.DAO;

public class CancioneroMenu extends AppCompatActivity {
    ListView  list_Rec;
    ListView list_Cancion;
    public static String cancion_titulo;
    SearchView searchView;

    public static String getCancion_titulo() {
        return cancion_titulo;
    }

    DAO usuarioDAO = MainActivity.getUsuarioDAO();
    private int user_id = MainActivity.getUser_id();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cancionero_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRec), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        list_Cancion = findViewById(R.id.listCanciones);
        crearLista2();
        searchView = findViewById(R.id.search_id);
        searchView.setIconified(false);  //TODO ESTO ES PARA QUE LA BARRA NAVEGADORA ESTÉ MÁS BONITA
        searchView.setClickable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String[] titulos = usuarioDAO.searchTitulosCanciones(user_id, newText);
                int[] images = new int[titulos.length];
                for (int i = 0; i < titulos.length; i++) {
                    images[i] = R.drawable.addtosong;
                }

                AdapterPersonalizado2 adapter2 = new AdapterPersonalizado2(CancioneroMenu.this, images, titulos);
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
                        cancion_titulo = titulos[position];
                        AlertDialog.Builder builder = new AlertDialog.Builder(CancioneroMenu.this);
                        builder.setTitle(getString(R.string.alerta))
                                .setMessage(getString(R.string.eliminar_cancion))
                                .setPositiveButton(getString(R.string.eliminar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        usuarioDAO.deleteCancion(user_id, cancion_titulo);
                                        Toast.makeText(CancioneroMenu.this, getString(R.string.cancion_eliminada), Toast.LENGTH_SHORT).show();
                                        crearLista2();
                                        searchView.clearFocus();
                                        searchView.setQuery("", false);


                                    }
                                })
                                .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });

                        builder.create().show();

                        return true;
                    }
                });
                return false;
            }});}









    public void nuevaCancion(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText finalInput = new EditText(this);
        builder.setTitle(getString(R.string.nueva_cancion))
                .setMessage(getString(R.string.titulo_cancion))
                .setView(finalInput)

                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = finalInput.getText().toString().trim();
                        if (!userInput.isEmpty()) {
                            int i = usuarioDAO.checkCancion(user_id, userInput);
                            if (i == 0) {
                                Toast.makeText(CancioneroMenu.this, getString(R.string.cancion_existente), Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(CancioneroMenu.this, getString(R.string.nueva_cancion) +": " + userInput, Toast.LENGTH_SHORT).show();
                                usuarioDAO.insertCancion(userInput,user_id);
                                crearLista2();

                            }


                        } else {

                            Toast.makeText(CancioneroMenu.this, getString(R.string.no_ingreso), Toast.LENGTH_SHORT).show();
                        }
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


    public void crearLista2(){


        String[] titulos=  usuarioDAO.selectAllTitulosCanciones(user_id);
        int [] images = new int[titulos.length];

        for (int i = 0; i < titulos.length; i++) {
            images[i] = R.drawable.addtosong;
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
                cancion_titulo = titulos[position];
                AlertDialog.Builder builder = new AlertDialog.Builder(CancioneroMenu.this);
                builder.setTitle(getString(R.string.alerta))
                        .setMessage(getString(R.string.eliminar_cancion))
                        .setPositiveButton(getString(R.string.eliminar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                usuarioDAO.deleteCancion(user_id, cancion_titulo);
                            Toast.makeText(CancioneroMenu.this, getString(R.string.cancion_eliminada), Toast.LENGTH_SHORT).show();
                            crearLista2();


                            }
                        })
                        .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                builder.create().show();

                return true;
            } });
    }
    public void onResume(){
        super.onResume();
        crearLista2();
        searchView.clearFocus();//ESTO ES PARA QUE EL TECLADO NO APAREZCA AUTOMATICAMENTE AL VOLVER
        searchView.setQuery("",false);
    }

}