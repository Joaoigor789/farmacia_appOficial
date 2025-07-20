package com.example.farmacia_app;

public class AppStarter {

    public static void main(String[] args) {
        verificarNivelBateria();
    }

    private static void verificarNivelBateria() {
        System.out.println("🔋 Simulação de verificação de bateria...");
        // Como Java SE não acessa bateria, use valor simulado
        int percentual = 12;

        if (percentual <= 15) {
            System.out.println("⚠️ Bateria baixa: " + percentual + "%");
        } else {
            System.out.println("🔋 Bateria OK: " + percentual + "%");
        }
    }
}
