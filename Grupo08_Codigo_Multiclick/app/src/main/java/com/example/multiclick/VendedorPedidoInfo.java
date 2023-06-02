package com.example.multiclick;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
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


public class VendedorPedidoInfo extends AppCompatActivity {


    FirebaseFirestore dbPPArticulosShow = FirebaseFirestore.getInstance();

    ListView listviewArticulosPedidoGen;
    List<String> listaArticulosPedidoGen = new ArrayList<>();
    List<String>listaIdArticulosPedidoGen= new ArrayList<>();
    ArrayAdapter<String> mAdapterArticulosPedidoGen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_pedido_info);
        listviewArticulosPedidoGen= findViewById(R.id.listpedidoart);


        cambioTitulo();
        showDireccion();
        showArticulos();

    }

    String tituloStoragePPendiente;



    public void cambioTitulo (){

        String texto = getIntent().getStringExtra("pedidoSeleccionado");
        TextView textView = findViewById(R.id.idpedidotitle);
        textView.setText(texto);
        tituloStoragePPendiente = texto;



    }







    List <String> listaCarritoAuxiliarPP;

    private void showArticulos (){



        dbPPArticulosShow.collection("Pedidos")
                .whereEqualTo("NumPedido",tituloStoragePPendiente)
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
                            mAdapterArticulosPedidoGen = new ArrayAdapter<>(VendedorPedidoInfo.this,R.layout.item_articulo_carrito,R.id.textViewArticuloCarrito, listaArticulosPedidoGen);
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
                .whereEqualTo("NumPedido", tituloStoragePPendiente)
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
                            Toast.makeText(VendedorPedidoInfo.this, "Error al obtener los datos del documento", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }






    public String getTituloStoragePPendiente() {
        return tituloStoragePPendiente;
    }

    public static boolean isEnvioStatus() {
        return envioStatus;
    }

    static boolean envioStatus;
    public void enviarPedido(View view){


        if (!listaArticulosPedidoGen.isEmpty()){
            for (String articulo : listaArticulosPedidoGen){
                String[] datos = articulo.split("-");
                String nombreCategoria = datos[1];
                String nombreArticulo = datos[0];
                // Toast.makeText(VendedorPedidoInfo.this, nombreCategoria, Toast.LENGTH_SHORT).show();
                // Toast.makeText(VendedorPedidoInfo.this, nombreArticulo, Toast.LENGTH_SHORT).show();
                dbPPArticulosShow.collection("Stock").whereEqualTo("Categoria", nombreCategoria).whereEqualTo("NombreArticulo", nombreArticulo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String cantidadActualString = document.getString("Cantidad");
                                int cantidadActual = Integer.parseInt(cantidadActualString);
                                if (cantidadActual > 0){
                                    int nuevaCantidad = cantidadActual - 1;
                                    String nuevaCantidadString = String.valueOf(nuevaCantidad);
                                    document.getReference().update("Cantidad", nuevaCantidadString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Toast.makeText(VendedorPedidoInfo.this, "Quantity updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Toast.makeText(VendedorPedidoInfo.this, "Update quantity failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(VendedorPedidoInfo.this, "Stock insufficient", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        }













            String numPedido = tituloStoragePPendiente; // Reemplaza con el valor que ya tienes

            FirebaseFirestore dbSend = FirebaseFirestore.getInstance();

// Realiza una consulta para obtener el documento con el NumPedido deseado
            Query query = dbSend.collection("PedidosPendientes").whereEqualTo("NumPedido", numPedido);
            query.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            if (!querySnapshot.isEmpty()) {
                                // Se encontraron documentos que coinciden con el NumPedido

                                // Obtén el primer documento encontrado (suponemos que solo hay uno)
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                                // Obtén los datos del documento encontrado
                                Map<String, Object> pedidoData = document.getData();

                                // Agrega los datos del pedido en la tabla de pedidos realizados
                                dbSend.collection("PedidosRealizados").add(pedidoData)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                // La réplica del documento se agregó con éxito en la tabla de pedidos realizados

                                                // Elimina el documento de la tabla de pedidos pendientes
                                                document.getReference().delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                // El documento se eliminó con éxito de la tabla de pedidos pendientes
                                                                Intent intent = new Intent(VendedorPedidoInfo.this, VendedorEnvioMess.class);
                                                                
                                                                envioStatus=true;
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                envioStatus=false;
                                                                // Ocurrió un error al eliminar el documento de la tabla de pedidos pendientes
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                envioStatus=false;
                                                // Ocurrió un error al agregar la réplica del documento en la tabla de pedidos realizados
                                            }
                                        });
                            } else {
                                // No se encontraron documentos que coincidan con el NumPedido
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            envioStatus=false;
                            // Ocurrió un error al realizar la consulta
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
                Intent intentstock = new Intent(VendedorPedidoInfo.this, MainStock.class);
                startActivity(intentstock);

                return true;


            case R.id.incoming:
                //activar pagina stock
                Intent intentin = new Intent(VendedorPedidoInfo.this, MainVendedor.class);
                startActivity(intentin);
                return true;

            case R.id.pedidosEnviados:
                //activar pagina pedidos
                Intent intentsent = new Intent(VendedorPedidoInfo.this, VendedorPedidosEnviados.class);
                startActivity(intentsent);
                return true;



            case R.id.logout:
                //logout
                Intent intentlog = new Intent(VendedorPedidoInfo.this, Splash.class);
                startActivity(intentlog);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }



















    }



