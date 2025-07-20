package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackActivity extends AppCompatActivity {

    private DatabaseReference feedbackRef;
    private MediaPlayer mediaPlayer;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);

        // Conectando ao Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        feedbackRef = database.getReference("feedbacks");

        // Vinculando os elementos do layout com os IDs
        EditText etUsername = findViewById(R.id.etUsername);
        TextView tvMessage = findViewById(R.id.tvMessage);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        // Inicia a reprodução do áudio ao abrir a Activity
        playAudio();

        // Definindo ação do botão de envio
        btnSubmit.setOnClickListener(view -> {
            var lambdaContext = new Object() {
                String username = etUsername.getText().toString().trim();
            };
            float rating = ratingBar.getRating();

            if (lambdaContext.username.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu nome", Toast.LENGTH_SHORT).show();
                return;
            }

            // Criando um objeto Feedback com os dados fornecidos
            Feedback feedback = new Feedback(lambdaContext.username, "Obrigado pelo feedback!", rating);

            // Usando os métodos getters para obter as informações
            lambdaContext.username = feedback.getUsername();  // Corrigido: usando o getter para acessar o nome

            // Corrigido: usando o getter para acessar a avaliação

            // Adicione a verificação da conexão antes de enviar os dados
            FirebaseDatabase.getInstance().getReference(".info/connected")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean connected = Boolean.TRUE.equals(dataSnapshot.getValue(Boolean.class));
                            if (connected) {
                                Log.d("Firebase", "Conectado ao Firebase");

                                // Criando o objeto Feedback após a confirmação da conexão
                                Feedback feedback = new Feedback(lambdaContext.username, "Obrigado pelo feedback!", rating);

                                // Enviando os dados para o Firebase
                                feedbackRef.push().setValue(feedback)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(FeedbackActivity.this, "Feedback enviado!", Toast.LENGTH_SHORT).show();
                                            tvMessage.setText("Obrigado pela avaliação, " + lambdaContext.username + "! Você deu " + rating + " estrelas.");

                                            // Limpando os campos
                                            etUsername.setText("");
                                            ratingBar.setRating(0);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(FeedbackActivity.this, "Erro ao enviar feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("Firebase", "Erro ao enviar feedback", e);
                                        });
                            } else {
                                Log.d("Firebase", "Sem conexão com o Firebase");
                                Toast.makeText(FeedbackActivity.this, "Sem conexão com o Firebase", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("Firebase", "Erro ao verificar conexão", databaseError.toException());
                        }
                    });


            // Exibindo as informações no Log
            Log.d("Feedback", "Usuário: " + feedback.getUsername());
            Log.d("Feedback", "Mensagem: " + feedback.getMessage());
            Log.d("Feedback", "Avaliação: " + feedback.getRating());

            // Enviando os dados para o Firebase
            feedbackRef.push().setValue(feedback)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Feedback enviado!", Toast.LENGTH_SHORT).show();
                        tvMessage.setText("Obrigado pela avaliação, " + lambdaContext.username + "! Você deu " + rating + " estrelas.");

                        // Limpando os campos
                        etUsername.setText("");
                        ratingBar.setRating(0);
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Erro ao enviar feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    private void playAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.francisca_feedback);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
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