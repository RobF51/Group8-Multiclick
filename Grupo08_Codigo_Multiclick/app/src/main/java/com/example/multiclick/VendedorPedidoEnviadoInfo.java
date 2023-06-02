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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VendedorPedidoEnviadoInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_pedido_enviado_info);
        listviewArticulosPedidoGen= findViewById(R.id.listpedidoartEnviado);



        cambioTitulo();

        showArticulos();
        showDireccion();
    }



    FirebaseFirestore dbPPArticulosShow = FirebaseFirestore.getInstance();

    ListView listviewArticulosPedidoGen;
    List<String> listaArticulosPedidoGen = new ArrayList<>();
    List<String>listaIdArticulosPedidoGen= new ArrayList<>();
    ArrayAdapter<String> mAdapterArticulosPedidoGen;


    String tituloStoragePEnviado;



    public void cambioTitulo (){

        String texto = getIntent().getStringExtra("pedidoSeleccionado");
        TextView textView = findViewById(R.id.idpedidotitle);
        textView.setText(texto);
        tituloStoragePEnviado = texto;



    }







    List <String> listaCarritoAuxiliarPP;

    private void showArticulos (){



        dbPPArticulosShow.collection("Pedidos")
                .whereEqualTo("NumPedido",tituloStoragePEnviado)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        listaArticulosPedidoGen.clear();
                        listaIdArticulosPedidoGen.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            listaIdArticulosPedidoGen.add(doc.getId());
                            listaArticulosPedidoGen.add(doc.getString("Articulo"));
                        }
                        //uso esta lista auxiliar para sacar los titulos ya en forma de string
                        listaCarritoAuxiliarPP = listaArticulosPedidoGen;

                        if (listaArticulosPedidoGen.size() == 0) {
                            listviewArticulosPedidoGen.setAdapter(null);
                        } else {
                            mAdapterArticulosPedidoGen = new ArrayAdapter<>(VendedorPedidoEnviadoInfo.this,R.layout.item_articulo_carrito,R.id.textViewArticuloCarrito, listaArticulosPedidoGen);
                            listviewArticulosPedidoGen.setAdapter(mAdapterArticulosPedidoGen);


                        }
                    }
                });


    }

    FirebaseFirestore dbShowDireccion = FirebaseFirestore.getInstance();

    private void showDireccion (){


        TextView textViewNombre = findViewById(R.id.NombreDireccionShow);
        TextView textViewApellido = findViewById(R.id.ApellidosDireccionShow);
        TextView textViewCP = findViewById(R.id.CPDireccionShow);
        TextView textViewDireccion = findViewById(R.id.CalleDireccionShow);
        TextView textViewMail = findViewById(R.id.emailDireccionShow);


        dbShowDireccion.collection("DireccionPedido")
                .whereEqualTo("NumPedido", tituloStoragePEnviado)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Recupera los datos del documento
                                String nombre = document.getString("nombre");
                                String apellido = document.getString("apellidos");
                                String cp = document.getString("codigoPostal");
                                String direccion = document.getString("direccion");
                                String mail = document.getString("Email");


                                // Por ejemplo, establece los valores en los TextView correspondientes
                                textViewNombre.setText(nombre);
                                textViewApellido.setText(apellido);
                                textViewCP.setText(cp);
                                textViewDireccion.setText(direccion);
                                textViewMail.setText(mail);
                            }
                        } else {
                            // Maneja el caso de error
                            Toast.makeText(VendedorPedidoEnviadoInfo.this, "Error al obtener los datos del documento", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }









    public String getTituloStoragePPendiente() {
        return tituloStoragePEnviado;
    }

    public static boolean isEnvioStatus() {
        return envioStatus;
    }

    static boolean envioStatus;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.stockbutton:
                Intent intentstock = new Intent(VendedorPedidoEnviadoInfo.this, MainStock.class);
                startActivity(intentstock);

                return true;


            case R.id.incoming:
                //activar pagina stock
                Intent intentin = new Intent(VendedorPedidoEnviadoInfo.this, MainVendedor.class);
                startActivity(intentin);
                return true;

            case R.id.pedidosEnviados:
                //activar pagina pedidos
                Intent intentsent = new Intent(VendedorPedidoEnviadoInfo.this, VendedorPedidosEnviados.class);
                startActivity(intentsent);
                return true;



            case R.id.logout:
                //logout
                Intent intentlog = new Intent(VendedorPedidoEnviadoInfo.this, Splash.class);
                startActivity(intentlog);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }


















}