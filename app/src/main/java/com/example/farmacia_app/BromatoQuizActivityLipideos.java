package com.example.farmacia_app;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.Handler;
import android.os.Looper;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import java.util.Random;

public class BromatoQuizActivityLipideos extends Activity {
    private TextView questionText;

    private MediaPlayer mediaPlayer;

    private TextView scoreText;
    private RadioGroup optionsGroup;
    private Button submitButton;
    private int score = 0;
    private int currentQuestionIndex = 0;

    private final String[][] questions = {
            {"Marque a alternativa que indica os principais tipos de lipídios encontrados no corpo humano:",
                    "a) Ácidos graxos, glicose e colesterol.",
                    "b) Triglicerídeos, fosfolipídios e esteróides.",
                    "c) Proteínas, ácidos graxos e amido.",
                    "d) Carboidratos, triglicerídeos e vitaminas.",
                    "e) Fosfolipídios, celulose e amido.",
                    "b"},

            {"Os lipídios têm diversas funções no organismo. Marque a alternativa que contém a função mais associada aos triglicerídeos:",
                    "a) Reservar energia a longo prazo.",
                    "b) Formar a estrutura das membranas celulares.",
                    "c) Atuar como hormônios.",
                    "d) Facilitar o transporte de oxigênio.",
                    "e) Facilitar a digestão.",
                    "a"},

            {"Qual dos seguintes lipídios é conhecido por ser a principal molécula envolvida na formação das membranas celulares?",
                    "a) Colesterol.",
                    "b) Triglicerídeos.",
                    "c) Fosfolipídios.",
                    "d) Ácidos graxos.",
                    "e) Glicerol.",
                    "c"},

            {"O colesterol é um lipídio importante para o organismo. Qual é a função do colesterol no corpo humano?",
                    "a) Produzir energia rapidamente.",
                    "b) Ser um componente das membranas celulares e precursos de hormônios.",
                    "c) Atuar no transporte de oxigênio.",
                    "d) Ajudar na digestão de carboidratos.",
                    "e) Formar os músculos.",
                    "b"},

            {"Os lipídios são hidrofóbicos. Isso significa que eles:",
                    "a) São solúveis em água.",
                    "b) Não se dissolvem em água.",
                    "c) São bons condutores de eletricidade.",
                    "d) São solúveis em óleos.",
                    "e) Não possuem energia.",
                    "b"},

            {"Qual a principal diferença entre os ácidos graxos saturados e insaturados?",
                    "a) Ácidos graxos saturados têm mais ligações duplas entre os átomos de carbono.",
                    "b) Ácidos graxos insaturados têm mais átomos de hidrogênio.",
                    "c) Ácidos graxos saturados são sólidos à temperatura ambiente e insaturados são líquidos.",
                    "d) Não existe diferença significativa entre eles.",
                    "e) Ácidos graxos saturados são mais saudáveis que os insaturados.",
                    "c"},

            {"Qual é a principal função dos lipídios no corpo humano?",
                    "a) Reservar energia e ajudar na formação das células.",
                    "b) Transportar oxigênio para as células.",
                    "c) Serem fontes rápidas de energia.",
                    "d) Regenerar tecidos musculares.",
                    "e) Ajudar na digestão dos carboidratos.",
                    "a"},

            {"O que é um lipídio essencial para a saúde humana?",
                    "a) Ácidos graxos trans.",
                    "b) Ácidos graxos essenciais como os ômega-3.",
                    "c) Colesterol LDL.",
                    "d) Triglicerídeos saturados.",
                    "e) Ácidos graxos monossaturados.",
                    "b"},

            {"Em que alimento podemos encontrar uma boa quantidade de ácidos graxos insaturados?",
                    "a) Carne vermelha.",
                    "b) Óleo de oliva.",
                    "c) Produtos lácteos.",
                    "d) Óleo de palma.",
                    "e) Farinha de trigo.",
                    "b"},

            {"Os lipídios desempenham papel importante no armazenamento de energia. Qual das alternativas descreve corretamente os triglicerídeos?",
                    "a) Eles são compostos por um glicerol e três ácidos graxos.",
                    "b) São formados apenas por glicerol.",
                    "c) São compostos apenas de carboidratos e proteínas.",
                    "d) São compostos por colesterol e fosfolipídios.",
                    "e) São ácidos graxos livres.",
                    "a"},

            {"Qual dos seguintes lipídios é responsável pela formação das membranas celulares?",
                    "a) Triglicerídeos.",
                    "b) Fosfolipídios.",
                    "c) Ácidos graxos saturados.",
                    "d) Colesterol.",
                    "e) Ácidos graxos trans.",
                    "b"},

            {"Os lipídios são classificados como gorduras boas ou ruins. Marque a alternativa correta sobre os lipídios ruins:",
                    "a) São encontrados principalmente em óleos vegetais.",
                    "b) São ricos em ácidos graxos insaturados.",
                    "c) São encontrados principalmente em alimentos processados e frituras.",
                    "d) São necessários para o bom funcionamento do organismo.",
                    "e) São encontrados apenas em alimentos orgânicos.",
                    "c"},

            {"Qual a principal função do colesterol no organismo?",
                    "a) Produzir energia.",
                    "b) Formar hormônios e compor as membranas celulares.",
                    "c) Controlar a digestão.",
                    "d) Ajudar no transporte de oxigênio.",
                    "e) Ajudar na contração muscular.",
                    "b"},

            {"Os lipídios que formam as membranas celulares são compostos por:",
                    "a) Ácidos graxos saturados e triglicerídeos.",
                    "b) Colesterol e glicose.",
                    "c) Fosfolipídios e proteínas.",
                    "d) Ácidos graxos insaturados e colesterol.",
                    "e) Proteínas e carboidratos.",
                    "c"},

            {"Os lipídios são classificados como simples, complexos e derivados. Os lipídios complexos são principalmente formados por:",
                    "a) Glicerol e ácidos graxos.",
                    "b) Ácidos graxos e colesterol.",
                    "c) Proteínas e carboidratos.",
                    "d) Colesterol e ácidos graxos.",
                    "e) Triglicerídeos e ácidos graxos.",
                    "a"},

            {"Qual é a consequência do consumo excessivo de lipídios saturados?",
                    "a) Aumento do risco de doenças cardíacas.",
                    "b) Aumento da resistência à insulina.",
                    "c) Melhora na função cerebral.",
                    "d) Aumento da produção de hormônios sexuais.",
                    "e) Redução de inflamações no corpo.",
                    "a"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guiquiz);

        shuffleQuestions();


        ProgressoEstudo progressoEstudo = new ProgressoEstudo(getApplicationContext());
        progressoEstudo.registrarProgresso("Bromatologia", 70);
        progressoEstudo.exibirProgresso();



        questionText = findViewById(R.id.questionText);
        scoreText = findViewById(R.id.scoreText);
        optionsGroup = findViewById(R.id.optionsGroup);
        submitButton = findViewById(R.id.submitButton);

        loadQuestion();

        submitButton.setOnClickListener(v -> checkAnswer());
    }


    private void loadQuestion() {
        if (currentQuestionIndex < questions.length) {
            questionText.setText(questions[currentQuestionIndex][0]);

            for (int i = 0; i < optionsGroup.getChildCount(); i++) {
                ((RadioButton) optionsGroup.getChildAt(i)).setText(questions[currentQuestionIndex][i + 1]);
            }
        } else {
            String quizMessage = getString(R.string.quiz_concluido, score, questions.length);
            questionText.setText(quizMessage);

            optionsGroup.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);


            // Atualizando a pontuação final no scoreText
            scoreText.setText(getString(R.string.pontuacao123, score));


            // Todas as questões foram respondidas, aguarda 3 segundos e volta para BromatologiaActivity
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(BromatoQuizActivityLipideos.this, BromatologiaActivity.class);
                startActivity(intent);
                finish();
            }, 3000);


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
            showLicenseDialog();
            return true;
        } else if (id == R.id.menu_autor) {
            showAutorDialog();
            return true;
        } else if (id == R.id.menu_atualizacao) {
            ChangelogUtil.exibirChangelog(this); // Chama o método centralizado

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAutorDialog() {
        String info = "Autor: João Igor Rodrigues Pereira da Silva\n" +
                "Empresa: Farmacia AppTech Tecnology\n" +
                "Projeto: Aplicativo de Farmácia";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informações do Autor")
                .setMessage(info)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
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


    private void shuffleQuestions() {
        Random random = new Random();
        for (int i = questions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Troca questions[i] com questions[j]
            String[] temp = questions[i];
            questions[i] = questions[j];
            questions[j] = temp;
        }
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            // Nenhuma opção selecionada
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Atenção");
            builder.setMessage("Por favor, selecione uma opção antes de continuar.");
            builder.setPositiveButton("OK", null);
            builder.show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedId);
        String selectedAnswer = selectedButton.getText().toString();
        String correctAnswer = questions[currentQuestionIndex][6]; // Exemplo: "b"

        boolean isCorrect = selectedAnswer.startsWith(correctAnswer + ")");

        // Toca som correspondente
        if (isCorrect) {
            score++;
            playSound(R.raw.correct); // Som de acerto
        } else {
            playSound(R.raw.incorret);   // Som de erro
        }

        // Exibe feedback ao usuário
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isCorrect ? "Correto!" : "Errado!");
        builder.setMessage(isCorrect ? "Você acertou!" :
                "Você errou. A resposta correta é: " + correctAnswer.toUpperCase() + ")");
        builder.setCancelable(false);
        builder.setPositiveButton("Continuar", (dialog, which) -> {
            currentQuestionIndex++;
            optionsGroup.clearCheck(); // Limpa a seleção
            loadQuestion(); // Carrega a próxima questão
        });
        builder.show();
    }

    // Método para tocar os sons
    private void playSound(int soundResId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();
    }

}


