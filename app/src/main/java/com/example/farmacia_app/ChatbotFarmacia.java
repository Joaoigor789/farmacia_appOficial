package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.*;

public class ChatbotFarmacia extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText respostaEditText;
    private TextView perguntaChatbot;
    private TextView respostaChatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);  // Conecta o layout activity_bot.xml

        // Inicializa os componentes da interface
        respostaEditText = findViewById(R.id.respostaEditText);
        perguntaChatbot = findViewById(R.id.perguntaChatbot);
        respostaChatbot = findViewById(R.id.respostaChatbot);
        Button enviarRespostaButton = findViewById(R.id.enviarRespostaButton);

        // Iniciar consulta
        iniciarConsulta();
        FirebaseApp.initializeApp(this);


        // Configurar o botão de envio da resposta
        enviarRespostaButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String resposta = respostaEditText.getText().toString().trim();

                // Validar a entrada
                if (!resposta.isEmpty()) {
                    // Aqui, você pode chamar o método para avaliar a resposta
                    avaliarResposta(resposta);
                } else {
                    respostaChatbot.setText("Por favor, forneça uma resposta.");
                }
            }
        });
    }

    // Inicia a consulta com exemplo de sintoma
    @SuppressLint("SetTextI18n")
    private void iniciarConsulta() {
        perguntaChatbot.setText("Paciente: Estou com dor de cabeça e febre. O que devo fazer?");
        respostaChatbot.setText("Dica: Responda com o tratamento recomendado para esses sintomas.");
    }

    // Avalia a resposta fornecida pelo usuário
    @SuppressLint("SetTextI18n")
    private void avaliarResposta(String resposta) {
        db.collection("sintomas")
                .whereEqualTo("sintoma", "dor de cabeça e febre")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String tratamentoCorreto = document.getString("tratamento");
                            if (resposta.equalsIgnoreCase(tratamentoCorreto)) {
                                respostaChatbot.setText("IA: Resposta correta! Esse é o tratamento recomendado.");
                            } else {
                                respostaChatbot.setText("IA: Resposta incorreta. O tratamento adequado seria: " + tratamentoCorreto);
                            }
                        }
                    } else {
                        respostaChatbot.setText("IA: Não encontramos um tratamento para esse sintoma.");
                    }
                })
                .addOnFailureListener(e -> {
                    respostaChatbot.setText("Erro ao buscar tratamento: " + e.getMessage());
                    Log.e("FirestoreError", "Erro ao buscar tratamento", e);  // Adicionando log
                });
    }}
