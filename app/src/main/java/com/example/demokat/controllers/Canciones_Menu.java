package com.example.demokat.controllers;

import static com.example.demokat.controllers.MainMenu.instrumento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;
import com.example.demokat.adapters.AdapterPersonalizado3;
import com.example.demokat.database.DAO;

import java.util.Objects;

public class Canciones_Menu extends AppCompatActivity {
    private String cancion_titulo = CancioneroMenu.getCancion_titulo();
    private TextView textView;
    DAO usuarioDAO = MainActivity.getUsuarioDAO();
    private int user_id = MainActivity.getUser_id();
    ListView list_Rec;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_canciones_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRec), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchView = findViewById(R.id.search_id);
        searchView.setIconified(false); //TODO ESTO ES PARA QUE LA BARRA NAVEGADORA ESTÉ MÁS BONITA
        searchView.setFocusable(true);
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
                String[] titulos = usuarioDAO.searchTitulosRecCanciones( cancion_titulo, user_id, newText);
                String[] instrumentos=usuarioDAO.searchInstrumentosCanciones(cancion_titulo, user_id, newText);
                String[] titulos2 = usuarioDAO.searchTitulosNotasCancion(cancion_titulo, user_id, newText);

                String[] notas = new String[titulos2.length];

                for (int i = 0; i < titulos2.length; i++) {
                    notas[i] =String.valueOf("Notas");
                }
                String[] combinados = new String[titulos.length + titulos2.length];

                int j = 0;
                for (String titulo : titulos) {
                    combinados[j++] = titulo;
                }
                for (String titulo : titulos2) {
                    combinados[j++] = titulo;
                }




                String[] combinados2 = new String[instrumentos.length + notas.length];

                int k = 0;
                for (String instrumento : instrumentos) {
                    combinados2[k++] = instrumento;
                }

                for (String titulo : notas) {
                    combinados2[k++] = titulo;
                }




                int [] images = new int[combinados.length];

                for (int i = 0; i < combinados.length; i++) {
                    images[i] = R.drawable.dino_click;
                }






                AdapterPersonalizado3 adapter = new AdapterPersonalizado3(Canciones_Menu.this, images, combinados, combinados2);
                list_Rec.setAdapter(adapter);
                String[] finalTitulos = combinados;
                list_Rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        InstrumentMenu.title = finalTitulos[position];
                        instrumento = combinados2[position];

                        if(instrumento.equals("Notas")){
                            InstrumentMenu.notaMode="edit";
                            Intent intent = new Intent(Canciones_Menu.this, Notas.class);
                            startActivity(intent);
                        }else {

                            Intent intent = new Intent(Canciones_Menu.this, InstrumentPlay.class);
                            startActivity(intent);
                        }
                    }


                });

                list_Rec.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setPressed(false);

                        InstrumentMenu.title = finalTitulos[position];
                        instrumento = combinados2[position];
                        AlertDialog.Builder builder = new AlertDialog.Builder(Canciones_Menu.this);
                        builder.setTitle(getString(R.string.alerta))
                                .setMessage(getString(R.string.deseas_retirar))
                                .setPositiveButton(getString(R.string.retirar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(instrumento.equals("Notas")){
                                            String cancion = usuarioDAO.checkNotasCancion(InstrumentMenu.title, user_id);
                                            usuarioDAO.deleteNotasCancion( cancion,InstrumentMenu.title, user_id);
                                            Toast.makeText(Canciones_Menu.this, getString(R.string.nota_retirada), Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            crearLista1();
                                            searchView.clearFocus();
                                            searchView.setQuery("", false);
                                        }else {
                                            String cancion = usuarioDAO.checkRecCancion(InstrumentMenu.title, instrumento, user_id);
                                            usuarioDAO.deleteRecCancion( cancion,InstrumentMenu.title, user_id,instrumento);
                                            Toast.makeText(Canciones_Menu.this, getString(R.string.audio_retirado), Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            crearLista1();
                                            searchView.clearFocus();
                                            searchView.setQuery("", false);
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

                        return true;
                    } });


                return false;

            }

        });




        textView = findViewById(R.id.CancionTitulo_tv);
        textView.setText(cancion_titulo);
        list_Rec = findViewById(R.id.listView);
        crearLista1();



    }

    public void crearLista1(){

        String[] titulos = usuarioDAO.selectAllTitulosRecCanciones( cancion_titulo, user_id);
        String[] instrumentos=usuarioDAO.selectAllInstrumentosCanciones(cancion_titulo, user_id);

        String[] titulos2 = usuarioDAO.selectAllTitulosNotasCancion(cancion_titulo, user_id);

        String[] notas = new String[titulos2.length];

        for (int i = 0; i < titulos2.length; i++) {
            notas[i] ="Notas";
        }


        String[] combinados = new String[titulos.length + titulos2.length];

        int j = 0;
        for (String titulo : titulos) {
            combinados[j++] = titulo;
        }
        for (String titulo : titulos2) {
            combinados[j++] = titulo;
        }




        String[] combinados2 = new String[instrumentos.length + notas.length];

        int k = 0;
        for (String instrumento : instrumentos) {
            combinados2[k++] = instrumento;
        }

        for (String titulo : notas) {
            combinados2[k++] = titulo;
        }




        int [] images = new int[combinados.length];

        for (int i = 0; i < combinados.length; i++) {
            images[i] = R.drawable.dino_click;
        }






       AdapterPersonalizado3 adapter = new AdapterPersonalizado3(this, images, combinados, combinados2);
        list_Rec.setAdapter(adapter);
        String[] finalTitulos = combinados;
        list_Rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InstrumentMenu.title = finalTitulos[position];
                instrumento = combinados2[position];

                if(Objects.equals(instrumento, String.valueOf("Notas"))){
                    InstrumentMenu.notaMode="edit";
                    Intent intent = new Intent(Canciones_Menu.this, Notas.class);
                    startActivity(intent);
                }else {

                    Intent intent = new Intent(Canciones_Menu.this, InstrumentPlay.class);
                    startActivity(intent);
                }
            }


        });

        list_Rec.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.setPressed(false);

                InstrumentMenu.title = finalTitulos[position];
                instrumento = combinados2[position];
                AlertDialog.Builder builder = new AlertDialog.Builder(Canciones_Menu.this);
                builder.setTitle(getString(R.string.alerta))
                        .setMessage(getString(R.string.deseas_retirar))
                        .setPositiveButton(getString(R.string.retirar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(instrumento.equals(String.valueOf("Notas"))){
                                    String cancion = usuarioDAO.checkNotasCancion(InstrumentMenu.title, user_id);
                                    usuarioDAO.deleteNotasCancion( cancion,InstrumentMenu.title, user_id);
                                    Toast.makeText(Canciones_Menu.this, getString(R.string.nota_retirada), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    crearLista1();
                                }else {
                                    String cancion = usuarioDAO.checkRecCancion(InstrumentMenu.title, instrumento, user_id);
                                    usuarioDAO.deleteRecCancion( cancion,InstrumentMenu.title, user_id, instrumento);
                                        Toast.makeText(Canciones_Menu.this, getString(R.string.audio_retirado), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    crearLista1();
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

                return true;
            } });

            }




    public void onResume(){
        super.onResume();
        crearLista1();
        searchView.clearFocus();//ESTO ES PARA QUE EL TECLADO NO APAREZCA AUTOMATICAMENTE AL VOLVER
        searchView.setQuery("",false);
    }

}