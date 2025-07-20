package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private TextView txtPontos;
    private SharedPreferences prefs;
    private int pontos;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.calendarView);
        txtPontos = findViewById(R.id.txtPontos);

        prefs = getSharedPreferences("uso_diario", MODE_PRIVATE);
        pontos = prefs.getInt("pontos", 0);
        atualizarTexto();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Formatar a data corretamente
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dataSelecionada = sdf.format(selectedDate.getTime());

            // Verificar se a data selecionada é o dia atual
            Calendar today = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String todayDate = dateFormat.format(today.getTime());

            if (!dataSelecionada.equals(todayDate)) {
                // Se a data não for hoje, exibe uma mensagem personalizada com texto vermelho
                Toast.makeText(this, "Você só pode marcar o dia de hoje!", Toast.LENGTH_SHORT).show();
                return; // Não permite marcar dias que não sejam o de hoje
            }

            if (!prefs.getBoolean(dataSelecionada, false)) {
                // Marca o dia como usado
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(dataSelecionada, true);
                pontos += 5; // Por exemplo: 5 pontos por dia
                editor.putInt("pontos", pontos);
                editor.apply();

                Toast.makeText(this, "Dia registrado! +5 pontos!", Toast.LENGTH_SHORT).show();
                verificarDesbloqueios();
                atualizarTexto();
                // Atualiza a visualização do calendário
                calendarView.setDate(selectedDate.getTimeInMillis());
            } else {
                // Caso o dia já tenha sido marcado, você pode alterar a cor de fundo da data
                Toast.makeText(this, "Você já marcou esse dia!", Toast.LENGTH_SHORT).show();
                calendarView.setDate(selectedDate.getTimeInMillis());
                // Alterar a cor de fundo para indicar que o dia já foi marcado
                calendarView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light)); // Usando o ContextCompat para obter a cor
            }
        });

        // Adicionando o callback para o botão "voltar"
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Crie a Intent para iniciar a MainActivity
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void atualizarTexto() {
        txtPontos.setText("Pontos: " + pontos);
    }

    private void verificarDesbloqueios() {
        if (pontos >= 30 && !prefs.getBoolean("desbloqueio_1", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("desbloqueio_1", true);
            editor.apply();
            Toast.makeText(this, "🎉 Função Exclusiva Desbloqueada (30 pontos)!", Toast.LENGTH_LONG).show();
            // Aqui você pode ativar alguma funcionalidade premium
        }
    }


}
