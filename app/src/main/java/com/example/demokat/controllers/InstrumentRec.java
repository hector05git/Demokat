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
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;
import com.example.demokat.database.UsuarioDAO;
import com.example.demokat.models.RecModel;

import java.io.IOException;
import java.time.LocalDate;

public class InstrumentRec extends AppCompatActivity {
    private ImageView bimboPlay;
    private boolean on;
    Thread animar;
    private MediaRecorder mediaRecorder;
    private String outputFilePath;
    int imageInicio = R.drawable.dino_normal;
    int imageFin = R.drawable.dino_click;
    ImageView play_btn;
    private String mode = "start";
    UsuarioDAO usuarioDAO = MainActivity.getUsuarioDAO();


    public ImageView bimboGuitar;
    boolean onM = false;
    Thread animarM;
    private SoundPool soundPool;
    int bpms = 60;
    int milisec = 1000;
    private int click;
    TextView bpm;
    private ImageView mas, menos;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_instrument_rec);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        play_btn = findViewById(R.id.play_btn2);
        bimboPlay = findViewById(R.id.bimbo_play2);


        bimboGuitar = findViewById(R.id.bimboMetro);
        bpm = findViewById(R.id.bpm2);
        mas = findViewById(R.id.mas2);
        menos = findViewById(R.id.menos2);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);

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

                    runOnUiThread(() -> bimboGuitar.setImageResource(R.drawable.dino_click));
                    soundPool.play(click, 1, 1, 1, 0, 1);

                    Thread.sleep(milisec);

                    runOnUiThread(() -> bimboGuitar.setImageResource(R.drawable.dino_normal));
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
        int id = usuarioDAO.getLastId()+1;
        outputFilePath = getExternalFilesDir(null).getAbsolutePath() + "/" + String.valueOf(id)+ ".3gp";
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

            play_btn.setImageResource(imageInicio);
            mode = "pause";

        } else if (on && mode.equals("pause")) {
            mediaRecorder.pause();

            animar.interrupt();
            mode = "restart";
            play_btn.setImageResource(imageFin);

        } else if (on && mode.equals("restart")) {
            mediaRecorder.resume();

            startAnim();
            play_btn.setImageResource(R.drawable.dino_normal);
            bimboPlay.setImageResource(imageInicio);
            mode = "pause";
            on = true;
        }
    }


    public void showInputDialog(View view) {

        EditText input = null;
        if (on) {
            try {
                mediaRecorder.pause();
                animar.interrupt();
                mode = "restart";
                play_btn.setImageResource(imageFin);


            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error al detener la grabación", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            input = new EditText(this);
            InputFilter filter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    String allowedChars = "^[a-zA-Z0-9]+$";


                    if (source.equals("")) {
                        return null;
                    }

                    if (source.toString().matches(allowedChars)) {
                        return null;
                    }

                    return "";
                }
            };
            input.setFilters(new InputFilter[]{filter});

        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText finalInput = input;
        builder.setTitle("Titulo")
                .setMessage("Escriba un titulo para su grabación")
                .setView(input)

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
                                Toast.makeText(InstrumentRec.this, "Texto ingresado: " + userInput, Toast.LENGTH_SHORT).show();

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
                        dialog.dismiss(); // Simplemente cerrar el diálogo
                    }
                });

        // Mostrar el AlertDialog
        builder.create().show();
    }


    public void finalizar(String input2) {

            String titulo2 = input2.trim();
            String instrumento = MainMenu.getInstrumento();
            LocalDate fecha = LocalDate.now();
            int user_id = MainActivity.getUser_id();


            RecModel recModel = new RecModel(outputFilePath, titulo2, instrumento, fecha, user_id);


            usuarioDAO.insertRec(recModel);

            bimboPlay.setImageResource(imageInicio);
            mode = "start";
            play_btn.setImageResource(imageFin);
            finish();

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
