package com.example.calcifer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InformaDetector extends AppCompatActivity {

    DatabaseReference testRef; // Referencia a la base de datos
    TextView txtTemperatura;    // TextView para mostrar la temperatura
    TextView txtHumoGas;        // TextView para mostrar el nivel de humo/gas
    TextView txtNombreDI;       // TextView para mostrar el nombre del detector
    Button btnConfigurar;       // Botón para configurar el detector
    ImageButton btncs;          // Botón para cerrar sesión
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser(); // Usuario actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informa_detector); // Establecer el diseño de la actividad

        // Obtener el nombre del nodo pasado como extra en el Intent
        String nombreNodo = getIntent().getStringExtra("nombreNodo");

        // Configurar la referencia de la base de datos para apuntar al nodo correcto
        testRef = FirebaseDatabase.getInstance().getReference(nombreNodo);

        // Referenciar los elementos de la interfaz de usuario
        btnConfigurar = findViewById(R.id.btnConfigura);
        txtNombreDI = findViewById(R.id.txtNombreDI);
        txtTemperatura = findViewById(R.id.txtTemperatura);
        txtHumoGas = findViewById(R.id.txtHumoGas);
        btncs = findViewById(R.id.btncerrarsesion);

        // Establecer el nombre del detector en el TextView correspondiente
        txtNombreDI.setText(nombreNodo);

        // Verificar si el usuario actual está autenticado
        if (currentUser == null) {
            btnConfigurar.setEnabled(false);
            btnConfigurar.setVisibility(View.GONE);
            btncs.setVisibility(View.GONE);
        } else {
            btnConfigurar.setVisibility(View.VISIBLE);
        }

        // Leer temperatura desde la base de datos
        testRef.child("Temperatura").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double temperatura = dataSnapshot.getValue(Double.class);
                if (temperatura != null) {
                    // Actualizar el TextView con el valor de la temperatura
                    txtTemperatura.setText(temperatura.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Manejar errores al leer la temperatura
                Log.w("TAG", "Error al leer la temperatura.", error.toException());
            }
        });

        // Leer nivel de humo/gas desde la base de datos
        testRef.child("Humo-Gas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double humoGas = dataSnapshot.getValue(Double.class);
                if (humoGas != null) {
                    // Actualizar el TextView con el valor de humo/gas
                    txtHumoGas.setText(humoGas.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Manejar errores al leer el nivel de humo/gas
                Log.w("TAG", "Error al leer el nivel de Humo/Gas.", error.toException());
            }
        });
    }

    // Método para ir a la pantalla de configuración del detector
    public void irAConfiguracion(View view) {
        Intent intent = new Intent(InformaDetector.this, Configuracion.class);
        String nombreNodo = getIntent().getStringExtra("nombreNodo");
        intent.putExtra("nombreNodo", nombreNodo);
        startActivity(intent);
    }

    // Método para cerrar sesión
    public void onCerrarSesionClickIA(View view) {
        // Llamando al método cerrarSesion de la clase SessionManager
        SessionManager.cerrarSesion(this);
        finish(); // Cierra la actividad actual
    }
}
