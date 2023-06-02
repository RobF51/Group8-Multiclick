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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carrito extends AppCompatActivity {

    FirebaseFirestore dbArticulosShow = FirebaseFirestore.getInstance();

    ListView listviewArticulosCarrito;
    List<String> listaArticulosCarrito = new ArrayList<>();
    List<String>listaIdArticulosCarrito= new ArrayList<>();
    ArrayAdapter<String> mAdapterArticulosCarrito;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);


        listviewArticulosCarrito= findViewById(R.id.viewlistarticulosCarrito);

        actualizarUI();

    }

    List <String> listaCarritoAuxiliar;
    String emailusuario = LoginComprador.getEmailAux();

    private void actualizarUI() {



        dbArticulosShow.collection("Carritos")
                .whereEqualTo("Mail",emailusuario)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        listaArticulosCarrito.clear();
                        listaIdArticulosCarrito.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            listaIdArticulosCarrito.add(doc.getId());
                            listaArticulosCarrito.add(doc.getString("Articulo"));
                        }
                        //uso esta lista auxiliar para sacar los titulos ya en forma de string
                            listaCarritoAuxiliar = listaArticulosCarrito;

                        if (listaArticulosCarrito.size() == 0) {
                            listviewArticulosCarrito.setAdapter(null);
                        } else {
                            mAdapterArticulosCarrito = new ArrayAdapter<>(Carrito.this,R.layout.item_articulo_carrito,R.id.textViewArticuloCarrito, listaArticulosCarrito);
                            listviewArticulosCarrito.setAdapter(mAdapterArticulosCarrito);


                        }
                    }
                });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comprador, menu);
//hola
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.carrito:
                //activar pagina stock
                Intent intentCarrito = new Intent(Carrito.this, Carrito.class);
                startActivity(intentCarrito);
                return true;

            case R.id.home:
                //activar pagina stock
                Intent intentHome= new Intent(Carrito.this, MainComprador.class);
                startActivity(intentHome);
                return true;

            case R.id.logout:
                //activar pagina stock
                Intent intentLogout= new Intent(Carrito.this, Splash.class);
                startActivity(intentLogout);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }






    FirebaseFirestore dbAddPedido = FirebaseFirestore.getInstance();
    FirebaseFirestore dbAddPedidoPendiente = FirebaseFirestore.getInstance();

    public static boolean isCompraStatus() {
        return compraStatus;
    }

    static boolean compraStatus;
    String NumPedido;

    public void pagarPedido(View view) {

        Date fechaHoraActual = new Date();

        // Crear un formato de fecha y hora
        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");

        // Convertir la fecha y hora actual a una cadena de texto
        String fechaHoraActualTexto = formatoFechaHora.format(fechaHoraActual);




        Map <String, Object> mapAddPedido = new HashMap<>();
        Map <String, Object> mapAddPedidoPendiente = new HashMap<>();

        NumPedido = fechaHoraActualTexto+emailusuario;

        crearDireccion();
        if (direccionStatus){

            for (int i = 0; i < listaCarritoAuxiliar.size(); i++) {
                mapAddPedido.put("NumPedido", NumPedido);
                mapAddPedido.put("Mail", emailusuario);

                mapAddPedido.put("Articulo", listaCarritoAuxiliar.get(i));

                dbAddPedido.collection("Pedidos").add(mapAddPedido);

                //aqui lo paso tambien a pedidos pendientes
                mapAddPedidoPendiente.put("NumPedido", NumPedido);
                mapAddPedidoPendiente.put("Mail", emailusuario);
            }





        }else{

        }


        try {
            if (mapAddPedidoPendiente.isEmpty()){

                throw new Exception("No hay items");

            }else{
                dbAddPedidoPendiente.collection("PedidosPendientes").add(mapAddPedidoPendiente);
            }



            if (direccionStatus){
                Intent intentCompraMess = new Intent(Carrito.this, CompradorCompraMess.class);
                startActivity(intentCompraMess);

                compraStatus = true;

            }else {
                throw new Exception("No se ha completado la direccionAAAA");
            }



        }catch(Exception e){
            Intent intentCompraMess = new Intent(Carrito.this, CompradorCompraMess.class);
            startActivity(intentCompraMess);
            compraStatus = false;

        }











        BorrarCarrito();
        actualizarUI();
    }





    CollectionReference coleccion = FirebaseFirestore.getInstance().collection("Carritos");
    public void BorrarCarrito(){


        // Eliminar todos los documentos de la colección
        coleccion.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documento : task.getResult()) {
                    documento.getReference().delete();
                }
            }
    });
    }

    FirebaseFirestore dbAddDireccion= FirebaseFirestore.getInstance();
    boolean direccionStatus = true;
    public void crearDireccion (){




        EditText editTextNombre, editTextApellidos, editTextDireccion, editTextCodigoPostal;

        editTextNombre = findViewById(R.id.NameDireccion);
        editTextApellidos = findViewById(R.id.ApellidosDireccion);
        editTextDireccion = findViewById(R.id.CalleDireccion);
        editTextCodigoPostal = findViewById(R.id.CPDireccion);


                // Obtén los valores de los campos de entrada
                String nombreDir = editTextNombre.getText().toString();
                String apellidosDir = editTextApellidos.getText().toString();
                String direccionDir = editTextDireccion.getText().toString();
                String codigoPostalDir = editTextCodigoPostal.getText().toString();
                String mail = LoginComprador.getEmailAux();


                // Crea un mapa con las claves y valores
                Map<String, Object> data = new HashMap<>();
                data.put("nombre", nombreDir);
                data.put("apellidos", apellidosDir);
                data.put("direccion", direccionDir);
                data.put("codigoPostal", codigoPostalDir);
                data.put("NumPedido", NumPedido );
                data.put("Email", mail );





        // Verifica si el campo de entrada está vacío


            try {
                if (nombreDir.isEmpty() || apellidosDir.isEmpty() || direccionDir.isEmpty()|| codigoPostalDir.isEmpty() ) {
                    Toast.makeText(Carrito.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                    throw new Exception("La direccion esta vacia");



                } else {
                // Crea un nuevo documento en la colección "DireccionPedido" con el mapa de datos
                dbAddDireccion.collection("DireccionPedido")
                        .add(data);
                    direccionStatus = true;

                Toast.makeText(Carrito.this, "Direccion añadida correctamente ", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                direccionStatus = false;
                Toast.makeText(Carrito.this, "Error al guardar el documento: " + e.getMessage(), Toast.LENGTH_SHORT).show();


            }









    }





}