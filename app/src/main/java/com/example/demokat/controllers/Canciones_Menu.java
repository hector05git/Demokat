package com.example.demokat.controllers;

import static com.example.demokat.controllers.MainMenu.instrumento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

public class Canciones_Menu extends AppCompatActivity {
    private String cancion_titulo = CancioneroMenu.getCancion_titulo();
    private TextView textView;
    UsuarioDAO usuarioDAO = MainActivity.getUsuarioDAO();
    private int user_id = MainActivity.getUser_id();
    ListView list_Rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_canciones_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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






       AdapterPersonalizado2 adapter = new AdapterPersonalizado2(this, images, combinados);
        list_Rec.setAdapter(adapter);
        String[] finalTitulos = combinados;
        list_Rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InstrumentMenu.title = finalTitulos[position];
                instrumento = combinados2[position];

                if(instrumento=="Notas"){
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
                builder.setTitle("¡Alerta!")
                        .setMessage("¿Deseas retirar este elemento?")
                        .setPositiveButton("Retirar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(instrumento== "Notas"){

                                }else {
                                    String cancion = usuarioDAO.checkRecCancion(InstrumentMenu.title, instrumento, user_id);
                                    usuarioDAO.deleteRecCancion( cancion,InstrumentMenu.title, user_id);
                                    Toast.makeText(Canciones_Menu.this, "Audio retirado con éxito", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    crearLista1();
                                }



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




    public void onResume(){

        super.onResume();
        crearLista1();
    }

}