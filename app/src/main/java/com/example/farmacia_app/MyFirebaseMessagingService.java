package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.BatteryManager;
import android.util.Log;
import android.content.Intent;
import android.content.IntentFilter;

import android.Manifest;


import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public  class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "FCM_Channel";
    private TokenData tokenData;

    @Override
    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);

        // Verifique se o token foi gerado corretamente
        Log.d("FCM", "Novo token gerado: " + token);

        // Inscrever no tópico (exemplo: "Aparelhos")
        FirebaseMessaging.getInstance().subscribeToTopic("Aparelhos")
                .addOnCompleteListener(task -> {
                    String msg = task.isSuccessful() ? "Inscrição no tópico 'Aparelhos' bem-sucedida" : "Falha na inscrição no tópico";
                    Log.d("FCM", msg);
                });

        // Obter informações do dispositivo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tokenData = new TokenData(
                    token,
                    Build.MODEL + "_" + Build.ID,
                    "Android",
                    Build.VERSION.RELEASE,
                    Build.MANUFACTURER + " " + Build.MODEL,
                    "user123",  // Aqui você pode definir o username do usuário logado
                    40.7128,  // Exemplo de latitude
                    -74.0060, // Exemplo de longitude
                    LocalDateTime.now(),
                    "Wi-Fi",  // Tipo de conexão simulada
                    "1.0",     // Versão do app simulada
                    getBatteryLevel(), // Nível da bateria
                    getSystemLanguage(), // Idioma do sistema
                    getWifiNetworkName() // Nome da rede Wi-Fi
            );
        }

        // Salvar o token no SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcm_token", token);  // Salvando o token
        editor.apply();

        // Enviar o token para o Firebase Database
        sendTokenToFirebase(tokenData);

        // Atualizar dados periodicamente
        startDataUpdate();
    }

    private void sendTokenToFirebase(TokenData tokenData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tokens");

        String deviceId = tokenData.getDeviceId();
        String tipoDispositivo = tokenData.getTipoDispositivo();

        // Enviar o token e deviceId para o Firebase
        myRef.child(deviceId).child("token").setValue(tokenData.getToken());
        myRef.child(deviceId).child("deviceId").setValue(deviceId);
        myRef.child(deviceId).child("tipoDispositivo").setValue(tipoDispositivo)
                .addOnSuccessListener(aVoid -> Log.d("FCM", "Token enviado para o Firebase com sucesso!"))
                .addOnFailureListener(e -> Log.e("FCM", "Erro ao enviar token para o Firebase", e));
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Verifique se a mensagem contém uma notificação
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Envia o Broadcast para a Activity
            Intent intent = new Intent("com.example.farmacia_app.PERMISSAO_WIFI");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            // Exibir uma notificação no dispositivo
            if (title != null && body != null) {
                showNotification(title, body);
            }
        }
    }


    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Para dispositivos Android 8.0 ou superior, é necessário criar um canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "FCM Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Criar a notificação
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_notification) // Certifique-se de ter um ícone em res/drawable
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // Enviar a notificação
        notificationManager.notify(0, notification);
    }

    // Atualizar dados periodicamente
    private void startDataUpdate() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Atualiza os dados
                tokenData.atualizarDados();
                // Exibe as informações do dispositivo no log
                tokenData.exibirInformacoesDispositivo();
            }
        }, 0, 60000); // Atualiza a cada 60 segundos
    }

    // Método para obter o nível da bateria
    private int getBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    }

    // Método para obter o idioma do sistema
    private String getSystemLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage(); // Retorna o código do idioma (ex: "en", "pt")
    }

    // Método para obter o nome da rede Wi-Fi conectada
    private String getWifiNetworkName() {
        // Verifique a permissão de localização
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Envia uma Intent para a Activity para solicitar a permissão
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("requestPermission", true); // Indica que a permissão precisa ser solicitada
            startActivity(intent);
            return "Permissão necessária";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Para Android 10 ou superior, o acesso ao SSID está restrito
            Toast.makeText(this, "Acesso ao Wi-Fi desativado no Android 10 ou superior", Toast.LENGTH_SHORT).show();
            return "Não disponível";
        } else {
            // Para versões abaixo do Android 10
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getSSID();
        }
    }
}



