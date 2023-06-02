package com.example.multiclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class VendedorEnvioMess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_envio_mess);

        boolean StatusEnvio = VendedorPedidoInfo.isEnvioStatus();

        if (StatusEnvio){

            TextView textView = findViewById(R.id.idMessageStatus);
            textView.setText("El pedido se ha enviado correctamente. Puedes volver a mas pedidos");
        }else{
            TextView textView = findViewById(R.id.idMessageStatus);
            textView.setText("Ha habido un error con el envio. Contacta a soporte");

        }



    }


    public void GoHomeSeller (View view){

        Intent intent = new Intent(VendedorEnvioMess.this, MainVendedor.class);
        startActivity(intent);
    }
}