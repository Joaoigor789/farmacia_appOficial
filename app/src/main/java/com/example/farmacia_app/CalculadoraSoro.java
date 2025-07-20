package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculadoraSoro extends AppCompatActivity {
    private EditText edtConcentracaoInicial, edtVolumeInicial, edtConcentracaoDesejada;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_calculadora_soro);

        edtConcentracaoInicial = findViewById(R.id.edtConcentracaoInicial);
        edtVolumeInicial = findViewById(R.id.edtVolumeInicial);
        edtConcentracaoDesejada = findViewById(R.id.edtConcentracaoDesejada);
        txtResultado = findViewById(R.id.txtResultado);
        Button btnCalcular = findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(v -> calcularSoro());
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void calcularSoro() {
        try {
            double concentracaoInicial = Double.parseDouble(edtConcentracaoInicial.getText().toString());
            double volumeInicial = Double.parseDouble(edtVolumeInicial.getText().toString());
            double concentracaoDesejada = Double.parseDouble(edtConcentracaoDesejada.getText().toString());

            if (concentracaoDesejada == 0) {
                txtResultado.setText("A concentração desejada não pode ser zero.");
                return;
            }

            double volumeFinal = (concentracaoInicial * volumeInicial) / concentracaoDesejada;
            double volumeDiluente = volumeFinal - volumeInicial;

            if (volumeDiluente < 0) {
                txtResultado.setText("A concentração desejada é maior que a inicial. Não é possível diluir.");
            } else {
                txtResultado.setText(String.format("Para obter %.2f mg/mL, adicione %.2f mL de diluente.", concentracaoDesejada, volumeDiluente));
            }
        } catch (NumberFormatException e) {
            txtResultado.setText("Por favor, insira valores numéricos válidos.");
        }
    }
}
