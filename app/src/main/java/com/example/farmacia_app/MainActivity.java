package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.CookieManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import com.example.farmacia_app.jogos.MisturadorActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import android.Manifest;
import com.google.android.gms.safetynet.SafetyNet;
import android.content.SharedPreferences;
import com.parse.Parse;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;




public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Context context;
    private boolean isDarkMode;

    private ImageView iconeAtualizacao;

    // Substitua com a chave da API que você gerou
    private static final String API_KEY = "AIzaSyA5TyFVJCPre9LVmXDakSRas6Cqj15Fwcw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ygc9x5nJyze9hzbYA3PMj6obTby6UH4L7Dy3fk6S")
                .clientKey("8MtLLY12itZjo8fPOwjDtSmwZP0wtHPX0ZMtRW23")
                .server("https://parseapi.back4app.com/")
                .build()
        );

        iconeAtualizacao = findViewById(R.id.icone_atualizacao);

        // Chama a verificação, passando esta activity e o callback para exibir o ícone
        // Quando tiver atualização, mostra o ícone
        Atualizador.verificarAtualizacao(this, () -> { runOnUiThread(() -> iconeAtualizacao.setVisibility(View.VISIBLE));
        });


        Android14Features features = new Android14Features(this);

        if (features.isAndroid14OrAbove()) {
            if (!features.hasNotificationPermission()) {
                // Solicitar permissão se necessário
            }

            if (features.isSensorRestricted()) {
                // Alertar o usuário que os sensores estão desabilitados
            }
        }

        // Verificar se o Google Play Protect está ativado


        verificarIntegridadeDispositivo();


        // Exibir o changelog logo após a instalação ou em um evento específico
        ChangelogUtil.exibirChangelog(this);

        // Chamar a criptografia de arquivos
        ChangelogUtil.criptografarArquivos(this);

        // Exibindo um AlertDialog com a confirmação de criptografia



        try (BancodeDados dbHelper = new BancodeDados(this)) {
            dbHelper.getWritableDatabase(); // Abre o banco de dados
            dbHelper.inserirDadosAutomaticos(); // Insere os dados automaticamente
        } catch (Exception e) {
            Log.e("BancodeDados", "Erro ao inicializar o banco de dados: " + e.getMessage(), e);
        }


        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("dark_mode", false);
        boolean jaConfirmou = prefs.getBoolean("google_play_protect_confirmado", false);

        if (!jaConfirmou) {
            Log.d("AppTech", "Google Play Protect ainda não confirmado. Mostrando diálogo.");
            verificarGooglePlayProtect();
        } else {
            Log.d("AppTech", "Google Play Protect já confirmado. Não mostrando diálogo.");
        }

        // Verifica se o alerta de criptografia já foi exibido
        boolean alertaExibido = prefs.getBoolean("alerta_criptografia_exibido", false);

        if (!alertaExibido) {
            new AlertDialog.Builder(this)
                    .setTitle("Criptografia Concluída")
                    .setMessage("A criptografia foi configurada corretamente no seu dispositivo.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        Toast.makeText(MainActivity.this, "Tudo certo!", Toast.LENGTH_SHORT).show();

                        // Marcar que o alerta foi exibido
                        prefs.edit().putBoolean("alerta_criptografia_exibido", true).apply();
                    })
                    .setCancelable(false)
                    .show();
        }

        // Verifica se o usuário já aceitou a política de privacidade
        boolean privacyPolicyAccepted = prefs.getBoolean("privacy_policy_accepted", false);

        if (!privacyPolicyAccepted) {
            showPrivacyPolicyDialog(prefs);
        } else {
            initializeApp();
        }

        // Marca que o áudio foi tocado
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("audioPlayed", true);  // Armazena no SharedPreferences
        editor.apply();

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);
        context = this;

        // Habilitar cookies
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        // Verifica se a Activity recebeu o pedido de permissão
        if (getIntent().getBooleanExtra("requestPermission", false)) {
            // Solicita a permissão
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        ImageView btnModo = findViewById(R.id.menuIcon2);
        btnModo.setOnClickListener(v -> {
            isDarkMode = !isDarkMode;
            prefs.edit().putBoolean("dark_mode", isDarkMode).apply();

            // **Recria a Activity para aplicar o novo tema**
            recreate();
        });

        // Configuração da imagem de fundo (GIF)
        ImageView gifBackground = findViewById(R.id.gif_background);
        Glide.with(this).load(R.drawable.giphy).into(gifBackground);

        ImageView btnSettings = findViewById(R.id.menuIcon3);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });


        String appName = getString(R.string.app_name); // Pega o texto do strings.xml

        SpannableString spannableString = new SpannableString(appName);


        // Aplicando as 9 cores do arco-íris nas partes do texto
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 'F' em vermelho
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7F00")), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 'a' em laranja
        spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 'r' em amarelo
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 'm' em verde
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 'á' em azul
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#4B0082")), 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 'c' em índigo
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8A2BE2")), 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 'i' em violeta
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF1493")), 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 'a' em rosa
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD700")), 8, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // ' ' em dourado




        Button nextButton = findViewById(R.id.nextButton);
        LinearLayout moduleContainer = findViewById(R.id.moduleContainer);
        LinearLayout moduleContainer_d = findViewById(R.id.moduleContainer_d);

        setupModules(moduleContainer);
        setupNewModules(moduleContainer_d);  // Adicione isso para chamar o método


        nextButton.setOnClickListener(v -> startActivity(new Intent(context, FAQMaster.class)));

        // Inicialização do Firebase Messaging
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        Log.d("FCMToken", "Token: " + token);
                        // Aqui você pode salvar o token em um banco de dados ou usá-lo para envio
                    } else {
                        Log.w("FCMToken", "Falha ao obter o token", task.getException());
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Chamando o método que acessa a rede Wi-Fi
                getWifiNetworkName();
            } else {
                // Permissão negada, notifique o usuário
                Toast.makeText(this, "Permissão necessária para acessar Wi-Fi", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Chame este método no onCreate para iniciar a verificação

    @SuppressLint("SetTextI18n")
    private void verificarGooglePlayProtect() {
        TextView mensagemTextView = new TextView(this);
        mensagemTextView.setText("O Google Play Protect protege seu dispositivo contra apps perigosos. " +
                "Caso ele já esteja ativado, nenhuma ação é necessária.\n\n" +
                "Se desejar, acesse as configurações de segurança para verificar o status.");
        mensagemTextView.setTextColor(Color.WHITE);
        mensagemTextView.setPadding(40, 40, 40, 40);
        mensagemTextView.setTextSize(16f);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Verificação do Google Play Protect")
                .setView(mensagemTextView)
                .setPositiveButton("Confirmo", (dialog, which) -> {
                })
                .create();

        alertDialog.setOnShowListener(dialog -> {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1565C0")));
            int titleId = getResources().getIdentifier("alertTitle", "id", "android");
            TextView titleView = alertDialog.findViewById(titleId);
            if (titleView != null) {
                titleView.setTextColor(Color.WHITE);
            }
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        });

        alertDialog.show();
    }





    // Método que verifica se o dispositivo está comprometido (root, etc)
    private void verificarIntegridadeDispositivo() {
        String nonce = gerarNonce();

        SafetyNet.getClient(this).attest(nonce.getBytes(StandardCharsets.UTF_8), API_KEY)
                .addOnSuccessListener(response -> {
                    // Dispositivo considerado seguro
                    Toast.makeText(this, "Dispositivo seguro com Google Play Protect!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    // Dispositivo possivelmente comprometido - mas não mostramos nada
                    // Você pode registrar isso internamente se quiser
                    // Log.w("Integridade", "Dispositivo possivelmente comprometido", e);
                });
    }


    // Método auxiliar para gerar um nonce aleatório
    private String gerarNonce() {
        SecureRandom random = new SecureRandom();
        byte[] nonceBytes = new byte[32];
        random.nextBytes(nonceBytes);

        return Base64.encodeToString(nonceBytes, Base64.NO_WRAP);
    }


    // Exibe o diálogo de Política de Privacidade
    private void showPrivacyPolicyDialog(SharedPreferences prefs) {
        // Texto da Política de Privacidade com a palavra "Ler termos de privacidade" como link
        String message = "A Política de Privacidade da Farmacia AppTech descreve como seus dados são coletados, usados e protegidos. Ao continuar, você concorda com os termos de privacidade.";

        SpannableString spannableMessage = new SpannableString(message);

        // Encontrar a posição da frase "termos de privacidade"
        int start = message.indexOf("termos de privacidade");
        int end = start + "termos de privacidade".length();

        // Adiciona o link à frase "Ler termos de privacidade"
        spannableMessage.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Aqui você pode mostrar a política completa no mesmo AlertDialog
                showFullPrivacyPolicy();
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Criação do AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Política de Privacidade")
                .setMessage(spannableMessage)
                .setCancelable(false)
                .setPositiveButton("Aceitar", (dialog1, which) -> {
                    // Salva no SharedPreferences que o usuário aceitou
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("privacy_policy_accepted", true);
                    editor.apply();

                    // Inicializa o aplicativo após aceitar
                    initializeApp();
                })
                .setNegativeButton("Negar", (dialog1, which) -> {
                    // Encerra o aplicativo caso o usuário não aceite
                    finish();
                })
                .create();

        // Habilita o clique do link dentro do AlertDialog
        dialog.show();
        TextView messageTextView = dialog.findViewById(android.R.id.message);
        if (messageTextView != null) {
            messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    // Função para mostrar a política de privacidade completa dentro do mesmo AlertDialog
    private void showFullPrivacyPolicy() {
        String fullPolicy = "Política de Privacidade\n" +
                "Farmacia AppTech\n\n" +
                "A presente Política de Privacidade tem como objetivo informar sobre a coleta, uso, armazenamento, tratamento e proteção dos dados pessoais dos usuários e visitantes do aplicativo móvel da Farmacia AppTech. Buscamos demonstrar transparência e esclarecer os tipos de dados coletados, os motivos da coleta e como os usuários podem gerenciar ou excluir suas informações pessoais.\n\n" +
                "Este documento foi elaborado em conformidade com a Lei Geral de Proteção de Dados Pessoais (Lei 13.709/18), o Marco Civil da Internet (Lei 12.965/14) e o Regulamento da UE n. 2016/6790. Ressaltamos que esta política poderá ser atualizada conforme alterações legais, portanto, recomendamos que o usuário a consulte periodicamente.\n\n" +
                "1. DADOS COLETADOS\n" +
                "1.1. Dados Pessoais Fornecidos pelo Usuário\n" +
                "Coletamos dados pessoais fornecidos diretamente pelo usuário, como:\n\n" +
                "Nome\n\n" +
                "1.2. Dados Coletados Automaticamente\n" +
                "Ao utilizar nosso aplicativo, também coletamos automaticamente:\n\n" +
                "Endereço de IP\n\n" +
                "Cookies\n\n" +
                "2. FINALIDADES DO USO DOS DADOS PESSOAIS\n" +
                "Utilizamos os dados pessoais para as seguintes finalidades:\n\n" +
                "Aprimoramento de Serviços: Melhorar a experiência do usuário, personalizando conteúdo e funcionalidades conforme suas preferências.\n\n" +
                "Comunicação: Enviar informações relevantes sobre nossos produtos, serviços ou atualizações do aplicativo.\n\n" +
                "Análises: Compreender como os usuários interagem com o aplicativo para desenvolver melhorias técnicas e de conteúdo.\n\n" +
                "Publicidade: Exibir anúncios personalizados com base nos dados fornecidos e no comportamento de navegação.\n\n" +
                "Cumprimento de Obrigações Legais: Atender a requisitos legais ou regulatórios aplicáveis.\n\n" +
                "3. COMPARTILHAMENTO DE DADOS\n" +
                "Seus dados pessoais podem ser compartilhados com:\n\n" +
                "Autoridades Competentes: Quando exigido por lei ou ordem judicial.\n\n" +
                "Instituições Financeiras: Para processamento de pagamentos ou transações financeiras.\n\n" +
                "Parceiros Comerciais: Com o objetivo de oferecer produtos ou serviços que possam interessar ao usuário, sempre respeitando a legislação vigente.\n\n" +
                "4. ARMAZENAMENTO DE DADOS\n" +
                "Os dados pessoais serão armazenados pelo período necessário para cumprir as finalidades mencionadas, respeitando os prazos legais. O usuário pode solicitar a remoção ou anonimização de seus dados, exceto quando sua retenção for obrigatória por lei.\n\n" +
                "5. SEGURANÇA DOS DADOS\n" +
                "Implementamos medidas técnicas e administrativas para proteger os dados pessoais contra acessos não autorizados, perda ou alteração. No entanto, não podemos garantir total segurança em caso de falhas decorrentes de terceiros ou do próprio usuário.\n\n" +
                "6. COOKIES\n" +
                "Utilizamos cookies para aprimorar a experiência do usuário no aplicativo. Cookies são pequenos arquivos armazenados no dispositivo que coletam informações sobre a navegação. O usuário pode configurar seu dispositivo para recusar cookies, mas algumas funcionalidades do aplicativo podem não funcionar corretamente.\n\n" +
                "7. CONSENTIMENTO\n" +
                "Ao utilizar nosso aplicativo e fornecer seus dados pessoais, o usuário consente com a coleta e uso das informações conforme descrito nesta Política de Privacidade. O consentimento pode ser revogado a qualquer momento, mediante solicitação.\n\n" +
                "8. ALTERAÇÕES NA POLÍTICA DE PRIVACIDADE\n" +
                "Reservamo-nos o direito de modificar esta Política de Privacidade a qualquer momento. Alterações serão notificadas aos usuários e terão efeito imediato após a publicação no aplicativo.\n\n" +
                "9. JURISDIÇÃO\n" +
                "Esta política é regida pelas leis brasileiras. Quaisquer litígios serão resolvidos no foro da comarca onde a Farmacia AppTech está sediada.";

        // Exibe o AlertDialog com os termos completos
        new AlertDialog.Builder(this)
                .setTitle("Política de Privacidade Completa")
                .setMessage(fullPolicy)
                .setPositiveButton("Fechar", (dialog, which) -> {
                    // Fechar o dialog quando o usuário clicar em "Fechar"
                    dialog.dismiss();
                })
                .show();
    }


    // Inicializa o aplicativo após o aceite da política
    private void initializeApp() {
        try (BancodeDados dbHelper = new BancodeDados(this)) {
            dbHelper.getWritableDatabase(); // Abre o banco de dados
            dbHelper.inserirDadosAutomaticos(); // Insere os dados automaticamente
        } catch (Exception e) {
            Log.e("BancodeDados", "Erro ao inicializar o banco de dados: " + e.getMessage(), e);
        }
    }

    // nova função do getwifi
    private void getWifiNetworkName() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Toast.makeText(this, "Erro ao acessar Wi-Fi", Toast.LENGTH_SHORT).show();
            return;
        }

        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            Toast.makeText(this, "Nenhuma conexão ativa", Toast.LENGTH_SHORT).show();
            return;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                String ssid = wifiManager.getConnectionInfo().getSSID();

                // Remove aspas extras que podem estar no SSID
                if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                    ssid = ssid.substring(1, ssid.length() - 1);
                }

                Toast.makeText(this, "Conectado à: " + ssid, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Wi-Fi não está conectado", Toast.LENGTH_SHORT).show();
        }
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
        } else if (id == R.id.menu_baixar_app) {
            Intent intent = new Intent(this, MenuRedeActivity.class);
            startActivity(intent);
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
        } else if (id == R.id.menu_calendario) {  // Novo item do menu
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

    private void showErrorDialog(Exception e) {
        // TODO document why this method is empty
    }

    private void mostrarCalendario() {
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setVisibility(View.VISIBLE); // Exibe o calendário

//
    }
    private void showAutorDialog () {
        String info = "Autor: João Igor Rodrigues Pereira da Silva\n" +
                "Empresa: Farmacia AppTech Tecnology\n" +
                "Projeto: Aplicativo de Farmácia";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informações do Autor")
                .setMessage(info)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss(); // Fecha o diálogo
                });
        builder.create().show();
    }

    private void showAboutDialog () {
        String version = "Versão 1.0.7";
        String info = "Aplicativo para Farmacia";
        new AlertDialog.Builder(this)
                .setTitle("Sobre")
                .setMessage(info + "\n" + version)
                .setPositiveButton("OK", null)
                .show();
    }

    // Novo método para exibir as informações de licença
    private void showLicenseDialog () {
        String licenseInfo = "Copyright 2025 João Igor Rodrigues Pereira da Silva, Farmacia AppTech Tecnology\n\n" +
                "Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License (CC BY-NC-ND 4.0);\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at:\n" +
                "https://creativecommons.org/licenses/by-nc-nd/4.0/\n\n" +
                "You are allowed to copy and redistribute the material, but you may **not modify** it or use it for commercial purposes without explicit permission from the author.\n\n" +
                "The use of this application is permitted **only for educational purposes**.";

        new AlertDialog.Builder(this)
                .setTitle("Licença")
                .setMessage(licenseInfo)
                .setPositiveButton("OK", null)
                .show();
    }


    // Método para configurar os módulos principais
    private void setupModules(LinearLayout container) {
        List<String> modules = Arrays.asList(
                "Medicamentos",
                "Quiz",
                "farmacia e suas atualidades",
                "Politica públicas",
                "Feedback",
                "Loja",
                "ChatBot",
                "Vigilância e Promoção à Saúde",
                "Bromatologia",
                "Google Meet"
        );

        for (String module : modules) {
            // Cria um LinearLayout para cada módulo
            LinearLayout moduleLayout = new LinearLayout(context);
            moduleLayout.setOrientation(LinearLayout.HORIZONTAL); // Alinha os itens na horizontal
            moduleLayout.setPadding(16, 8, 16, 8);





            ImageView icon = new ImageView(context);


            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module.equals("Medicamentos")) {
                icon.setImageResource(R.drawable.pills); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module.equals("Bromatologia")) {
                icon.setImageResource(R.drawable.tubes); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module.equals("Vigilância e Promoção à Saúde")) {
                icon.setImageResource(R.drawable.vigilance); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module.equals("Feedback")) {
                icon.setImageResource(R.drawable.feedback); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module.equals("Quiz")) {
                icon.setImageResource(R.drawable.quiz); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module.equals("Loja")) {
                icon.setImageResource(R.drawable.shop); // Ícone específico para "JOGO FORMULAS"
            }


            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module.equals("farmacia e suas atualidades")) {
                icon.setImageResource(R.drawable.help); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module.equals("Politica públicas")) {
                icon.setImageResource(R.drawable.politician); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module.equals("ChatBot")) {
                icon.setImageResource(R.drawable.technical_support); // Ícone específico para "JOGO FORMULAS"
            }


            icon.setLayoutParams(new LinearLayout.LayoutParams(80, 80)); // Define o tamanho do ícone
            //--------------------



            switch (module) {
                case "ChatBot":
                    //iconResId = R.drawable.ic_chat; // Substitua com o seu ícone
                    break;
                case "Politica públicas":

                    break;
                case "Vigilância e Promoção à Saúde":

                    break;
                case "Quiz":

                    break;
                case "farmacia e suas atualidades":

                    break;
                case "Medicamentos":

                    break;
                case "Bromatologia":

                    break;
                case "Feedback":

                    break;
                case "Loja":

                    break;
                default:

                    break;
            }



            // Cria o TextView para o nome do módulo
            TextView textView = new TextView(context);
            textView.setText(module);
            textView.setTextSize(18);
            textView.setTypeface(null, Typeface.BOLD);  // Coloca o texto em negrito
            textView.setPadding(16, 8, 16, 8);
            textView.setBackgroundColor(0xFFD3D3D3);

            // Define a cor de fundo
            int color;
            switch (module) {
                case "ChatBot":
                case "Politica públicas":
                case "Vigilância e Promoção à Saúde":
                    color = 0xFFFFA07A; // Laranja
                    break;
                case "Quiz":
                case "farmacia e suas atualidades":
                    color = 0xFF018786; // Azul
                    break;
                case "Medicamentos":
                case "Bromatologia":
                case "Feedback":
                    color = 0xFF90EE90; // Verde
                    break;
                case "Loja":
                    color = 0xFFFFD700; // Dourado (amarelo vibrante que chama atenção)
                    break;
                default:
                    color = 0xFF000000; // Preto
                    break;
            }

            textView.setTextColor(color);

            // Inicia a animação de piscar
            startBlinkingAnimation(textView);

            // Adiciona o ícone e o nome do módulo ao layout
            moduleLayout.addView(icon);
            moduleLayout.addView(textView);

            // Define o clique para o módulo
            textView.setOnClickListener(v -> handleModuleClick(module));

            // Adiciona o layout do módulo ao contêiner
            container.addView(moduleLayout);
        }
    }





    // Método para configurar os novos módulos
    private void setupNewModules(LinearLayout container) {
        List<String> newModules1 = Arrays.asList(
                "Calculos kcal",
                "Calculo de soro",
                "JOGO FORMULAS",
                "OCR",
                "Quimica Ambiental",
                "LIVROS QUIMICA",
                "Broadcaster",
                "Chat"
        );

        for (String module1 : newModules1) {
            // Cria um LinearLayout para o módulo
            LinearLayout moduleLayout1 = new LinearLayout(context);
            moduleLayout1.setOrientation(LinearLayout.HORIZONTAL); // Alinha os itens na horizontal
            moduleLayout1.setPadding(16, 8, 16, 8);


            ImageView icon = new ImageView(context);



            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module1.equals("JOGO FORMULAS")) {
                icon.setImageResource(R.drawable.lab1); // Ícone específico para "JOGO FORMULAS"
            }

// Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module1.equals("OCR")) {
                icon.setImageResource(R.drawable.webcam); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module1.equals("Broadcaster")) {
                icon.setImageResource(R.drawable.broadcast); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "LIVROS QUIMICA"
            if (module1.equals("LIVROS QUIMICA")) {
                icon.setImageResource(R.drawable.livros); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module1.equals("Quimica Ambiental")) {
                icon.setImageResource(R.drawable.quimicambiental); // Ícone específico para "JOGO FORMULAS"
            }

// Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module1.equals("Calculo de soro")) {
                icon.setImageResource(R.drawable.hair_oil); // Ícone específico para "JOGO FORMULAS"
            }

            // Condicional para atribuir o ícone somente para "JOGO FORMULAS"
            if (module1.equals("Calculos kcal")) {
                icon.setImageResource(R.drawable.kcal); // Ícone específico para "JOGO FORMULAS"
            }



            icon.setLayoutParams(new LinearLayout.LayoutParams(80, 80)); // Define o tamanho do ícone

            // Cria o TextView para o nome do módulo
            TextView textView = new TextView(context);
            textView.setText(module1);  // Corrigido para usar "module1" em vez de "newModules1"
            textView.setTextSize(18);
            textView.setTypeface(null, Typeface.BOLD);  // Coloca o texto em negrito
            textView.setPadding(16, 8, 16, 8);
            textView.setBackgroundColor(0xFFD3D3D3);

            // Define a cor de fundo
            int color = 0xFF00008B; // Azul escuro
            textView.setTextColor(color);

            // Inicia a animação de piscar
            startBlinkingAnimation(textView);

            // Define o clique para o módulo
            textView.setOnClickListener(v -> handleModuleClick(module1));

            // Adiciona o ícone e o nome do módulo ao layout
            moduleLayout1.addView(icon);
            moduleLayout1.addView(textView);

            // Adiciona o layout do módulo ao contêiner
            container.addView(moduleLayout1);
        }
    }



    private void startBlinkingAnimation (TextView textView){
        AlphaAnimation animacaoDePiscar = new AlphaAnimation(0.0f, 1.0f); // animação de opacidade
        animacaoDePiscar.setDuration(1000); // duração de 500ms
        animacaoDePiscar.setRepeatMode(Animation.REVERSE); // inverte a animação a cada ciclo
        animacaoDePiscar.setRepeatCount(Animation.INFINITE); // repete a animação infinitamente
        textView.startAnimation(animacaoDePiscar); // inicia a animação no TextView
    }

    private void verificarPermissaoNotificacoes() {
        if (!Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners")
                .contains(getPackageName())) {
            Toast.makeText(this, "Ative a permissão de notificações!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }
    }

    private void handleModuleClick (String module){
        Class<?> activityClass;
        switch (module) {
            case "Medicamentos":
                activityClass = MedicamentosManager.class;
                break;
            case "Quiz":
                Log.d("ActivitySelection", "Opção selecionada: Quizzes");
                activityClass = QuizActivity.class;
                break;
            case "Bromatologia":
                activityClass = BromatologiaActivity.class;
                break;
            case "Feedback":
                activityClass = FeedbackActivity.class;
                break;
            case "Loja":
                activityClass = LojaActivity.class;
                break;
            case "Politica públicas":
                Log.d("ActivitySelection", "Opção selecionada: Política públicas");
                activityClass = PolicyActivity.class;
                break;
            case "farmacia e suas atualidades":
                Log.d("ActivitySelection", "Opção selecionada: Farmacia e suas atualidades");
                activityClass = AtualidadesActivity.class;
                break;
            case "ChatBot":
                activityClass = ChatbotActivity.class;
                break;
            case "Vigilância e Promoção à Saúde":
                activityClass = VigilanciaTelaActivity.class;
                break;
            case "Calculo de soro":
                activityClass = CalculadoraSoro.class;
                break;
            case "Calculos kcal":
                activityClass = CaloriasActivity.class;
                break;
            case "JOGO FORMULAS":
                activityClass = MisturadorActivity.class;
                break;
            case "OCR":
                activityClass = VisionOCRActivity.class;
                break;
            case "Quimica Ambiental":
                activityClass = QuimicaAmbientalActivity.class;
                break;
            case "LIVROS QUIMICA":
                activityClass = DownloadPdfActivity.class;
                break;
            case "Broadcaster":
                activityClass = VoiceBroadcasterActivity.class;
                break;
            case "Chat":
                activityClass = ChatActivity.class;
                break;


            default:
                Toast.makeText(context, "Selecionado: " + module, Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(new Intent(context, activityClass));
    }

    @Override
    protected void onPause () {
        super.onPause();
        // Removido o código relacionado ao áudio.
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        // Removido o código relacionado ao áudio.
    }
}


