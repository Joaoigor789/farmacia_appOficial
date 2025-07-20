package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Recupera as referências dos componentes da interface
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.loginButton);
        Button actionButton = findViewById(R.id.actionButton);

        ImageView userIcon = findViewById(R.id.userIcon1);
        TextView loggedInMessage = findViewById(R.id.loggedInMessage);

        // Verifica se o usuário já está logado localmente (exemplo com SharedPreferences)
        String savedUsername = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("username", null);
        if (savedUsername != null) {
            // Exibe o ícone e a mensagem
            userIcon.setVisibility(View.VISIBLE);
            loggedInMessage.setVisibility(View.VISIBLE);
            loggedInMessage.setText("Você está logado como " + savedUsername);
        } else {
            // Caso contrário, o ícone e a mensagem não são visíveis
            userIcon.setVisibility(View.GONE);
            loggedInMessage.setVisibility(View.GONE);
        }

        // Ao clicar no botão de login
        loginButton.setOnClickListener(v -> loginUser());

        // Ao clicar no botão de registro
        actionButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            if (!username.isEmpty() && !password.isEmpty()) {
                registerUser(username, password);  // Registra o usuário localmente
            } else {
                Toast.makeText(this, "Preencha todos os campos para registrar", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Função para registrar o usuário localmente
    private void registerUser(String username, String password) {
        // Armazenando o nome de usuário e senha localmente (exemplo com SharedPreferences)
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .putString("username", username)
                .putString("password", password)  // Armazenando senha (não recomendado para produção)
                .apply();

        Toast.makeText(this, "Usuário registrado com sucesso", Toast.LENGTH_SHORT).show();
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
        }
        return super.onOptionsItemSelected(item);
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


    // Função de login local
    private void loginUser() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recuperando dados armazenados (exemplo com SharedPreferences)
        String savedUsername = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("username", null);
        String savedPassword = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("password", null);

        if (savedUsername != null && savedPassword != null &&
                savedUsername.equals(username) && savedPassword.equals(password)) {
            // Login bem-sucedido
            Toast.makeText(LoginActivity.this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();

            // Redirecionar para a MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
        }
    }
}
