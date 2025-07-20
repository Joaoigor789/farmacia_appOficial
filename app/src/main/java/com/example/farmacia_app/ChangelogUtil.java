package com.example.farmacia_app;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;

public class ChangelogUtil {

    // Versão atual do changelog
    private static final String VERSAO_ATUAL = "1.0.9";
    private static final String PREFS_NAME = "changelog_prefs";
    private static final String KEY_ULTIMA_VERSAO = "ultima_versao_exibida";

    // Função para exibir o changelog
    public static void exibirChangelog(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String ultimaVersaoExibida = prefs.getString(KEY_ULTIMA_VERSAO, "");

        // Se já foi exibido para essa versão, não mostrar novamente
        if (VERSAO_ATUAL.equals(ultimaVersaoExibida)) {
            return;
        }

        String changelog = "Versão 1.0.9 (2025-04-29)\n" +
                "- Inicialização do Aplicativo:\n" +
                "  • Criação do esqueleto do aplicativo de farmácia.\n" +
                "  • Implementação das telas de login, cadastro e autenticação.\n" +
                "  • Tela principal configurada com a integração de criptografia.\n\n" +
                "- Criptografia:\n" +
                "  • Implementação da criptografia e descriptografia automática de arquivos no aplicativo.\n" +
                "  • A criptografia será realizada assim que o aplicativo for instalado, garantindo a segurança dos dados do usuário.\n\n" +
                "- MainActivity:\n" +
                "  • Adicionada verificação de criptografia correta com alerta exibido na inicialização.\n" +
                "  • Mensagens de sucesso via Toast ou AlertDialog ao configurar a criptografia.\n\n" +
                "- Correção de Permissões:\n" +
                "  • Correção nas permissões no AndroidManifest.xml.\n" +
                "  • Permissões para biometria e acesso à rede aplicadas.\n\n" +
                "- Integração Firebase:\n" +
                "  • Firebase para notificações push e métricas de uso.\n" +
                "  • Serviço Firebase Messaging implementado.\n\n" +
                "- Melhorias na UI/UX:\n" +
                "  • Layout modernizado e mais fluido.\n" +
                "  • Correções de bugs na navegação.\n\n" +
                "- Outras Melhorias:\n" +
                "  • Ajustes no Firebase e desempenho de rede.\n" +
                "  • Backup e recuperação de dados adicionados.\n";

        new AlertDialog.Builder(context)
                .setTitle("Changelog")
                .setMessage(changelog)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Salva a versão como exibida
                    prefs.edit().putString(KEY_ULTIMA_VERSAO, VERSAO_ATUAL).apply();
                    dialog.dismiss();
                })
                .show();
    }

    // Função de criptografia (exemplo simplificado)
    public static void criptografarArquivos(Context context) {
        CryptoManager cryptoManager = new CryptoManager(context);
        cryptoManager.criptografarTodosArquivos();
    }
}
