package com.example.demokat.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;
import com.example.demokat.adapters.AdapterPersonalizado2;
import com.example.demokat.database.DAO;

import java.io.File;
import java.sql.Timestamp;

public class InstrumentMenu extends AppCompatActivity {
    private ListView listView;
    private String instrumento = MainMenu.getInstrumento();
    private TextView instrument;
    private ConstraintLayout fondo;
    private ImageView recBtn;
    DAO usuarioDAO = MainActivity.getUsuarioDAO();
    private int user_id = MainActivity.getUser_id();
    public static String title;
    public static String notaMode;
    public SearchView searchView;

    public static String getNotaMode() {
        return notaMode;
    }


    public static String getTitle2() {
        return title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_instrument_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRec), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        instrument = findViewById(R.id.instrument_tv);
        listView = findViewById(R.id.listRec);
        recBtn = findViewById(R.id.rec_btn);

        instrument.setText(instrumento);
        fondo = findViewById(R.id.mainRec);
        searchView = findViewById(R.id.search_id);
        searchView.setIconified(false);//TODO ESTO ES PARA QUE LA BARRA NAVEGADORA ESTÉ MÁS BONITA
        searchView.setFocusable(true);
        searchView.setClickable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.setOnClickListener(v -> searchView.onActionViewExpanded());

        crearLista();
        loadInstrument();








        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(instrumento.equals("Notas")){
                    String [] titulos = usuarioDAO.searchTitulosNotas(user_id,newText);
                    int [] images = new int[titulos.length];

                    for (int i = 0; i < titulos.length; i++) {
                        images[i] = R.drawable.startnotas;
                    }
                    AdapterPersonalizado2 adapter = new AdapterPersonalizado2(InstrumentMenu.this, images, titulos);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            title = titulos[position];
                            notaMode="edit";
                            Intent intent = new Intent(InstrumentMenu.this, Notas.class);
                            startActivity(intent);


                        }});

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setPressed(false);

                            title = titulos[position];
                            AlertDialog.Builder builder = new AlertDialog.Builder(InstrumentMenu.this);
                            builder.setTitle(getString(R.string.alerta))
                                    .setMessage(getString(R.string.eliminar_nota))
                                    .setPositiveButton(getString(R.string.eliminar), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            usuarioDAO.deleteNota(user_id, title);
                                            Toast.makeText(InstrumentMenu.this, getString(R.string.nota_eliminada), Toast.LENGTH_SHORT).show();
                                            crearLista();
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
                else {
                    String [] titulos = usuarioDAO.searchTitulosRec(user_id, instrumento,newText);
                    int [] images = new int[titulos.length];

                    for (int i = 0; i < titulos.length; i++) {
                        images[i] = R.drawable.startplay;
                    }
                    AdapterPersonalizado2 adapter = new AdapterPersonalizado2(InstrumentMenu.this, images, titulos);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            title = titulos[position];
                            Intent intent = new Intent(InstrumentMenu.this, InstrumentPlay.class);
                            startActivity(intent);


                        }


                    });
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    view.setPressed(false);
                                    title = titulos[position];



                                    AlertDialog.Builder builder = new AlertDialog.Builder(InstrumentMenu.this);
                                    builder.setTitle(getString(R.string.alerta))
                                            .setMessage(getString(R.string.eliminar_audio))
                                            .setPositiveButton(getString(R.string.eliminar), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    usuarioDAO.deleteRec(user_id, instrumento, title);
                                                    String ruta = usuarioDAO.loadURI(user_id,instrumento,title) + ".3gp";
                                                    File archivo = new File(ruta);

                                                    if (archivo.exists()) {
                                                        boolean deleted = archivo.delete();
                                                        if (deleted) {
                                                            Toast.makeText(InstrumentMenu.this, getString(R.string.audio_eliminado), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(InstrumentMenu.this, getString(R.string.audio_eliminado_error), Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(InstrumentMenu.this, getString(R.string.audio_eliminado_error), Toast.LENGTH_SHORT).show();
                                                    }
                                                    crearLista();
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

                    }



                return false;


            }

        });





    }




    public void loadInstrument(){
        if(instrumento.equals("Guitarra")){
            fondo.setBackgroundResource(R.drawable.guitarback);
        }
        if(instrumento.equals("Voz")){
            fondo.setBackgroundResource(R.drawable.vozback);
        }
        if(instrumento.equals("Batería")){
            fondo.setBackgroundResource(R.drawable.drumback);
        }
        if(instrumento.equals("Bajo")){
            fondo.setBackgroundResource(R.drawable.bassback);
        }
        if(instrumento.equals("Notas")){
            fondo.setBackgroundResource(R.drawable.notasback);
            recBtn.setImageResource(R.drawable.newnota);



        }

    }






    public void crearLista(){


        String[] titulos=  usuarioDAO.selectTitulosRec(user_id, instrumento);
        if(instrumento=="Notas"){
           titulos=  usuarioDAO.selectTitulosNotas(user_id);
        }

        int [] images = new int[titulos.length];

        for (int i = 0; i < titulos.length; i++) {
            if(instrumento=="Notas"){
                images[i] = R.drawable.startnotas;
            } else  {
                images[i] = R.drawable.startplay;
            }
        }

        AdapterPersonalizado2 adapter = new AdapterPersonalizado2(this, images, titulos);
        listView.setAdapter(adapter);
        String[] finalTitulos = titulos;
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.setPressed(false);

                title = finalTitulos[position];
                if(instrumento=="Notas"){

                    AlertDialog.Builder builder = new AlertDialog.Builder(InstrumentMenu.this);
                    builder.setTitle("¡Alerta!")
                            .setMessage("¿Deseas eliminar esta nota?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    usuarioDAO.deleteNota(user_id, title);
                                    Toast.makeText(InstrumentMenu.this, "Nota eliminada con éxito", Toast.LENGTH_SHORT).show();
                                    crearLista();
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
                } ;
                AlertDialog.Builder builder = new AlertDialog.Builder(InstrumentMenu.this);
                builder.setTitle("¡Alerta!")
                        .setMessage("¿Deseas eliminar este audio?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Timestamp fecha = usuarioDAO.checkFecha(title, instrumento, user_id);
                                String[] rutastamp = String.valueOf(fecha).split("[-:. ]");
                                String rutaJunta="";
                                for (int i = 0; i < rutastamp.length; i++) {
                                    rutaJunta += rutastamp[i]; // Concatenamos cada parte
                                }

                                usuarioDAO.deleteRec(user_id, instrumento, title);
                                String ruta = getExternalFilesDir(null).getAbsolutePath() + "/" + rutaJunta+".3gp";
                                File archivo = new File(ruta);

                                if (archivo.exists()) {
                                    boolean deleted = archivo.delete();
                                    if (deleted) {
                                        Toast.makeText(InstrumentMenu.this, "Audio eliminado con éxito", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(InstrumentMenu.this, "No se pudo eliminar el audio", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Toast.makeText(InstrumentMenu.this, "El archivo no existe", Toast.LENGTH_SHORT).show();

                                }
                                crearLista();
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
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                title = finalTitulos[position];
                if(instrumento=="Notas"){
                    notaMode="edit";
                    Intent intent = new Intent(InstrumentMenu.this, Notas.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(InstrumentMenu.this, InstrumentPlay.class);
                    startActivity(intent);
                }


            }
        });

    }




    public void goToRec(View view){
        if(instrumento == "Notas"){
            notaMode="create";
            Intent intent = new Intent(this,Notas.class);
            startActivity(intent);

        }else{
            Intent intent = new Intent(this, InstrumentRec.class);
            startActivity(intent);
        }


    }

    public void onResume(){
        super.onResume();
        crearLista();
        searchView.clearFocus();//ESTO ES PARA QUE EL TECLADO NO APAREZCA AUTOMATICAMENTE AL VOLVER
        searchView.setQuery("",false);
    }

}