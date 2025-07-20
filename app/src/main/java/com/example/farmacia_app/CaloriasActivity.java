package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CaloriasActivity extends AppCompatActivity {

    private EditText edtGramas, edtCaloriasPorGrama;
    private TextView txtResultadoCalorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorias);

        // Inicializando os elementos da interface
        edtGramas = findViewById(R.id.edtGramas);
        edtCaloriasPorGrama = findViewById(R.id.edtCaloriasPorGrama);
        txtResultadoCalorias = findViewById(R.id.txtResultadoCalorias);
        Button btnCalcularCalorias = findViewById(R.id.btnCalcularCalorias);

        // Configurando o ouvinte de clique no botão para calcular calorias
        btnCalcularCalorias.setOnClickListener(v -> calcularCalorias());
    }

    // Método para calcular as calorias
    @SuppressLint("SetTextI18n")
    private void calcularCalorias() {
        String strGramas = edtGramas.getText().toString();
        String strCaloriasPorGrama = edtCaloriasPorGrama.getText().toString();

        // Validando os campos de entrada
        if (strGramas.isEmpty() || strCaloriasPorGrama.isEmpty()) {
            Toast.makeText(CaloriasActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertendo os valores para números
        double gramas = Double.parseDouble(strGramas);
        double caloriasPorGrama = Double.parseDouble(strCaloriasPorGrama);

        // Calculando as calorias totais
        double totalCalorias = gramas * caloriasPorGrama;

        // Exibindo o resultado
        txtResultadoCalorias.setText("Total de Calorias: " + totalCalorias + " kcal");
    }
}
