package com.example.farmacia_app;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class QuimicaAmbientalActivity extends AppCompatActivity {

    // Agora com layoutPH como LinearLayout
    private LinearLayout layoutConcentracao, layoutIQA, layoutPH, layoutToxicidade, layoutDiluicao;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quimica_ambiental);

        mediaPlayer = MediaPlayer.create(this, R.raw.click);

        layoutConcentracao = findViewById(R.id.layout_concentracao);
        layoutIQA = findViewById(R.id.layout_iqa);
        layoutPH = findViewById(R.id.layout_ph); // <- adicionado
        layoutToxicidade = findViewById(R.id.layout_toxicidade);
        layoutDiluicao = findViewById(R.id.layout_diluicao);

        Button btnConcentracao = findViewById(R.id.btnConcentracao);
        Button btnIQA = findViewById(R.id.btnIQA);
        Button btnPH = findViewById(R.id.btnPH); // <- adicionado
        Button btnToxicidade = findViewById(R.id.btnToxicidade);
        Button btnDiluicao = findViewById(R.id.btnDiluicao);

        View.OnClickListener btnClickListener = v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }

            int id = v.getId();
            if (id == R.id.btnConcentracao) {
                mostrarSecao(0);
            } else if (id == R.id.btnIQA) {
                mostrarSecao(1);
            } else if (id == R.id.btnPH) {
                mostrarSecao(2);
            } else if (id == R.id.btnToxicidade) {
                mostrarSecao(3);
            } else if (id == R.id.btnDiluicao) {
                mostrarSecao(4);
            }
        };

        btnConcentracao.setOnClickListener(btnClickListener);
        btnIQA.setOnClickListener(btnClickListener);
        btnPH.setOnClickListener(btnClickListener); // <- adicionado
        btnToxicidade.setOnClickListener(btnClickListener);
        btnDiluicao.setOnClickListener(btnClickListener);

        configurarConcentracao();
        configurarIQA();
        configurarPH(); // <- adicionado

        mostrarSecao(0);
        iniciarAnimacaoCorFundo();
    }

    private void mostrarSecao(int index) {
        layoutConcentracao.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
        layoutIQA.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
        layoutPH.setVisibility(index == 2 ? View.VISIBLE : View.GONE); // <- adicionado
        layoutToxicidade.setVisibility(index == 3 ? View.VISIBLE : View.GONE);
        layoutDiluicao.setVisibility(index == 4 ? View.VISIBLE : View.GONE);
    }

    private void configurarConcentracao() {
        EditText edtMassa = layoutConcentracao.findViewById(R.id.edtMassa);
        EditText edtVolume = layoutConcentracao.findViewById(R.id.edtVolume);
        TextView txtResultado = layoutConcentracao.findViewById(R.id.txtResultadoConcentracao);
        Button btnCalcular = layoutConcentracao.findViewById(R.id.btnCalcularConcentracao);

        btnCalcular.setOnClickListener(v -> {
            String massaStr = edtMassa.getText().toString();
            String volumeStr = edtVolume.getText().toString();
            if (massaStr.isEmpty() || volumeStr.isEmpty()) {
                toast("Preencha todos os campos");
                return;
            }
            try {
                double massa = Double.parseDouble(massaStr);
                double volume = Double.parseDouble(volumeStr);
                if (volume == 0) {
                    toast("Volume não pode ser zero");
                    return;
                }
                double concentracao = massa / volume;
                txtResultado.setText("Concentração: " + concentracao + " mg/m³");
            } catch (NumberFormatException e) {
                toast("Valores inválidos");
            }
        });
    }

    private void configurarIQA() {
        EditText edtCO = layoutIQA.findViewById(R.id.edtCO);
        EditText edtNO2 = layoutIQA.findViewById(R.id.edtNO2);
        EditText edtPM25 = layoutIQA.findViewById(R.id.edtPM25);
        EditText edtSO2 = layoutIQA.findViewById(R.id.edtSO2);
        EditText edtO3 = layoutIQA.findViewById(R.id.edtO3);
        TextView txtResultado = layoutIQA.findViewById(R.id.txtResultadoIQA);
        Button btnCalcular = layoutIQA.findViewById(R.id.btnCalcularIQA);

        btnCalcular.setOnClickListener(v -> {
            try {
                double co = parseOrZero(edtCO.getText().toString());
                double no2 = parseOrZero(edtNO2.getText().toString());
                double pm25 = parseOrZero(edtPM25.getText().toString());
                double so2 = parseOrZero(edtSO2.getText().toString());
                double o3 = parseOrZero(edtO3.getText().toString());

                double iqa = (co * 0.2 + no2 * 0.2 + pm25 * 0.3 + so2 * 0.15 + o3 * 0.15);

                String categoria;
                if (iqa <= 50) categoria = "Bom";
                else if (iqa <= 100) categoria = "Moderado";
                else if (iqa <= 150) categoria = "Ruim";
                else if (iqa <= 200) categoria = "Muito Ruim";
                else categoria = "Péssimo";

                txtResultado.setText(String.format("IQA: %.2f - %s", iqa, categoria));
            } catch (NumberFormatException e) {
                toast("Valores inválidos");
            }
        });
    }

    private void configurarPH() {
        EditText edtPHValor = layoutPH.findViewById(R.id.edtPHValor);
        TextView txtResultadoPH = layoutPH.findViewById(R.id.txtResultadoPH);
        Button btnCalcularPH = layoutPH.findViewById(R.id.btnCalcularPH);

        btnCalcularPH.setOnClickListener(v -> {
            String phStr = edtPHValor.getText().toString();
            if (phStr.isEmpty()) {
                toast("Digite um valor de pH");
                return;
            }
            try {
                double ph = Double.parseDouble(phStr);
                String resultado;
                if (ph < 7) resultado = "Ácido";
                else if (ph == 7) resultado = "Neutro";
                else resultado = "Básico";

                txtResultadoPH.setText("pH: " + ph + " - " + resultado);
            } catch (NumberFormatException e) {
                toast("Valor inválido");
            }
        });
    }

    private void iniciarAnimacaoCorFundo() {
        if (layoutConcentracao == null) return;

        int corInicio = Color.rgb(173, 216, 230);
        int corMeio = Color.rgb(144, 238, 144);
        int corFim = Color.rgb(255, 182, 193);

        ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), corInicio, corMeio, corFim, corInicio);
        colorAnim.setDuration(15000);
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.RESTART);

        colorAnim.addUpdateListener(animator -> {
            int corAtual = (int) animator.getAnimatedValue();
            layoutConcentracao.setBackgroundColor(corAtual);
        });

        colorAnim.start();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private double parseOrZero(String str) {
        if (str == null || str.isEmpty()) return 0;
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
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
        } else if (id == R.id.menu_autor) {
            showAutorDialog();
            return true;
        } else if (id == R.id.menu_atualizacao) {
            ChangelogUtil.exibirChangelog(this);
            return true;
        } else if (id == R.id.menu_perfil) {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
            return false;
        } else if (id == R.id.menu_calendario) {  // Novo item do menu
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
            mostrarCalendario();
            return true;
        } else if (id == R.id.menu_licenca) {
            showLicenseDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showErrorDialog(Exception e) {
        // TODO document why this method is empty
    }

    private void mostrarCalendario() {
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setVisibility(View.VISIBLE); // Exibe o calendário

//
    }
    private void showAutorDialog () {
        String info = "Autor: João Igor Rodrigues Pereira da Silva\n" +
                "Empresa: Farmacia AppTech Tecnology\n" +
                "Projeto: Aplicativo de Farmácia";

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Informações do Autor")
                .setMessage(info)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss(); // Fecha o diálogo
                });
        builder.create().show();
    }

    private void showAboutDialog () {
        String version = "Versão 1.0.7";
        String info = "Aplicativo para Farmacia";
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Sobre")
                .setMessage(info + "\n" + version)
                .setPositiveButton("OK", null)
                .show();
    }

    // Novo método para exibir as informações de licença
    private void showLicenseDialog () {
        String licenseInfo = "Copyright 2025 João Igor Rodrigues Pereira da Silva, Farmacia AppTech Tecnology\n\n" +
                "Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License (CC BY-NC-ND 4.0);\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at:\n" +
                "https://creativecommons.org/licenses/by-nc-nd/4.0/\n\n" +
                "You are allowed to copy and redistribute the material, but you may **not modify** it or use it for commercial purposes without explicit permission from the author.\n\n" +
                "The use of this application is permitted **only for educational purposes**.";

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Licença")
                .setMessage(licenseInfo)
                .setPositiveButton("OK", null)
                .show();
    }



}
