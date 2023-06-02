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



public class MainComprador extends AppCompatActivity{

    //esta instacia db2 es la de pedidos
    FirebaseFirestore db2;

    ListView listviewCategorias;
    List<String>listaCategorias = new ArrayList<>();
    List<String>listaIdCategorias = new ArrayList<>();
    ArrayAdapter<String> mAdapterCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comprador);

        db2 = FirebaseFirestore.getInstance();
        listviewCategorias = findViewById(R.id.viewcategorias);

        InicializarCategorias();

    //se actualiza la UI con las categorias
        actualizarUI();

    }


    private void actualizarUI() {



        db2.collection("Categorias")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        listaCategorias.clear();
                        listaIdCategorias.clear();

                        for (QueryDocumentSnapshot doc : value) {
                            listaIdCategorias.add(doc.getId());
                            listaCategorias.add(doc.getString("Categoria"));
                        }


                        if (listaCategorias.size() == 0) {
                            listviewCategorias.setAdapter(null);
                        } else {
                            mAdapterCategorias = new MyCustomAdapter(MainComprador.this, listaCategorias);

                            listviewCategorias.setAdapter(mAdapterCategorias);


                        }
                    }
                });

    }

    public void InicializarCategorias(){

        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("PS5");
        categorias.add("PS4");
        categorias.add("XboxOneXS");
        categorias.add("XboxONE");
        categorias.add("NintendoSwitch");
        categorias.add("PC");

        Map <String, Object> mapCateg = new HashMap<>();

        //delete all
        for (int i = 0;i< categorias.size();i++){
            String iConvert = Integer.toString(i);
            db2.collection("Categorias").document(iConvert).delete();
        }

        for (int i = 0;i< categorias.size();i++) {
            mapCateg.put("Categoria", categorias.get(i));
            String iConvert = Integer.toString(i);
            db2.collection("Categorias").document(iConvert).set(mapCateg);

        }

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
                Intent intentCarrito = new Intent(MainComprador.this, Carrito.class);
                startActivity(intentCarrito);
                return true;

            case R.id.home:
                //activar pagina stock
                Intent intentHome= new Intent(MainComprador.this, MainComprador.class);
                startActivity(intentHome);
                return true;

            case R.id.logout:
                //activar pagina stock
                Intent intentLogout= new Intent(MainComprador.this, Splash.class);
                startActivity(intentLogout);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }





    public void GoCategMas(View view) {

        Button boton = (Button) findViewById(R.id.botoncategmas);

        // Establecer el OnClickListener
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el texto del TextView
                TextView textView = (TextView) findViewById(R.id.textViewcateg);
                String texto = textView.getText().toString();

                // Ir a la siguiente actividad
                Intent intent = new Intent(MainComprador.this, ComprCategTipos.class);
                intent.putExtra("texto", texto);
                startActivity(intent);
            }
        });




    }



}