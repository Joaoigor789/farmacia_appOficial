package com.example.farmacia_app.jogos;

import java.util.List;

public class Formula {
    private String nomeFormula;
    private List<Ingrediente> ingredientes;

    public Formula(String nomeFormula, List<Ingrediente> ingredientes) {
        this.nomeFormula = nomeFormula;
        this.ingredientes = ingredientes;
    }

    public String getNomeFormula() {
        return nomeFormula;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }
}
