package com.example.farmacia_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContatoDesenvolvedorActivity extends AppCompatActivity {

    private TextView textMensagensDev;
    private EditText editMensagemDev;
    private Button btnEnviarMensagemDev;
    private DatabaseReference devChatReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_layout);

        textMensagensDev = findViewById(R.id.textMensagensDev);
        editMensagemDev = findViewById(R.id.editMensagemDev);
        btnEnviarMensagemDev = findViewById(R.id.btnEnviarMensagemDev);

        devChatReference = FirebaseDatabase.getInstance().getReference("chat_dev");
        carregarMensagensDesenvolvedor();

        btnEnviarMensagemDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagemDesenvolvedor();
            }
        });

    }

    private void carregarMensagensDesenvolvedor() {
        devChatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder mensagens = new StringBuilder();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    mensagens.append(dataSnapshot.getValue(String.class)).append("\n");
                }
                textMensagensDev.setText(mensagens.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Lidar com erro
            }
        });
    }

    private void enviarMensagemDesenvolvedor() {
        String texto = editMensagemDev.getText().toString().trim();
        if (!texto.isEmpty()) {
            String id = devChatReference.push().getKey();
            devChatReference.child(id).setValue(texto);
            editMensagemDev.setText("");
        }
    }
}
