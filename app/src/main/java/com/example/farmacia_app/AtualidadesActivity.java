package com.example.farmacia_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AtualidadesActivity extends AppCompatActivity {

    private TextView estudosTextView;
    private LinearLayout textSection, webSection;
    private int currentTextIndex = 0;
    private WebView webView;

    private final String[] estudosTextos = {
            "Estudo 1: A farmácia clínica é essencial para o acompanhamento terapêutico dos pacientes, promovendo o uso racional de medicamentos.",
            "Estudo 2: A assistência farmacêutica envolve a gestão eficiente de medicamentos, garantindo acesso, qualidade e uso adequado pela população.",
            "Estudo 3: O farmacêutico tem papel ativo na educação em saúde, colaborando na prevenção de doenças e na promoção do bem-estar.",
            "Estudo 4: O SUS oferece assistência farmacêutica gratuita, sendo o farmacêutico responsável pela dispensação ética e orientada.",
            "Estudo 5: A digitalização das farmácias trouxe avanços no controle de estoque, rastreabilidade e atendimento remoto ao paciente.",
            "Estudo 6: A prescrição farmacêutica foi regulamentada e fortalece a autonomia do profissional no cuidado em saúde.",
            "Estudo 7: O farmacêutico pode atuar em farmácias hospitalares, clínicas, drogarias, laboratórios e serviços públicos.",
            "Estudo 8: A farmacovigilância monitora efeitos adversos e reações medicamentosas, protegendo a saúde da população.",
            "Estudo 9: A humanização no atendimento farmacêutico contribui para maior adesão ao tratamento e melhora na qualidade de vida do paciente.",
            "Estudo 10: O farmacêutico deve manter-se atualizado com legislações, novas terapias e tecnologias aplicadas à saúde."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualidades);

        SearchView searchView = findViewById(R.id.searchView);
        estudosTextView = findViewById(R.id.estudosTextView);
        textSection = findViewById(R.id.textSection);
        webSection = findViewById(R.id.webSection);
        webView = findViewById(R.id.webViewEstudo);
        Button nextButton = findViewById(R.id.nextButton);
        Button toggleViewButton = findViewById(R.id.toggleViewButton);

        // Mostrar primeiro texto
        displayText(currentTextIndex);

        // Botão "Próximo"
        nextButton.setOnClickListener(v -> {
            currentTextIndex = (currentTextIndex + 1) % estudosTextos.length;
            displayText(currentTextIndex);
        });

        // WebView inicial
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.youtube.com/results?search_query=farmacia+assistencia+farmaceutica");

        // Botão "Alternar Visualização"
        toggleViewButton.setOnClickListener(v -> {
            if (textSection.getVisibility() == View.VISIBLE) {
                textSection.setVisibility(View.GONE);
                webSection.setVisibility(View.VISIBLE);
            } else {
                textSection.setVisibility(View.VISIBLE);
                webSection.setVisibility(View.GONE);
            }
        });

        // Comportamento do SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Alternar para WebView ao buscar
                textSection.setVisibility(View.GONE);
                webSection.setVisibility(View.VISIBLE);

                // Buscar no YouTube
                String searchUrl = "https://www.google.com/search?q=" + query.replace(" ", "+");

                webView.loadUrl(searchUrl);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // Nenhuma ação enquanto digita
            }
        });
    }

    private void displayText(int index) {
        String texto = estudosTextos[index];
        SpannableString spannableString = new SpannableString(texto);

        // Cor verde escuro no "Estudo X"
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#66BB6A")), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Cor rosa + negrito no restante
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF69B4")), 9, texto.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 9, texto.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        estudosTextView.setText(spannableString);
    }

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
        } else if (id == R.id.menu_licenca) {
            showLicenseDialog();
            return true;
        } else if (id == R.id.menu_autor) {
            showAutorDialog();
            return true;
        } else if (id == R.id.menu_atualizacao) {
            ChangelogUtil.exibirChangelog(this);
            return true;
        } else if (id == R.id.menu_perfil) {
            try {
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            } catch (Exception e) {
                showErrorDialog(e);
                return false;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showErrorDialog(Exception e) {
        new AlertDialog.Builder(this)
                .setTitle("Erro")
                .setMessage("Erro ao abrir o perfil: " + e.getMessage())
                .setPositiveButton("OK", null)
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sobre")
                .setMessage("Aplicativo para Farmácia\nVersão 1.0.7")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showLicenseDialog() {
        String licenseInfo = "Licença: CC BY-NC-ND 4.0\n" +
                "Uso permitido somente para fins educacionais.";
        new AlertDialog.Builder(this)
                .setTitle("Licença")
                .setMessage(licenseInfo)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showAutorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Autor")
                .setMessage("João Igor Rodrigues Pereira da Silva\nFarmacia AppTech Tecnology")
                .setPositiveButton("OK", null)
                .show();
    }
}
