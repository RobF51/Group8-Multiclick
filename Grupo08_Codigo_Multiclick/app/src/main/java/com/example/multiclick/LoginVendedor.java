package com.example.multiclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginVendedor extends AppCompatActivity {

    Button botonLoginVendedor;
    TextView botonRegistroVendedor;
    TextView botonVolver;

    EditText textoEmail, textoPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_vendedor);

        getSupportActionBar().hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        textoEmail = findViewById(R.id.cajaCorreoVendedor);
        textoPassword = findViewById(R.id.cajaPassVendedor);

        botonLoginVendedor = findViewById(R.id.botonLoginVendedor);
        botonLoginVendedor.setOnClickListener(view -> {
            String email = textoEmail.getText().toString();
            String password = textoPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginVendedor.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            } else {
                // INICIAR SESION EN FIREBASE
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent = new Intent(LoginVendedor.this, MainVendedor.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginVendedor.this, "La autenticación falló.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        botonRegistroVendedor = findViewById(R.id.botonCrearCuentaVendedor);
        botonRegistroVendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textoEmail.getText().toString();
                String password = textoPassword.getText().toString();

                if (email.isEmpty()) {
                    textoEmail.setError("El campo email está vacío");
                } else if (!email.contains("@")) {
                    textoEmail.setError("No es una dirección válida");
                } else if (!email.contains(".")) {
                    textoEmail.setError("No es una dirección válida");
                } else if (!email.contains("multiclick.com")) {
                    textoEmail.setError("No es una dirección de Multiclick");
                } else if (password.length() < 6) {
                    textoPassword.setError("Debe tener al menos 6 caracteres");
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginVendedor.this, "Vendedor registrado", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginVendedor.this, MainVendedor.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginVendedor.this, "Error al crear el vendedor.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        botonVolver = findViewById(R.id.botonVolver);
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginVendedor.this, AccountType.class);
                startActivity(intent);
            }
        });
    }
}
