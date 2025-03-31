package com.example.demokat.controllers;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;
import com.example.demokat.database.UsuarioDAO;
import com.example.demokat.models.UsuarioModel;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private EditText usu;
    private EditText con;
    private UsuarioModel usuarioModel;
    public static int user_id;

    public static int getUser_id() {
        return user_id;
    }

    public static UsuarioDAO usuarioDAO;
    public static UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    static {
        try {
            usuarioDAO = new UsuarioDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usu = findViewById(R.id.usu_et);
        con = findViewById(R.id.con_et);

    }
    public void goToMenu(View view) {
        String usuario = String.valueOf(usu.getText()).trim();
        String contrasena = String.valueOf(con.getText()).trim();
        if (usuario.isEmpty()) {
            Toast.makeText(this, R.string.rellene_usuario, Toast.LENGTH_SHORT).show();

            return;
        }
        if (contrasena.isEmpty()) {
            Toast.makeText(this, R.string.rellene_contrasena, Toast.LENGTH_SHORT).show();

            return;
        }

        usuarioModel = new UsuarioModel(usuario, contrasena);
        UsuarioModel usuarioModel1 = usuarioDAO.selectUserData(usuarioModel, this);
        if (usuarioModel1.getUser() == "errordb") {

            try {
                usuarioDAO = new UsuarioDAO();
                usu.setText("");
                con.setText("");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(usuarioModel1.getUser()=="erroruser"){
            usu.setText("");
            con.setText("");

        }

        else {
            user_id = usuarioModel1.getId();
            Toast.makeText(this,   usuarioModel1.getUser() + " logeado", Toast.LENGTH_SHORT).show();
            usuarioModel = null;
            Intent intent = new Intent(this, ButtonsMenu.class);
            startActivity(intent);

        }


    }




}
