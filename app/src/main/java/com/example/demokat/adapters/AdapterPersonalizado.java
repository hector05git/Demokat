package com.example.demokat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.demokat.R;

public class AdapterPersonalizado extends BaseAdapter {

    private Context context;
    private int [] images;
    private String [] titulos;




    public AdapterPersonalizado(Context aplicationContext, int[] images, String[] titulos){
        this.context = aplicationContext;
        this.images = images;
        this.titulos = titulos;
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

        view = inflater.inflate(R.layout.layout_main_menu, null);
        ImageView image = view.findViewById(R.id.imagen_id);
        TextView titulo = view.findViewById(R.id.titulo_id);
        ConstraintLayout fondo = view.findViewById(R.id.mainList);
        image.setImageResource(images[i]);
        titulo.setText(titulos[i]);
        if(titulos[i].equals("Guitarra")){
            fondo.setBackgroundResource(R.drawable.guitarback);
        }
        if(titulos[i].equals("Batería")){
            fondo.setBackgroundResource(R.drawable.drumback);
        }
        if(titulos[i].equals("Voz")){
            fondo.setBackgroundResource(R.drawable.vozback);
        }
        if (titulos[i].equals("Bajo")) {
            fondo.setBackgroundResource(R.drawable.bassback);
        }
        if(titulos[i].equals("Notas")){
            fondo.setBackgroundResource(R.drawable.notasback);
        }


        return view;
    }
}
