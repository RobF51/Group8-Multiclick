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

public class VendedorPedidosEnviados extends AppCompatActivity {

    FirebaseFirestore dbPEnviados;

    ListView listviewPEnviados;
    List<String> listaPEnviados = new ArrayList<>();
    List<String>listaIdPEnviados = new ArrayList<>();
    ArrayAdapter<String> mAdapterPEnviados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_pedidos_enviados);


        dbPEnviados = FirebaseFirestore.getInstance();
        listviewPEnviados = findViewById(R.id.viewpedidosEnviados);

        actualizarUI();

    }



    private void actualizarUI() {



        dbPEnviados.collection("PedidosRealizados")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        listaPEnviados.clear();
                        listaIdPEnviados.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            listaIdPEnviados.add(doc.getId());
                            listaPEnviados.add(doc.getString("NumPedido"));
                        }


                        if (listaPEnviados.size() == 0) {
                            listviewPEnviados.setAdapter(null);
                        } else {
                            mAdapterPEnviados = new MyCustomAdapterPEnviados(VendedorPedidosEnviados.this, listaPEnviados);

                            listviewPEnviados.setAdapter(mAdapterPEnviados);


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
                Intent intentstock = new Intent(VendedorPedidosEnviados.this, MainStock.class);
                startActivity(intentstock);

                return true;


            case R.id.incoming:
                //activar pagina stock
                Intent intentin = new Intent(VendedorPedidosEnviados.this, MainVendedor.class);
                startActivity(intentin);
                return true;

            case R.id.pedidosEnviados:
                //activar pagina pedidos
                Intent intentsent = new Intent(VendedorPedidosEnviados.this, VendedorPedidosEnviados.class);
                startActivity(intentsent);
                return true;



            case R.id.logout:
                //logout
                Intent intentlog = new Intent(VendedorPedidosEnviados.this, Splash.class);
                startActivity(intentlog);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }


















}