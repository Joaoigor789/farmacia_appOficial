package com.example.farmacia_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

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

        } else if (id == R.id.menu_baixar_app) {
            Intent intent = new Intent(this, MenuRedeActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menu_sobre) {
            showAboutDialog();
            return true;

        } else if (id == R.id.menu_autor) {
            showAutorDialog();
            return true;

        } else if (id == R.id.menu_atualizacao) {
            exibirChangelog();
            return true;

        } else if (id == R.id.menu_perfil) {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menu_calendario) {
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

    private void mostrarCalendario() {
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setVisibility(CalendarView.VISIBLE);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String dataSelecionada = "Data: " + dayOfMonth + "/" + (month + 1) + "/" + year;
            new AlertDialog.Builder(MenuActivity.this)
                    .setMessage(dataSelecionada)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    private void exibirChangelog() {
        String changelog = "Versão 1.0.7\n" +
                "- Correção de bugs\n" +
                "- Correção feita no Menu\n" +
                "- Correção blink main\n" +
                "- Melhorias no desempenho\n" +
                "- add politicas públicas e cookies\n" +
                "- Bromatologia modificado/banco de dados\n" +
                "- Tempo esgotado, só será exibido no quiz\n" +
                "- Corrigido o quiz\n" +
                "- Modificação feita no banco de dados de medicamentos\n" +
                "- Adicionada barra de progresso sincronizada com a porcentagem assistida do vídeo.\n" +
                "- Novo sistema de Quiz, progresso";

        new AlertDialog.Builder(this)
                .setTitle("Changelog - Atualizações")
                .setMessage(changelog)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                .show();
    }

    private void showAutorDialog() {
        String info = "Autor: João Igor Rodrigues Pereira da Silva\n" +
                "Empresa: Farmacia AppTech Tecnology\n" +
                "Projeto: Aplicativo de Farmácia";

        new AlertDialog.Builder(this)
                .setTitle("Informações do Autor")
                .setMessage(info)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                .show();
    }

    private void showAboutDialog() {
        String version = "Versão 1.0.7";
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
}
