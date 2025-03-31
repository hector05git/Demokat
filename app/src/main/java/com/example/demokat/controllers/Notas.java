package com.example.demokat.controllers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;
import com.example.demokat.database.UsuarioDAO;

import java.io.File;

public class Notas extends AppCompatActivity {

    private String title = InstrumentMenu.getTitle2();
    private int user_id = MainActivity.getUser_id();
    UsuarioDAO usuarioDAO = MainActivity.getUsuarioDAO();
    private EditText notas;
    private EditText tituloNotas;
    String notaOg;
    String mode = InstrumentMenu.getNotaMode();
    private ImageView delete_Btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        notas = findViewById(R.id.notas_et);
        tituloNotas = findViewById(R.id.title_et2);


        if(mode.equals("edit")){
            tituloNotas.setText(title);
            notaOg= usuarioDAO.loadNota(user_id, title);
            notas.setText(notaOg);
        }


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
                        Toast.makeText(this, "Ya tienes una nota con ese nombre", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Escriba un nombre para tus notas", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (!tituloEdit.isEmpty()){
                int res = usuarioDAO.checkTitleNotas(user_id, tituloEdit);
                if (res == 0) {
                    Toast.makeText(this, "Ya tienes una nota con ese nombre", Toast.LENGTH_SHORT).show();
                    return;

                }else{
                    usuarioDAO.insertNota(tituloEdit,notasEdit,user_id);
                    finish();
                }

            }

        }
    }
}

