package com.example.farmacia_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisionOCRActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MedicamentosPrefs";
    private static final String KEY_MEDICAMENTOS_SET = "medicamentos_set";

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ListaMedicamentoAdapter adapter;
    private List<Medicamento> listaMedicamentos = new ArrayList<>();
    private List<Medicamento> listaMedicamentosCompleta = new ArrayList<>(); // pra filtrar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_ocr);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerViewMedicamentos);

        initMedicamentos(); // inicializa a lista no SharedPreferences se vazio

        carregarMedicamentos(); // carrega todos, mas não exibe ainda
        listaMedicamentos.clear(); // limpa para iniciar vazio
        adapter = new ListaMedicamentoAdapter(new ArrayList<>()); // lista vazia no início


        adapter = new ListaMedicamentoAdapter(listaMedicamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarMedicamentos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarMedicamentos(newText);
                return true;
            }
        });
    }

    private void initMedicamentos() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (!prefs.contains(KEY_MEDICAMENTOS_SET)) {
            Set<String> medicamentosDefault = new HashSet<>();
            medicamentosDefault.add("Omeprazol|Usado para reduzir a acidez no estômago.");
            medicamentosDefault.add("Losartana|Anti-hipertensivo usado para controlar a pressão arterial.");
            medicamentosDefault.add("Metformina|Anti-hiperglicemiante oral para diabetes tipo 2.");
            medicamentosDefault.add("Dipirona|Analgésico e antipirético para dor e febre.");
            medicamentosDefault.add("Ranitidina|Usada para diminuir a produção de ácido gástrico.");
            medicamentosDefault.add("Ibuprofeno|AINE usado para dor, febre e inflamação.");
            medicamentosDefault.add("Paracetamol|Analgésico e antipirético comum para dor leve e febre.");
            medicamentosDefault.add("Amoxicilina|Antibiótico usado para infecções bacterianas.");
            medicamentosDefault.add("Captopril|Inibidor da enzima conversora de angiotensina para hipertensão.");
            medicamentosDefault.add("Aspirina|Analgésico e anti-inflamatório, também usado para prevenção cardiovascular.");
            medicamentosDefault.add("Clonazepam|Ansiolítico usado para transtornos de ansiedade e epilepsia.");
            medicamentosDefault.add("Losartana potássica|Antihipertensivo para controle da pressão arterial.");
            medicamentosDefault.add("Fluoxetina|Antidepressivo inibidor seletivo da recaptação de serotonina.");
            medicamentosDefault.add("Prednisona|Corticosteroide para inflamações e doenças autoimunes.");
            medicamentosDefault.add("Cetoconazol|Antifúngico para tratamento de infecções de pele e mucosas.");
            medicamentosDefault.add("Claritromicina|Antibiótico macrolídeo para infecções bacterianas.");
            medicamentosDefault.add("Diazepam|Ansiolítico usado para ansiedade, espasmos musculares e convulsões.");
            medicamentosDefault.add("Salbutamol|Broncodilatador para asma e DPOC.");
            medicamentosDefault.add("Metronidazol|Antibiótico para infecções bacterianas e protozoárias.");
            medicamentosDefault.add("Dexametasona|Corticosteroide para inflamação e condições alérgicas.");
            medicamentosDefault.add("Furosemida|Diurético para edema e hipertensão.");
            medicamentosDefault.add("Enalapril|Inibidor da ECA usado para hipertensão e insuficiência cardíaca.");
            medicamentosDefault.add("Clonidina|Antihipertensivo e para sintomas de abstinência.");
            medicamentosDefault.add("Sertralina|Antidepressivo para depressão e transtornos de ansiedade.");
            medicamentosDefault.add("Nifedipino|Bloqueador dos canais de cálcio para hipertensão.");
            medicamentosDefault.add("Carbamazepina|Antiepiléptico para convulsões e transtornos bipolares.");
            medicamentosDefault.add("Risperidona|Antipsicótico para esquizofrenia e transtornos bipolares.");
            medicamentosDefault.add("Tramadol|Analgésico opioide para dor moderada a grave.");
            medicamentosDefault.add("Alprazolam|Ansiolítico para transtornos de ansiedade e pânico.");
            medicamentosDefault.add("Cloridrato de amitriptilina|Antidepressivo tricíclico para depressão e dor neuropática.");
            medicamentosDefault.add("Levotiroxina|Hormônio tireoidiano para hipotireoidismo.");
            medicamentosDefault.add("Lorazepam|Ansiolítico para ansiedade e insônia.");
            medicamentosDefault.add("Glimepirida|Antidiabético oral para diabetes tipo 2.");
            medicamentosDefault.add("Metildopa|Antihipertensivo usado na hipertensão na gravidez.");
            medicamentosDefault.add("Clopidogrel|Antiagregante plaquetário para prevenção de eventos cardiovasculares.");
            medicamentosDefault.add("Fenitoína|Antiepiléptico para controle de convulsões.");
            medicamentosDefault.add("Atorvastatina|Estatina para redução do colesterol.");
            medicamentosDefault.add("Prednisolona|Corticosteroide para inflamação e alergias.");
            medicamentosDefault.add("Tansulosina|Relaxante muscular para tratar hiperplasia prostática benigna.");
            medicamentosDefault.add("Amiodarona|Antiarrítmico para arritmias cardíacas.");
            medicamentosDefault.add("Citalopram|Antidepressivo inibidor seletivo de recaptação de serotonina.");
            medicamentosDefault.add("Cloridrato de tramadol|Analgésico opioide para dor moderada a severa.");
            medicamentosDefault.add("Venlafaxina|Antidepressivo para depressão e ansiedade.");
            medicamentosDefault.add("Levodopa|Usado no tratamento da doença de Parkinson.");
            medicamentosDefault.add("Doxiciclina|Antibiótico para diversas infecções bacterianas.");
            medicamentosDefault.add("Sulfasalazina|Usada em doenças autoimunes como artrite reumatoide.");
            medicamentosDefault.add("Clindamicina|Antibiótico para infecções bacterianas graves.");
            medicamentosDefault.add("Ketorolaco|Analgésico anti-inflamatório para dor aguda.");
            medicamentosDefault.add("Glucosamina|Suplemento para saúde das articulações.");
            medicamentosDefault.add("Cetirizina|Anti-histamínico para alergias.");
            medicamentosDefault.add("Montelucaste|Usado para asma e alergias respiratórias.");
            medicamentosDefault.add("Rivaroxabana|Anticoagulante oral para prevenção de tromboses.");
            medicamentosDefault.add("Olanzapina|Antipsicótico para esquizofrenia e bipolaridade.");
            medicamentosDefault.add("Levofloxacino|Antibiótico fluoroquinolona para infecções.");
            medicamentosDefault.add("Metoclopramida|Usada para náuseas e vômitos.");
            medicamentosDefault.add("Clorfeniramina|Anti-histamínico para alergias.");
            medicamentosDefault.add("Fexofenadina|Anti-histamínico para alergias.");
            medicamentosDefault.add("Tadalafil|Tratamento para disfunção erétil e hipertensão pulmonar.");
            medicamentosDefault.add("Oxicodona|Analgésico opioide para dor severa.");
            medicamentosDefault.add("Naproxeno|Analgésico e anti-inflamatório.");
            medicamentosDefault.add("Amoxicilina com clavulanato|Antibiótico combinado para infecções resistentes.");
            medicamentosDefault.add("Oxcarbazepina|Antiepiléptico para convulsões.");
            medicamentosDefault.add("Topiramato|Antiepiléptico para convulsões e enxaquecas.");
            medicamentosDefault.add("Lamotrigina|Antiepiléptico usado também para transtorno bipolar.");
            medicamentosDefault.add("Betametasona|Corticosteroide para condições inflamatórias.");
            medicamentosDefault.add("Haloperidol|Antipsicótico para esquizofrenia e agitação.");
            medicamentosDefault.add("Midazolam|Ansiolítico e sedativo.");
            medicamentosDefault.add("Cloridrato de bupropiona|Antidepressivo e auxiliar no abandono do tabagismo.");
            medicamentosDefault.add("Buspirona|Ansiolítico para transtornos de ansiedade.");
            medicamentosDefault.add("Propranolol|Betabloqueador para hipertensão e ansiedade.");
            medicamentosDefault.add("Metoprolol|Betabloqueador para hipertensão e insuficiência cardíaca.");
            medicamentosDefault.add("Valsartana|Antihipertensivo antagonista dos receptores de angiotensina II.");
            medicamentosDefault.add("Rosuvastatina|Estatina para redução do colesterol LDL.");
            medicamentosDefault.add("Clobazam|Ansiolítico e antiepiléptico.");
            medicamentosDefault.add("Oxazepam|Ansiolítico para ansiedade e insônia.");
            medicamentosDefault.add("Metadona|Opioide usado para tratamento de dor e dependência química.");
            medicamentosDefault.add("Nistatina|Antifúngico para infecções por Candida.");
            medicamentosDefault.add("Ketoconazol|Antifúngico usado para micoses.");
            medicamentosDefault.add("Rifampicina|Antibiótico para tuberculose e outras infecções.");
            medicamentosDefault.add("Isoniazida|Antibiótico para tuberculose.");
            medicamentosDefault.add("Ethambutol|Antibiótico usado em combinação para tuberculose.");
            medicamentosDefault.add("Clarithromicina|Antibiótico macrolídeo.");
            medicamentosDefault.add("Azitromicina|Antibiótico para infecções bacterianas.");
            medicamentosDefault.add("Levodopa com carbidopa|Tratamento para Parkinson.");
            medicamentosDefault.add("Escitalopram|Antidepressivo para transtornos depressivos e ansiedade.");
            medicamentosDefault.add("Duloxetina|Antidepressivo para depressão e dor neuropática.");
            medicamentosDefault.add("Paroxetina|Antidepressivo para depressão e ansiedade.");
            medicamentosDefault.add("Clorpromazina|Antipsicótico para esquizofrenia e náuseas.");
            medicamentosDefault.add("Levamisol|Imunoestimulante e antiparasitário.");
            medicamentosDefault.add("Clobetasol|Corticosteroide tópico para inflamações de pele.");
            medicamentosDefault.add("Nitrofurantoína|Antibiótico usado para infecções urinárias.");
            medicamentosDefault.add("Baclofeno|Relaxante muscular para espasticidade.");
            medicamentosDefault.add("Cefalexina|Antibiótico cefalosporina para infecções bacterianas.");
            medicamentosDefault.add("Glucocorticoide|Hormônio anti-inflamatório e imunossupressor.");
            medicamentosDefault.add("Amitriptilina|Antidepressivo tricíclico e analgésico para dor neuropática.");
            medicamentosDefault.add("Tiamina|Vitamina B1 usada para prevenção de deficiência.");
            medicamentosDefault.add("Rifabutina|Antibiótico para tratamento de tuberculose resistente.");
            medicamentosDefault.add("Loperamida|Antidiarreico para controle de diarreia.");
            medicamentosDefault.add("Meloxicam|Anti-inflamatório não esteroide para artrite.");
            medicamentosDefault.add("Clemastina|Anti-histamínico para alergias e urticária.");

            // adicione outros medicamentos que quiser aqui...

            prefs.edit().putStringSet(KEY_MEDICAMENTOS_SET, medicamentosDefault).apply();
        }
    }

    private void carregarMedicamentos() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> medicamentosSet = prefs.getStringSet(KEY_MEDICAMENTOS_SET, new HashSet<>());

        listaMedicamentos.clear();
        for (String entry : medicamentosSet) {
            String[] partes = entry.split("\\|", 2); // separa em nome e descricao
            if (partes.length == 2) {
                listaMedicamentos.add(new Medicamento(partes[0], partes[1]));
            }
        }

        // Mantém uma cópia completa para filtrar depois
        listaMedicamentosCompleta.clear();
        listaMedicamentosCompleta.addAll(listaMedicamentos);

        Log.i("Medicamentos", "Carregados: " + listaMedicamentos.size());
    }

    private void filtrarMedicamentos(String texto) {
        texto = texto.toLowerCase().trim();
        listaMedicamentos.clear();



        if (texto.isEmpty()) {
            listaMedicamentos.addAll(listaMedicamentosCompleta);
        } else {
            for (Medicamento m : listaMedicamentosCompleta) {
                if (m.getNome().toLowerCase().contains(texto) || m.getDescricao().toLowerCase().contains(texto)) {
                    listaMedicamentos.add(m);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    // Se quiser salvar uma lista atualizada, faça assim:
    private void salvarMedicamentos() {
        Set<String> medicamentosSet = new HashSet<>();
        for (Medicamento m : listaMedicamentosCompleta) {
            // monta a string no formato "nome|descricao"
            medicamentosSet.add(m.getNome() + "|" + m.getDescricao());
        }
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(KEY_MEDICAMENTOS_SET, medicamentosSet).apply();
    }

    public static class Medicamento {
        private String nome;
        private String descricao;

        public Medicamento(String nome, String descricao) {
            this.nome = nome;
            this.descricao = descricao;
        }

        public String getNome() {
            return nome;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
