package com.example.farmacia_app;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
import java.util.Random;

public class TokenData {
    private final String token;
    private final String deviceId;
    private final String tipoDispositivo;
    private final String versaoAndroid;
    private final String nomeDispositivo;
    private final String username;
    private double latitude;
    private double longitude;
    private LocalDateTime dataHora;
    private String tipoConexao;
    private String versaoApp;

    private final Random random = new Random();

    // Construtor
    public TokenData(String token, String deviceId, String tipoDispositivo, String versaoAndroid,
                     String nomeDispositivo, String username, double latitude, double longitude,
                     LocalDateTime dataHora, String tipoConexao, String versaoApp, int batteryLevel, String systemLanguage, String wifiNetworkName) {
        this.token = token;
        this.deviceId = deviceId;
        this.tipoDispositivo = tipoDispositivo;
        this.versaoAndroid = versaoAndroid;
        this.nomeDispositivo = nomeDispositivo;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataHora = dataHora;
        this.tipoConexao = tipoConexao;
        this.versaoApp = versaoApp;
    }

    @SuppressLint("NewApi")
    public void atualizarDados() {
        this.latitude += (random.nextDouble() - 0.5) * 0.001;
        this.longitude += (random.nextDouble() - 0.5) * 0.001;
        this.dataHora = LocalDateTime.now();
        this.tipoConexao = random.nextBoolean() ? "Wi-Fi" : "4G";
        this.versaoApp = "1." + random.nextInt(10);
    }

    public void exibirInformacoesDispositivo() {
        System.out.println("\n--- Informações Atualizadas ---");
        System.out.println("Token: " + token);
        System.out.println("Device ID: " + deviceId);
        System.out.println("Tipo de Dispositivo: " + tipoDispositivo);
        System.out.println("Versão do Android: " + versaoAndroid);
        System.out.println("Nome do Dispositivo: " + nomeDispositivo);
        System.out.println("Username: " + username);
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
        System.out.println("Data e Hora: " + dataHora);
        System.out.println("Tipo de Conexão: " + tipoConexao);
        System.out.println("Versão do App: " + versaoApp);
    }



    // Métodos getters
    public String getTipoDispositivo() {
        return tipoDispositivo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getToken() {
        return token;
    }
}
