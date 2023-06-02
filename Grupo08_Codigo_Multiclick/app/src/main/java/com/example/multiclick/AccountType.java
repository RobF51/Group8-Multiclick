package com.example.multiclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountType extends AppCompatActivity {

   Button botonComprador;
   Button botonVendedor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        getSupportActionBar().hide();

       botonComprador = findViewById(R.id.botonComprador);
       botonComprador.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(AccountType.this, LoginComprador.class);
               startActivity(intent);
           }
       });

       botonVendedor = findViewById(R.id.botonVendedor);
       botonVendedor.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(AccountType.this, LoginVendedor.class);
               startActivity(intent);
           }
       });

    }
}