package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import org.jetbrains.annotations.NotNull;
import android.webkit.WebViewClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BromatologiaActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private SQLiteDatabase db;
    private int perguntaAtual = 0;
    private int acertos = 0;

    private WebView webView;
    private TextView tvConteudoEstudo, tvPergunta;
    private RadioGroup rgOpcoes;
    private RadioButton rbOpcao1, rbOpcao2, rbOpcao3;
    private Button btnResponder, btnReiniciar;


    @Override
    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bromatologia);

        // Inicializa o WebView
        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        // Carregar o arquivo HTML dentro da pasta raw
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.seuarquivo);
            String htmlContent = convertStreamToString(inputStream);

            // Carregar o conteúdo no WebView, garantindo que o WebView consiga acessar os recursos dentro da pasta raw
            String baseUrl = "file:///android_res/raw/";

            // Carregar o HTML com base no caminho correto para os recursos
            webView.loadDataWithBaseURL(baseUrl, htmlContent, "text/html", "UTF-8", null);

        } catch (IOException e) {
            Log.e("BromatologiaActivity", "Erro ao carregar arquivo HTML: " + e.getMessage(), e);
        }

        // Configuração do OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack(); // Volta para a página anterior no WebView
                } else {
                    finish(); // Se não houver páginas anteriores, sai da Activity
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);


        // Array de imagens para o ViewPager2
        int[] imagesArray = { R.drawable.image123, R.drawable.image1234, R.drawable.image12345, R.drawable.image789 };
        List<Integer> imagesList = new ArrayList<>();
        for (int image : imagesArray) {
            imagesList.add(image);
        }

        // Configuração do ViewPager2 com ImageFragmentStateAdapter
        ViewPager2 viewPager = findViewById(R.id.viewPager1);
        ImageFragmentStateAdapter adapter = new ImageFragmentStateAdapter(this, imagesList);
        viewPager.setAdapter(adapter);

        // Inicializando elementos da interface
        viewFlipper = findViewById(R.id.viewFlipper);
        TextView tvInfoBromatologia = findViewById(R.id.tvInfoBromatologia);

        Button btnSimulado = findViewById(R.id.btnSimulado);
        Button btnIrParaEstudo = findViewById(R.id.btnIrParaEstudo);

        tvConteudoEstudo = findViewById(R.id.tvConteudoEstudo);
        tvPergunta = findViewById(R.id.tvPergunta);

        rgOpcoes = findViewById(R.id.rgOpcoes);
        rbOpcao1 = findViewById(R.id.rbOpcao1);
        rbOpcao2 = findViewById(R.id.rbOpcao2);
        rbOpcao3 = findViewById(R.id.rbOpcao3);
        btnResponder = findViewById(R.id.btnResponder);
        btnReiniciar = findViewById(R.id.btnReiniciar);

        // Inicializando o banco de dados
        Log.d("BromatologiaApp", "Abrindo o banco de dados...");
        db = openOrCreateDatabase("bromatologia.db", MODE_PRIVATE, null);
        Log.d("BromatologiaApp", "Banco de dados aberto com sucesso.");

        // Chamando a função para criar a tabela
        Log.d("BromatologiaApp", "Criando tabela...");
        criarTabela();
        Log.d("BromatologiaApp", "Tabela criada com sucesso.");

        String textoEstudo = "A bromatologia estuda a composição, qualidade e segurança dos alimentos...";
        tvInfoBromatologia.setText(textoEstudo);

        btnIrParaEstudo.setOnClickListener(v -> {
            // Ação para ir para a nova Activity
            Intent intent = new Intent(BromatologiaActivity.this, EstudoActivity.class);
            startActivity(intent);
            // Exibir o próximo item no ViewFlipper
            viewFlipper.setDisplayedChild(1);
        });

        // Novo botão para notas
        Button btnNotas = findViewById(R.id.btnNotas);
        btnNotas.setOnClickListener(v -> {
            Intent intent = new Intent(BromatologiaActivity.this, NotasActivity.class);
            startActivity(intent);
        });

        // Novo botão para quiz estudo novo (prova revisão)
        Button btnquizestudo = findViewById(R.id.btnquizestudo);
        btnquizestudo.setOnClickListener(v -> {
            Intent intent = new Intent(BromatologiaActivity.this, BromatoQuizActivity.class);
            startActivity(intent);
        });

        // Novo botão para quiz estudo novo (prova revisão)
        Button btnquizlipideo = findViewById(R.id.btnquizlipideo);
        btnquizlipideo.setOnClickListener(v -> {
            Intent intent = new Intent(BromatologiaActivity.this, BromatoQuizActivityLipideos.class);
            startActivity(intent);
        });



        btnSimulado.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(2);
            iniciarSimulado();
        });

        btnResponder.setOnClickListener(v -> verificarResposta());
        btnReiniciar.setOnClickListener(v -> reiniciarSimulado());
    }

    // Método auxiliar para converter InputStream em String
    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
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
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
            ChangelogUtil.exibirChangelog(this);
            return true;
        } else if (id == R.id.menu_perfil) {
            try {
                Intent intent = new Intent(this, PerfilActivity.class);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                // Captura e exibe o erro
                showErrorDialog(e);
                return false;
            }
        
        }
        return super.onOptionsItemSelected(item);
    }

    private void showErrorDialog(Exception e) {
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



    private void criarTabela() {
        db.execSQL("CREATE TABLE IF NOT EXISTS questions (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "question TEXT, option_1 TEXT, option_2 TEXT, option_3 TEXT, correct_option INTEGER)");
        adicionarPerguntasAoBanco();
    }

    private void adicionarPerguntasAoBanco() {
        db.execSQL("DELETE FROM questions"); // Evita duplicatas
        String[][] perguntas = {
                {"O que a bromatologia estuda?", "Alimentos e sua composição", "Reações químicas no corpo", "Medicamentos e efeitos", "1"},
                {"Qual órgão regulamenta a segurança alimentar no Brasil?", "ANVISA", "OMS", "FDA", "1"},
                {"Qual análise NÃO é realizada na bromatologia?", "Análise de combustíveis", "Análises microbiológicas", "Teste de qualidade alimentar", "1"},
                {"O que a bromatologia avalia nos alimentos?", "Composição química e qualidade", "Apenas sabor", "Apenas embalagem", "1"},
                {"O que são macronutrientes?", "Proteínas, carboidratos e gorduras", "Vitaminas e minerais", "Fibras e água", "1"},
                {"Qual técnica é usada para detectar microorganismos em alimentos?", "Cultura microbiológica", "Espectroscopia", "Titulação ácida", "1"},
                {"O que é um aditivo alimentar?", "Substância adicionada para melhorar conservação", "Ingrediente principal dos alimentos", "Componente tóxico dos alimentos", "1"},
                {"O que significa APPCC na segurança alimentar?", "Análise de Perigos e Pontos Críticos de Controle", "Associação de Pesquisa em Produtos de Consumo", "Avaliação de Processos Produtivos de Controle", "1"},
                {"O que é um contaminante químico em alimentos?", "Pesticidas, metais pesados, toxinas", "Bactérias e vírus", "Partículas de plástico", "1"},
                {"Qual é a principal fonte de carboidratos na dieta humana?", "Grãos e tubérculos", "Carnes e ovos", "Leite e derivados", "1"},
                {"O que a análise sensorial avalia em um alimento?", "Sabor, textura, aroma e aparência", "Valor nutricional", "Apenas a composição química", "1"},
                {"Qual método é usado para identificar proteínas em alimentos?", "Método de Kjeldahl", "Cromatografia gasosa", "Espectrofotometria", "1"},
                {"Qual processo é usado para eliminar microorganismos patogênicos em alimentos?", "Pasteurização", "Filtração", "Congelamento", "1"},
                {"O que é um alimento ultraprocessado?", "Produto industrializado com muitos aditivos", "Frutas e vegetais in natura", "Carne fresca e leite", "1"},
                {"O que são micronutrientes?", "Vitaminas e minerais", "Proteínas e gorduras", "Carboidratos e fibras", "1"},
                {"O que significa rotulagem nutricional?", "Informações sobre composição do alimento", "Avaliação de embalagem", "Análise microbiológica", "1"},
                {"Qual é o objetivo da embalagem dos alimentos?", "Proteger e conservar alimentos", "Apenas atrair consumidores", "Apenas facilitar transporte", "1"},
                {"Qual substância é usada para detectar adulteração no leite?", "Lactodensímetro", "Refratômetro", "pHmetro", "1"},
                {"O que é um alimento funcional?", "Alimento que traz benefícios adicionais à saúde", "Alimento industrializado", "Alimento sem calorias", "1"},
                {"Qual é a função dos conservantes nos alimentos?", "Evitar deterioração por microorganismos", "Dar cor aos alimentos", "Melhorar sabor", "1"},
                {"O que a bromatologia estuda na carne?", "Teor de proteínas, gorduras e qualidade", "Apenas sabor", "Apenas cor", "1"},
                {"O que significa pH em alimentos?", "Indicador de acidez ou alcalinidade", "Nível de proteínas", "Quantidade de água presente", "1"},
                {"Qual método é usado para medir o teor de gordura em alimentos?", "Extração Soxhlet", "Cromatografia líquida", "Titulação básica", "1"},
                {"O que são fibras alimentares?", "Componentes vegetais não digeríveis", "Proteínas essenciais", "Gorduras saturadas", "1"},
                {"Qual a importância da água na composição dos alimentos?", "Ajuda na textura e conservação", "Apenas para diluir ingredientes", "Não tem importância", "1"},
                {"O que são toxinas alimentares?", "Substâncias tóxicas produzidas por microorganismos", "Vitaminas essenciais", "Minerais essenciais", "1"},
                {"O que significa validade de um alimento?", "Período seguro para consumo", "Período de produção", "Período de congelamento", "1"},
                {"Qual método é usado para detectar resíduos de pesticidas em alimentos?", "Cromatografia gasosa", "Destilação simples", "Titulação ácida", "1"},
                {"O que significa umidade em um alimento?", "Quantidade de água presente", "Nível de conservantes", "Teor de gorduras", "1"},
                {"Qual análise verifica a qualidade do leite?", "Teste de acidez, densidade e gordura", "Apenas análise sensorial", "Apenas pasteurização", "1"}
        };
        for (String[] p : perguntas) {
            db.execSQL("INSERT INTO questions (question, option_1, option_2, option_3, correct_option) VALUES (?, ?, ?, ?, ?)",
                    new Object[]{p[0], p[1], p[2], p[3], Integer.parseInt(p[4])});
        }
    }


    @SuppressLint("SetTextI18n")
    private void carregarPergunta() {
        try (Cursor cursor = db.rawQuery("SELECT question, option_1, option_2, option_3 FROM questions LIMIT 1 OFFSET ?",
                new String[]{String.valueOf(perguntaAtual)})) {
            if (cursor.moveToFirst()) {
                tvPergunta.setText(cursor.getString(0));
                rbOpcao1.setText(cursor.getString(1));
                rbOpcao2.setText(cursor.getString(2));
                rbOpcao3.setText(cursor.getString(3));
                rgOpcoes.clearCheck();
            } else {
                tvPergunta.setText("Fim do simulado! Você acertou " + acertos + " de " + perguntaAtual + " perguntas.");
                rgOpcoes.setVisibility(View.GONE);
                btnResponder.setVisibility(View.GONE);
                btnReiniciar.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("Bromatologia", "Erro ao carregar pergunta: " + e.getMessage());
        }
    }

    @SuppressLint("SetTextI18n")
    private void verificarResposta() {
        int respostaSelecionada = rgOpcoes.getCheckedRadioButtonId();
        if (respostaSelecionada == -1) {
            tvPergunta.setText("Selecione uma opção antes de continuar!");
            return;
        }

        int respostaCorreta = -1;
        try (Cursor cursor = db.rawQuery("SELECT correct_option FROM questions LIMIT 1 OFFSET ?",
                new String[]{String.valueOf(perguntaAtual)})) {
            if (cursor.moveToFirst()) {
                respostaCorreta = cursor.getInt(0);
            }

        } catch (Exception e) {
            Log.e("Bromatologia", "Erro ao verificar resposta: " + e.getMessage());

        }

        int indexSelecionado = rgOpcoes.indexOfChild(findViewById(respostaSelecionada));
        if (indexSelecionado == respostaCorreta - 1)

        {
            acertos++;
        }

        perguntaAtual++;

        carregarPergunta();
    }

    private void iniciarSimulado() {
        perguntaAtual = 0;
        acertos = 0;
        rgOpcoes.setVisibility(View.VISIBLE);
        btnResponder.setVisibility(View.VISIBLE);
        btnReiniciar.setVisibility(View.GONE);
        carregarPergunta();
    }

    private void reiniciarSimulado() {
        iniciarSimulado();
    }
}

// Fragment que exibe as imagens no ViewPager2
class ImageFragment extends Fragment {
    private final int imageResId;

    public ImageFragment(int imageResId) {
        this.imageResId = imageResId;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(imageResId);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }
}

