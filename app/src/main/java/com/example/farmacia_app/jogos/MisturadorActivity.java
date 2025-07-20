package com.example.farmacia_app.jogos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.farmacia_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MisturadorActivity extends AppCompatActivity {

    private final List<Ingrediente> ingredientesDisponiveis = new ArrayList<>();
    private final List<Formula> formulas = new ArrayList<>();
    private Formula formulaAtual;

    private ListView listViewIngredientes;
    private EditText editTextIngrediente1;
    private EditText editTextIngrediente2;
    private EditText editTextIngrediente3;
    private TextView textoResultado;
    private TextView textoNomeFormula;

    private final List<String> nomesIngredientes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misturador);

        // Inicialização dos componentes
        listViewIngredientes = findViewById(R.id.listViewIngredientes);
        editTextIngrediente1 = findViewById(R.id.editTextIngrediente1);
        editTextIngrediente2 = findViewById(R.id.editTextIngrediente2);
        editTextIngrediente3 = findViewById(R.id.editTextIngrediente3);
        Button botaoMisturar = findViewById(R.id.botaoMisturar);
        textoResultado = findViewById(R.id.textoResultado);
        textoNomeFormula = findViewById(R.id.textoNomeFormula);

        // Criar ingredientes e fórmulas
        criarIngredientes();
        criarFormulas();
        carregarFormulaAleatoria();
        exibirIngredientesDisponiveis();

        // Configurar o botão para verificar a mistura
        botaoMisturar.setOnClickListener(view -> verificarMistura());

        // Configurar a seleção de ingredientes nos EditTexts
        configurarSelecaoIngredientes(editTextIngrediente1);
        configurarSelecaoIngredientes(editTextIngrediente2);
        configurarSelecaoIngredientes(editTextIngrediente3);
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

    @SuppressLint("SetTextI18n")
    private void carregarFormulaAleatoria() {
        // Gerar índice aleatório para a fórmula
        Random random = new Random();
        int indice = random.nextInt(formulas.size());
        formulaAtual = formulas.get(indice);
        textoNomeFormula.setText("Misture para criar: " + formulaAtual.getNomeFormula());
    }

    private void exibirIngredientesDisponiveis() {
        nomesIngredientes.clear();
        for (Ingrediente ingrediente : ingredientesDisponiveis) {
            nomesIngredientes.add(ingrediente.getNome());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                nomesIngredientes
        );
        listViewIngredientes.setAdapter(adapter);
    }

    private void configurarSelecaoIngredientes(final EditText editText) {
        editText.setFocusable(false); // Não abre o teclado
        editText.setClickable(true);  // Torna clicável
        editText.setOnClickListener(view -> exibirDialogoSelecao(editText));
    }

    private void exibirDialogoSelecao(final EditText editText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione um ingrediente");
        builder.setItems(nomesIngredientes.toArray(new String[0]), (dialogInterface, i) -> editText.setText(nomesIngredientes.get(i)));
        builder.show();
    }

    private void verificarMistura() {
        List<Ingrediente> tentativa = new ArrayList<>();

        // Mostrar o ProgressBar
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        // Adicionando os ingredientes selecionados à tentativa
        if (!editTextIngrediente1.getText().toString().isEmpty())
            tentativa.add(new Ingrediente(editTextIngrediente1.getText().toString()));

        if (!editTextIngrediente2.getText().toString().isEmpty())
            tentativa.add(new Ingrediente(editTextIngrediente2.getText().toString()));

        if (!editTextIngrediente3.getText().toString().isEmpty())
            tentativa.add(new Ingrediente(editTextIngrediente3.getText().toString()));


        // Simular o processo de mistura (delay artificial)
        new Handler().postDelayed(() -> {
            // Comparar os ingredientes sem considerar a ordem
            boolean resultado = Misturador.misturar(formulaAtual, tentativa);

            if (resultado) {
                textoResultado.setText("✅ Mistura correta! Fórmula criada!");
                Toast.makeText(this, "Parabéns! 🎉", Toast.LENGTH_SHORT).show();
            } else {
                textoResultado.setText("❌ Mistura incorreta! Tente novamente.");
                Toast.makeText(this, "Ops... Algo errado! 🚫", Toast.LENGTH_SHORT).show();
            }

            // Esconder o ProgressBar após o processo
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }, 2000); // 2 segundos de atraso para simular o efeito de mistura
    }
}
