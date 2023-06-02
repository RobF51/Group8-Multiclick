package com.example.multiclick;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComprCategTipos extends AppCompatActivity {


    FirebaseFirestore dbartic;

    ListView listviewArticulos;
    List<String> listaArticulos = new ArrayList<>();
    List<String>listaIdArticulos = new ArrayList<>();
    ArrayAdapter<String> mAdapterArticulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compr_categ_tipos);

        dbartic = FirebaseFirestore.getInstance();
        listviewArticulos = findViewById(R.id.viewlistarticulos);

        InicializarAriculosList();
        cambioTitulo();
        actualizarUI();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comprador, menu);
//hola
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.carrito:
                //activar pagina stock
                Intent intentCarrito = new Intent(ComprCategTipos.this, Carrito.class);
                startActivity(intentCarrito);
                return true;

            case R.id.home:
                //activar pagina stock
                Intent intentHome= new Intent(ComprCategTipos.this, MainComprador.class);
                startActivity(intentHome);
                return true;

            case R.id.logout:
                //activar pagina stock
                Intent intentLogout= new Intent(ComprCategTipos.this, Splash.class);
                startActivity(intentLogout);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }


    public static String getTituloStorage() {
        return tituloStorage;
    }

    static String tituloStorage;




    public void cambioTitulo (){

        String texto = getIntent().getStringExtra("categoriaSeleccionada");
        TextView textView = findViewById(R.id.idarticulo);
        textView.setText(texto);
        tituloStorage = texto;

    }


    private void actualizarUI() {



        dbartic.collection("Articulos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        listaArticulos.clear();
                        listaIdArticulos.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            listaIdArticulos.add(doc.getId());
                            listaArticulos.add(doc.getString("Articulo"));
                        }


                        if (listaArticulos.size() == 0) {
                            listviewArticulos.setAdapter(null);
                        } else {
                            mAdapterArticulos = new MyCustomAdapter2Categ(ComprCategTipos.this, listaArticulos);

                            listviewArticulos.setAdapter(mAdapterArticulos);
                        }


                    }
                });

    }


    public void InicializarAriculosList(){

        ArrayList<String> articulos = new ArrayList<>();
        articulos.add("Consola");
        articulos.add("Videojuego");
        articulos.add("Accesorio");


        Map<String, Object> mapArtic = new HashMap<>();

        //delete all
        for (int i = 0;i<  articulos.size();i++){
            String iConvert = Integer.toString(i);
            dbartic.collection("Articulos").document(iConvert).delete();
        }

        for (int i = 0;i<  articulos.size();i++) {
            mapArtic.put("Articulo",  articulos.get(i));
            String iConvert = Integer.toString(i);
            dbartic.collection("Articulos").document(iConvert).set(mapArtic);

        }

    }


    public void GoInfoArticulo(View view) {
        Intent intentInfoArticulo = new Intent(ComprCategTipos.this, InfoArticulos.class);
        startActivity(intentInfoArticulo);

        String nombreCategoria;

        //nombreCategoria = (String) getText(R.id.textViewcateg);

    }




}

