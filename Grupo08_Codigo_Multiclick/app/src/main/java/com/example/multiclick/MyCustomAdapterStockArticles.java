package com.example.multiclick;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MyCustomAdapterStockArticles extends ArrayAdapter<ArticuloStock> {
    private final List<ArticuloStock> listaArticulosStock;
    private final Context context;
    private final ListView listviewArticulosStock;
    private final FirebaseFirestore dbartic;
    private EditText editTextQuantityToAdd;

    public MyCustomAdapterStockArticles(Context context, List<ArticuloStock> listaArticulosStock, ListView listviewArticulosStock, FirebaseFirestore dbartic, EditText editTextQuantityToAdd) {
        super(context, R.layout.item_stock_article, listaArticulosStock);
        this.context = context;
        this.listaArticulosStock = listaArticulosStock;
        this.listviewArticulosStock = listviewArticulosStock;
        this.dbartic = dbartic;
        this.editTextQuantityToAdd = editTextQuantityToAdd;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_stock_article, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ArticuloStock articuloStock = listaArticulosStock.get(position);

        viewHolder.textViewArticle.setText(articuloStock.getNombreArticulo());
        viewHolder.botonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción cuando se hace clic en el botón Add
                String stringQuantityToAdd = editTextQuantityToAdd.getText().toString();
                ArticuloStock articuloStock = listaArticulosStock.get(position);
                int position = listviewArticulosStock.getPositionForView(v);
                incrementarCantidadArticulo(position, stringQuantityToAdd);
            }
        });
        viewHolder.textViewQuantity.setText("Quantity:");
        viewHolder.textViewQuantityValue.setText(articuloStock.getCantidad());

        return convertView;
    }

    static class ViewHolder {
        TextView textViewArticle;
        Button botonAdd;
        TextView textViewQuantity;
        TextView textViewQuantityValue;

        public ViewHolder(View view) {
            textViewArticle = view.findViewById(R.id.textViewArticle);
            botonAdd = view.findViewById(R.id.botonAdd);
            textViewQuantity = view.findViewById(R.id.textViewQuantity);
            textViewQuantityValue = view.findViewById(R.id.textViewQuantityValue);
        }
    }

    public void incrementarCantidadArticulo(int position, String stringQuantityToAdd) {
        ArticuloStock articulo = listaArticulosStock.get(position);
        String categoria = articulo.getCategoria();
        String nombreArticulo = articulo.getNombreArticulo();

        // Realiza la consulta a la base de datos para obtener el documento correspondiente al artículo
        dbartic.collection("Stock").whereEqualTo("Categoria", categoria).whereEqualTo("NombreArticulo", nombreArticulo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String cantidadActualString = document.getString("Cantidad");
                        int cantidadActual = Integer.parseInt(cantidadActualString);

                        int cantidadAnadir = Integer.parseInt(stringQuantityToAdd);

                        int nuevaCantidad = cantidadActual + cantidadAnadir;
                        String nuevaCantidadString = String.valueOf(nuevaCantidad);
                        document.getReference().update("Cantidad", nuevaCantidadString).addOnSuccessListener(new OnSuccessListener<Void>() {


                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Update quantity failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}