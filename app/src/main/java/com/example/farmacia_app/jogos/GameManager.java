package com.example.farmacia_app.jogos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    private List<Ingrediente> ingredientesDisponiveis = new ArrayList<>();
    private List<Formula> formulas = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public GameManager() {
        criarIngredientes();
        criarFormulas();
    }

    private void criarIngredientes() {
        ingredientesDisponiveis.add(new Ingrediente("Paracetamol"));
        ingredientesDisponiveis.add(new Ingrediente("Cafeína"));
        ingredientesDisponiveis.add(new Ingrediente("Base de Gel Neutro"));
        ingredientesDisponiveis.add(new Ingrediente("Ácido Salicílico"));
        ingredientesDisponiveis.add(new Ingrediente("Óleo de Amêndoas"));
    }

    private void criarFormulas() {
        formulas.add(new Formula(
                "Pomada Analgésica",
                Arrays.asList(
                        new Ingrediente("Paracetamol"),
                        new Ingrediente("Base de Gel Neutro")
                )
        ));

        formulas.add(new Formula(
                "Creme Estimulante",
                Arrays.asList(
                        new Ingrediente("Cafeína"),
                        new Ingrediente("Óleo de Amêndoas"),
                        new Ingrediente("Base de Gel Neutro")
                )
        ));
    }

    public void jogar() {
        System.out.println("🎮 Bem-vindo ao Misturador de Fórmulas!");
        System.out.println("--------------------------------------\n");

        Formula formula = formulas.get((int) (Math.random() * formulas.size()));

        System.out.println("📜 Receita solicitada: " + formula.getNomeFormula());
        System.out.println("Escolha os ingredientes na ordem correta!");

        exibirIngredientesDisponiveis();

        List<Ingrediente> tentativa = new ArrayList<>();
        for (int i = 0; i < formula.getIngredientes().size(); i++) {
            System.out.print("Digite o nome do ingrediente #" + (i + 1) + ": ");
            String escolha = scanner.nextLine();
            tentativa.add(new Ingrediente(escolha));
        }

        boolean resultado = Misturador.misturar(formula, tentativa);

        if (resultado) {
            System.out.println("\n✅ Mistura realizada com sucesso! Fórmula criada!");
        } else {
            System.out.println("\n❌ Mistura incorreta! Fórmula falhou...");
        }
    }

    private void exibirIngredientesDisponiveis() {
        System.out.println("\nIngredientes disponíveis:");
        for (Ingrediente ingrediente : ingredientesDisponiveis) {
            System.out.println("- " + ingrediente.getNome());
        }
        System.out.println();
    }
}
