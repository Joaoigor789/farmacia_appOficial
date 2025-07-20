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

public class BromatoQuizActivity extends Activity {
    private TextView questionText;

    private MediaPlayer mediaPlayer;

    private TextView scoreText;
    private RadioGroup optionsGroup;
    private Button submitButton;
    private int score = 0;
    private int currentQuestionIndex = 0;

    private final String[][] questions = {
            {"Marque a alternativa que indica quais os elementos químicos fundamentais encontrados na composição de um carboidrato:",
                    "a) Carbono, hidrogênio e hélio.",
                    "b) Carbono, oxigênio e hidrogênio.",
                    "c) Carbono, cálcio e potássio.",
                    "d) Sódio, potássio e carbono.",
                    "e) Carbono, magnésio e hidrogênio.",
                    "b"},

            {"Podemos classificar os glicídios em três grupos principais: monossacarídeos, dissacarídeos e polissacarídeos. Marque a alternativa onde encontramos apenas glicídios formados pela união de dois monossacarídeos (dissacarídeos):",
                    "a) Amido e celulose.",
                    "b) Sacarose e celulose.",
                    "c) Frutose e glicose.",
                    "d) Celulose e glicogênio.",
                    "e) Sacarose e lactose.",
                    "e"},

            {"Sabemos que o amido é uma importante substância de reserva encontrada em plantas e algumas algas.Marque a alternativa correta a respeito do amido:",
                    "a) O amido não é um carboidrato.",
                    "b) O amido é um dissacarídeo, assim como a frutose.",
                    "c) O amido é um monossacarídeo, assim como a glicose.",
                    "d) O amido é um polissacarídeo, assim como o glicogênio e a celulose.",
                    "e) Carboidratos, fotossíntese.",
                    "d"},

            {"As substâncias que se destinam a fornecer energia, além de serem responsáveis pela rigidez de certos tecidos, sendo mais abundantes nos vegetais, são os ______________ sintetizados no processo de ________________:",
                    "a) Lipídios, fotossíntese.",
                    "b) Ácidos nucleicos, autoduplicação.",
                    "c) Ácidos nucleicos, fotossíntese.",
                    "d) Álcoois, fermentação.",
                    "e) Carboidratos, fotossíntese.",
                    "e"},

            {"O papel comum é formado, basicamente, pelo polissacarídeo mais abundante no planeta. Este carboidrato, nas células vegetais, tem a seguinte função:",
                    "a) Revestir as organelas.",
                    "b) Formar a membrana plasmática.",
                    "c) Compor a estrutura da parede celular.",
                    "d) Acumular reserva energética no hialoplasma.",
                    "e) Acumular reserva força.",
                    "c"},

            {"Marque a alternativa que contém apenas monossacarídeos:",
                    "a) Maltose e glicose.",
                    "b) Sacarose e frutose.",
                    "c) Glicose e galactose.",
                    "d) Lactose e glicose.",
                    "e) Frutose e lactose.",
                    "c"},

            // 7 questão//
            {" Quanto aos carboidratos, assinale a alternativa incorreta.:",
                    "a)Os glicídios são classificados de acordo com o tamanho e a organização de sua molécula em três grupos: monossacarídeos, oligossacarídeos e polissacarídeos.",
                    "b)Os polissacarídeos compõem um grupo de glicídios cujas moléculas não apresentam sabor adocicado, embora sejam formadas pela união de centenas ou mesmo milhares de monossacarídeos.",
                    "c)Os dissacarídeos são constituídos pela união de dois monossacarídeos, e seus representantes mais conhecidos são a celulose, a quitina e o glicogênio.",
                    "d)Os glicídios, além de terem função energética, ainda participam da estrutura dos ácidos nucleicos, tanto RNA quanto DNA.",
                    "e)A função do glicogênio para os animais é equivalente à do amido para as plantas.",

                    "c"},


            {"A fórmula geral dos carboidratos é:",
                    "a) (CH2O)n",
                    "b) (CH3O)n",
                    "c) (CH4O)n",
                    "d) (CH5O)n",
                    "e) (CH6O)n",
                    "a"},

            {" Sobre as funções dos carboidratos é INCORRETO afirmar:",
                    "a)Os alimentos que contém carboidratos fornecem energia ao corpo humano.",
                    "b) O amido é um carboidrato considerado a principal reserva energética dos vegetais.",
                    "c) Os carboidratos participam da formação dos ácidos nucleicos, chamados de pentoses.",
                    "d) Os carboidratos têm a função estrutural em algumas células.",
                    "e) Os carboidratos auxiliam na formação dos ossos do corpo humano.",
                    "e"},

//questão 10
            {"A glicose e a frutose são dois tipos de açúcares essenciais para o consumo humano que apresentam algumas diferenças. I. Tanto a glicose como a frutose são carboidratos simples (monossacarídeos).\n" +
                    "II. A união das moléculas de glicose e frutose gera um outro tipo de açúcar: a sacarose.\n" +
                    "III. A glicose está presente nas frutas, enquanto a frutose nos vegetais adocicados.\n" +
                    "As alternativas corretas são:",
                    "a) I, II e III",
                    "b) I e II",
                    "c) I e III",
                    "d) II e III",
                    "e) n.d.a",
                    "b"},

            {"A celulose é um carboidrato fibroso encontrado em todas as plantas, sendo o polissacarídeo mais abundante na natureza, formado pela condensação de moléculas de:",
                    "a)sacarose",
                    "b)ribulose",
                    "c)maltose",
                    "d)glicose",
                    "e)ribose",
                    "d"},

            // questão 12
            {"Os açúcares mais simples apresentam, em geral, as seguintes propriedades  I. são razoavelmente solúveis em água; \n" +
                    "II. oxidam-se facilmente. :",
                    "a) grupos aldeído e ligações de hidrogênio.",
                    "b) grupos hidroxila e ligações de hidrogênio.",
                    "c) ligações de hidrogênio e grupos hidroxila.",
                    "d) ligações de hidrogênio e grupos aldeído.",
                    "e) grupos hidroxila e grupos carboxila.",
                    "b"},

            {"Os açúcares mais simples apresentam, em geral, as seguintes propriedades  I. são razoavelmente solúveis em água; \n" +
                    "II. oxidam-se facilmente. As propriedades I e II são, respectivamente, devidas à presença de:",
                    "a) grupos aldeído e ligações de hidrogênio.",
                    "b) grupos hidroxila e ligações de hidrogênio.",
                    "c) ligações de hidrogênio e grupos hidroxila.",
                    "d) ligações de hidrogênio e grupos aldeído.",
                    "e) grupos hidroxila e grupos carboxila.",
                    "b"},

            {"Os polissacarídeos são moléculas grandes de carboidratos, denominados de carboidratos complexos. Eles são formados pela união de vários monossacarídeos por meio de ligações:",
                    "a) peptídicas",
                    "b) iônicas",
                    "c) covalentes",
                    "d) glicosídicas",
                    "e) metálicas",
                    "d"},


            {"A equação da fermentação alcoólica é:",
                    "a) (C6H10O5)n + nH2O → nC6H12O6",
                    "b) C12H22O11 + 12 O2 → 12 CO2 + 11 H2O",
                    "c) C2H5OH + 3 O2 → 2 CO2 + 3 H2O",
                    "d) C2H5OH + O2 → CH3COOH + H2O",
                    "e) C6H12O6 → 2 C2H5OH + 2 CO2",
                    "e"},

            // questão 15
            {"Os alimentos ricos em proteínas e lipídios são, respectivamente:",
                    "a)peixe e glicose.",
                    "b)abacate e pera.",
                    "c)carne bovina e óleo de soja.",
                    "d)leite e beterraba.",
                    "e)farinha de trigo e mel.",
                    "c"},

            {"Dentre os seguintes hidratos de carbono, o único praticamente não degradável no aparelho digestivo humano é:",
                    "a)glicose",
                    "b)amido",
                    "c)sacarose",
                    "d)celulose",
                    "e)glicogênio",
                    "d"},


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
                Intent intent = new Intent(BromatoQuizActivity.this, BromatologiaActivity.class);
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


