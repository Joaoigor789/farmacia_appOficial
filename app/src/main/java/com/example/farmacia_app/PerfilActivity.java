package com.example.farmacia_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    private TextView nomeUsuario, cursoUsuario;
    private ProgressBar progressoEstudo;
    private Button btnEditarPerfil, btnSair;
    private ImageView fotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Conectar elementos com o XML
        nomeUsuario = findViewById(R.id.nome_usuario);
        cursoUsuario = findViewById(R.id.curso_usuario);
        progressoEstudo = findViewById(R.id.progresso_estudo);
        btnEditarPerfil = findViewById(R.id.btn_editar_perfil);
        btnSair = findViewById(R.id.btn_sair);
        fotoPerfil = findViewById(R.id.foto_perfil);

        // Simulando dados carregados do usuário (pode vir de SharedPreferences, banco, etc)
        nomeUsuario.setText("João Igor");
        cursoUsuario.setText("Farmácia - Turma 2025");
        progressoEstudo.setProgress(60); // 60% de progresso

        // Botão de editar perfil (abre nova tela, por exemplo)
        btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, EditarPerfilActivity.class);
            startActivity(intent);
        });

        // Botão de sair
        btnSair.setOnClickListener(v -> {
            // Aqui você pode limpar as preferências de login ou encerrar a sessão
            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // limpa a pilha de atividades
            startActivity(intent);
        });
    }
}
