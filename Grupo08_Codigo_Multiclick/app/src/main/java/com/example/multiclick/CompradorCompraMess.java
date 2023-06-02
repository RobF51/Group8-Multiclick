package com.example.multiclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class CompradorCompraMess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprador_compra_mess);


        boolean StatusCompr = Carrito.isCompraStatus();

        if (StatusCompr){

            TextView textView = findViewById(R.id.idMessageStatus);
            textView.setText("Tu compra se ha realizado correctamente. Puedes volver a la pantalla principal");
        }else{
            TextView textView = findViewById(R.id.idMessageStatus);
            textView.setText("Ha habido un error con tu compra. Vuelve a intentarlo");

        }



    }

    public void GoHomeBuyer (View view){

        Intent intentGoHomeBuyer = new Intent(CompradorCompraMess.this, MainComprador.class);
        startActivity(intentGoHomeBuyer);

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
                Intent intentCarrito = new Intent(CompradorCompraMess.this, Carrito.class);
                startActivity(intentCarrito);
                return true;

            case R.id.home:
                //activar pagina stock
                Intent intentHome= new Intent(CompradorCompraMess.this, MainComprador.class);
                startActivity(intentHome);
                return true;

            case R.id.logout:
                //activar pagina stock
                Intent intentLogout= new Intent(CompradorCompraMess.this, Splash.class);
                startActivity(intentLogout);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }




}