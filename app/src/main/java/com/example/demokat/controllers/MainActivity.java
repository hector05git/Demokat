package com.example.demokat.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;
import com.example.demokat.database.DAO;
import com.example.demokat.models.UsuarioModel;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private EditText usu;
    private EditText con;
    private UsuarioModel usuarioModel;
    public static int user_id;
    Thread animar;
    boolean on = false;
    ImageView bimboPlay;
    int imageFin = R.drawable.bimbometro2;
    int imageInicio = R.drawable.bimbometro1;


    public static int getUser_id() {
        return user_id;
    }

    public static DAO usuarioDAO;
    public static DAO getUsuarioDAO() {
        return usuarioDAO;
    }

    static {
        try {
            usuarioDAO = new DAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRec), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usu = findViewById(R.id.usu_et);
        con = findViewById(R.id.con_et);
        bimboPlay = findViewById(R.id.bimboPlayMain);
        on = true;
        startAnim();


    }



    private void startAnim() {
        if (animar != null && animar.isAlive()) {
            animar.interrupt();
        }
        animar = new Thread(() -> {
            while (on) {
                try {
                    runOnUiThread(() -> bimboPlay.setImageResource(imageFin));
                    Thread.sleep(1000);
                    runOnUiThread(() -> bimboPlay.setImageResource(imageInicio));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        animar.start();
    }

    public void goToMenu(View view) {
        String usuario = String.valueOf(usu.getText()).trim();
        String contrasena = String.valueOf(con.getText()).trim();
        if (usuario.isEmpty()) {
            Toast.makeText(this, getString(R.string.rellene_usuario), Toast.LENGTH_SHORT).show();

            return;
        }
        if (contrasena.isEmpty()) {
            Toast.makeText(this, getString(R.string.rellene_contrasena), Toast.LENGTH_SHORT).show();

            return;
        }

        usuarioModel = new UsuarioModel(usuario, contrasena);
        UsuarioModel usuarioModel1 = usuarioDAO.selectUserData(usuarioModel, this);
        if (usuarioModel1.getUser() == "errordb") {

            try {
                usuarioDAO = new DAO();
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
            Toast.makeText(this,   usuarioModel1.getUser() + " "+ getString(R.string.logueado), Toast.LENGTH_SHORT).show();
            usuarioModel = null;
            animar.interrupt();
            Intent intent = new Intent(this, ButtonsMenu.class);
            startActivity(intent);

        }


    }

    public void goToRegister(View view) {
        animar.interrupt();
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);


    }
public void onResume(){
    usu.setText("");
    con.setText("");
    startAnim();

    super.onResume();
}


}
