package com.example.farmacia_app;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DrawableHelper {

    // Método para descompactar o arquivo ZIP
    public static void unzipAssets(Context context, String zipFileName) {
        try {
            // Acessa o arquivo zip da pasta assets
            InputStream is = context.getAssets().open(zipFileName);
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry zipEntry;

            // Cria o diretório temporário para armazenar as imagens
            File outputDir = context.getCacheDir();
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Descompacta as imagens dentro do diretório cache
            while ((zipEntry = zis.getNextEntry()) != null) {
                File outputFile = new File(outputDir, zipEntry.getName());
                if (!zipEntry.isDirectory()) {
                    // Extrai a imagem
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    fos.close();
                }
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para carregar uma imagem descompactada
    public static Drawable loadDrawableFromCache(Context context, String fileName) {
        File imgFile = new File(context.getCacheDir(), fileName);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return new BitmapDrawable(context.getResources(), bitmap);
        } else {
            return null; // Imagem não encontrada
        }
    }
}
