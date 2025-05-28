package com.example.demokat.controllers;

import static com.example.demokat.controllers.MainActivity.user_id;
import static com.example.demokat.controllers.MainMenu.instrumento;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.demokat.database.DAO;
import com.example.demokat.models.RecModel;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InstrumentRec extends AppCompatActivity {
    private ImageView bimboPlay;
    private boolean on;
    Thread animar;
    private MediaRecorder mediaRecorder;
    private String outputFilePath;
    int imageInicio = R.drawable.bimborec;
    int imageFin = R.drawable.bimborec2;
    ImageView play_btn;
    private String mode = "start";
    DAO usuarioDAO = MainActivity.getUsuarioDAO();
    private ConstraintLayout fondo;

    public ImageView bimboMetro;
    boolean onM = false;
    Thread animarM;
    private SoundPool soundPool;
    int bpms = 60;
    int milisec = 1000;
    private int click;
    TextView bpm;
    private ImageView mas, menos;
    Timestamp timestamp;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_instrument_rec);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRec), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        play_btn = findViewById(R.id.play_btn2);
        bimboPlay = findViewById(R.id.bimbo_play2);
        bimboPlay.setImageResource(imageInicio);
        fondo = findViewById(R.id.mainRec);
        bimboMetro = findViewById(R.id.bimboMetro);
        bpm = findViewById(R.id.bpm2);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        loadInstrument();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();//ESTA LINEA ES PARA PODER UTILIZAR EL SOUNDPOOL Y EL MEDIARECORDER A LA VEZ

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        click = soundPool.load(this, R.raw.click, 1);
        bpm.setText(String.valueOf(bpms));

    }

    private void startAnimMetro() {
        if (animarM != null && animarM.isAlive()) {
            animarM.interrupt();
        }
        animarM = new Thread(() -> {
            while (onM) {
                try {

                    runOnUiThread(() -> bimboMetro.setImageResource(R.drawable.bimbometro1));
                    soundPool.play(click, 1, 1, 1, 0, 1);

                    Thread.sleep(milisec);

                    runOnUiThread(() -> bimboMetro.setImageResource(R.drawable.bimbometro2));
                    soundPool.play(click, 1, 1, 1, 0, 1);
                    Thread.sleep(milisec);

                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        animarM.start();
    }


    public void empezarMetro(View view) {
        if (!onM) {
            onM = true;
            startAnimMetro();
            bpm.setText(String.valueOf(bpms));

        }
        else if(onM){
            onM = false;
            if (animarM != null && animarM.isAlive()) {
                animarM.interrupt();
                bimboMetro.setImageResource(R.drawable.bimbometro);
            }
        }
    }

    private void recalcularIntervalo() {
        milisec = 60000 / bpms;
    }

    public void restar(View view) {
        if (bpms > 5) {
            bpms -= 5;
            recalcularIntervalo();
            bpm.setText(String.valueOf(bpms));
        }
    }

    public void sumar(View view) {
        if (bpms < 300) {
            bpms += 5;
            recalcularIntervalo();
            bpm.setText(String.valueOf(bpms));
        }
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

    private void prepareMediaRecorder() {
        //LO PRIMERO


        LocalDateTime fechaHora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHoraString = fechaHora.format(formatter);
        timestamp = Timestamp.valueOf(fechaHoraString);
        String[] rutastamp = String.valueOf(timestamp).split("[-:. ]");
        String rutaJunta="";
        for (int i = 0; i < rutastamp.length; i++) {
            rutaJunta += rutastamp[i];
        }

        outputFilePath = getExternalFilesDir(null).getAbsolutePath() + "/" + rutaJunta + ".3gp";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(outputFilePath);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "Error al preparar la grabación", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void empezar(View view) {
        if (!on && mode.equals("start")) {


            on = true;
            startAnim();

            prepareMediaRecorder();

            try {
                mediaRecorder.start();



            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error al iniciar la grabación", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            play_btn.setImageResource(R.drawable.pauserec);
            mode = "pause";

        } else if (on && mode.equals("pause")) {
            mediaRecorder.pause();

            animar.interrupt();
            mode = "restart";
            play_btn.setImageResource(R.drawable.startrec);

        } else if (on && mode.equals("restart")) {
            mediaRecorder.resume();

            startAnim();
            play_btn.setImageResource(R.drawable.pauserec);
            bimboPlay.setImageResource(imageInicio);
            mode = "pause";
            on = true;
        }
    }


    public void showInputDialog(View view) {

        if (on) {
            EditText inputTitulo = null;
            try {
                mediaRecorder.pause();
                animar.interrupt();
                mode = "restart";
                play_btn.setImageResource(R.drawable.startrec);


            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error al detener la grabación", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            inputTitulo = new EditText(this);




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText finalInput = inputTitulo;
        builder.setTitle("Titulo")
                .setMessage("Escriba un titulo para su grabación")
                .setView(inputTitulo)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = finalInput.getText().toString().trim();
                        if (!userInput.isEmpty()) {
                            int i = usuarioDAO.checkTitle(user_id, instrumento, userInput);
                            if (i == 0) {
                                Toast.makeText(InstrumentRec.this, "Ya tienes una grabación con ese título", Toast.LENGTH_SHORT).show();
                            } else {
                                mediaRecorder.release();
                                mediaRecorder = null;
                                Toast.makeText(InstrumentRec.this, "Audio guardado  " + userInput, Toast.LENGTH_SHORT).show();

                                finalizar(userInput);
                            }


                        } else {

                            Toast.makeText(InstrumentRec.this, "No se ingresó texto", Toast.LENGTH_SHORT).show();
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
        }else {
            Toast.makeText(InstrumentRec.this, "Primero debes empezar a grabar", Toast.LENGTH_SHORT).show();
        }
    }


    public void finalizar(String input2) {

            String titulo2 = input2.trim();
            String instrumento = MainMenu.getInstrumento();


        int user_id = MainActivity.getUser_id();


            RecModel recModel = new RecModel(outputFilePath, titulo2, instrumento, timestamp, user_id);


            usuarioDAO.insertRec(recModel);

            bimboPlay.setImageResource(imageInicio);
            mode = "start";
            play_btn.setImageResource(R.drawable.startrec);
            finish();

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


    }




    public void onPause(){
        super.onPause();
        if (animarM != null && animarM.isAlive()) {
            on = false;
            animarM.interrupt();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (animarM != null && animarM.isAlive()) {
            on = false;
            animarM.interrupt();
        }
    }
}
