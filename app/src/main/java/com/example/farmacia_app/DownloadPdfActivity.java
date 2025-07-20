package com.example.farmacia_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.*;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DownloadPdfActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    // URLs dos 3 livros
    private final String pdfUrl1 = "https://www.dropbox.com/scl/fi/odvk6px3e6xumgdae7upx/livro.pdf?rlkey=g2vovfxjd8t7tzx4jrbwjy7tc&e=1&st=o94nkzof&dl=1";
    private final String pdfUrl2 = "https://www.dropbox.com/scl/fi/6tqsl0g2x3tckvfrzymka/5-Quimica-II.pdf?rlkey=rrthz6mtpvak8m64ncoai5acr&st=f9e0ddxr&dl=1";
    private final String pdfUrl3 = "https://www.dropbox.com/scl/fi/aiuizlr4cl6zbklpwehue/An-lise-Qu-mica-Quantitativa-9-ed..pdf?rlkey=itu8ebzdhl99bh932hju9k08z&st=vg6kgwhd&dl=1";

    private final String pdfUrl4 = "https://www.dropbox.com/scl/fi/glty1w47rw6x8nv3w5qbd/Quimica-Analitica-Baccan.pdf?rlkey=q02t6198e7idn2tsmzj9p0qil&st=wc2946jj&dl=1";
    private final String pdfUrl5 = "https://www.dropbox.com/scl/fi/z92ihwdn3zpnct7me4bvf/FUMARC-QU-MICA-2018-SEE-MG.pdf?rlkey=t9i3b6rszhdxzreydv0moaq48&st=egkuzm24&dl=1";

    private final String pdfUrl6 = "https://www.dropbox.com/scl/fi/yf12honyllflh5olj5sgn/Vol.-2-F-sico-Qu-mica.pdf?rlkey=xczjszhxhnrrg4d8estfktjha&st=np5042uc&dl=1";

    private final String pdfUrl7 = "https://www.dropbox.com/scl/fi/w3mdf9pc1jwh40lj6stmb/Quimica-Medicinal-1.pdf?rlkey=svvn1087hzoancmhfzn1bq2iu&st=q39l9j96&dl=1";

    private final String pdfUrl8 = "https://www.dropbox.com/scl/fi/o1osjfy98e0cj8g7mub23/Qu-mica_Medicinal-2.pdf?rlkey=fblklqetcpu2f1k841irnrfcg&st=74k0ib2r&dl=1";

    private final String pdfUrl9 = "https://www.dropbox.com/scl/fi/bi2b1ct9cxafnig0tmb3j/Enem-F-sica-e-Qu-mica-vol-1.pdf?rlkey=ldo3xqdahqkhr55fknqz3h4dx&st=t60p2aje&dl=1";

    private final String pdfUrl10 = "https://www.dropbox.com/scl/fi/0ly40x8plczoeakedjdjp/ENEM-2017-Qu-mica.pdf?rlkey=qlby9wc5bduhojsxhgiegth6v&st=wootlmil&dl=1";

    private final String pdfUrl11 = "https://www.dropbox.com/scl/fi/4u464pnd6bv690rgbo09g/QU-MICA-AMBIENTAL.pdf?rlkey=5sjhloom401jmzoabcb5yqzqd&st=5gcutk4x&dl=1";

    private final String pdfUrl12 = "https://www.dropbox.com/scl/fi/6yr6p9rzgqltqegszx0yr/VOGEL-Qu-mica-Org-nica-Pr-tica-5th-Edition.pdf?rlkey=gyne1c969akxmwq004qdeqxrn&st=3ele0rcv&dl=1";

    private final String pdfUrl13 = "https://www.dropbox.com/scl/fi/2x8nhxqb3ub2nt4lbehrf/ATKINS-F-sico-Qu-mica-V1-8ed-PORTUGUES-COMPLETO.pdf?rlkey=ww2ryt9a79rpdat701nrz9jom&st=7mvakb18&dl=1";

    private final String pdfUrl14 = "https://www.dropbox.com/scl/fi/18qupqt4fp60jazl8e8su/ATKINS-F-sico-Qu-mica-V2-8ed..pdf?rlkey=empkk2ard6w6dwiw5dlw46oe6&st=e13lc7sr&dl=1";

    private final String pdfUrl15 = "https://www.dropbox.com/scl/fi/l1csg3n64pkhhzqf82wbd/A_Quimica_dos_Alimentos.pdf?rlkey=0vf2sj3tta86e2tyuncwzzsrq&st=muspmwb8&dl=1";

    private final String pdfUrl16 = "https://www.dropbox.com/scl/fi/gjlst1s9rr4lff6qjpie5/HARRIS_QUIMICA_ANALITICA_C_Harris.pdf?rlkey=qs6334n8a6u0hrr7yum1ppms1&st=te4jvmwp&dl=1";

    // Chaves SharedPreferences para favoritos
    private final String PREF_KEY_FAV_1 = "livroFavorito1";
    private final String PREF_KEY_FAV_2 = "livroFavorito2";
    private final String PREF_KEY_FAV_3 = "livroFavorito3";
    private final String PREF_KEY_FAV_4 = "livroFavorito4";
    private final String PREF_KEY_FAV_5 = "livroFavorito5";

    private final String PREF_KEY_FAV_6 = "livroFavorito6";

    private final String PREF_KEY_FAV_7 = "livroFavorito7";
    private final String PREF_KEY_FAV_8 = "livroFavorito8";
    private final String PREF_KEY_FAV_9 = "livroFavorito9";

    private final String PREF_KEY_FAV_10 = "livroFavorito10";

    private final String PREF_KEY_FAV_11 = "livroFavorito11";

    private final String PREF_KEY_FAV_12 = "livroFavorito12";
    private final String PREF_KEY_FAV_13 = "livroFavorito13";
    private final String PREF_KEY_FAV_14 = "livroFavorito14";
    private final String PREF_KEY_FAV_15 = "livroFavorito15";
    private final String PREF_KEY_FAV_16 = "livroFavorito16";

    private SharedPreferences prefs;
    private TextView favoriteStatus;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_pdf);

        prefs = getSharedPreferences("favoritos", MODE_PRIVATE);
        favoriteStatus = findViewById(R.id.favoriteStatus);

        // Configura botão e estrela livro 1
        setupBook(
                R.id.btnDownload1,
                R.id.favoriteStar1,
                pdfUrl1,
                PREF_KEY_FAV_1,
                "Química Ambiental - Livro 1"
        );

        // Configura botão e estrela livro 2
        setupBook(
                R.id.btnDownload2,
                R.id.favoriteStar2,
                pdfUrl2,
                PREF_KEY_FAV_2,
                "Título Livro 2"
        );

        // Configura botão e estrela livro 3
        setupBook(
                R.id.btnDownload3,
                R.id.favoriteStar3,
                pdfUrl3,
                PREF_KEY_FAV_3,
                "Título Livro 3"
        );

        // Configura botão e estrela livro 4
        setupBook(
                R.id.btnDownload4,
                R.id.favoriteStar4,
                pdfUrl4,
                PREF_KEY_FAV_4,
                "Quimica analitica"
        );

        // Configura botão e estrela livro 5
        setupBook(
                R.id.btnDownload5,
                R.id.favoriteStar5,
                pdfUrl5,
                PREF_KEY_FAV_5,
                "Quimica nivel 6"
        );

        // Configura botão e estrela livro 6
        setupBook(
                R.id.btnDownload6,
                R.id.favoriteStar6,
                pdfUrl6,
                PREF_KEY_FAV_6,
                "Fisio-Quimica vol 2"
        );

        // Configura botão e estrela livro 7
        setupBook(
                R.id.btnDownload7,
                R.id.favoriteStar7,
                pdfUrl7,
                PREF_KEY_FAV_7,
                "Quimica Medicinal 1"
        );

        // Configura botão e estrela livro 8
        setupBook(
                R.id.btnDownload8,
                R.id.favoriteStar8,
                pdfUrl8,
                PREF_KEY_FAV_8,
                "Quimica Medicinal 2"
        );

        // Configura botão e estrela livro 9
        setupBook(
                R.id.btnDownload9,
                R.id.favoriteStar9,
                pdfUrl9,
                PREF_KEY_FAV_9,
                "Quimica fisioquimica vol 1"
        );

        // Configura botão e estrela livro 10
        setupBook(
                R.id.btnDownload10,
                R.id.favoriteStar10,
                pdfUrl10,
                PREF_KEY_FAV_10,
                "Quimica ENEM 2017"
        );

        // Configura botão e estrela livro 11
        setupBook(
                R.id.btnDownload11,
                R.id.favoriteStar11,
                pdfUrl11,
                PREF_KEY_FAV_11,
                "Quimica Ambiental Vol2"
        );

        // Configura botão e estrela livro 12
        setupBook(
                R.id.btnDownload12,
                R.id.favoriteStar12,
                pdfUrl12,
                PREF_KEY_FAV_12,
                "Quimica Organica VOL1"
        );

        // Configura botão e estrela livro 13
        setupBook(
                R.id.btnDownload13,
                R.id.favoriteStar13,
                pdfUrl13,
                PREF_KEY_FAV_13,
                "Quimica Fisioquimica VOL3"
        );

        // Configura botão e estrela livro 14
        setupBook(
                R.id.btnDownload14,
                R.id.favoriteStar14,
                pdfUrl14,
                PREF_KEY_FAV_14,
                "Quimica Fisioquimica VOL4"
        );

        // Configura botão e estrela livro 15
        setupBook(
                R.id.btnDownload15,
                R.id.favoriteStar15,
                pdfUrl15,
                PREF_KEY_FAV_15,
                "Quimica dos Alimentos"
        );

        // Configura botão e estrela livro 16
        setupBook(
                R.id.btnDownload16,
                R.id.favoriteStar16,
                pdfUrl16,
                PREF_KEY_FAV_16,
                "Quimica Analitica"
        );
    }

    @SuppressLint("SetTextI18n")
    private void setupBook(int btnId, int starId, String url, String prefKey, String bookTitle) {
        Button btnDownload = findViewById(btnId);
        ImageView favoriteStar = findViewById(starId);

        // MediaPlayers
        final MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);
        final MediaPlayer starSound = MediaPlayer.create(this, R.raw.star);

        // Estado inicial da estrela
        boolean isFavorito = prefs.getBoolean(prefKey, false);
        favoriteStar.setImageResource(isFavorito ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        favoriteStar.setContentDescription(isFavorito ? "Livro marcado como favorito" : "Livro não está favoritado");

        // Clique no botão para download
        btnDownload.setOnClickListener(v -> {
            // Tocar som de clique
            if (clickSound.isPlaying()) {
                clickSound.seekTo(0);
            } else {
                clickSound.start();
            }
            checkAndRequestPermission(url, bookTitle);
        });

        // Clique na estrela para favoritar/desfavoritar
        favoriteStar.setOnClickListener(v -> {
            // Tocar som da estrela apenas do segundo 1 ao 2
            starSound.seekTo(1000); // 1 segundo
            starSound.start();

            new android.os.Handler().postDelayed(() -> {
                if (starSound.isPlaying()) {
                    starSound.pause();
                    starSound.seekTo(0);
                }
            }, 1000); // parar após 1 segundo

            // Lógica de favoritar/desfavoritar
            boolean novoEstado = !prefs.getBoolean(prefKey, false);
            prefs.edit().putBoolean(prefKey, novoEstado).apply();
            favoriteStar.setImageResource(novoEstado ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
            favoriteStar.setContentDescription(novoEstado ? "Livro marcado como favorito" : "Livro não está favoritado");

            favoriteStatus.setText(novoEstado ? "Você favoritou \"" + bookTitle + "\"" : "Você desfavoritou \"" + bookTitle + "\"");
            favoriteStatus.setVisibility(View.VISIBLE);

            new android.os.Handler().postDelayed(() -> favoriteStatus.setVisibility(View.GONE), 2000);
        });
    }


    private void checkAndRequestPermission(String url, String bookTitle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ não exige permissão para DownloadManager
            downloadPdf(url, bookTitle);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                downloadPdf(url, bookTitle);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionExplanationDialog();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        } else {
            downloadPdf(url, bookTitle); // Android < 6.0
        }
    }

    private void downloadPdf(String url, String bookTitle) {
        if (!isInternetAvailable()) {
            Toast.makeText(this, "Sem conexão com a internet.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(bookTitle);
            request.setDescription("Baixando PDF...");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setAllowedOverMetered(true);
            request.setAllowedOverRoaming(true);

            // Salva com nome próprio baseado no título do livro (sem espaços)
            String fileName = bookTitle.replaceAll("\\s+", "_").toLowerCase() + ".pdf";

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            downloadManager.enqueue(request);
            Toast.makeText(this, "Download iniciado de: " + bookTitle, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao iniciar download: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    private void showPermissionExplanationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permissão Necessária")
                .setMessage("Este app precisa da permissão de armazenamento para baixar o PDF. Por favor, permita o acesso.")
                .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(
                        DownloadPdfActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE))
                .setNegativeButton("Cancelar", (dialog, which) ->
                        Toast.makeText(DownloadPdfActivity.this, "Permissão negada. O download não será possível.", Toast.LENGTH_SHORT).show())
                .create()
                .show();
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permissão Negada Permanentemente")
                .setMessage("Você negou a permissão permanentemente. Para permitir o download, ative manualmente nas configurações do app.")
                .setPositiveButton("Abrir Configurações", (dialog, which) -> openAppSettings())
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Aqui, como não sabemos qual URL pediu, você pode aprimorar passando o contexto — para simplificar, pode pedir para tentar novamente manualmente.
                Toast.makeText(this, "Permissão concedida. Tente o download novamente.", Toast.LENGTH_SHORT).show();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showSettingsDialog();
                } else {
                    Toast.makeText(this, "Permissão negada. O download não será possível.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Os métodos do menu, diálogo, calendário, etc., podem ficar exatamente como no seu código original...

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_inicio) {
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        } else if (id == R.id.menu_sobre) {
            showAboutDialog();
            return true;
        } else if (id == R.id.menu_autor) {
            showAutorDialog();
            return true;
        } else if (id == R.id.menu_atualizacao) {
            ChangelogUtil.exibirChangelog(this);
            return true;
        } else if (id == R.id.menu_perfil) {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
            return false;
        } else if (id == R.id.menu_calendario) {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
            mostrarCalendario();
            return true;
        } else if (id == R.id.menu_licenca) {
            showLicenseDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarCalendario() {
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setVisibility(View.VISIBLE);
    }

    private void showAutorDialog() {
        String info = "Autor: João Igor Rodrigues Pereira da Silva\n" +
                "Empresa: Farmacia AppTech Tecnology\n" +
                "Projeto: Aplicativo de Farmácia";

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Informações do Autor")
                .setMessage(info)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showAboutDialog() {
        String version = "Versão 1.0.7";
        String info = "Aplicativo para Farmacia";
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Sobre")
                .setMessage(info + "\n" + version)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showLicenseDialog() {
        String licenseInfo = "Copyright 2025 João Igor Rodrigues Pereira da Silva, Farmacia AppTech Tecnology\n\n" +
                "Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License (CC BY-NC-ND 4.0);\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at:\n" +
                "https://creativecommons.org/licenses/by-nc-nd/4.0/\n\n" +
                "You are allowed to copy and redistribute the material, but you may **not modify** it or use it for commercial purposes without explicit permission from the author.\n\n" +
                "The use of this application is permitted **only for educational purposes**.";

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Licença")
                .setMessage(licenseInfo)
                .setPositiveButton("OK", null)
                .show();
    }
}
