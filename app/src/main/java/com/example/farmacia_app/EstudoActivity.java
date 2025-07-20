package com.example.farmacia_app;

import android.webkit.*;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EstudoActivity extends AppCompatActivity {

    private ProgressoEstudo progressoEstudo; // Declaração da variável

    private ProgressBar progressBar;  // Declaração da barra de progresso


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudo);

        progressBar = findViewById(R.id.progressBar); // Inicializa o ProgressBar

        progressoEstudo = new ProgressoEstudo(this); // ou getApplicationContext()
        // Inicializa a classe de progresso
        progressoEstudo.exibirProgresso();



// Mostra o progresso atual ao iniciar a atividade
        int progressoEstudo = 0;
        Toast.makeText(this, "Progresso do Estudo: " + progressoEstudo + "%", Toast.LENGTH_SHORT).show();


        // WebView para o vídeo
        WebView webViewVideo = findViewById(R.id.webViewVideo);
        WebSettings webSettings = webViewVideo.getSettings();
        webSettings.setJavaScriptEnabled(true); // Ativa JavaScript


        // Adicionando a interface JavaScript para pegar o progresso
        webViewVideo.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void updateProgress(int progresso) {
                runOnUiThread(() -> onVideoProgress(progresso)); // Atualiza a interface do usuário
            }
        }, "Android");

        // Adiciona a interface JavaScript para completar vídeo
        webViewVideo.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void onVideoCompleteJS() {
                onVideoComplete(); // Chama o método quando o vídeo é concluído
            }
        }, "Android");

        // Definindo o WebViewClient
        webViewVideo.setWebViewClient(new WebViewClient());
        webViewVideo.setWebChromeClient(new WebChromeClient());

        // WebView para o vídeo
        configurarWebView(R.id.webViewVideo, "https://www.youtube.com/embed/8Bk8_vPDd94?enablejsapi=1");

        // WebView para o artigo
        configurarWebView(R.id.webViewArtigo, "https://www.youtube.com/embed/aIoBlCJ4AZg");

        // WebView para o simulado
        configurarWebView(R.id.webViewSimulado, "https://www.youtube.com/embed/3PUkfmI03_8");

        // WebView para o exemplo adicional
        configurarWebView(R.id.webViewExemplo, "https://www.youtube.com/embed/1jLQNiUh2FU?si=QUDVb6OZcTjeuzuz");
    }

    // Método para configurar o WebView
    @SuppressLint("SetJavaScriptEnabled")
    private void configurarWebView(int idWebView, String url) {
        WebView webView = findViewById(idWebView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Ativa JavaScript
        webView.setWebViewClient(new WebViewClient());

        // Inicia o carregamento do vídeo ou conteúdo
        webView.loadUrl(url);
    }

    // Método chamado quando o vídeo é concluído
    public void onVideoComplete() {
        progressoEstudo.registrarProgresso("Estudo", 100); // 100% de progresso
        progressBar.setProgress(100);  // Define a barra de progresso como 100%
        Toast.makeText(this, "Vídeo Completo! Progresso: 100%", Toast.LENGTH_SHORT).show();
    }

    // Método chamado para atualizar o progresso com base no tempo assistido
    public void onVideoProgress(int progresso) {
        progressoEstudo.registrarProgresso("Estudo", progresso);
        progressBar.setProgress(progresso);  // Atualiza a barra de progresso
        Toast.makeText(this, "Progresso do vídeo: " + progresso + "%", Toast.LENGTH_SHORT).show();
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

            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
