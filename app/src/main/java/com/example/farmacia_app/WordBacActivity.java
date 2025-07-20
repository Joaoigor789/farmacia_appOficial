package com.example.farmacia_app;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WordBacActivity extends AppCompatActivity {

    private PdfRenderer pdfRenderer;  // Para ler o PDF
    private PdfRenderer.Page currentPage;
    private ImageView pdfImageView;   // Para exibir o conteúdo do PDF

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excel_bac); // Seu layout XML

        // Referência ao ImageView onde o PDF será exibido
        pdfImageView = findViewById(R.id.pdfImageView);

        // Configura o botão para abrir o arquivo PDF e iniciar o estudo
        findViewById(R.id.btnAbrirExcel).setOnClickListener(v -> {
            // Chama o método para ler e exibir o PDF
            lerPDF();
        });
    }

    // Método para ler o PDF do arquivo raw
    private void lerPDF() {
        new Thread(() -> {
            try {
                // Abre o arquivo PDF a partir de raw
                InputStream inputStream = getResources().openRawResource(R.raw.pdfbanco); // Seu PDF

                // Cria um arquivo temporário para o PDF
                File tempFile = File.createTempFile("tempPdf", ".pdf", getCacheDir());
                try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                }

                // Usa PdfRenderer para abrir o arquivo temporário PDF
                ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY);
                pdfRenderer = new PdfRenderer(fileDescriptor);

                // Renderizar e exibir a primeira página como imagem
                renderizarPagina(0);  // Exibe a primeira página

            } catch (IOException e) {
                Log.e("WordBacActivity", "Erro ao ler o arquivo PDF", e);
            }
        }).start();
    }

    // Método para renderizar uma página do PDF como uma imagem e exibir no ImageView
    private void renderizarPagina(int pageIndex) {
        try {
            // Abrir a página do PDF
            currentPage = pdfRenderer.openPage(pageIndex);

            // Configurações para a renderização
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = (int) (currentPage.getHeight() * (width / (float) currentPage.getWidth()));

            // Cria o Bitmap para renderizar a página
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            // Exibe o Bitmap no ImageView
            runOnUiThread(() -> pdfImageView.setImageBitmap(bitmap));

            // Fechar a página após o uso
            currentPage.close();

        } catch (Exception e) {
            // Captura qualquer exceção e exibe log
            Log.e("WordBacActivity", "Erro ao renderizar a página", e);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fechar o PdfRenderer
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
    }
}
