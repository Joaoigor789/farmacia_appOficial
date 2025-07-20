package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;
import android.Manifest;


public class ChatbotActivity extends AppCompatActivity implements ChatbotVoz.ChatbotListener {

    private ChatbotVoz chatbotVoz;
    private Button btnOuvir;
    private boolean escutando = false;
    private TextView tvFala;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        chatbotVoz = new ChatbotVoz(this, this);
        verificarPermissaoMicrofone();


        Button btnFalar = findViewById(R.id.btnFalar);
        btnOuvir = findViewById(R.id.btnOuvir);
        ImageView gifView = findViewById(R.id.gifView);
        tvFala = findViewById(R.id.tvFala);
        ImageView gifView2 = findViewById(R.id.gifView2);

        Glide.with(this)
                .asGif()
                .load(R.drawable.eva)
                .into(gifView2);

        btnOuvir.setOnClickListener(v -> {
            try {
                escutando = true;
                atualizarCoresBotoes();
                chatbotVoz.ouvirUsuario();

                Glide.with(this)
                        .asGif()
                        .load(R.drawable.rob)
                        .into(gifView);
            } catch (Exception e) {
                Log.e("ChatbotActivity", "Erro ao tentar ouvir o usuário", e);
                Toast.makeText(this, "Houve um erro ao tentar ouvir", Toast.LENGTH_SHORT).show();
            }
        });







        btnFalar.setOnClickListener(v -> {
            String resposta = "Olá! Como posso ajudar? Me diga um Sintoma";
            chatbotVoz.falarResposta(resposta);
            tvFala.setText(resposta);
        });

        atualizarCoresBotoes();
    }

    private void verificarPermissaoMicrofone() {
        // Checar se a permissão foi concedida
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Se não foi concedida, solicitar permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            // Caso a permissão já tenha sido concedida
            Toast.makeText(this, "Permissão de microfone já concedida", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUserInput(String input) {
        escutando = false;
        atualizarCoresBotoes();
        tvFala.setText("Você disse: " + input);
        chatbotVoz.falarResposta(input);
    }

    private void atualizarCoresBotoes() {
        int cor = escutando ? Color.GREEN : Color.RED;
        btnOuvir.setBackgroundColor(cor);
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
            showLicenseDialog();
            return true;
        } else if (id == R.id.menu_autor) {
            showAutorDialog();
            return true;
        } else if (id == R.id.menu_atualizacao) {
            ChangelogUtil.exibirChangelog(this); // Chama o método centralizado

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
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
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
    public void onRequestPermissionsResult(int requestCode, String @NotNull [] permissions, int @NotNull [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ChatbotActivity", "Permissão de microfone concedida.");
                Toast.makeText(this, "Permissão de microfone concedida", Toast.LENGTH_SHORT).show();
                chatbotVoz.ouvirUsuario();

            } else {
                Log.d("ChatbotActivity", "Permissão de microfone negada.");
                Toast.makeText(this, "A permissão de microfone é necessária para usar o chatbot", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
