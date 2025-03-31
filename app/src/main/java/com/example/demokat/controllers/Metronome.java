package com.example.demokat.controllers;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demokat.R;

public class Metronome extends AppCompatActivity {

    public ImageView bimboGuitar;
    boolean on = false;
    Thread animar;
    private SoundPool soundPool;
    int bpms = 60;
    int milisec = 1000;
    private int click;
    TextView bpm;
    private ImageView mas, menos, encender;
    private SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_metronome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bimboGuitar = findViewById(R.id.bimboguitar);
        bpm = findViewById(R.id.bpm2);
        mas = findViewById(R.id.mas);
        menos = findViewById(R.id.menos);
        encender = findViewById(R.id.encender);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        click = soundPool.load(this, R.raw.click, 1);
        bpm.setText(String.valueOf(bpms));
        seekbar = findViewById(R.id.seekBar);
        seekbar.setMin(1);
        seekbar.setMax(300);
        seekbar.setProgress(60);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bpms = progress;
                recalcularIntervalo();
                bpm.setText(String.valueOf(bpms));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private void startAnim() {
        if (animar != null && animar.isAlive()) {
            animar.interrupt();
        }
        animar = new Thread(() -> {
            while (on) {
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
        animar.start();
    }

    public void empezar(View view) {
        if (!on) {
            on = true;
            startAnim();
            bpm.setText(String.valueOf(bpms));

        }
      else if(on){
            on = false;
            encender.setEnabled(true);
            if (animar != null && animar.isAlive()) {
                animar.interrupt();
            }
        }
    }

    private void recalcularIntervalo() {
        milisec = 60000 / bpms;
    }

    public void restar(View view) {
        if (bpms > 5) {
            bpms -= 5;
            seekbar.setProgress(seekbar.getProgress()-5);
            recalcularIntervalo();
            bpm.setText(String.valueOf(bpms));
        }
    }

    public void sumar(View view) {
        if (bpms < 300) {
            bpms += 5;
            seekbar.setProgress(seekbar.getProgress()+5);
            recalcularIntervalo();
            bpm.setText(String.valueOf(bpms));
        }
    }

    public void restar1(View view) {
        if (bpms > 5) {
            bpms -= 1;
            seekbar.setProgress(seekbar.getProgress()-1);
            recalcularIntervalo();
            bpm.setText(String.valueOf(bpms));
        }
    }

    public void sumar1(View view) {
        if (bpms < 300) {
            seekbar.setProgress(seekbar.getProgress()+5);
            bpms += 1;
            recalcularIntervalo();
            bpm.setText(String.valueOf(bpms));
        }
    }





        public void onPause(){
            super.onPause();
            if (animar != null && animar.isAlive()) {
                on = false;
                animar.interrupt();
            }
        }

        public void onDestroy(){
            super.onDestroy();
            if (animar != null && animar.isAlive()) {
                on = false;
                animar.interrupt();
            }
        }

}