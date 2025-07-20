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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity {

    private EditText editMensagem, editMensagemDev;
    private TextView textMensagensDev;
    private ArrayList<String> mensagensChat;
    private ChatAdapter chatAdapter;
    private DatabaseReference database;
    private MediaPlayer mediaPlayer; // Declara o MediaPlayer para reprodução de áudio

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_layout);

        // Inicialização dos componentes
        ListView listViewChat = findViewById(R.id.listViewChat);
        editMensagem = findViewById(R.id.editMensagem);
        editMensagemDev = findViewById(R.id.editMensagemDev);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        Button btnEnviarMensagemDev = findViewById(R.id.btnEnviarMensagemDev);
        textMensagensDev = findViewById(R.id.textMensagensDev);

        // Inicializar o Firebase Database
        database = FirebaseDatabase.getInstance().getReference("mensagens");

        // Inicializar a lista de mensagens
        mensagensChat = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, mensagensChat);
        listViewChat.setAdapter(chatAdapter);

        // Configuração do RecyclerView para tópicos
        RecyclerView recyclerViewTopicos = findViewById(R.id.recyclerViewTopicos);
        recyclerViewTopicos.setLayoutManager(new LinearLayoutManager(this));

        // Observar novas mensagens no Firebase em tempo real
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                mensagensChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String mensagem = snapshot.child("mensagem").getValue(String.class);
                    if (mensagem != null) {
                        mensagensChat.add(mensagem);
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                Toast.makeText(ForumActivity.this, "Erro ao carregar mensagens.", Toast.LENGTH_SHORT).show();
            }
        });

        // Tocar o áudio ao iniciar a Activity
        playAudio();

        // Handler de clique do botão Enviar Mensagem no Chat
        btnEnviar.setOnClickListener(v -> {
            String mensagem = editMensagem.getText().toString();
            if (!mensagem.isEmpty()) {
                String id = database.push().getKey();
                if (id != null) {
                    Mensagem novaMensagem = new Mensagem(mensagem);
                    database.child(id).setValue(novaMensagem).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForumActivity.this, "Mensagem Enviada!", Toast.LENGTH_SHORT).show();
                            editMensagem.setText("");
                        } else {
                            Toast.makeText(ForumActivity.this, "Erro ao enviar mensagem!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(ForumActivity.this, "Digite uma mensagem!", Toast.LENGTH_SHORT).show();
            }
        });

        // Handler de clique do botão Enviar Mensagem para o Desenvolvedor
        btnEnviarMensagemDev.setOnClickListener(v -> {
            String mensagemDev = editMensagemDev.getText().toString();
            if (!mensagemDev.isEmpty()) {
                String idDev = database.push().getKey();
                if (idDev != null) {
                    Mensagem novaMensagemDev = new Mensagem(mensagemDev);
                    database.child("mensagens_desenv").child(idDev).setValue(novaMensagemDev).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForumActivity.this, "Mensagem Enviada para o Desenvolvedor!", Toast.LENGTH_SHORT).show();
                            editMensagemDev.setText("");
                            textMensagensDev.append("\n" + mensagemDev);
                        } else {
                            Toast.makeText(ForumActivity.this, "Erro ao enviar mensagem para o desenvolvedor!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(ForumActivity.this, "Digite uma mensagem para o desenvolvedor!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.forum);
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


    public static class Mensagem {
        public String mensagem;
        public Mensagem() {}
        public Mensagem(String mensagem) { this.mensagem = mensagem; }
    }
}