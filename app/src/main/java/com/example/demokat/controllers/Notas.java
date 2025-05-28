package com.example.demokat.controllers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;
import com.example.demokat.adapters.AdapterPersonalizado2;
import com.example.demokat.database.DAO;

public class Notas extends AppCompatActivity {

    private String title = InstrumentMenu.getTitle2();
    private int user_id = MainActivity.getUser_id();
    DAO usuarioDAO = MainActivity.getUsuarioDAO();
    private EditText notas;
    private EditText tituloNotas;
    String notaOg;
    String mode = InstrumentMenu.getNotaMode();
    private ImageView add_Btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRec), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        notas = findViewById(R.id.notas_et);
        tituloNotas = findViewById(R.id.title_et2);
        add_Btn = findViewById(R.id.addBtn);


        if(mode.equals("edit")){
            tituloNotas.setText(title);
            notaOg= usuarioDAO.loadNota(user_id, title);
            notas.setText(notaOg);
            add_Btn.setVisibility(View.VISIBLE);
            add_Btn.setEnabled(true);
        }else{
            add_Btn.setVisibility(View.INVISIBLE);
            add_Btn.setEnabled(false);
        }


    }



    public void addToSong(View view){
        ListView list_Cancion = new ListView(this);

        String[] titulos=  usuarioDAO.selectAllTitulosCanciones(user_id);
        int [] images = new int[titulos.length];

        for (int i = 0; i < titulos.length; i++) {
            images[i] = R.drawable.addtosong;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        AdapterPersonalizado2 adapter2 = new AdapterPersonalizado2(this, images, titulos);
        list_Cancion.setAdapter(adapter2);

        android.app.AlertDialog dialog = builder
                .setTitle(R.string.nueva_cancion)
                .setMessage(R.string.elige_cancion)
                .setView(list_Cancion)
                .create();

        list_Cancion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioDAO.insertNotasCancion(titulos[position],title, user_id);
                Toast.makeText(Notas.this, R.string.nota_anadida, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        dialog.show();

    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        boolean titleEdited = false;
        String tituloEdit = String.valueOf(tituloNotas.getText()).trim();
        String notasEdit = String.valueOf(notas.getText());
        if (mode.equals( "edit")) {
                if (tituloEdit.equals(title) && notasEdit.equals(notaOg) || tituloEdit.isEmpty() ) {
                    finish();
                }
                else if (!tituloEdit.equals(title)) {
                    int res = usuarioDAO.checkTitleNotas(user_id, tituloEdit);
                    if (res == 0) {
                        Toast.makeText(this, getString(R.string.titulo_notas), Toast.LENGTH_SHORT).show();
                        return;

                    } else {
                        usuarioDAO.editTitleNotas(user_id, tituloEdit, title);
                        titleEdited = true;
                    }
                }

             if (!notasEdit.equals(notaOg)) {
                if(titleEdited){
                    usuarioDAO.editNotas(user_id, notasEdit, tituloEdit);
                    finish();
                }else {
                    usuarioDAO.editNotas(user_id, notasEdit, title);
                    finish();
                }


            } else {
                finish();
            }
        }



        else if(mode.equals( "create")){
            if(tituloEdit.isEmpty() && notasEdit.isEmpty()){
            finish();
        }
        else if(tituloEdit.isEmpty() && !notasEdit.isEmpty()){
                Toast.makeText(this, getString(R.string.titulo_notas), Toast.LENGTH_SHORT).show();
                return;
            }
            else if (!tituloEdit.isEmpty()){
                int res = usuarioDAO.checkTitleNotas(user_id, tituloEdit);
                if (res == 0) {
                    Toast.makeText(this, getString(R.string.nota_existente), Toast.LENGTH_SHORT).show();
                    return;

                }else{
                    usuarioDAO.insertNota(tituloEdit,notasEdit,user_id);
                    Toast.makeText(this, getString(R.string.nota_creada), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

        }
    }
}

