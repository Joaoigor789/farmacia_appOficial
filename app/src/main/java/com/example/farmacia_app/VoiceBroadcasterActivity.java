package com.example.farmacia_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoiceBroadcasterActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 101;

    private VoiceBroadcaster voiceBroadcaster;
    private TextView textStatus;
    private Button buttonStart;
    private Button buttonStop;
    private ProgressBar audioLevelBar;

    private MediaPlayer mediaPlayer;

    private RecyclerView recyclerView;
    private TransmitterAdapter adapter;
    private List<BroadcastStatus> transmissores = new ArrayList<>();
    private FirebaseFirestore db;

    private String nomeUsuario = "";  // armazenar nome do usuário
    private String mensagemUsuario = ""; // armazenar mensagem do usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_broadcaster);

        // Inicializa views
        textStatus = findViewById(R.id.textStatus);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        audioLevelBar = findViewById(R.id.audioLevelBar);
        recyclerView = findViewById(R.id.recyclerTransmitters);

        // Inicializa MediaPlayer com o som do raw
        mediaPlayer = MediaPlayer.create(this, R.raw.click);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransmitterAdapter(transmissores);
        recyclerView.setAdapter(adapter);

        voiceBroadcaster = new VoiceBroadcaster(this);
        voiceBroadcaster.setAudioLevelListener(level -> runOnUiThread(() -> {
            audioLevelBar.setProgress(level);
        }));

        // Configura listeners dos botões com som de clique
        buttonStart.setOnClickListener(v -> {
            playClickSound();
            if (nomeUsuario.isEmpty()) {
                pedirNomeEMensagemUsuario();
            } else if (mensagemUsuario.isEmpty()) {
                pedirMensagemUsuario();
            } else {
                checkPermissionAndStart();
            }
        });

        buttonStop.setOnClickListener(v -> {
            playClickSound();
            stopBroadcasting();
        });

        db = FirebaseFirestore.getInstance();
        carregarTransmissores();
    }

    private void playClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    // Pede nome e mensagem do usuário (dois inputs)
    private void pedirNomeEMensagemUsuario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informe seu nome e mensagem");

        // Inflate layout customizado com dois EditTexts
        LayoutInflater inflater = this.getLayoutInflater();
        final android.view.View dialogView = inflater.inflate(R.layout.dialog_nome_mensagem, null);
        builder.setView(dialogView);

        final EditText inputNome = dialogView.findViewById(R.id.editNome);
        final EditText inputMensagem = dialogView.findViewById(R.id.editMensagem);

        inputNome.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        inputMensagem.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String nomeDigitado = inputNome.getText().toString().trim();
            String mensagemDigitada = inputMensagem.getText().toString().trim();

            if (nomeDigitado.isEmpty()) {
                Toast.makeText(this, "Nome não pode ficar vazio!", Toast.LENGTH_SHORT).show();
            } else if (mensagemDigitada.isEmpty()) {
                Toast.makeText(this, "Mensagem não pode ficar vazia!", Toast.LENGTH_SHORT).show();
            } else {
                nomeUsuario = nomeDigitado;
                mensagemUsuario = mensagemDigitada;
                checkPermissionAndStart();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Caso o nome já esteja preenchido, pede só a mensagem
    private void pedirMensagemUsuario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informe a mensagem");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String mensagemDigitada = input.getText().toString().trim();
            if (mensagemDigitada.isEmpty()) {
                Toast.makeText(this, "Mensagem não pode ficar vazia!", Toast.LENGTH_SHORT).show();
            } else {
                mensagemUsuario = mensagemDigitada;
                checkPermissionAndStart();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void carregarTransmissores() {
        db.collection("transmissoes")
                .whereEqualTo("status", "started")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;
                    transmissores.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        BroadcastStatus status = doc.toObject(BroadcastStatus.class);
                        if (status != null) transmissores.add(status);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void checkPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            startBroadcasting();
        }
    }

    private void startBroadcasting() {
        voiceBroadcaster.startBroadcasting();

        BroadcastStatus bs = new BroadcastStatus("started", nomeUsuario, mensagemUsuario);
        db.collection("transmissoes").add(bs);

        textStatus.setText("Status: Transmitindo");
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);
        Toast.makeText(this, "Transmissão iniciada como " + nomeUsuario, Toast.LENGTH_SHORT).show();
    }

    private void stopBroadcasting() {
        voiceBroadcaster.stopBroadcasting();

        BroadcastStatus bs = new BroadcastStatus("stopped", nomeUsuario, mensagemUsuario);
        db.collection("transmissoes").add(bs);

        textStatus.setText("Status: Parado");
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
        Toast.makeText(this, "Transmissão parada", Toast.LENGTH_SHORT).show();

        audioLevelBar.setProgress(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBroadcasting();
            } else {
                Toast.makeText(this, "Permissão para gravar áudio negada.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        voiceBroadcaster.stopBroadcasting();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Modelo para dados do Firestore
    public static class BroadcastStatus {
        private String nome;
        private String status;
        private Date timestamp;
        private String mensagem;  // novo campo

        public BroadcastStatus() {
            // Para Firestore precisa do construtor vazio
        }

        public BroadcastStatus(String status, String nome, String mensagem) {
            this.status = status;
            this.nome = nome;
            this.mensagem = mensagem;
            this.timestamp = new Date();
        }

        // Getters
        public String getNome() { return nome; }
        public String getStatus() { return status; }
        public Date getTimestamp() { return timestamp; }
        public String getMensagem() { return mensagem; }

        // Setter para mensagem caso queira atualizar
        public void setMensagem(String mensagem) {
            this.mensagem = mensagem;
        }
    }


    // Adapter RecyclerView com mensagem personalizada
    public static class TransmitterAdapter extends RecyclerView.Adapter<TransmitterAdapter.ViewHolder> {

        private final List<BroadcastStatus> lista;

        public TransmitterAdapter(List<BroadcastStatus> lista) {
            this.lista = lista;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            android.view.View itemView = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_transmitter, parent, false); // usa layout customizado
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BroadcastStatus transmissor = lista.get(position);
            holder.nome.setText(transmissor.getNome());

            // Mensagem personalizada do Firestore
            holder.mensagem.setText(transmissor.getMensagem());

            holder.horario.setText(android.text.format.DateFormat.getTimeFormat(holder.itemView.getContext())
                    .format(transmissor.getTimestamp()));
        }

        @Override
        public int getItemCount() {
            return lista.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView nome, mensagem, horario;

            ViewHolder(android.view.View itemView) {
                super(itemView);
                nome = itemView.findViewById(R.id.textNome);
                mensagem = itemView.findViewById(R.id.textMensagem);
                horario = itemView.findViewById(R.id.textHorario);
            }
        }
    }
}
