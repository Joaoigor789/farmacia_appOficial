package com.example.farmacia_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Locale;




public class ChatbotVoz {
    private static final String TAG = "ChatbotVoz";
    private final SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private final Context context;

    public ChatbotVoz(Context context, ChatbotListener listener) {
        this.context = context;
        Log.d(TAG, "Inicializando ChatbotVoz");

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String userInput = matches.get(0);
                    Log.d(TAG, "Texto reconhecido: " + userInput);

                    String resposta = verificarSintomas(userInput);
                    listener.onUserInput(resposta);

                    falarResposta(resposta);

                    // Exibe um Toast com a resposta
                    Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
                }
            }

            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onError(int error) { Log.e(TAG, "Erro: " + error); }
            @Override public void onBeginningOfSpeech() {}
            @Override public void onEndOfSpeech() {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onRmsChanged(float rmsdB) {}
        });

        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(new Locale("pt", "BR"));
                Log.d(TAG, "TextToSpeech inicializado com sucesso");
            } else {
                Log.e(TAG, "Falha ao inicializar TextToSpeech");
            }
        });
    }

    public void ouvirUsuario() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
            speechRecognizer.startListening(intent);
        } else {
            ActivityCompat.requestPermissions((MainActivity) context, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    public void falarResposta(String mensagem) {
        textToSpeech.speak(mensagem, TextToSpeech.QUEUE_FLUSH, null, "CHATBOT_VOZ");
    }

    private String verificarSintomas(String input) {
        input = input.toLowerCase();
        if (input.contains("febre") && input.contains("dor de cabeça")) {
            return "Você pode estar com gripe. Recomendo Paracetamol ou Ibuprofeno.";
        } else if (input.contains("febre")) {
            return "Você pode tomar Paracetamol para ajudar a controlar a febre. Mas, se os sintomas persistirem, consulte um médico. O Chá de Hortelã também pode ser útil para aliviar o mal-estar causado pela febre.";
        } else if (input.contains("dor de cabeça")) {
            return "Você pode tentar tomar Dipirona ou Paracetamol para aliviar a dor de cabeça.";
        } else if (input.contains("tosse") && input.contains("febre")) {
            return "Se você está com tosse e febre, pode ser um sintoma de gripe. O uso de antitussígeno e um antitérmico, como Paracetamol, pode ajudar. O Chá de Eucalipto pode ser ótimo para aliviar a tosse e descongestionar as vias respiratórias.";
        } else if (input.contains("tosse")) {
            return "Se a tosse for seca, você pode tentar um xarope antitussígeno. Se for com muco, um expectorante pode ajudar. O Chá de Alcaçuz é eficaz no tratamento de tosse seca e irritação na garganta.";
        } else if (input.contains("náusea")) {
            return "Para náuseas, um antiemético como Dimenidrato pode ser útil.";
        } else if (input.contains("dores musculares")) {
            return "Para dores musculares, você pode usar analgésicos como Ibuprofeno ou um relaxante muscular.";
        } else if (input.contains("calafrios")) {
            return "Os calafrios podem estar associados a febre. Recomendamos monitorar sua temperatura e, se necessário, tomar um antitérmico.";
        } else if (input.contains("fadiga")) {
            return "Para fadiga, uma boa hidratação e descanso são essenciais. Se o cansaço persistir, consulte um médico.";
        } else if (input.contains("dor abdominal")) {
            return "Para dor abdominal, pode ser causado por diversas razões. Se for uma dor aguda e persistente, procure ajuda médica imediatamente.";
        } else if (input.contains("dificuldade para respirar")) {
            return "Se você está tendo dificuldade para respirar, é importante procurar ajuda médica imediatamente, pois isso pode ser um sinal de algo grave.";
        } else if (input.contains("diarreia")) {
            return "Se você está com diarreia, é importante se hidratar. Se continuar por mais de 2 dias ou vier com sangue, procure um médico.";
        } else if (input.contains("dor nas articulações")) {
            return "Para dor nas articulações, você pode usar analgésicos ou anti-inflamatórios. Se a dor persistir, consulte um médico.";
        } else if (input.contains("vômito")) {
            return "Em caso de vômito, um antiemético como Dimenidrato pode ajudar. Mantenha-se hidratado e, caso o problema persista, consulte um médico.";
        } else if (input.contains("dor nas costas")) {
            return "Para dor nas costas, o uso de analgésicos como Ibuprofeno ou um relaxante muscular pode ajudar. Consulte um médico se a dor persistir.";
        } else if (input.contains("mal-estar")) {
            return "Mal-estar pode ser causado por diversos fatores. Recomendo descanso e hidratação. Caso os sintomas se agravem, procure um médico.";
        } else if (input.contains("garganta inflamada")) {
            return "Se você está com dor de garganta, pode tentar pastilhas para a garganta ou gargarejo com água morna e sal. Se a dor continuar, procure um médico.";
        } else if (input.contains("dificuldade para engolir")) {
            return "Se você está com dificuldade para engolir, pode ser um sintoma de inflamação na garganta. Gargarejos com água morna e sal podem ajudar.";
        } else if (input.contains("zumbido no ouvido")) {
            return "O zumbido no ouvido pode ser causado por diversas razões. Se for persistente, é importante procurar um otorrinolaringologista.";
        } else if (input.contains("olhos vermelhos")) {
            return "Olhos vermelhos podem ser um sintoma de alergia ou conjuntivite. Se a irritação persistir, consulte um oftalmologista.";
        } else if (input.contains("dificuldade para dormir")) {
            return "Dificuldade para dormir pode ser causada por estresse ou ansiedade. Evite cafeína e tente relaxar antes de dormir.";
        } else if (input.contains("vertigem")) {
            return "Vertigem pode ser um sintoma de várias condições, incluindo problemas no ouvido interno. Consulte um médico se for persistente.";
        } else if (input.contains("sudorese excessiva")) {
            return "A sudorese excessiva pode ser um sinal de ansiedade ou problemas hormonais. Consulte um médico se os sintomas persistirem.";
        } else if (input.contains("perda de apetite")) {
            return "A perda de apetite pode ser um sintoma de várias condições. Mantenha uma alimentação leve e, se necessário, procure um médico.";
        } else if (input.contains("febre e tosse")) {
            return "Febre com tosse pode ser sinal de infecção respiratória. O uso de antitérmicos e antitussígenos pode ajudar. Procure um médico se os sintomas persistirem.";
        } else if (input.contains("sangramento")) {
            return "Se você está com sangramentos, é importante procurar ajuda médica imediatamente, especialmente se for intenso ou incomum.";
        } else if (input.contains("coração acelerado")) {
            return "Se seu coração está acelerado, isso pode ser devido ao estresse, mas também pode ser sinal de uma condição médica. Consulte um médico se persistir.";
        } else if (input.contains("dificuldade de concentração")) {
            return "Dificuldade de concentração pode ser causada por estresse, cansaço ou distúrbios do sono. Tente descansar e reduzir a ansiedade.";
        } else if (input.contains("dor no peito")) {
            return "Dor no peito pode ser um sinal de diversas condições graves, como problemas cardíacos. Procure ajuda médica imediatamente.";
        } else if (input.contains("pressão alta")) {
            return "Se você tem pressão alta, evite alimentos salgados e faça exercícios leves. Consulte um médico para orientação sobre tratamento.";
        } else if (input.contains("insônia")) {
            return "A insônia pode ser causada por diversos fatores. Evite cafeína e dispositivos eletrônicos antes de dormir. Se persistir, consulte um médico.";
        } else if (input.contains("náusea e dor abdominal")) {
            return "Náusea combinada com dor abdominal pode ser sinal de intoxicação alimentar ou problemas gástricos. Procure ajuda médica se os sintomas persistirem.";
        } else if (input.contains("frio nas extremidades")) {
            return "Frio nas extremidades pode ser causado por baixa circulação sanguínea. Tente aquecer o corpo e, se necessário, consulte um médico.";
        } else if (input.contains("desmaio")) {
            return "Se você desmaiou ou se sente tonto, é importante descansar e se hidratar. Se os sintomas persistirem, procure ajuda médica.";
        } else if (input.contains("inchaço")) {
            return "Inchaço pode ser causado por retenção de líquidos, alergias ou lesões. Se o inchaço for súbito ou severo, procure um médico.";
        } else if (input.contains("tosse com sangue")) {
            return "Tosse com sangue é um sintoma grave que deve ser avaliado por um médico imediatamente, pois pode indicar uma condição respiratória grave.";
        } else if (input.contains("dificuldade para andar")) {
            return "Dificuldade para andar pode ser causada por problemas neurológicos ou musculares. Consulte um médico para diagnóstico e tratamento.";
        } else if (input.contains("erupção cutânea")) {
            return "Erupção cutânea pode ser causada por alergias, infecções ou doenças autoimunes. Se persistir ou se espalhar, consulte um dermatologista.";
        } else if (input.contains("boca seca")) {
            return "Boca seca pode ser um sintoma de desidratação ou uso de medicamentos. Aumente a ingestão de líquidos e, se necessário, procure orientação médica.";
        } else if (input.contains("dificuldade para urinar")) {
            return "Dificuldade para urinar pode ser um sintoma de infecção urinária ou problemas prostáticos. Consulte um médico para investigação.";
        } else if (input.contains("urina escura")) {
            return "Urina escura pode ser um sinal de desidratação ou problemas no fígado. Aumente a ingestão de líquidos e consulte um médico se necessário.";
        } else if (input.contains("tontura")) {
            return "Tontura pode ser causada por diversos fatores, incluindo pressão baixa ou problemas vestibulares. Se persistir, consulte um médico.";
        } else if (input.contains("amigdalite")) {
            return "Amigdalite pode ser causada por infecção viral ou bacteriana. Gargarejos com água morna e sal podem ajudar, mas consulte um médico para tratamento.";
        } else if (input.contains("mau hálito")) {
            return "Mau hálito pode ser causado por problemas dentários ou digestivos. Uma boa higiene bucal pode ajudar, mas consulte um dentista se persistir.";
        } else if (input.contains("arrotos frequentes")) {
            return "Arrotos frequentes podem ser um sintoma de refluxo ácido. Evite alimentos pesados e acidez, e consulte um médico se necessário.";
        } else {
            return "Desculpe, não consegui identificar um sintoma relacionado. Se precisar de ajuda, consulte um médico.";
        }
    }


    public interface ChatbotListener {
        void onUserInput(String response);
    }
}
