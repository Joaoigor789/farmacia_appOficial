package com.example.farmacia_app;

import android.content.Context;

public class PolicyAdapter {
    private final Context context;
    private final String question;

    public PolicyAdapter(Context context, String question) {
        this.context = context;
        this.question = question;
    }

    // Retorna as opções de resposta baseadas na questão
    public String[] getOptions() {
        switch (question) {
            case "Qual a principal função das políticas públicas de farmácia?":
                return new String[] {
                        "Garantir acesso a medicamentos",
                        "Aumentar o lucro das farmácias",
                        "Criar novos medicamentos",
                        "Reforçar as campanhas publicitárias"
                };
            case "O que é a Lei nº 13.021/2014?":
                return new String[] {
                        "Regula o funcionamento das farmácias",
                        "Regula o mercado de medicamentos",
                        "Regula os preços dos medicamentos",
                        "Cria novas políticas de saúde"
                };
            case "Qual é a função do farmacêutico nas políticas de saúde pública?":
                return new String[] {
                        "Gerir farmácias",
                        "Participar de políticas de saúde coletiva",
                        "Vender medicamentos",
                        "Fazer pesquisa científica"
                };
            default:
                return new String[] {};
        }
    }

    public Context getContext() {
        return context;
    }
}
