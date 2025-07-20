package com.example.farmacia_app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.parse.ParseObject;

public class VoiceBroadcaster {

    private static final String TAG = "VoiceBroadcaster";
    private static final int SAMPLE_RATE = 16000;

    private boolean isBroadcasting = false;
    private Context context;

    // Callback para enviar nível de áudio para a Activity
    public interface AudioLevelListener {
        void onAudioLevel(int level);
    }

    private AudioLevelListener audioLevelListener;

    public VoiceBroadcaster(Context context) {
        this.context = context;
    }

    public void setAudioLevelListener(AudioLevelListener listener) {
        this.audioLevelListener = listener;
    }

    public void startBroadcasting() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permissão RECORD_AUDIO não concedida!");
            return;
        }

        ParseObject broadcast = new ParseObject("Broadcast");
        broadcast.put("status", "started");
        broadcast.saveInBackground(e -> {
            if (e == null) Log.d(TAG, "Broadcast salvo como STARTED no Parse");
            else Log.e(TAG, "Erro ao salvar Broadcast no Parse", e);
        });

        new Thread(this::recordAudio).start();
    }

    private void recordAudio() {
        isBroadcasting = true;

        int bufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );

        AudioRecord recorder;
        try {
            recorder = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize
            );
        } catch (SecurityException e) {
            Log.e(TAG, "Permissão para gravar áudio negada!", e);
            return;
        }

        if (recorder == null || recorder.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "Falha ao inicializar AudioRecord");
            return;
        }

        recorder.startRecording();

        byte[] buffer = new byte[bufferSize];

        while (isBroadcasting) {
            int read = recorder.read(buffer, 0, buffer.length);
            if (read > 0) {
                int level = calculateAudioLevel(buffer, read);
                if (audioLevelListener != null) {
                    audioLevelListener.onAudioLevel(level);
                }
                // Aqui você pode processar ou enviar o áudio capturado
            }
        }

        recorder.stop();
        recorder.release();
    }

    private int calculateAudioLevel(byte[] buffer, int read) {
        long sum = 0;
        // Percorre os samples em 16 bits (2 bytes)
        for (int i = 0; i < read; i += 2) {
            // Converte bytes para short (little endian)
            short sample = (short) ((buffer[i] & 0xFF) | (buffer[i + 1] << 8));
            sum += sample * sample;
        }
        double rms = Math.sqrt(sum / (read / 2));
        int level = (int) ((rms / 32768) * 100);
        if (level > 100) level = 100;
        return level;
    }

    public void stopBroadcasting() {
        isBroadcasting = false;

        ParseObject broadcast = new ParseObject("Broadcast");
        broadcast.put("status", "stopped");
        broadcast.saveInBackground(e -> {
            if (e == null) Log.d(TAG, "Broadcast salvo como STOPPED no Parse");
            else Log.e(TAG, "Erro ao salvar Broadcast no Parse", e);
        });
    }
}
