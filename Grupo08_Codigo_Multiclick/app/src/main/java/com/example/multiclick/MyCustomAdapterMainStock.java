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
public class MyCustomAdapterMainStock extends ArrayAdapter<String> {
    private final List<String> listaCategorias;
    private final Context context;

    public MyCustomAdapterMainStock(Context context, List<String> listaCategorias) {
        super(context, R.layout.item_categoria, listaCategorias);
        this.context = context;
        this.listaCategorias = listaCategorias;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_categoria, parent, false);
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
                Intent intent = new Intent(context, StockCategoria.class);
                intent.putExtra("categoriaSeleccionada", categoria);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView textViewCateg;
        Button boton;

        public ViewHolder(View view) {
            textViewCateg = view.findViewById(R.id.textViewcateg);
            boton = view.findViewById(R.id.botoncategmas);
        }
    }
}