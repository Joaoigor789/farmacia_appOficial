package com.example.farmacia_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NotasActivity extends AppCompatActivity {

    private EditText etNota;
    private TextView tvNotasSalvas;
    private SharedPreferences sharedPreferences;
    private static final String CHAVE_NOTAS = "minhas_anotacoes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notas_activity);

        etNota = findViewById(R.id.etNota);
        Button btnSalvarNota = findViewById(R.id.btnSalvarNota);
        Button btnApagarNota = findViewById(R.id.btnApagarNota); // Novo botão para apagar a anotação
        tvNotasSalvas = findViewById(R.id.tvNotasSalvas);

        sharedPreferences = getSharedPreferences("AppNotas", MODE_PRIVATE);

        // Carregar notas salvas
        String notaSalva = sharedPreferences.getString(CHAVE_NOTAS, "");
        tvNotasSalvas.setText(notaSalva.isEmpty() ? "Nenhuma anotação salva" : notaSalva);

        // Salvar a nota
        btnSalvarNota.setOnClickListener(v -> {
            String novaNota = etNota.getText().toString();
            if (!novaNota.isEmpty()) {
                sharedPreferences.edit().putString(CHAVE_NOTAS, novaNota).apply();
                tvNotasSalvas.setText(novaNota);
            }
        });

        // Apagar a nota
        btnApagarNota.setOnClickListener(v -> {
            sharedPreferences.edit().remove(CHAVE_NOTAS).apply(); // Remove a nota salva
            tvNotasSalvas.setText("Nenhuma anotação salva"); // Atualiza a interface
        });
    }
}
