package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class FAQMaster extends AppCompatActivity {

    private MediaPlayer mediaPlayer; // Declara o MediaPlayer para reprodução de áudio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity); // Certifique-se de que o layout está correto

        RecyclerView faqRecyclerView = findViewById(R.id.faqRecyclerView); // Certifique-se de que o ID está correto no layout

        // Carregar dados do banco de dados
        List<FAQItem> faqList = getFAQFromDatabase();

        // Definir o layout manager para o RecyclerView
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar o adaptador e associar ao RecyclerView
        FAQAdapter faqAdapter = new FAQAdapter(faqList, this);
        faqRecyclerView.setAdapter(faqAdapter);

        // Inicia a reprodução do áudio ao abrir a Activity
        playAudio();


    }

    private void playAudio() {
        // Libera o MediaPlayer se já estiver em uso para evitar vazamento de memória
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Cria e inicia a reprodução do áudio
        mediaPlayer = MediaPlayer.create(this, R.raw.echo);
        mediaPlayer.start();

        // Libera o MediaPlayer quando o áudio terminar
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
        });
    }

    private List<FAQItem> getFAQFromDatabase() {
        List<FAQItem> faqList = new ArrayList<>();

        // Aqui, você pode integrar com o seu banco de dados (SQLite, Firebase, etc.)
        // Para fins de teste, estamos a adicionar as perguntas e respostas manualmente

        faqList.add(new FAQItem("O que são medicamentos bioequivalentes?", "Medicamentos bioequivalentes são aqueles que, apesar de terem formulações diferentes, apresentam o mesmo efeito terapêutico no organismo, com o mesmo tempo de ação."));
        faqList.add(new FAQItem("Como posso saber a dosagem correta de um medicamento?", "A dosagem correta depende da indicação médica e das características do paciente, como peso, idade e condições de saúde."));
        faqList.add(new FAQItem("O que fazer se eu esquecer de tomar um medicamento?", "Caso você se esqueça de tomar um medicamento, consulte as instruções na bula ou pergunte ao seu médico. Não tome doses em dobro para compensar a dose esquecida."));
        faqList.add(new FAQItem("Como posso reduzir o risco de interações medicamentosas?", "Sempre informe seu médico sobre todos os medicamentos que está utilizando e siga as recomendações para evitar combinações perigosas."));
        faqList.add(new FAQItem("É seguro tomar medicamentos durante a amamentação?", "Alguns medicamentos podem passar para o leite materno e afetar o bebê. Consulte sempre um médico antes de usar qualquer medicamento durante a amamentação."));
        faqList.add(new FAQItem("O que fazer se eu tiver uma reação alérgica a um medicamento?", "Se você perceber sinais de alergia, como dificuldade para respirar, inchaço ou erupções cutâneas, suspenda o uso do medicamento imediatamente e procure atendimento médico."));
        faqList.add(new FAQItem("O que é a farmacovigilância?", "Farmacovigilância é o processo de monitoramento da segurança dos medicamentos após sua comercialização, para identificar e prevenir efeitos adversos."));
        faqList.add(new FAQItem("Como posso saber se um medicamento é seguro?", "Verifique sempre se o medicamento possui registro na ANVISA, leia a bula e consulte um profissional de saúde antes de iniciar o tratamento."));
        faqList.add(new FAQItem("O que são medicamentos de uso off-label?", "Medicamentos off-label são aqueles usados para tratar condições para as quais não foram originalmente aprovados. Isso deve ser feito sob orientação médica."));
        faqList.add(new FAQItem("O que é a farmácia clínica?", "A farmácia clínica é uma área da farmácia que se concentra na otimização do uso de medicamentos, garantindo segurança e eficácia no tratamento dos pacientes."));
        faqList.add(new FAQItem("Qual a diferença entre farmácia e drogaria?", "Farmácia é o estabelecimento onde se vendem medicamentos com orientação de um farmacêutico, enquanto drogaria vende produtos de higiene, beleza e saúde, sem necessidade de orientação profissional."));
        faqList.add(new FAQItem("Como posso verificar se um medicamento é original?", "Verifique a embalagem, o número de registro e a procedência do produto. Desconfie de medicamentos vendidos sem a embalagem original ou com rótulos suspeitos."));
        faqList.add(new FAQItem("Quais os cuidados ao tomar medicamentos para pressão alta?", "Medicamentos para pressão alta devem ser tomados regularmente e sob a orientação médica, e o paciente deve monitorar a pressão arterial frequentemente."));
        faqList.add(new FAQItem("Como evitar a resistência bacteriana aos antibióticos?", "Evite o uso indiscriminado de antibióticos e siga sempre a prescrição médica, completando o ciclo do tratamento, mesmo que os sintomas desapareçam antes."));
        faqList.add(new FAQItem("O que são medicamentos de farmácia popular?", "São medicamentos que fazem parte de um programa do governo para facilitar o acesso a medicamentos essenciais e de baixo custo para a população."));
        faqList.add(new FAQItem("Como posso verificar a autenticidade de um medicamento?", "Você pode verificar a autenticidade do medicamento consultando o número de registro na ANVISA ou usando sistemas de rastreamento fornecidos pelo fabricante."));
        faqList.add(new FAQItem("O que fazer em caso de overdose de medicamento?", "Em caso de overdose, procure imediatamente atendimento médico. Leve a embalagem do medicamento para informar o que foi ingerido."));
        faqList.add(new FAQItem("Como funciona a farmácia popular?", "A farmácia popular oferece medicamentos essenciais com preços acessíveis para a população, com a possibilidade de descontos para alguns grupos de pacientes."));
        faqList.add(new FAQItem("Quais cuidados ao usar medicamentos para diabetes?", "Pacientes com diabetes devem monitorar os níveis de glicose regularmente e seguir as orientações médicas sobre o uso dos medicamentos."));
        faqList.add(new FAQItem("O que são medicamentos com tarja preta?", "Medicamentos com tarja preta são substâncias controladas devido ao alto risco de abuso e dependência, sendo necessário receita médica especial para sua aquisição."));
        faqList.add(new FAQItem("Qual a diferença entre farmácia hospitalar e farmácia comunitária?", "A farmácia hospitalar é voltada para o atendimento dentro de hospitais, enquanto a farmácia comunitária atende a população em geral, oferecendo medicamentos e orientações."));
        faqList.add(new FAQItem("O que são medicamentos oncológicos?", "Medicamentos oncológicos são aqueles usados para tratar o câncer, e seu uso deve ser cuidadosamente monitorado por médicos especialistas."));
        faqList.add(new FAQItem("Como posso usar o app para estudar farmacologia?", "Você pode acessar conteúdos interativos, quizzes e simulados dentro da seção 'Farmacologia' do aplicativo para ajudar nos seus estudos."));
        faqList.add(new FAQItem("O que é o efeito rebote de medicamentos?", "O efeito rebote ocorre quando o efeito de um medicamento diminui ou se inverte após a interrupção abrupta de seu uso."));
        faqList.add(new FAQItem("Como funcionam os medicamentos de venda livre?", "Medicamentos de venda livre podem ser comprados sem receita médica, mas devem ser usados com cautela. Consulte um farmacêutico ou médico antes de usá-los."));
        faqList.add(new FAQItem("Como posso acompanhar os efeitos dos medicamentos no aplicativo?", "O aplicativo permite que você registre seus sintomas e reações ao medicamento para acompanhar a evolução do tratamento."));
        faqList.add(new FAQItem("O que é a farmacocinética?", "Farmacocinética é o estudo de como o corpo absorve, distribui, metaboliza e excreta os medicamentos."));
        faqList.add(new FAQItem("Como posso configurar alertas para reabastecimento de medicamentos?", "Você pode configurar alertas nas configurações do aplicativo para ser notificado quando for hora de comprar um novo medicamento."));
        faqList.add(new FAQItem("O que é um medicamento de referência?", "Medicamento de referência é aquele que foi inicialmente aprovado pela autoridade sanitária e serve como base para a produção de medicamentos genéricos."));
        faqList.add(new FAQItem("O que são medicamentos de uso contínuo?", "Medicamentos de uso contínuo são aqueles que devem ser tomados por longos períodos para o controle de doenças crônicas, como hipertensão e diabetes."));
        faqList.add(new FAQItem("Como posso configurar a tela inicial do aplicativo?", "Acesse as configurações do aplicativo e personalize os widgets na tela inicial para acessar rapidamente as funcionalidades mais usadas."));
        faqList.add(new FAQItem("O que é o Sistema de Alerta de Efeitos Adversos?", "O sistema de alerta de efeitos adversos no aplicativo notifica os usuários sobre possíveis reações indesejadas a medicamentos com base em suas interações e histórico médico."));
        faqList.add(new FAQItem("Como posso adicionar novos medicamentos à minha lista de prescrições?", "Você pode adicionar medicamentos à sua lista de prescrições diretamente no aplicativo, registrando os detalhes da dosagem e frequência de uso."));
        faqList.add(new FAQItem("Como posso obter suporte técnico para o aplicativo?", "Para suporte técnico, entre em contato com a equipe de atendimento via a opção 'Ajuda' no menu do aplicativo."));
        faqList.add(new FAQItem("O que são medicamentos de uso tópico?", "Medicamentos de uso tópico são aqueles aplicados diretamente na pele ou mucosas, como cremes e pomadas."));
        faqList.add(new FAQItem("Como posso compartilhar um artigo científico com outros usuários?", "Você pode compartilhar artigos científicos com outros usuários diretamente pela seção 'Biblioteca', utilizando a opção de compartilhamento no aplicativo."));
        faqList.add(new FAQItem("O que são medicamentos imunoestimulantes?", "Medicamentos imunoestimulantes são usados para fortalecer o sistema imunológico, ajudando a combater infecções e doenças."));



        return faqList;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Para e libera o MediaPlayer quando a Activity for destruída
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_sobre) {
            showAboutDialog();
            return true;
        } else if (id == R.id.menu_licenca) {
            showLicenseDialog();  // Nova função para exibir informações de licença
            return true;
        } else if (id == R.id.menu_autor) {
            showAutorDialog(); //nova função para exibir informações do autor
            return true;
        } else if (id == R.id.menu_atualizacao) {
            ChangelogUtil.exibirChangelog(this); // Chama o método centralizado

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exibirChangelog() {
        String changelog = "Versão 1.0.7\n" +
                "- Correção de bugs\n" +
                "- Correção feita no Menu\n" +
                "- Melhorias no desempenho\n" +
                "- Bromatologia modificado/banco de dados\n" +
                "- Tempo esgotado, só será exibido no quiz\n" +
                "- Corrigido o quiz\n" +
                "- Modificação feita no banco de dados de medicamentos\n" +
                "- Adicionada barra de progresso sincronizada com a porcentagem assistida do vídeo.\n" +
                "- Novo sistema de Quiz, progresso";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Changelog - Atualizações")
                .setMessage(changelog)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss(); // Fecha o diálogo
                });
        builder.create().show();
    }


    private void showAutorDialog() {
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

    private void showAboutDialog() {
        String version = "Versão 1.0.5";
        String info = "Aplicativo para Farmacia";
        new AlertDialog.Builder(this)
                .setTitle("Sobre")
                .setMessage(info + "\n" + version)
                .setPositiveButton("OK", null)
                .show();
    }

    // Novo método para exibir as informações de licença
    private void showLicenseDialog() {
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


    public static class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {
        private final List<FAQItem> faqList;  // 'final' adicionado
        private final Context context;  // 'final' adicionado

        public FAQAdapter(List<FAQItem> faqList, Context context) {
            this.faqList = faqList;
            this.context = context;
        }

        @NonNull
        @Override
        public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflando o layout do ‘item’ da FAQ
            View view = LayoutInflater.from(context).inflate(R.layout.faq_item, parent, false);
            return new FAQViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
            FAQItem faq = faqList.get(position);

            // Configurando o texto da pergunta e resposta
            holder.question.setText(faq.getPergunta());
            holder.answer.setText(faq.getResposta());

            // Inicializando a resposta como invisível
            holder.answer.setVisibility(View.GONE);

            // Toggling da visibilidade da resposta quando clicar na pergunta
            holder.itemView.setOnClickListener(v -> {
                if (holder.answer.getVisibility() == View.GONE) {
                    holder.answer.setVisibility(View.VISIBLE);
                } else {
                    holder.answer.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return faqList.size();  // Retorna o número de itens na lista
        }

        // ViewHolder para cada ‘item’ da FAQ
        public static class FAQViewHolder extends RecyclerView.ViewHolder {
            TextView question;
            TextView answer;

            public FAQViewHolder(View itemView) {
                super(itemView);

                // Inicializando as views
                question = itemView.findViewById(R.id.faq_question);
                answer = itemView.findViewById(R.id.faq_answer);
            }
        }
    }

    public static class FAQItem {
        private final String pergunta;
        private final String resposta;

        public FAQItem(String pergunta, String resposta) {
            this.pergunta = pergunta;
            this.resposta = resposta;
        }

        public String getPergunta() {
            return pergunta;
        }

        public String getResposta() {
            return resposta;
        }
    }
}
