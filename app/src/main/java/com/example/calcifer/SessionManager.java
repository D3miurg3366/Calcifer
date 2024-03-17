package com.example.calcifer;
import android.content.Context;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;

public class SessionManager {

    // Método para cerrar sesión
    public static void cerrarSesion(Context context) {
        FirebaseAuth.getInstance().signOut(); // Cerrar sesión del usuario actual
        // Redirigir a la actividad de inicio de sesión o cualquier otra actividad deseada
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent); // Iniciar la actividad de inicio de sesión
    }

}
