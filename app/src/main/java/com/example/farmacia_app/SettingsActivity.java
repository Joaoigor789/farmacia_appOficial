package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {
    private TextView volumeLabel;
    private AudioManager audioManager;
    private SharedPreferences preferences;
    private static final int REQUEST_MIC_PERMISSION = 1; // Código para solicitar permissão
    private static final int REQUEST_LOCATION_PERMISSION = 2; // Código para solicitar permissão de localização
    private static final int REQUEST_NOTIFICATION_PERMISSION = 3; // Código para solicitar permissão de notificações

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Local onde o arquivo ZIP é descompactado
        String gifPath = getFilesDir() + "/drawable/giphy.gif"; // Aponte para o diretório onde o GIF foi descompactado

        // Configuração da imagem de fundo (GIF)
        ImageView gifBackground = findViewById(R.id.gif_background);
        Glide.with(this).load(new File(gifPath)).into(gifBackground);

        // Inicializar os Switches e SeekBar
        Switch micSwitch = findViewById(R.id.mic_switch);
        Switch darkModeSwitch = findViewById(R.id.dark_mode_switch);
        Switch notificationSwitch = findViewById(R.id.notification_switch);
        Switch locationSwitch = findViewById(R.id.location_switch);
        SeekBar volumeSeekBar = findViewById(R.id.volume_seekbar);
        volumeLabel = findViewById(R.id.volume_label);

        preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Recuperar os estados salvos
        micSwitch.setChecked(preferences.getBoolean("mic_enabled", false));
        darkModeSwitch.setChecked(preferences.getBoolean("dark_mode_enabled", false));
        notificationSwitch.setChecked(preferences.getBoolean("notification_enabled", false));
        locationSwitch.setChecked(preferences.getBoolean("location_enabled", false));

        int savedVolume = preferences.getInt("volume_level", 50);
        volumeSeekBar.setProgress(savedVolume);
        volumeLabel.setText("Volume: " + savedVolume);

        // Listener para as mudanças nos switches
        micSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("mic_enabled", isChecked);
            editor.apply();
            if (isChecked) {
                // Solicitar permissão para o microfone
                requestMicPermission();
            } else {
                // Desativar o uso do microfone
                // Aqui, você pode adicionar algum código se precisar desativar ou liberar o microfone
            }
        });

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("dark_mode_enabled", isChecked);
            editor.apply();
            setTheme(isChecked ? R.style.AppTheme_Dark : R.style.AppTheme);
            recreate();
        });

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("notification_enabled", isChecked);
            editor.apply();
            if (isChecked) {
                // Solicitar permissão para notificações, se necessário
                requestNotificationPermission();
            }
        });

        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("location_enabled", isChecked);
                    editor.apply();
                    if (isChecked) {
                        // Solicitar permissão para localização
                        requestLocationPermission();
                    }
        });

        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("location_enabled", isChecked);
            editor.apply();
        });

        /// Listener para a SeekBar de volume
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeLabel.setText("Volume: " + progress);

                // Mapeando o valor de 0 a 100 para o intervalo do AudioManager (0-15)
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int volume = (int) ((progress / 100.0) * maxVolume);
                if (audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("volume_level", seekBar.getProgress());
                editor.apply();
            }
        });
    }

    // Verificar e solicitar permissão para o microfone
    private void requestMicPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão para o microfone
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MIC_PERMISSION);
        }
    }

    // Verificar e solicitar permissão para a localização
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão para a localização
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_inicio) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_sobre) {
            showAboutDialog();
            return true;
        } else if (id == R.id.menu_licenca) {
            showLicenseDialog();  // Nova função para exibir informações de licença
            return true;
        } else if (id == R.id.menu_autor) {
            showAutorDialog(); //nova função para exibir informações do autor
            return true;
        } else if (id == R.id.menu_atualizacao) {
            ChangelogUtil.exibirChangelog(this); // Chama o método centralizado
            //nova função para exibir informações do changelog
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAutorDialog() {
        String info = "Autor: João Igor Rodrigues Pereira da Silva\n" +
                "Empresa: Farmacia AppTech Tecnology\n" +
                "Projeto: Aplicativo de Farmácia";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informações do Autor")
                .setMessage(info)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss(); // Fecha o diálogo
                });
        builder.create().show();
    }

    private void showAboutDialog() {
        String version = "Versão 1.3.2";
        String info = "Aplicativo para Farmacia";
        new AlertDialog.Builder(this)
                .setTitle("Sobre")
                .setMessage(info + "\n" + version)
                .setPositiveButton("OK", null)
                .show();
    }

    // Novo método para exibir as informações de licença
    private void showLicenseDialog() {
        String licenseInfo = "Copyright 2025 João Igor Rodrigues Pereira da Silva, Farmacia AppTech Tecnology\n\n" +
                "Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License (CC BY-NC-ND 4.0);\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at:\n" +
                "https://creativecommons.org/licenses/by-nc-nd/4.0/\n\n" +
                "You are allowed to copy and redistribute the material, but you may **not modify** it or use it for commercial purposes without explicit permission from the author.\n\n" +
                "The use of this application is permitted **only for educational purposes**.";

        new AlertDialog.Builder(this)
                .setTitle("Licença")
                .setMessage(licenseInfo)
                .setPositiveButton("OK", null)
                .show();
    }


    // Verificar e solicitar permissão para notificações
    private void requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String @NotNull [] permissions, int @NotNull [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MIC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida
                Toast.makeText(this, "Permissão de microfone concedida", Toast.LENGTH_SHORT).show();
            } else {
                // Permissão negada
                Toast.makeText(this, "Permissão de microfone negada", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida
                Toast.makeText(this, "Permissão de localização concedida", Toast.LENGTH_SHORT).show();
            } else {
                // Permissão negada
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida
                Toast.makeText(this, "Permissão de notificações concedida", Toast.LENGTH_SHORT).show();
            } else {
                // Permissão negada
                Toast.makeText(this, "Permissão de notificações negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
