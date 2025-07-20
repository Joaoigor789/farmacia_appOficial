package com.example.farmacia_app;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Atualizador {

    private static final String UPDATE_URL = "https://www.dropbox.com/scl/fi/xzc6ww6owra6lqqdo5nqe/update.json?rlkey=1ca9yoz6j5x14f9v3iijwn68b&st=maehjip9&dl=1";
    private static boolean baixando = false;
    private static long downloadId;
    private static MediaPlayer mediaPlayerAtual = null;

    private static String getVersaoAtual(Activity activity) {
        try {
            return activity.getPackageManager()
                    .getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "0.0.0";
        }
    }

    private static int compareVersion(String v1, String v2) {
        String[] arr1 = v1.split("\\.");
        String[] arr2 = v2.split("\\.");
        int length = Math.max(arr1.length, arr2.length);
        for (int i = 0; i < length; i++) {
            int num1 = i < arr1.length ? Integer.parseInt(arr1[i]) : 0;
            int num2 = i < arr2.length ? Integer.parseInt(arr2[i]) : 0;
            if (num1 != num2) return num1 - num2;
        }
        return 0;
    }

    public static void verificarAtualizacao(Activity activity, Runnable onUpdateAvailable) {
        new Thread(() -> {
            try {
                URL url = new URL(UPDATE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder json = new StringBuilder();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    json.append(linha);
                }
                reader.close();

                JSONObject obj = new JSONObject(json.toString());
                String novaVersao = obj.getString("versao");
                String linkAPK = obj.getString("link_apk");
                String descricao = obj.optString("descricao", "");
                String updateFlag = obj.optString("update", "não");

                String versaoAtual = getVersaoAtual(activity);
                boolean versaoMaisNova = compareVersion(novaVersao, versaoAtual) > 0;
                boolean mesmaVersao = compareVersion(novaVersao, versaoAtual) == 0;

                activity.runOnUiThread(() -> {
                    if (updateFlag.equalsIgnoreCase("sim")) {
                        if (versaoMaisNova && !baixando) {
                            baixando = true;
                            tocarSom(activity, R.raw.update, () -> {
                                Toast.makeText(activity, "Nova versão disponível: " + novaVersao + "\n" + descricao, Toast.LENGTH_LONG).show();
                                iniciarDownload(activity, linkAPK);
                                if (onUpdateAvailable != null) onUpdateAvailable.run();
                            });
                        } else if (mesmaVersao) {
                            Toast.makeText(activity, "Você já está na versão mais recente: " + versaoAtual, Toast.LENGTH_SHORT).show();
                        }
                        // Caso contrário (ex: versaoMaisNova == false mas não igual), não faz nada
                    } else {
                        // update == "não", portanto não libera atualização
                        tocarSom(activity, R.raw.atualizado1, () ->
                                Toast.makeText(activity, "Atualização desativada.\nVersão atual: " + versaoAtual, Toast.LENGTH_SHORT).show()
                        );
                    }
                });

            } catch (Exception e) {
                //e.printStackTrace();
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "Erro ao verificar atualização", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }



    private static void iniciarDownload(Activity activity, String urlAPK) {
        String nomeArquivo = "farmaciaapp_update.apk";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlAPK));
        request.setTitle("Atualização FarmáciaApp");
        request.setDescription("Baixando nova versão...");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nomeArquivo);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = manager.enqueue(request);

        Toast.makeText(activity, "Download iniciado! Aguarde...", Toast.LENGTH_LONG).show();
        Toast.makeText(activity, "Verifique a pasta Downloads do seu dipositivo...", Toast.LENGTH_LONG).show();

        File destino = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nomeArquivo);
        monitorarDownload(activity, manager, downloadId, destino);
    }

    private static void monitorarDownload(Activity activity, DownloadManager manager, long downloadId, File destino) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                android.database.Cursor cursor = manager.query(query);

                if (cursor != null && cursor.moveToFirst()) {
                    int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (statusIndex == -1) {
                        cursor.close();
                        Toast.makeText(activity, "Erro ao obter status do download.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int status = cursor.getInt(statusIndex);
                    cursor.close();

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        tocarSom(activity, R.raw.baixou, null);
                        Toast.makeText(activity, "Download concluído\nProcure na pasta Downloads do seu dispositivo", Toast.LENGTH_LONG).show();

                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri downloadsUri = Uri.parse("content://com.android.externalstorage.documents/document/primary:Download");
                            intent.setDataAndType(downloadsUri, "*/*");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            try {
                                Intent fallbackIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                fallbackIntent.setDataAndType(Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toURI().toString()), "*/*");
                                fallbackIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(fallbackIntent);
                            } catch (Exception ignored) {}
                        }

                        instalarAPK(activity, destino);
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Toast.makeText(activity, "Falha no download.", Toast.LENGTH_SHORT).show();
                    } else {
                        handler.postDelayed(this, 1000);
                    }
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.post(runnable);
    }

    private static void instalarAPK(Activity activity, File file) {
        if (!file.exists()) {
            Toast.makeText(activity, "Arquivo de instalação não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }

        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "Erro ao iniciar instalação: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private static void tocarSom(Context context, int resId, Runnable onComplete) {
        if (mediaPlayerAtual != null) {
            try {
                mediaPlayerAtual.stop();
                mediaPlayerAtual.release();
            } catch (Exception ignored) {}
            mediaPlayerAtual = null;
        }

        mediaPlayerAtual = MediaPlayer.create(context, resId);
        if (mediaPlayerAtual != null) {
            mediaPlayerAtual.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayerAtual = null;
                if (onComplete != null) onComplete.run();
            });
            mediaPlayerAtual.start();
        } else {
            if (onComplete != null) onComplete.run();
        }
    }
}
