package com.example.farmacia_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PolicyActivity extends AppCompatActivity {








    private MediaPlayer mediaPlayer;    // Adiciona o MediaPlayer para tocar o áudio

    private static final String TAG = "PolicyActivity"; // Tag para o log




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.politicas_publicas_layout);







        // Inicializando o MediaPlayer para tocar o áudio
        mediaPlayer = MediaPlayer.create(this, R.raw.trofeu); // Troféu.mp3 em raw


        configurarBotaoEstudo();


    }

    private void configurarBotaoEstudo() {
        Button btnIniciarEstudo = findViewById(R.id.btnIniciarEstudo);
        btnIniciarEstudo.setOnClickListener(v -> {
            Intent intent = new Intent(PolicyActivity.this, VideoEstudoActivity.class);
            startActivity(intent);
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
            Toast.makeText(this, "Iniciando estudo sobre políticas públicas...", Toast.LENGTH_SHORT).show();
        });
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
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        } else if (id == R.id.menu_sobre) {
            showAboutDialog();
            return true;
        } else if (id == R.id.menu_licenca) {
            showLicenseDialog();
            return true;
        } else if (id == R.id.menu_autor) {
            showAutorDialog();
            return true;
        } else if (id == R.id.menu_atualizacao) {
            ChangelogUtil.exibirChangelog(this);
            return true;
        } else if (id == R.id.menu_perfil) {
            try {
                Intent intent = new Intent(this, PerfilActivity.class);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                // Captura e exibe o erro
                showErrorDialog(e);
                return false;
            }
        }
        
        
        return super.onOptionsItemSelected(item);
    }

    private void showErrorDialog(Exception e) {

    }

    private void exibirChangelog() {
        String changelog = "Versão 1.0.7\n" +
                "- Correção de bugs\n" +
                "- Correção feita no Menu\n" +
                "- Melhorias no desempenho\n" +
                "- Bromatologia modificado/banco de dados\n" +
                "- Tempo esgotado, só será exibido no quiz\n" +
                "- Corrigido o quiz\n" +
                "- Modificação feita no banco de dados de medicamentos\n" +
                "- Adicionada barra de progresso sincronizada com a porcentagem assistida do vídeo.\n" +
                "- Novo sistema de Quiz, progresso";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Changelog - Atualizações")
                .setMessage(changelog)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss(); // Fecha o diálogo
                });
        builder.create().show();
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
        String version = "Versão 1.0.5";
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

    @Override
    protected void onDestroy() {
        // Libera o MediaPlayer quando a Activity for destruída
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
