package com.example.farmacia_app;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

public class Android14Features {

    private final Context context;

    public Android14Features(Context context) {
        this.context = context;
    }

    /**
     * Verifica se o dispositivo está executando Android 14 ou superior.
     */
    public boolean isAndroid14OrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE; // Android 14
    }

    /**
     * Solicita permissão para postar notificações no Android 13+ (incluindo 14).
     */
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public boolean hasNotificationPermission() {
        return context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Verifica se o usuário restringiu a coleta de dados por sensores no Android 14.
     */
    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public boolean isSensorRestricted() {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOps != null) {
            int mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_FINE_LOCATION,
                    android.os.Process.myUid(), context.getPackageName());
            return mode == AppOpsManager.MODE_IGNORED;
        }
        return false;
    }

    /**
     * Abre a tela de configurações para o usuário conceder permissões sensíveis.
     */
    public void openAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        activity.startActivity(intent);
    }
}
