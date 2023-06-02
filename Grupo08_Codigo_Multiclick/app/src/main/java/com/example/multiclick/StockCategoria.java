package com.example.multiclick;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class StockCategoria extends AppCompatActivity {


    FirebaseFirestore dbartic;
    ListView listViewArticulosStock;
    List<ArticuloStock> listaArticulosStock = new ArrayList<>();
    //List<String> listaIdArticulos = new ArrayList<>();
    private MyCustomAdapterStockArticles mAdapterArticulosStock;
    private EditText editTextQuantityToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_categoria);
        dbartic = FirebaseFirestore.getInstance();
        listViewArticulosStock = findViewById(R.id.viewlistarticulos);
        editTextQuantityToAdd = findViewById(R.id.editTextQuantityToAdd);
        cambioTitulo();
        actualizarUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.stockbutton:
                Intent intentstock = new Intent(StockCategoria.this, MainStock.class);
                startActivity(intentstock);

                return true;


            case R.id.incoming:
                //activar pagina stock
                Intent intentin = new Intent(StockCategoria.this, MainVendedor.class);
                startActivity(intentin);
                return true;

            case R.id.pedidosEnviados:
                //activar pagina pedidos
                Intent intentsent = new Intent(StockCategoria.this, VendedorPedidosEnviados.class);
                startActivity(intentsent);
                return true;



            case R.id.logout:
                //logout
                Intent intentlog = new Intent(StockCategoria.this, Splash.class);
                startActivity(intentlog);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }

    static String tituloStorage;

    public void cambioTitulo() {
        String categoriaSeleccionada = getIntent().getStringExtra("categoriaSeleccionada");
        TextView textView = findViewById(R.id.stockCategoria);
        textView.setText(categoriaSeleccionada);
        tituloStorage = categoriaSeleccionada;
    }

    private void actualizarUI() {
        dbartic.collection("Stock").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                listaArticulosStock.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String categoria = doc.getString("Categoria");
                    if(categoria.equals(tituloStorage)) {
                        String nombreArticulo = doc.getString("NombreArticulo");
                        String cantidad = doc.getString("Cantidad");
                        ArticuloStock articuloStock = new ArticuloStock(categoria, nombreArticulo, cantidad);
                        listaArticulosStock.add(articuloStock);

                        editTextQuantityToAdd.setText("0");
                    }
                }
                if (listaArticulosStock.size() == 0) {
                    listViewArticulosStock.setAdapter(null);
                } else {
                    mAdapterArticulosStock = new MyCustomAdapterStockArticles(StockCategoria.this, listaArticulosStock, listViewArticulosStock, dbartic, editTextQuantityToAdd);
                    listViewArticulosStock.setAdapter(mAdapterArticulosStock);
                }
            }
        });
    }
}