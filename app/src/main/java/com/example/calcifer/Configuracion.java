package com.example.calcifer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Configuracion extends AppCompatActivity {
    Spinner Temperatura; // Selector para la temperatura
    Spinner GasHumo;     // Selector para el gas y humo
    String nombreDelDetector; // Nombre del detector seleccionado

    // Obtén la referencia de Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion); // Establecer el diseño de la actividad

        // Recuperar el nombre del detector de los extras
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("nombreNodo")) {
            nombreDelDetector = extras.getString("nombreNodo");
        }

        Temperatura = findViewById(R.id.TemperaturaM); // Referenciar el selector de temperatura
        GasHumo = findViewById(R.id.Gashumo);         // Referenciar el selector de gas y humo

        // Configurar el adapter para los Spinners con tus opciones
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones_configuracion, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Temperatura.setAdapter(adapter); // Establecer el adaptador para el selector de temperatura
        GasHumo.setAdapter(adapter);     // Establecer el adaptador para el selector de gas y humo

        Button btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Actualizar los campos en Firestore
                actualizarCamposFirestore(); // Llamar al método para actualizar los campos en Firestore
            }
        });
    }

    // Método para actualizar los campos en Firestore
    private void actualizarCamposFirestore() {
        if (nombreDelDetector != null) { // Verificar que el nombre del detector no sea nulo
            // Obtener la opción seleccionada y convertirla a cadena
            String opcionTemperatura = String.valueOf(Temperatura.getSelectedItem());
            String opcionGasHumo = String.valueOf(GasHumo.getSelectedItem());

            // Crear mapas para actualizar los campos especificados
            Map<String, Object> campoTemperatura = new HashMap<>();
            campoTemperatura.put("MaxTemperatura", opcionTemperatura);

            Map<String, Object> campoGasHumo = new HashMap<>();
            campoGasHumo.put("MaxGasHumo", opcionGasHumo);

            // Obtener la referencia del documento del detector
            DocumentReference detectorRef = db.collection("Detector").document(nombreDelDetector);

            // Actualizar los campos en Firestore
            detectorRef.update(campoTemperatura)
                    .addOnSuccessListener(aVoid -> {
                        // Mostrar mensaje de éxito
                        Toast.makeText(Configuracion.this, "Valor Temperatura actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Mostrar mensaje de error en caso de fallo
                        Toast.makeText(Configuracion.this, "Error al actualizar el campo MaxTemperatura", Toast.LENGTH_SHORT).show();
                        Log.e("Configuracion", "Error al actualizar el campo MaxTemperatura", e);
                    });

            detectorRef.update(campoGasHumo)
                    .addOnSuccessListener(aVoid -> {
                        // Mostrar mensaje de éxito
                        Toast.makeText(Configuracion.this, "Valor Gas y Humo actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Mostrar mensaje de error en caso de fallo
                        Toast.makeText(Configuracion.this, "Error al actualizar el campo MaxGasHumo", Toast.LENGTH_SHORT).show();
                        Log.e("Configuracion", "Error al actualizar el campo MaxGasHumo", e);
                    });
        } else {
            // Mostrar mensaje de error si el nombre del detector es nulo
            Toast.makeText(Configuracion.this, "Error: nombreDelDetector es nulo", Toast.LENGTH_SHORT).show();
            Log.e("Configuracion", "Error: nombreDelDetector es nulo");
        }
    }

    // Método para cerrar sesión
    public void onCerrarSesionClick(View view) {
        // Llamando al método cerrarSesion de la clase SessionManager
        SessionManager.cerrarSesion(this);
        finish(); // Cierra la actividad actual
    }
}
