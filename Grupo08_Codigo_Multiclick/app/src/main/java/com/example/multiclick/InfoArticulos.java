package com.example.multiclick;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoArticulos extends AppCompatActivity {

    FirebaseFirestore dbArticInfo = FirebaseFirestore.getInstance();

    //FirebaseFirestore dbPedidosNoConfirmados = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_articulos);

        cambioTituloInfo();
        actualizarUI();
    }

    public static String getTituloStorage2() {
        return tituloStorage2;
    }

    static String tituloStorage2;

    public void cambioTituloInfo() {

        String texto = getIntent().getStringExtra("categoriaSeleccionada");
        TextView textView = findViewById(R.id.idTipoArticulo);
        textView.setText(texto);
        tituloStorage2 = texto;

    }

    public double getPrecioArticuloPass() {
        return precioArticuloPass;
    }

    double precioArticuloPass;
    private void actualizarUI() {

        ArrayList<String> arrayListCategArtic = new ArrayList<String>(Arrays.asList(tituloStorage2.split("-")));
        String Categoria = arrayListCategArtic.get(1);
        String Articulo = arrayListCategArtic.get(0);

        ArrayList<String> listaInformacion = new ArrayList<>();


        dbArticInfo.collection("InfodeArticulos")

                .whereEqualTo("Consola", Categoria)
                .whereEqualTo("Articulo", Articulo)
                //.get();
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        listaInformacion.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            listaInformacion.add(doc.getString("ImagenID"));
                            listaInformacion.add(doc.getString("Descripcion"));
                            listaInformacion.add(doc.getString("Precio"));

                            String imagen = listaInformacion.get(0);
                            String descripcion = listaInformacion.get(1);
                            String precio = listaInformacion.get(2);


                            //inserto la imagen por el ID
                            ImageView imagenInfoArtic = findViewById(R.id.imageViewArticInfo);


                            int drawableResourceId = getResources().getIdentifier(imagen, "drawable", getPackageName());

                            imagenInfoArtic.setImageResource(drawableResourceId);


                            //Inserto descripcion
                            TextView DescrpcionSustit = findViewById(R.id.textViewDescripArtic);

                            DescrpcionSustit.setText(descripcion);

                            //inserto precio
                            TextView PrecioSustit = findViewById(R.id.textViewPrecioArtic);
                            PrecioSustit.setText("Precio: " + precio + "€");



                            precioArticuloPass = Double.parseDouble(precio);


                        }

                    }
                });
    }

    FirebaseFirestore dbAddArticulo = FirebaseFirestore.getInstance();
    boolean accionRealizada = false;
    public void addArticulo(View view) {

        if (accionRealizada) {
            return;
        }

        String mail = LoginComprador.getEmailAux();

        dbAddArticulo.collection("Carritos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    Map<String, Object> mapArticuloAdd = new HashMap<>();
                    mapArticuloAdd.put("Mail", mail);
                    mapArticuloAdd.put("Articulo", tituloStorage2);
                    dbAddArticulo.collection("Carritos").add(mapArticuloAdd);

                    accionRealizada = true;

                } else {
                    accionRealizada = false;
                }

            }
        });

        //String precioArticuloPassString = Double.toString(precioArticuloPass);
        Intent intentPassPrecio = new Intent(InfoArticulos.this, Carrito.class);
        //intentPassPrecio.putExtra("PrecioPass", precioArticuloPassString );
        startActivity(intentPassPrecio);
    }

}



