package com.example.farmacia_app;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProgressoEstudo {
    private final Map<String, Integer> progresso;
    private final SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ProgressoEstudoPrefs";
    private static final String PROGRESSO_KEY = "progresso_json";

    // Construtor com Contexto Android
    public ProgressoEstudo(Context context) {
        this.progresso = new HashMap<>();
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        carregarProgresso();
    }

    public void registrarProgresso(String classe, int progressoClasse) {
        if (progressoClasse >= 0 && progressoClasse <= 100) {
            progresso.put(classe, progressoClasse);
            salvarProgresso(); // Salva sempre que atualizar
        } else {
            System.out.println("O progresso deve estar entre 0 e 100.");
        }
    }

    public void exibirProgresso() {
        System.out.println("Progresso de Estudo:");
        for (Map.Entry<String, Integer> entry : progresso.entrySet()) {
            System.out.println("Classe: " + entry.getKey() + " | Progresso: " + entry.getValue() + "%");
        }
    }

    public int obterProgresso(String classe) {
        return progresso.getOrDefault(classe, 0);
    }

    public void removerProgresso(String classe) {
        progresso.remove(classe);
        salvarProgresso();
    }

    public void resetarProgresso() {
        progresso.clear();
        salvarProgresso();
    }

    public double calcularProgressoTotal() {
        if (progresso.isEmpty()) return 0;
        int soma = 0;
        for (int valor : progresso.values()) {
            soma += valor;
        }
        return (double) soma / progresso.size();
    }

    // Salva o progresso em JSON no SharedPreferences
    private void salvarProgresso() {
        JSONObject json = new JSONObject(progresso);
        sharedPreferences.edit().putString(PROGRESSO_KEY, json.toString()).apply();
    }

    // Carrega o progresso do SharedPreferences
    private void carregarProgresso() {
        String jsonString = sharedPreferences.getString(PROGRESSO_KEY, null);
        if (jsonString != null) {
            try {
                JSONObject json = new JSONObject(jsonString);
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    int value = json.getInt(key);
                    progresso.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
