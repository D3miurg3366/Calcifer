package com.example.calcifer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Login";
    private String Usuario;
    private EditText txtUsuario;
    private String Contrasena;
    private EditText txtContrasena;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise(); // Inicializar componentes de la interfaz de usuario y Firebase
    }

    // Método para inicializar componentes de la interfaz de usuario y Firebase
    private void initialise(){
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        mAuth = FirebaseAuth.getInstance(); // Obtener instancia de FirebaseAuth
    }

    // Método para ir a la actividad principal después de la autenticación exitosa
    private void goHome(){
        Toast.makeText(MainActivity.this, "Autenticacion exitosa.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, Gestion.class);
        startActivity(intent);
    }

    // Método para iniciar sesión
    public void login(View view){
        Usuario = txtUsuario.getText().toString();
        Contrasena = txtContrasena.getText().toString();

        if (!TextUtils.isEmpty(Usuario) && !TextUtils.isEmpty(Contrasena)){

            mAuth.signInWithEmailAndPassword(Usuario, Contrasena).addOnCompleteListener(this, task ->{
                if (task.isSuccessful()){
                    goHome(); // Ir a la actividad principal después de la autenticación exitosa
                } else {
                    Toast.makeText(MainActivity.this, "Autenticacion fallida.", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(MainActivity.this, "LLene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para enviar notificación usando FCM
    private void enviarNotificacion(String evento, String mensaje) {
        // Lógica para enviar la notificación, por ejemplo, usando FCM
        // Código de envío de notificación omitido por brevedad
    }

    // Método para obtener el token del dispositivo
    private String obtenerTokenDispositivo() {
        // Aquí obtienes el token del dispositivo usando FirebaseMessaging
        // Código para obtener el token del dispositivo omitido por brevedad
        return "";
    }

    // Método para enviar notificación a través de FCM
    private void enviarNotificacionFCM(JSONObject json) {
        // Lógica para enviar notificación a través de FCM
        // Código de envío de notificación a través de FCM omitido por brevedad
    }

    // Método para iniciar sesión como usuario normal
    public void userNormal(View v){
        Intent intent = new Intent(MainActivity.this, Detectores.class);
        startActivity(intent); // Ir a la actividad Detectores
    }
}
