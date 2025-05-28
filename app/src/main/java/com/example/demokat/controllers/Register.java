package com.example.demokat.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;
import com.example.demokat.database.DAO;

public class Register extends AppCompatActivity {

    private EditText userName;
    private EditText name;
    private EditText surName;
    private EditText pass1;
    private EditText pass2;
    DAO dao = MainActivity.getUsuarioDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRec), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        userName = findViewById(R.id.userNameS);
        name = findViewById(R.id.nameS);
        surName = findViewById(R.id.surNameS);
        pass1 = findViewById(R.id.pass1S);
        pass2 = findViewById(R.id.pass2S);

    }

    public void RegisterIn(View view){
        String username = String.valueOf(userName.getText());
        String namee = String.valueOf(name.getText());
        String surname = String.valueOf(surName.getText());
        String password1 = String.valueOf(pass1.getText());
        String password2 = String.valueOf(pass2.getText());
        if(username.isEmpty()){
            Toast.makeText(this, getString( R.string.rellene_usuario), Toast.LENGTH_SHORT).show();
        } else if (namee.isEmpty()) {
            Toast.makeText(this, getString(R.string.rellene_nombre), Toast.LENGTH_SHORT).show();

        } else if (surname.isEmpty()) {
            Toast.makeText(this, getString(R.string.rellene_apellido), Toast.LENGTH_SHORT).show();

        } else if (password1.isEmpty()) {
            Toast.makeText(this, getString(R.string.rellene_contrasena), Toast.LENGTH_SHORT).show();

        } else if (password2.isEmpty()) {
            Toast.makeText(this, getString(R.string.repita_contrasena), Toast.LENGTH_SHORT).show();

        } else if (!password1.equals(password2)) {
            pass1.setText("");
            pass2.setText("");
            Toast.makeText(this, getString(R.string.contrasenas_no_coinciden), Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, getString( R.string.usuario_creado), Toast.LENGTH_SHORT).show();

            dao.insertUser(username, namee, surname, password1);
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
        }


    }

}