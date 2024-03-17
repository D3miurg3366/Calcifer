package com.example.calcifer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.IOException;
import java.util.Random;
public class Fcm extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("token","mi token es:"+s);

    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();

        if (remoteMessage.getData().size() > 0) {
            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("detalle");
            String foto = remoteMessage.getData().get("foto");

            // Obtener información adicional de Firestore
            String nombreDetector = "Obtener de Firestore";  // Reemplazar con la lógica para obtener el nombre
            String area = "Obtener de Firestore";  // Reemplazar con la lógica para obtener el área
            String sala = "Obtener de Firestore";  // Reemplazar con la lógica para obtener la sala

            // Verificar si la temperatura es elevada
            int temperatura = Integer.parseInt(remoteMessage.getData().get("temperatura"));
            if (temperatura >= 28) {
                mayorqueoreo(titulo, detalle, nombreDetector, area, sala, "Temperatura Elevada");
            }
        }
    }


    private void mayorqueoreo(String titulo, String detalle, String nombreDetector, String area, String sala, String evento) {
        String id = "mensaje";
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(detalle)
                .setContentIntent(clicknoti())
                .setContentInfo("nuevo");

        // Agregar la información adicional al intent para pasar a la actividad correspondiente
        Intent nf = new Intent(getApplicationContext(), MainActivity.class);
        nf.putExtra("color", "rojo");
        nf.putExtra("nombreDetector", nombreDetector);
        nf.putExtra("area", area);
        nf.putExtra("sala", sala);
        nf.putExtra("evento", evento);

        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nf, PendingIntent.FLAG_MUTABLE));

        Random random = new Random();
        int idNotity = random.nextInt(8000);

        assert nm != null;
        nm.notify(idNotity, builder.build());
    }
    public PendingIntent clicknoti(){
        Intent nf=new Intent(getApplicationContext(), MainActivity.class);
        nf.putExtra("color","rojo");
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,nf, PendingIntent.FLAG_MUTABLE);
    }

}
