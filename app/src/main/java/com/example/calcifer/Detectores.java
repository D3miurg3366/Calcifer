package com.example.calcifer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Detectores extends AppCompatActivity {
    private static final String TAG = "Detectores";
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    Spinner spinnerD;
    Button btnEliminar;
    Button btnVisualizar;
    ImageButton btnCS;
    private String selectedNode;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detectores); // Establecer el diseño de la actividad

        btnCS = findViewById(R.id.btncerrarsesion);
        spinnerD = findViewById(R.id.spnElegirD);
        btnVisualizar = findViewById(R.id.btnVer);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Verificar si el usuario actual está autenticado
        if (currentUser == null) {
            btnEliminar.setEnabled(false);
            btnEliminar.setVisibility(View.GONE);
            btnCS.setVisibility(View.GONE);
        } else {
            btnEliminar.setVisibility(View.VISIBLE);
        }

        // Escuchar una vez para obtener la lista de nodos de Firebase Realtime Database
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Crear una lista para almacenar los nombres de los nodos
                List<String> nodeList = new ArrayList<>();

                // Recorrer los nodos en la raíz y agregar sus nombres a la lista
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nodeName = snapshot.getKey();
                    if (nodeName != null) {
                        nodeList.add(nodeName);
                    }
                }

                // Crear un adaptador para el Spinner con la lista de nombres de nodos
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Detectores.this, android.R.layout.simple_spinner_item, nodeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Establecer el adaptador para el Spinner
                spinnerD.setAdapter(adapter);

                // Configurar un Listener para el Spinner
                spinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Obtener el nombre del nodo seleccionado
                        selectedNode = (String) parentView.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Mostrar un mensaje si no se ha seleccionado ningún nodo
                        Toast.makeText(Detectores.this, "Seleccione un detector", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Mostrar un mensaje de error en caso de fallo al leer los nodos
                Log.w(TAG, "Error al leer nodos en la raíz.", databaseError.toException());
            }
        });

        // Configurar el Listener para el botón de eliminación
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedNode != null) {
                    eliminarRegistro(selectedNode); // Llamar al método para eliminar el registro
                } else {
                    Toast.makeText(Detectores.this, "Seleccione un detector antes de eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el Listener para el botón de visualización
        btnVisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNode != null) {
                    // Abrir la actividad de información del detector con el nombre del nodo seleccionado
                    Intent intent = new Intent(Detectores.this, InformaDetector.class);
                    intent.putExtra("nombreNodo", selectedNode);
                    startActivity(intent);
                } else {
                    Toast.makeText(Detectores.this, "Seleccione un detector antes de ver", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para eliminar el registro de un detector en Firestore
    private void eliminarRegistro(String nombreDetector) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Detector")
                .document(nombreDetector)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Mostrar mensaje de éxito
                        Toast.makeText(Detectores.this, "Registro eliminado con éxito", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Mostrar mensaje de error en caso de fallo al eliminar el registro
                        Toast.makeText(Detectores.this, "Error al eliminar el registro de Firestore", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al eliminar el registro de Firestore", e);
                    }
                });
    }

    // Método para cerrar sesión
    public void onCerrarSesionClickD(View view) {
        // Llamando al método cerrarSesion de la clase SessionManager
        SessionManager.cerrarSesion(this);
        finish(); // Cierra la actividad actual
    }
}
