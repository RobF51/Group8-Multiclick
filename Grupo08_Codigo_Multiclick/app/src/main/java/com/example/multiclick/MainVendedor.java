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


import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class MainVendedor extends AppCompatActivity {


    FirebaseFirestore dbPPendientes;

    ListView listviewPPendientes;
    List<String>listaPPendientes = new ArrayList<>();
    List<String>listaIdPPendientes = new ArrayList<>();
    ArrayAdapter<String> mAdapterPPendintes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbPPendientes = FirebaseFirestore.getInstance();
        listviewPPendientes = findViewById(R.id.viewpedidospendientes);

        actualizarUI();
        //hay datos en la base

    }

    private void actualizarUI() {



        dbPPendientes.collection("PedidosPendientes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        listaPPendientes.clear();
                        listaIdPPendientes.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            listaIdPPendientes.add(doc.getId());
                            listaPPendientes.add(doc.getString("NumPedido"));
                        }


                        if (listaPPendientes.size() == 0) {
                            listviewPPendientes.setAdapter(null);
                        } else {
                            mAdapterPPendintes = new MyCustomAdapterPPendientes(MainVendedor.this, listaPPendientes);

                            listviewPPendientes.setAdapter(mAdapterPPendintes);


                        }
                    }
                });

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
                Intent intentstock = new Intent(MainVendedor.this, MainStock.class);
                startActivity(intentstock);

                return true;


            case R.id.incoming:
                //activar pagina stock
                Intent intentin = new Intent(MainVendedor.this, MainVendedor.class);
                startActivity(intentin);
                return true;

            case R.id.pedidosEnviados:
                //activar pagina pedidos
                Intent intentsent = new Intent(MainVendedor.this, VendedorPedidosEnviados.class);
                startActivity(intentsent);
                return true;



            case R.id.logout:
                //logout
                Intent intentlog = new Intent(MainVendedor.this, Splash.class);
                startActivity(intentlog);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }




}