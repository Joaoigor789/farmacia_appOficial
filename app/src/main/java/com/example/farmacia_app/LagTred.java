package com.example.farmacia_app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LagTred extends Service {
    private static final String TAG = "LagTred";

    /** Ação e extra usadas no broadcast, caso queira manter também essa via de notificação. */
    public static final String ACTION_UPDATE_STATUS = "com.example.farmacia_app.ACTION_UPDATE_STATUS";
    public static final String EXTRA_STATUS       = "status";

    /** Binder para o cliente se conectar ao serviço. */
    private final IBinder binder = new LocalBinder();

    /** Callback que a Activity implementa para receber atualizações. */
    private StatusCallback callback;

    /** Interface que a Activity deve implementar para receber status. */
    public interface StatusCallback {
        void onStatusChanged(String status);
    }

    /** Binder que expõe o serviço ao cliente. */
    public class LocalBinder extends Binder {
        public LagTred getService() {
            return LagTred.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind called");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        // Notifica imediatamente que o serviço iniciou
        notifyStatus("start");
        // TODO: coloque aqui a lógica de background do seu serviço
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        // Notifica que o serviço parou
        notifyStatus("stop");
    }

    /**
     * Registra a Activity (ou outro cliente) para receber callbacks
     */
    public void registerCallback(StatusCallback cb) {
        this.callback = cb;
    }

    /**
     * Cancela o registro do callback
     */
    public void unregisterCallback() {
        this.callback = null;
    }

    /**
     * Chama o callback registrado e, opcionalmente, envia um broadcast
     */
    private void notifyStatus(String status) {
        // Notifica via callback direto
        if (callback != null) {
            callback.onStatusChanged(status);
        }
        // Se ainda quiser manter o broadcast:
        Intent i = new Intent(ACTION_UPDATE_STATUS);
        i.putExtra(EXTRA_STATUS, status);
        sendBroadcast(i);
    }
}
