package com.example.farmacia_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText edtNome, edtCurso;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        edtNome = findViewById(R.id.edt_nome);
        edtCurso = findViewById(R.id.edt_curso);
        btnSalvar = findViewById(R.id.btn_salvar);

        // Carrega os dados salvos anteriormente, se houver
        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        String nomeSalvo = prefs.getString("nome", "");
        String cursoSalvo = prefs.getString("curso", "");

        edtNome.setText(nomeSalvo);
        edtCurso.setText(cursoSalvo);

        btnSalvar.setOnClickListener(v -> {
            String nome = edtNome.getText().toString();
            String curso = edtCurso.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nome", nome);
            editor.putString("curso", curso);
            editor.apply();

            Toast.makeText(this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Volta para a tela anterior
        });
    }
}
