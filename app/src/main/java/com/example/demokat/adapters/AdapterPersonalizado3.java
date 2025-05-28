package com.example.demokat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.demokat.R;

public class AdapterPersonalizado3 extends BaseAdapter {

    private Context context;
    private int [] images;
    private String [] titulos;
    private String [] instrumentos;



    public AdapterPersonalizado3(Context aplicationContext, int[] images, String[] titulos, String[] instrumentos){
        this.context = aplicationContext;
        this.images = images;
        this.titulos = titulos;
        this.instrumentos = instrumentos;
        this.inflater = (LayoutInflater.from(aplicationContext));
    }

    private LayoutInflater inflater;
    @Override
    public int getCount() {
        return this.images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.layout_main_menu2, null);
        ImageView image = view.findViewById(R.id.imagen_id);
        TextView titulo = view.findViewById(R.id.titulo_id);
        ConstraintLayout fondo = view.findViewById(R.id.mainList2);

        image.setImageResource(images[i]);
        titulo.setText(titulos[i]);


        if(instrumentos[i].equals("Guitarra")){
            fondo.setBackgroundResource(R.drawable.guitarback);
            image.setImageResource(R.drawable.guitaricon);
        }
        if(instrumentos[i].equals("Batería")){
            fondo.setBackgroundResource(R.drawable.drumback);
            image.setImageResource(R.drawable.drumsicon);
        }
        if(instrumentos[i].equals("Voz")){
            fondo.setBackgroundResource(R.drawable.vozback);
            image.setImageResource(R.drawable.vozicon);
        }
        if(instrumentos[i].equals("Bajo")){
            fondo.setBackgroundResource(R.drawable.bassback);
            image.setImageResource(R.drawable.bassicon);
        }
        if(instrumentos[i].equals("Notas")){
            fondo.setBackgroundResource(R.drawable.notasback);
            image.setImageResource(R.drawable.notasicon);
        }
        return view;
    }
}
