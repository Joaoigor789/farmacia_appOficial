package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView questionText, scoreText;
    private Button option1, option2, option3;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Se o jogo já tiver terminado, não reinicie tudo

        questionText = findViewById(R.id.questionText);
        scoreText = findViewById(R.id.scoreText);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);

        if (questionList == null) {
            loadQuestions();
        }
        showQuestion();
    }

    private void loadQuestions() {
        questionList = new ArrayList<>();
        questionList.add(new Question("Paracetamol é usado para?", "Analgésico", "Antibiótico", "Anti-inflamatório", 1));
        questionList.add(new Question("Amoxicilina pertence a qual categoria?", "Anti-inflamatório", "Antibiótico", "Analgésico", 2));
        questionList.add(new Question("Ibuprofeno é um...", "Analgésico", "Anti-inflamatório", "Antibiótico", 2));
        questionList.add(new Question("Dipirona é um...", "Analgésico", "Antibiótico", "Antifúngico", 1));
        questionList.add(new Question("Cetoconazol é usado para tratar...", "Infecções fúngicas", "Infecções bacterianas", "Dor", 1));
        questionList.add(new Question("Aspirina tem ação...", "Anti-inflamatória", "Antibiótica", "Antifúngica", 1));
        questionList.add(new Question("Omeprazol é um...", "Protetor gástrico", "Analgésico", "Antibiótico", 1));
        questionList.add(new Question("Loratadina é usada para...", "Alergias", "Infecções", "Dor", 1));
        questionList.add(new Question("Ranitidina pertence a qual classe?", "Antiácido", "Analgésico", "Antibiótico", 1));
        questionList.add(new Question("Doxiciclina é um...", "Antibiótico", "Anti-inflamatório", "Protetor gástrico", 1));
        questionList.add(new Question("Metformina é usada para tratar...", "Diabetes", "Hipertensão", "Infecções", 1));
        questionList.add(new Question("Atenolol pertence a qual classe?", "Anti-hipertensivo", "Antibiótico", "Anti-inflamatório", 1));
        questionList.add(new Question("Salbutamol é um...", "Broncodilatador", "Analgésico", "Antibiótico", 1));
        questionList.add(new Question("Prednisona pertence a qual classe?", "Corticosteroide", "Antibiótico", "Antifúngico", 1));
        questionList.add(new Question("Ciprofloxacino é um...", "Antibiótico", "Anti-inflamatório", "Protetor gástrico", 1));
        questionList.add(new Question("Furosemida é usada para...", "Diurético", "Analgésico", "Antibiótico", 1));
        questionList.add(new Question("Enalapril pertence a qual classe?", "Anti-hipertensivo", "Antibiótico", "Analgésico", 1));
        questionList.add(new Question("Insulina é usada para tratar...", "Diabetes", "Hipertensão", "Infecções", 1));
        questionList.add(new Question("Hidroxicloroquina é um...", "Antimalárico", "Antibiótico", "Anti-inflamatório", 1));
        questionList.add(new Question("Sinvastatina é usada para...", "Colesterol", "Hipertensão", "Diabetes", 1));
        questionList.add(new Question("Azitromicina pertence a qual classe?", "Antibiótico", "Anti-inflamatório", "Antifúngico", 1));
        questionList.add(new Question("Diazepam pertence a qual classe?", "Ansiolítico", "Analgésico", "Antibiótico", 1));
        questionList.add(new Question("Fluoxetina pertence a qual classe?", "Antidepressivo", "Antibiótico", "Anti-inflamatório", 1));
        questionList.add(new Question("Lisinopril é usado para tratar...", "Hipertensão", "Diabetes", "Infecções", 1));
        questionList.add(new Question("Ibuprofeno atua como...", "Anti-inflamatório", "Antibiótico", "Antidepressivo", 1));
        questionList.add(new Question("Omeprazol é um medicamento...", "Protetor gástrico", "Analgésico", "Antibiótico", 1));
        questionList.add(new Question("Betametasona pertence a qual classe?", "Corticosteroide", "Antibiótico", "Anti-inflamatório", 1));
        questionList.add(new Question("Clonazepam pertence a qual classe?", "Ansiolítico", "Analgésico", "Antibiótico", 1));
        questionList.add(new Question("Rosuvastatina é usada para tratar...", "Colesterol", "Hipertensão", "Diabetes", 1));
        questionList.add(new Question("Amlodipina pertence a qual classe?", "Anti-hipertensivo", "Antibiótico", "Analgésico", 1));
        questionList.add(new Question("Captopril pertence a qual classe?", "Anti-hipertensivo", "Antibiótico", "Analgésico", 1));
        questionList.add(new Question("Dexametasona é um...", "Corticosteroide", "Antibiótico", "Anti-inflamatório", 1));
        questionList.add(new Question("Levotiroxina é usada para tratar...", "Hipotireoidismo", "Diabetes", "Hipertensão", 1));
        questionList.add(new Question("Metronidazol pertence a qual classe?", "Antibiótico", "Antifúngico", "Analgésico", 1));
        questionList.add(new Question("Rivaroxabana pertence a qual classe?", "Anticoagulante", "Antibiótico", "Analgésico", 1));
        questionList.add(new Question("Carbamazepina pertence a qual classe?", "Anticonvulsivante", "Analgésico", "Antibiótico", 1));

        Collections.shuffle(questionList);
    }


    private void showQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question q = questionList.get(currentQuestionIndex);
            questionText.setText(q.getQuestion());
            option1.setText(q.getOption1());
            option2.setText(q.getOption2());
            option3.setText(q.getOption3());


            option1.setOnClickListener(v -> checkAnswer(1));
            option2.setOnClickListener(v -> checkAnswer(2));
            option3.setOnClickListener(v -> checkAnswer(3));
        } else {
            Toast.makeText(this, "Jogo Finalizado! Pontuação: " + score, Toast.LENGTH_LONG).show();
            finish();
        }
    }



    @SuppressLint("SetTextI18n")
    private void checkAnswer(int selectedAnswer) {
        if (timer != null) {
            timer.cancel();
        }
        if (selectedAnswer == questionList.get(currentQuestionIndex).getCorrectAnswer()) {
            score++;
            Toast.makeText(this, "Correto!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Errado!", Toast.LENGTH_SHORT).show();
        }
        scoreText.setText("Pontuação: " + score);
        nextQuestion();
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

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            showQuestion();
        } else {
            Toast.makeText(this, "Jogo Finalizado! Pontuação: " + score, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private static class Question {
        private final String question;
        private final String option1;
        private final String option2;
        private final String option3;
        private final int correctAnswer;

        public Question(String question, String option1, String option2, String option3, int correctAnswer) {
            this.question = question;
            this.option1 = option1;
            this.option2 = option2;
            this.option3 = option3;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestion() { return question; }
        public String getOption1() { return option1; }
        public String getOption2() { return option2; }
        public String getOption3() { return option3; }
        public int getCorrectAnswer() { return correctAnswer; }
    }
}

