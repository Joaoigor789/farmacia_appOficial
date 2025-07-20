package com.example.farmacia_app;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class WhatsappNotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Código desativado
        // if (!sbn.getPackageName().equals("com.whatsapp")) return;

        // Notification notification = sbn.getNotification();
        // if (notification == null || notification.extras == null) return;

        // String title = notification.extras.getString(Notification.EXTRA_TITLE);
        // CharSequence text = notification.extras.getCharSequence(Notification.EXTRA_TEXT);

        // if (title != null && text != null) {
        //     // Aqui você pode verificar o nome do grupo
        //     if (title.contains("Apptech")) {
        //         Log.d("WHATSAPP_NOTIF", "Grupo: " + title + " | Mensagem: " + text);
        //         // Você pode salvar isso em banco, exibir ou mandar pro Firebase
        //     }
        // }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Código desativado
    }
}
