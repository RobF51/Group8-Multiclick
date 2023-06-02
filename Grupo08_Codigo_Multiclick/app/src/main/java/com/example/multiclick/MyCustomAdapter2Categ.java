package com.example.multiclick;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyCustomAdapter2Categ  extends ArrayAdapter<String> {

    private final List<String> listaCategorias;
    private final Context context;

    public MyCustomAdapter2Categ(Context context, List<String> listaCategorias) {
        super(context, R.layout.item_articulo, listaCategorias);
        this.context = context;
        this.listaCategorias = listaCategorias;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {




        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_articulo, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String categoria = listaCategorias.get(position);
        viewHolder.textViewCateg.setText(categoria);

        viewHolder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tituloPrev = ComprCategTipos.getTituloStorage();

                Intent intent = new Intent(context, InfoArticulos.class);
                intent.putExtra("categoriaSeleccionada", categoria + "-"+ tituloPrev );
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView textViewCateg;
        Button boton;

        public ViewHolder(View view) {
            textViewCateg = view.findViewById(R.id.textViewartic);
            boton = view.findViewById(R.id.botonArticmas);
        }
    }
}
