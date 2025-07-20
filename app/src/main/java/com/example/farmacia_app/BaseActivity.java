package com.example.farmacia_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.app.AlertDialog;  // Adicionada a importação de AlertDialog
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseActivity extends AppCompatActivity {

    private static final Logger log = LoggerFactory.getLogger(BaseActivity.class);

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

}

//

//Agora, nas atividades onde você deseja que o menu apareça, basta herdar de BaseActivity em vez de AppCompatActivity.//

//public class MainActivity extends BaseActivity// <- usa esse

//menu será automaticamente aplicado a todas as atividades que herdam de BaseActivity, sem necessidade de duplicar o código.//

//Caso queira sobrescrever ou personalizar o comportamento do menu para alguma atividade específica, você pode fazer isso nas atividades que herdam a BaseActivity usando a anotação overide
