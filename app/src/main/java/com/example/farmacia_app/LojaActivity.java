package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.BarcodeEncoder;

import androidx.appcompat.app.AlertDialog;

public class LojaActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable playAudioRunnable;

    private int moedas = 100; // Valor inicial (depois integrar com banco de dados)
    private TextView moedasTextView;

    // Preços dos itens
    private static final int PRECO_DICA = 10;
    private static final int PRECO_PULAR_QUESTAO = 15;
    private static final int PRECO_TENTATIVA_EXTRA = 25;
    private static final int PRECO_CONTEUDO_PREMIUM = 40;
    private static final int PRECO_TEMA_NEON = 30;
    private static final int PRECO_AVATAR = 30;
    private static final int PRECO_SOM = 10;
    private static final int PRECO_MEDALHA = 20;
    private static final int PRECO_BOOST = 50;
    private static final int PRECO_MOEDAS_DOBRADAS = 100;
    private static final int PRECO_CHAT = 60;
    private static final int PRECO_SUPORTE_VIP = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loja_layout);

        moedasTextView = findViewById(R.id.moedasTextView);
        atualizarMoedas();

        Button botaoDoarPix = findViewById(R.id.botaoDoarPix);
        botaoDoarPix.setOnClickListener(v -> {
            iniciarAudioRepetido();
            mostrarDialogoDoacaoPix();
        });
    }

    private void iniciarAudioRepetido() {
        playAudioRunnable = new Runnable() {
            @Override
            public void run() {
                tocarAudioDoacao();
                handler.postDelayed(this, 20000); // 20 segundos
            }
        };
        playAudioRunnable.run(); // Começa já tocando
    }

    private void tocarAudioDoacao() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.ia_doacao);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
        });
    }

    private void mostrarDialogoDoacaoPix() {
        String chavePix = "83993437321";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pix_donacao, null);
        builder.setView(dialogView);

        ImageView qrCodeImageView = dialogView.findViewById(R.id.qrCodeImageView);
        TextView chavePixTextView = dialogView.findViewById(R.id.chavePixTextView);
        Button copiarChaveButton = dialogView.findViewById(R.id.copiarChaveButton);

        chavePixTextView.setText("Chave PIX: " + chavePix);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(chavePix, com.google.zxing.BarcodeFormat.QR_CODE, 400, 400);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog dialog = builder.create();

        copiarChaveButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Chave PIX", chavePix);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Chave PIX copiada!", Toast.LENGTH_SHORT).show();
        });

        dialog.setOnDismissListener(dialogInterface -> {
            handler.removeCallbacks(playAudioRunnable);
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });

        dialog.show();
    }

    // Método genérico de configuração de botões da loja
    private void configurarBotao(int id, int preco, String mensagemSucesso, Runnable acao) {
        Button botao = findViewById(id);
        botao.setOnClickListener(v -> {
            if (moedas >= preco) {
                moedas -= preco;
                atualizarMoedas();
                Toast.makeText(this, mensagemSucesso, Toast.LENGTH_SHORT).show();
                acao.run();
            } else {
                exibirErroMoedasInsuficientes();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void atualizarMoedas() {
        moedasTextView.setText("Moedas: " + moedas);
    }

    private void exibirErroMoedasInsuficientes() {
        Toast toast = Toast.makeText(this, "Moedas insuficientes!", Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(Color.RED);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.show();
    }

    // Métodos específicos dos itens da loja (vazios)
    private void mostrarDicaExtra() {}
    private void pularQuestao() {}
    private void liberarTentativaExtra() {}
    private void desbloquearConteudoPremium() {}
    private void aplicarTemaNeon() {}
    private void equiparAvatarCientista() {}
    private void aplicarSomClique() {}
    private void conquistarMedalha() {}
    private void ativarBoostPontos() {}
    private void dobrarMoedas() {}
    private void desbloquearChatEspecial() {}
    private void concederSuporteVIP() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(playAudioRunnable);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
