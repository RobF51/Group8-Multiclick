package com.example.multiclick;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainStock extends AppCompatActivity {
    FirebaseFirestore db2;
    ListView listviewCategorias;
    List<String> listaCategorias = new ArrayList<>();
    List<String>listaIdCategorias = new ArrayList<>();
    ArrayAdapter<String> mAdapterMainStock;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference stockRef = db.collection("Stock");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stock);
        db2 = FirebaseFirestore.getInstance();
        listviewCategorias = findViewById(R.id.viewcategorias);
        actualizarUI();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.stockbutton:
                Intent intentstock = new Intent(MainStock.this, MainStock.class);
                startActivity(intentstock);

            return true;


            case R.id.incoming:
                //activar pagina stock
                Intent intentin = new Intent(MainStock.this, MainVendedor.class);
                startActivity(intentin);
                return true;

            case R.id.pedidosEnviados:
                //activar pagina pedidos
                Intent intentsent = new Intent(MainStock.this, VendedorPedidosEnviados.class);
                startActivity(intentsent);
                return true;



            case R.id.logout:
                //logout
                Intent intentlog = new Intent(MainStock.this, Splash.class);
                startActivity(intentlog);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }

    private void actualizarUI() {
        db2.collection("Categorias").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                listaCategorias.clear();

                for (QueryDocumentSnapshot doc : value) {
                    listaCategorias.add(doc.getString("Categoria"));
                }

                if (listaCategorias.size() == 0) {
                    listviewCategorias.setAdapter(null);
                } else {
                    mAdapterMainStock = new MyCustomAdapterMainStock(MainStock.this, listaCategorias);
                    listviewCategorias.setAdapter(mAdapterMainStock);
                }
            }
        });
    }
}