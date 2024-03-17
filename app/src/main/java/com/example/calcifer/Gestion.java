package com.example.calcifer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Gestion extends AppCompatActivity {
    Button btnver;      // Botón para ver detectores
    Button btnagregar;  // Botón para agregar un detector

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion); // Establecer el diseño de la actividad

        btnagregar = findViewById(R.id.btnAgrega);   // Referenciar el botón de agregar
        btnver = findViewById(R.id.btnVerDetector);  // Referenciar el botón de ver detectores
    }

    // Método para manejar el clic en el botón de ver detectores
    public void BtnVerDetector(View v){
        Intent intent = new Intent(Gestion.this, Detectores.class); // Crear un intent para abrir la actividad de detectores
        startActivity(intent); // Iniciar la actividad de detectores
    }

    // Método para manejar el clic en el botón de agregar un detector
    public void BtnAgregar(View v){
        Intent intent = new Intent(Gestion.this, Agregar.class); // Crear un intent para abrir la actividad de agregar
        startActivity(intent); // Iniciar la actividad de agregar
    }

    // Método para cerrar sesión
    public void onCerrarSesionClickG(View view) {
        // Llamando al método cerrarSesion de la clase SessionManager
        SessionManager.cerrarSesion(this);
        finish(); // Cierra la actividad actual
    }
}
