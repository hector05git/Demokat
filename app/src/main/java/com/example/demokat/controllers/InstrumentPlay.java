package com.example.demokat.controllers;

import android.annotation.SuppressLint;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import java.io.File;
import java.io.IOException;

public class InstrumentPlay extends AppCompatActivity {

   private ImageView bimboPlay;
    private boolean on;
    private boolean loop = false;
    Thread animar;
    private MediaPlayer mediaPlayer;
    int imageInicio = R.drawable.dino_normal;
    int imageFin = R.drawable.dino_click;
    private int user_id = MainActivity.getUser_id();
    private String instrumentoSelected = MainMenu.getInstrumento();
    private String title = InstrumentMenu.getTitle2();
    String tituloEdit;
    UsuarioDAO usuarioDAO = MainActivity.getUsuarioDAO();
    Uri uri;

    private TextView tituloPlay;
    ImageView play_btn;
    ImageView loop_btn;
    private String mode = "start";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_instrument_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bimboPlay = findViewById(R.id.bimboGuitar);
        play_btn = findViewById(R.id.play_btn2);
        loop_btn = findViewById(R.id.btn_loop);
        File audioFile = new File(usuarioDAO.loadURI(user_id, instrumentoSelected,title));
        uri = Uri.fromFile(audioFile);


        tituloPlay = findViewById(R.id.titulo_tv);
        tituloPlay.setText(title);
    }

    private void startAnim() {
        if (animar != null && animar.isAlive()) {
            animar.interrupt();
        }
        animar = new Thread(() -> {
            while (on) {
                try {

                    runOnUiThread(() -> bimboPlay.setImageResource(imageFin));


                    Thread.sleep(500);

                    runOnUiThread(() -> bimboPlay.setImageResource(imageInicio));

                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        animar.start();
    }



    public void empezar(View view) {
        if (!on && mode == "start") {
            on = true;
            startAnim();

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this,uri);
                mediaPlayer.prepare();

                if (loop){
                    mediaPlayer.setLooping(true);
                }


            } catch (IOException e) {
                Toast.makeText(this,"UPSI",Toast.LENGTH_SHORT).show();

            }
            mediaPlayer.start();
            play_btn.setImageResource(imageInicio);
            mode ="pause";
           mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               @Override
               public void onCompletion(MediaPlayer mp) {
                   animar.interrupt();
                   mediaPlayer.release();
                   bimboPlay.setImageResource(imageInicio);
                   play_btn.setImageResource(imageFin);
                   mode = "start";
                   on = false;

               }
           });
        }else if(on && mode == "pause"){

                animar.interrupt();
                mediaPlayer.pause();
                mode="restart";
                play_btn.setImageResource(imageFin);


        }
        else if (on && mode == "restart"){
            startAnim();
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
            mediaPlayer.start();
            play_btn.setImageResource(R.drawable.dino_normal);
            bimboPlay.setImageResource(imageInicio);
            mode="pause";
            on = true;

        }
    }


    public void loop(View view) {
        if(on){
            if(!mediaPlayer.isLooping() && !loop){
                loop = true;
                loop_btn.setImageResource(imageInicio);
                mediaPlayer.setLooping(true);

            }else if(mediaPlayer.isLooping() && loop) {
                loop = false;
                mediaPlayer.setLooping(false);
                loop_btn.setImageResource(imageFin);

            }

        }
        else{
            if(!loop) {
                loop = true;
                loop_btn.setImageResource(imageInicio);
            }

            else if(loop){
                loop = false;
                loop_btn.setImageResource(imageFin);
            }

        }




        }





    public void finalizar(View view) {

        if (on){
            on = false;
        animar.interrupt();
        mediaPlayer.stop();
        bimboPlay.setImageResource(imageInicio);
        mode = "start";
        play_btn.setImageResource(imageFin);

    }

        }


    public void addToSong(View view){
        ListView list_Cancion = new ListView(this);

        String[] titulos=  usuarioDAO.selectAllTitulosCanciones(user_id);
        int [] images = new int[titulos.length];

        for (int i = 0; i < titulos.length; i++) {
            images[i] = R.drawable.dino_click;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        AdapterPersonalizado2 adapter2 = new AdapterPersonalizado2(this, images, titulos);
        list_Cancion.setAdapter(adapter2);

        android.app.AlertDialog dialog = builder
                .setTitle("Nueva Canción")
                .setMessage("Elige una canción para añadir")
                .setView(list_Cancion)
                .create();

        list_Cancion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioDAO.insertRecCancion(titulos[position],title, user_id);
                Toast.makeText(InstrumentPlay.this, "Audio añadido con éxito", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        dialog.show();

    }








    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

            tituloEdit = String.valueOf(tituloPlay.getText()).trim();
            if (tituloEdit.equals(title) || tituloEdit.trim().isEmpty()) {
                finish();
            } else {
                int res = usuarioDAO.checkTitle(user_id, instrumentoSelected, tituloEdit);
                if (res == 0) {
                    Toast.makeText(this, "Ya tienes una grabación con ese nombre", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    usuarioDAO.editTitle(user_id, instrumentoSelected, tituloEdit, title);
                    finish();
                }
            }
            finish();
        }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null
        ){
            mediaPlayer.release();
        }

        if (animar != null && animar.isAlive()) {
            on = false;
            animar.interrupt();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
        if (animar != null && animar.isAlive()) {
            on = false;
            animar.interrupt();
        }
    }
}












