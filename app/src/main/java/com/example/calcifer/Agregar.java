package com.example.calcifer;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Agregar extends AppCompatActivity {

    private EditText Nombre; // Campo de texto para ingresar el nombre del detector
    private EditText Area;   // Campo de texto para ingresar el área del detector
    private EditText Sala;   // Campo de texto para ingresar la sala del detector
    private Button AgregarD; // Botón para agregar un nuevo detector

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar); // Establecer el diseño de la actividad

        // Inicializar los campos de texto y el botón
        Nombre = findViewById(R.id.NombreD);
        Area = findViewById(R.id.AreaD);
        Sala = findViewById(R.id.SalaD);
        AgregarD = findViewById(R.id.btnAgregarD);

        // Configurar un Listener para el botón de agregar
        AgregarD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarDetector(); // Llamar al método para agregar el detector
            }
        });
    }

    // Método para agregar un nuevo detector a la base de datos
    public void agregarDetector() {
        // Obtener los valores de los campos
        String nombre = Nombre.getText().toString();
        String area = Area.getText().toString();
        String sala = Sala.getText().toString();

        // Verificar que se haya ingresado al menos el nombre
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Ingrese el nombre del detector", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un mapa para almacenar los datos del detector
        Map<String, Object> detectorData = new HashMap<>();
        detectorData.put("nombre", nombre);
        detectorData.put("sala", sala);
        detectorData.put("area", area);
        detectorData.put("MaxTemperatura", 0);  // Valor de ejemplo, puedes cambiarlo
        detectorData.put("MaxGasHumo", 0);       // Valor de ejemplo, puedes cambiarlo

        // Obtener la referencia de la colección "Detector"
        CollectionReference detectorsCollection = FirebaseFirestore.getInstance().collection("Detector");

        // Agregar un nuevo documento con ID igual al nombre del detector
        detectorsCollection.document(nombre)
                .set(detectorData)
                .addOnSuccessListener(aVoid -> {
                    // Mostrar un mensaje de éxito
                    Toast.makeText(Agregar.this, "Detector agregado exitosamente", Toast.LENGTH_SHORT).show();
                    // Puedes agregar aquí cualquier lógica adicional después de agregar el detector
                })
                .addOnFailureListener(e -> {
                    // Mostrar un mensaje de error en caso de fallo
                    Log.w(TAG, "Error al agregar documento", e);
                    Toast.makeText(Agregar.this, "Error al agregar detector: " + e.toString(), Toast.LENGTH_SHORT).show();
                });
    }

    // Método para cerrar sesión
    public void onCerrarSesionClickA(View view) {
        // Llamando al método cerrarSesion de la clase SessionManager
        SessionManager.cerrarSesion(this);
        finish(); // Cierra la actividad actual
    }
}
