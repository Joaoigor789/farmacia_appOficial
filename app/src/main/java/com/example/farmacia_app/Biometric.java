package com.example.farmacia_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Biometric extends AppCompatActivity {

    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_dispositivo);

        ImageView biometricIcon = findViewById(R.id.biometric_icon);
        textError = findViewById(R.id.text_error);

        // Inicializar o banco de dados
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Obter ID único do dispositivo
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Verificar se o dispositivo já está registrado no banco de dados
        if (databaseHelper.isDeviceRegistered(deviceId)) {
            // Dispositivo já registrado, liberar acesso
            textError.setVisibility(View.GONE);
            Toast.makeText(this, "Acesso liberado!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Biometric.this, MainActivity.class));
            finish();
        } else {
            // Dispositivo não registrado, registrar no banco de dados
            databaseHelper.registerDevice(deviceId);
            Toast.makeText(this, "Dispositivo registrado com sucesso!", Toast.LENGTH_SHORT).show();

            // Liberar acesso após registro
            startActivity(new Intent(Biometric.this, MainActivity.class));
            finish();
        }
    }
}
