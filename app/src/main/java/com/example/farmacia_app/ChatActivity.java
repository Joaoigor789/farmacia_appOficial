package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ChatActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout messageContainer;
    private ScrollView scrollView;
    private EditText editTextMessage;
    private Button buttonSend;

    private DatabaseReference chatRef;
    private String username;

    private Map<String, Integer> userColors = new HashMap<>();
    private final int[] coresUsuarios = new int[]{
            Color.parseColor("#e6194b"), // vermelho
            Color.parseColor("#3cb44b"), // verde
            Color.parseColor("#ffe119"), // amarelo
            Color.parseColor("#4363d8"), // azul
            Color.parseColor("#f58231"), // laranja
            Color.parseColor("#911eb4"), // roxo
            Color.parseColor("#46f0f0"), // ciano
            Color.parseColor("#f032e6"), // rosa
            Color.parseColor("#bcf60c"), // lima
            Color.parseColor("#fabebe"), // rosa claro
    };



    private static final String PREFS_NAME = "ChatUserColors";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        username = "Usuário_" + (int) (Math.random() * 1000);

        messageContainer = findViewById(R.id.messageContainer);
        scrollView = findViewById(R.id.scrollViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        carregarCoresSalvas();

        chatRef = FirebaseDatabase.getInstance().getReference("chat");

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                String textoCompleto = snapshot.getValue(String.class);
                Log.d("ChatActivity", "Mensagem recebida: " + textoCompleto);
                adicionarMensagem(textoCompleto);
            }

            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatActivity", "Erro no Firebase: " + error.getMessage());
                adicionarMensagem("[Erro no Firebase]");
            }
        });


        buttonSend.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start())
                    .start();

            String msg = editTextMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                String mensagemCompleta = username + ": " + msg;
                chatRef.push().setValue(mensagemCompleta);
                editTextMessage.setText("");
            }
        });

        buttonSend.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false; // permite o clique continuar funcionando normalmente
        });


        buttonSend.setOnClickListener(v -> {
            String msg = editTextMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
                String mensagemCompleta = username + ": " + msg + "||" + dataHora;
                chatRef.push().setValue(mensagemCompleta);
                editTextMessage.setText("");
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Aqui você deve recarregar os dados, por exemplo:
            recarregarMensagens();

            // Após recarregar, para o spinner de carregamento
            swipeRefreshLayout.setRefreshing(false);
        });






    }



    private void recarregarMensagens() {
        messageContainer.removeAllViews();

        // Recarregue as mensagens do Firebase (exemplo simples)
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot msgSnapshot : snapshot.getChildren()) {
                    String msg = msgSnapshot.getValue(String.class);
                    adicionarMensagem(msg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                adicionarMensagem("[Erro ao recarregar mensagens]");
            }
        });
    }


    private void adicionarMensagem(String textoCompleto) {
        runOnUiThread(() -> {
            String[] partes = textoCompleto.split("\\|\\|");
            String textoComUsuario = partes.length > 0 ? partes[0] : textoCompleto;
            String dataHora = partes.length > 1 ? partes[1] : "";

            String remetente = "Desconhecido";
            int idx = textoComUsuario.indexOf(":");
            if (idx != -1) {
                remetente = textoComUsuario.substring(0, idx).trim();
            }

            int corUsuario = obterCorUsuario(remetente);

            LinearLayout container = new LinearLayout(ChatActivity.this);
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.setPadding(8, 4, 8, 4);

            TextView textMensagem = new TextView(ChatActivity.this);
            textMensagem.setText(textoComUsuario);
            textMensagem.setTextSize(16);
            textMensagem.setTextColor(corUsuario);
            textMensagem.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView textDataHora = new TextView(ChatActivity.this);
            textDataHora.setText(dataHora);
            textDataHora.setTextSize(14);
            textDataHora.setTextColor(Color.RED);
            textDataHora.setTypeface(null, Typeface.BOLD);
            textDataHora.setPadding(16, 0, 0, 0);

            container.addView(textMensagem);
            container.addView(textDataHora);

            messageContainer.addView(container);

            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        });
    }

    private int obterCorUsuario(String usuario) {
        if (userColors.containsKey(usuario)) {
            return userColors.get(usuario);
        }

        List<Integer> coresDisponiveis = new ArrayList<>();
        for (int c : coresUsuarios) {
            if (!userColors.containsValue(c)) {
                coresDisponiveis.add(c);
            }
        }

        int corEscolhida;
        if (coresDisponiveis.isEmpty()) {
            corEscolhida = coresUsuarios[new Random().nextInt(coresUsuarios.length)];
        } else {
            corEscolhida = coresDisponiveis.get(new Random().nextInt(coresDisponiveis.size()));
        }

        userColors.put(usuario, corEscolhida);
        salvarCorUsuario(usuario, corEscolhida);

        return corEscolhida;
    }

    private void salvarCorUsuario(String usuario, int cor) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(usuario, cor);
        editor.apply();
    }

    private void carregarCoresSalvas() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Map<String, ?> todasCores = prefs.getAll();

        for (Map.Entry<String, ?> entry : todasCores.entrySet()) {
            if (entry.getValue() instanceof Integer) {
                userColors.put(entry.getKey(), (Integer) entry.getValue());
            }
        }
    }
}
