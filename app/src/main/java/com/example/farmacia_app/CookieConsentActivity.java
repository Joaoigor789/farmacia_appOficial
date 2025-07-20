package com.example.farmacia_app;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CookieConsentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookie_consent);

        // Verificar se o usuário já aceitou os cookies
        SharedPreferences sharedPreferences = getSharedPreferences("cookies", MODE_PRIVATE);
        boolean cookiesAceitos = sharedPreferences.getBoolean("cookies_aceitos", false);

        if (!cookiesAceitos) {
            // Exibir o diálogo de consentimento de cookies
            new AlertDialog.Builder(this)
                    .setTitle("Política de Privacidade")
                    .setMessage("Este aplicativo usa cookies para melhorar sua experiência. Você aceita o uso de cookies?")
                    .setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Armazenar que o usuário aceitou os cookies
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("cookies_aceitos", true);
                            editor.apply();
                            Toast.makeText(CookieConsentActivity.this, "Cookies aceitos", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Recusar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Armazenar que o usuário recusou os cookies
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("cookies_aceitos", false);
                            editor.apply();
                            Toast.makeText(CookieConsentActivity.this, "Cookies recusados", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    }
}

