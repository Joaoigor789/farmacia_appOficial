package com.example.farmacia_app.jogos;

import java.util.HashSet;
import java.util.List;

public class Misturador {
    public static boolean misturar(Formula formula, List<Ingrediente> tentativa) {
        // Criar Sets para ignorar a ordem dos ingredientes
        HashSet<Ingrediente> ingredientesDaFormula = new HashSet<>(formula.getIngredientes());
        HashSet<Ingrediente> ingredientesDaTentativa = new HashSet<>(tentativa);

        // Comparar as duas listas sem considerar a ordem
        return ingredientesDaFormula.equals(ingredientesDaTentativa);
    }
}
