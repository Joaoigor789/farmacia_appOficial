package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private Button actionButton; // O botão será usado tanto para login quanto para registro
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "userPrefs"; // Nome do arquivo de preferências

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Corrigido para o layout de login/registro

        // Inicializa as preferências
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        usernameField = findViewById(R.id.usernameField);  // Verifique os IDs no seu layout
        passwordField = findViewById(R.id.passwordField);
        actionButton = findViewById(R.id.actionButton);

        // Verifica se o usuário já está cadastrado
        if (isUserRegistered()) {
            actionButton.setText("Login");
            actionButton.setOnClickListener(v -> loginUser());
        } else {
            actionButton.setText("Cadastrar");
            actionButton.setOnClickListener(v -> registerUser());
        }

    }

    private boolean isUserRegistered() {
        String storedUsername = sharedPreferences.getString("username", "");
        return !storedUsername.isEmpty(); // Verifica se há um nome de usuário armazenado
    }

    @SuppressLint("SetTextI18n")
    private void registerUser() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        // Verifica se os campos estão vazios
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se o endereço eletrônico é válido
        if (!isValidUsername(username)) {
            Toast.makeText(this, "Username inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Salva os dados no SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();

        Toast.makeText(AuthActivity.this, "Cadastro bem-sucedido", Toast.LENGTH_SHORT).show();

        // Limpar os campos
        usernameField.setText("");
        passwordField.setText("");

        // Após o registro, o botão será alterado para Login
        actionButton.setText("Login");
        actionButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        // Verifica se os campos estão vazios
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recupera os dados armazenados
        String storedUsername = sharedPreferences.getString("username", "");
        String storedPassword = sharedPreferences.getString("password", "");

        // Verifica se os dados estão corretos
        if (username.equals(storedUsername) && password.equals(storedPassword)) {
            Toast.makeText(AuthActivity.this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();

            // Redireciona para a tela principal
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finaliza a atividade de login/cadastro para não permitir voltar
        } else {
            Toast.makeText(AuthActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para verificar se o e-mail é válido
    private boolean isValidUsername(String username) {
        // Verifica se o username parece ser um e-mail simples
        return username.contains("@") && username.contains(".");
    }
}
