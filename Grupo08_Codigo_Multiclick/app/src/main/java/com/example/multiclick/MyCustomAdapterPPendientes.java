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

public class MyCustomAdapterPPendientes extends ArrayAdapter<String> {

    private final List<String> listaPPendientes;
    private final Context context;

    public MyCustomAdapterPPendientes(Context context, List<String> listaPPendientes) {
        super(context, R.layout.item_pedido_p, listaPPendientes);
        this.context = context;
        this.listaPPendientes = listaPPendientes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_pedido_p, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String pedido = listaPPendientes.get(position);
        viewHolder.textViewCateg.setText(pedido);

        viewHolder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendedorPedidoInfo.class);
                intent.putExtra("pedidoSeleccionado", pedido);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView textViewCateg;
        Button boton;

        public ViewHolder(View view) {
            textViewCateg = view.findViewById(R.id.textViewpedidopendiente);
            boton = view.findViewById(R.id.botonpedidoGo);
        }
    }
}
