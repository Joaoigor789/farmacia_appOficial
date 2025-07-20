package com.example.farmacia_app;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class MedicamentosManager extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);

        EditText searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);


        playAudio();  // Chama o método que você criou para tocar o áudio
        RecyclerView medicamentosRecyclerView = findViewById(R.id.medicamentosRecyclerView);
        medicamentosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Map<String, Pair<Pair<String, String>, Pair<String, String>>> medicamentosMap = new HashMap<>();

        medicamentosMap.put("Histamin", new Pair<>(new Pair<>("C5H9N3", "Histamin"), new Pair<>("Sem Tarja", "Requer prescrição")));
        medicamentosMap.put("Zufillin", new Pair<>(new Pair<>("C9H13ClN4O3", "Levofloxacino"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Zyloric", new Pair<>(new Pair<>("C5H4N4O", "Alopurinol"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Acetaminofeno", new Pair<>(new Pair<>("C8H9NO2", "Tylenol"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Ácido acetilsalicílico", new Pair<>(new Pair<>("C9H8O4", "Aspirina"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Atropina", new Pair<>(new Pair<>("C17H23NO3", "Atropion"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Cloxacilina", new Pair<>(new Pair<>("C19H17Cl2N3O5S", "Orbenin"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Doxazosina", new Pair<>(new Pair<>("C19H22N4O4", "Carduran"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Gabapentina", new Pair<>(new Pair<>("C9H17NO2", "Neurontin"), new Pair<>("Tarja Preta", "Requer prescrição")));
        medicamentosMap.put("Hidroclorotiazida", new Pair<>(new Pair<>("C7H8ClN3O4S2", "Clorana"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Irbesartan", new Pair<>(new Pair<>("C25H28N6O", "Aprovel"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Doxiciclina", new Pair<>(new Pair<>("C22H24N2O8", "Vibramicina"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Divalproato", new Pair<>(new Pair<>("C16H22N2O2", "Depakote"), new Pair<>("Sem Tarja", "Requer prescrição")));

        medicamentosMap.put("Dantroleno", new Pair<>(new Pair<>("C14H9N3O2", "Dantrium"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Metotrexato", new Pair<>(new Pair<>("C20H22N8O5", "Reumatrex"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Oxybutinina", new Pair<>(new Pair<>("C22H30N2O3", "Ditropan"), new Pair<>("Sem Tarja", "Requer prescrição")));
        medicamentosMap.put("Pantoprazol", new Pair<>(new Pair<>("C16H15F2N3O4S", "Pantozol"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Diltiazem", new Pair<>(new Pair<>("C22H26N2O4S", "Cardizem"), new Pair<>("Sem Tarja", "Requer prescrição")));
        medicamentosMap.put("Estatina", new Pair<>(new Pair<>("C24H36O6", "Lipitor"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Amlodipino", new Pair<>(new Pair<>("C20H25ClN2O5", "Norvasc"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Cefepime", new Pair<>(new Pair<>("C19H24N6O5S2", "Maxipime"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Espironolactona", new Pair<>(new Pair<>("C24H32O4S", "Aldactone"), new Pair<>("Sem Tarja", "Requer prescrição")));
        medicamentosMap.put("Miconazol", new Pair<>(new Pair<>("C18H14Cl4N2", "Daktarin"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Terbinafina", new Pair<>(new Pair<>("C21H27N", "Lamisil"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Dopamina", new Pair<>(new Pair<>("C8H11NO2", "Intropin"), new Pair<>("Sem Tarja", "Requer prescrição")));
        medicamentosMap.put("Hydroxizina", new Pair<>(new Pair<>("C21H27ClN2O2", "Hixizine"), new Pair<>("Sem Tarja", "Requer prescrição")));
        medicamentosMap.put("Salbutamol", new Pair<>(new Pair<>("C13H21NO3", "Aerolin"), new Pair<>("Sem Tarja", "Venda livre")));




        medicamentosMap.put("Ropivacaína", new Pair<>(new Pair<>("C17H26N2O2", "Naropin"), new Pair<>("Tarja Vermelha", "Requer prescrição")));

        medicamentosMap.put("Tobramicina", new Pair<>(new Pair<>("C18H37N5O9", "Tobrex"), new Pair<>("Tarja Vermelha", "Requer prescrição")));

        medicamentosMap.put("Valproato de Sódio", new Pair<>(new Pair<>("C8H15NaO2", "Depakene"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Vigabatrina", new Pair<>(new Pair<>("C6H11NO2", "Sabril"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Vismodegib", new Pair<>(new Pair<>("C19H18N2O2", "Erivedge"), new Pair<>("Tarja Preta", "Uso controlado")));
        medicamentosMap.put("Zafirlucaste", new Pair<>(new Pair<>("C24H23N3O6S", "Accolate"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Zolmitriptano", new Pair<>(new Pair<>("C16H21N3O2S", "Zomig"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Zoledronato", new Pair<>(new Pair<>("C5H10N2O7P2", "Zometa"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Candesartana", new Pair<>(new Pair<>("C24H24N6O3", "Atacand"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Duloxetina", new Pair<>(new Pair<>("C18H19NOS", "Cymbalta"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Escitalopram", new Pair<>(new Pair<>("C20H21FN2O", "Lexapro"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Fenobarbital", new Pair<>(new Pair<>("C12H12N2O3", "Gardenal"), new Pair<>("Tarja Preta", "Uso controlado")));
        medicamentosMap.put("Olanzapina", new Pair<>(new Pair<>("C17H20N4S", "Zyprexa"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Omeprazol", new Pair<>(new Pair<>("C17H19N3O3S", "Losec"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Paroxetina", new Pair<>(new Pair<>("C19H20FNO3", "Aropax"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Sertralina", new Pair<>(new Pair<>("C17H17NCl2", "Zoloft"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Sumatriptano", new Pair<>(new Pair<>("C14H21N3O2S", "Imigran"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Tamsulosina", new Pair<>(new Pair<>("C20H28N2O2", "Secotex"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Tiotropio", new Pair<>(new Pair<>("C19H22NO4S", "Spiriva"), new Pair<>("Sem Tarja", "Venda livre")));



        medicamentosMap.put("Tramadol", new Pair<>(new Pair<>("C16H25NO2", "Dorless, Tramal"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Venlafaxina", new Pair<>(new Pair<>("C17H27NO2", "Efexor XR"), new Pair<>("Tarja Vermelha", "Requer prescrição")));

        medicamentosMap.put("Adapaleno", new Pair<>(new Pair<>("C18H16O3", "Differin"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Atenolol", new Pair<>(new Pair<>("C14H22N2O3", "Atenol"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Beclometasona", new Pair<>(new Pair<>("C22H21ClO5", "Clenil, Beclosol"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Ciprofloxacino", new Pair<>(new Pair<>("C17H18FN3O3", "Cipro"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Cloridrato de Fluoxetina", new Pair<>(new Pair<>("C17H18F3NO", "Prozac"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Cloridrato de Metformina", new Pair<>(new Pair<>("C4H11N5", "Glycon, Glifage"), new Pair<>("Sem Tarja", "Venda livre")));

        medicamentosMap.put("Corticosteróides", new Pair<>(new Pair<>("C21H30O5", "Predsim, Meticorten"), new Pair<>("Tarja Vermelha", "Requer prescrição")));

        medicamentosMap.put("Difenidramina", new Pair<>(new Pair<>("C17H21NO", "Benadryl"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Enoxaparina", new Pair<>(new Pair<>("C29H40N2O8S2", "Clexane"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Fenitoína", new Pair<>(new Pair<>("C15H12N2O2", "Hidantal"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Fexofenadina", new Pair<>(new Pair<>("C32H39NO4", "Allegra"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Flunarizina", new Pair<>(new Pair<>("C19H22F2N2", "Vertix"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Furosemida", new Pair<>(new Pair<>("C12H11ClN2O5S", "Lasix"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Glibenclamida", new Pair<>(new Pair<>("C23H28ClN3O5S", "Daonil"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Ibuprofeno", new Pair<>(new Pair<>("C13H18O2", "Advil, Alivium"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Insulina Aspart", new Pair<>(new Pair<>("C257H383N65O77S6", "NovoRapid"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Loratadina", new Pair<>(new Pair<>("C22H23ClN2", "Claritin"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Moxifloxacino", new Pair<>(new Pair<>("C21H24FN3O4", "Avalox"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Nebivolol", new Pair<>(new Pair<>("C22H25F2N3O4", "Nebilet"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Nimesulida", new Pair<>(new Pair<>("C13H12N2O5S", "Nisulid"), new Pair<>("Sem Tarja", "Venda livre")));

        medicamentosMap.put("Ranitidina", new Pair<>(new Pair<>("C13H22N4O3S", "Antak"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Salmeterol", new Pair<>(new Pair<>("C25H37NO4", "Serevent"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Simvastatina", new Pair<>(new Pair<>("C25H38O5", "Zocor"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
       medicamentosMap.put("Triamcinolona", new Pair<>(new Pair<>("C21H27FO6", "Triax"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Trimetoprima", new Pair<>(new Pair<>("C14H18N4O3", "Bactrim"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Valproato", new Pair<>(new Pair<>("C8H15NaO2", "Depakene"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Vareniclina", new Pair<>(new Pair<>("C13H13ClN2", "Chantix"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Rabeprazol", new Pair<>(new Pair<>("C18H21N3O3S", "AcipHex"), new Pair<>("Sem Tarja", "Venda livre")));





        medicamentosMap.put("Abacavir", new Pair<>(new Pair<>("[(1S,4R)-4-(2-amino-6-oxo-3H-purin-9-yl)cyclopent-2-en-1-yl]methanol", "C14H18N6O"), new Pair<>("Tarja Preta", "Requer prescrição")));
        medicamentosMap.put("Aciclovir", new Pair<>(new Pair<>("C8H11N5O3", "Zovirax"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Acetazolamida", new Pair<>(new Pair<>("C4H6N4O3S2", "Diamox"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Adenosina", new Pair<>(new Pair<>("C10H13N5O4", "Adenocard"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Albuterol", new Pair<>(new Pair<>("C13H21NO3", "Ventolin"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Amoxicilina", new Pair<>(new Pair<>("C16H19N3O5S", "Amoxil"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Amlodipina", new Pair<>(new Pair<>("C20H25ClN2O5", "Norvasc"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Ampicilina", new Pair<>(new Pair<>("C16H19N3O4S", "Principen"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Aripiprazol", new Pair<>(new Pair<>("C23H27Cl2N3O2", "Abilify"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Atorvastatina", new Pair<>(new Pair<>("C33H35FN2O5", "Lipitor"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Azitromicina", new Pair<>(new Pair<>("C38H72N2O12", "Zithromax"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Benzonatato", new Pair<>(new Pair<>("C30H53NO11", "Tessalon"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Bisoprolol", new Pair<>(new Pair<>("C18H31NO4", "Zebeta"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Budesonida", new Pair<>(new Pair<>("C25H34O6", "Pulmicort"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Bevacizumabe", new Pair<>(new Pair<>("C6638H10160N1740O2108S44", ""), new Pair<>("Tarja Preta", "Requer prescrição")));
        medicamentosMap.put("Bortezomibe", new Pair<>(new Pair<>("C19H25BN4O4", ""), new Pair<>("Tarja Preta", "Requer prescrição")));
        medicamentosMap.put("Captopril", new Pair<>(new Pair<>("C9H15NO3S", "Capoten"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Cefalexina", new Pair<>(new Pair<>("C16H17N3O4S", "Keflex"), new Pair<>("Sem Tarja", "Venda livre")));
        medicamentosMap.put("Ceftriaxona", new Pair<>(new Pair<>("C18H18N8O7S3", "Rocefin"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Ciclosporina", new Pair<>(new Pair<>("C62H111N11O12", "Sandimmun"), new Pair<>("Tarja Preta", "Requer prescrição")));
        medicamentosMap.put("Cabergolina", new Pair<>(new Pair<>("C26H37N5O2", "Dostinex"), new Pair<>("Tarja Vermelha", "Requer prescrição")));
        medicamentosMap.put("Capecitabina", new Pair<>(new Pair<>("C15H22FN3O6", "Xeloda"), new Pair<>("Tarja Vermelha", "Requer prescrição")));

        medicamentosMap.put("Betametasona", new Pair<>(
                new Pair<>("C22H29FO5", "Celestone"),
                new Pair<>("Tarja Vermelha", "Requer prescrição")
        ));

        medicamentosMap.put("Diclofenaco Sódico", new Pair<>(
                new Pair<>("C14H10Cl2NNaO2", "Voltaren"),
                new Pair<>("Sem Tarja", "Venda livre")
        ));

        medicamentosMap.put("Diclofenaco Potássico", new Pair<>(
                new Pair<>("C14H10Cl2KNO2", "Cataflam"),
                new Pair<>("Sem Tarja", "Venda livre")
        ));

        medicamentosMap.put("Cetirizina", new Pair<>(
                new Pair<>("C21H25ClN2O3", "Zyrtec"),
                new Pair<>("Sem Tarja", "Venda livre")
        ));

        medicamentosMap.put("Clobetasol", new Pair<>(
                new Pair<>("C22H28ClFO4", "Temovate"),
                new Pair<>("Tarja Vermelha", "Requer prescrição")
        ));

        medicamentosMap.put("Colchicina", new Pair<>(
                new Pair<>("C22H25NO6", "Colcrys"),
                new Pair<>("Sem Tarja", "Venda livre")
        ));

        medicamentosMap.put("Cloridrato de Amitriptilina", new Pair<>(
                new Pair<>("C20H23ClN2", "Elavil"),
                new Pair<>("Tarja Vermelha", "Requer prescrição")
        ));



        //tarja preta//

        medicamentosMap.put("Lorazepam", new Pair<>(new Pair<>("C15H10Cl2N2O2", "Lorax"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
       medicamentosMap.put("Ritalina", new Pair<>(new Pair<>("C14H19NO2", "Metilfenidato"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
         medicamentosMap.put("Fentanil", new Pair<>(new Pair<>("C22H28N2O", "Durogesic"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Morfina", new Pair<>(new Pair<>("C17H19NO3", "Dimorf"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Metadona", new Pair<>(new Pair<>("C21H27NO", "Metadose"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
         medicamentosMap.put("Clordiazepóxido", new Pair<>(new Pair<>("C16H14ClN3O", "Librium"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Flunitrazepam", new Pair<>(new Pair<>("C16H12FN3O3", "Rohypnol"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Tiopental", new Pair<>(new Pair<>("C11H18N2O2S", "Pentothal"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Pentazocina", new Pair<>(new Pair<>("C18H27NO2", "Talwin"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Pentobarbitol", new Pair<>(new Pair<>("C15H22N2O3", "Nembutal"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));


        medicamentosMap.put("Oximetazolina", new Pair<>(new Pair<>("C8H10N2O", "Afrin"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Methylphenidate", new Pair<>(new Pair<>("C14H19NO2", "Ritalin"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Buprenorfina", new Pair<>(new Pair<>("C29H41NO4", "Suboxone"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Lidocaína", new Pair<>(new Pair<>("C14H22N2O", "Xylocaine"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Carisoprodol", new Pair<>(new Pair<>("C12H24N2O4", "Soma"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Diazepam", new Pair<>(new Pair<>("C16H13ClN2O", "Valium"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Fluoxetina", new Pair<>(new Pair<>("C17H18F3NO", "Prozac"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Bromazepam", new Pair<>(new Pair<>("C14H10BrN3O", "Lexotan"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Temazepam", new Pair<>(new Pair<>("C16H11ClN2O2", "Restoril"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Midazolam", new Pair<>(new Pair<>("C18H13ClFN3", "Versed"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Fentanyl", new Pair<>(new Pair<>("C22H28N2O", "Duragesic"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Clemastina", new Pair<>(new Pair<>("C16H19ClN2", "Tavist"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Hydrocodone", new Pair<>(new Pair<>("C18H21NO3", "Vicodin"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Nortriptilina", new Pair<>(new Pair<>("C19H22N2", "Pamelor"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Mepivacaína", new Pair<>(new Pair<>("C15H22N2O", "Carbocaine"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Fluconazol", new Pair<>(new Pair<>("C13H12F2N6O", "Diflucan"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Clonazepam", new Pair<>(new Pair<>("C15H10ClN3O3", "Rivotril"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Alprazolam", new Pair<>(new Pair<>("C17H13ClN4", "Frontal"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Zolpidem", new Pair<>(new Pair<>("C19H21N3O", "Stilnox"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Oxicodona", new Pair<>(new Pair<>("C18H21NO4", "OxyContin"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Codeína", new Pair<>(new Pair<>("C18H21NO3", "Codein"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Loperamida", new Pair<>(new Pair<>("C29H33NO2", "Imodium"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Naloxona", new Pair<>(new Pair<>("C19H21NO4", "Narcan"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Trazodona", new Pair<>(new Pair<>("C19H22ClN5O", "Desyrel"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Prazepam", new Pair<>(new Pair<>("C15H11ClN2O", "Centrax"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Sertindol", new Pair<>(new Pair<>("C19H23N3O2S", "Serdolect"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Carbamazepina", new Pair<>(new Pair<>("C15H12N2O", "Tegretol"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Amisulprida", new Pair<>(new Pair<>("C17H22N2O4S", "Solian"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Zopiclona", new Pair<>(new Pair<>("C17H17ClN6O", "Imovane"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Quetiapina", new Pair<>(new Pair<>("C21H25N3O2S", "Seroquel"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Haloperidol", new Pair<>(new Pair<>("C21H23ClFNO2", "Haldol"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Risperidona", new Pair<>(new Pair<>("C23H27FN4O2", "Risperdal"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Clozapina", new Pair<>(new Pair<>("C18H19ClN4", "Clozaril"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));
        medicamentosMap.put("Mirtazapina", new Pair<>(new Pair<>("C17H19N3", "Remeron"), new Pair<>("Tarja Preta", "Requer prescrição e retenção de receita")));







        MedicamentosAdapter adapter = new MedicamentosAdapter(medicamentosMap);
        medicamentosRecyclerView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { /* TODO document why this method is empty */ }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterMedicamentos(charSequence.toString(), medicamentosMap, adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) { /* TODO document why this method is empty */ }
        });

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            filterMedicamentos(query, medicamentosMap, adapter);
        });
    }

    private void playAudio() {
        // Libera qualquer instância anterior do MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Cria uma nova instância do MediaPlayer e inicia a reprodução
        mediaPlayer = MediaPlayer.create(this, R.raw.medi_echo);

        if (mediaPlayer != null) {
            mediaPlayer.start();

            // Configurar o listener para liberar o MediaPlayer após a reprodução
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        } else {
            Log.e("MediaPlayer", "Falha ao criar o MediaPlayer.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Liberar o MediaPlayer se ainda estiver em uso
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void filterMedicamentos(String query, Map<String, Pair<Pair<String, String>, Pair<String, String>>> medicamentosMap, MedicamentosAdapter adapter) {
        query = query.toLowerCase();
        List<String> filteredList = new ArrayList<>();

        for (String medicamento : medicamentosMap.keySet()) {
            Pair<Pair<String, String>, Pair<String, String>> medicamentoInfo = medicamentosMap.get(medicamento);

            if (medicamentoInfo != null &&
                    (medicamento.toLowerCase().contains(query) ||
                            medicamentoInfo.first.first.toLowerCase().contains(query) || // Nome científico
                            medicamentoInfo.first.second.toLowerCase().contains(query) || // Fórmula
                            medicamentoInfo.second.first.toLowerCase().contains(query) || // Tarja
                            medicamentoInfo.second.second.toLowerCase().contains(query))) { // Prescrição

                filteredList.add(medicamento);
            }
        }

        adapter.updateList(filteredList);
    }

    static class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.MedicamentoViewHolder> {
        private List<String> medicamentos;
        private final Map<String, Pair<Pair<String, String>, Pair<String, String>>> medicamentosMap;

        MedicamentosAdapter(Map<String, Pair<Pair<String, String>, Pair<String, String>>> medicamentosMap) {
            this.medicamentos = new ArrayList<>(medicamentosMap.keySet());
            this.medicamentosMap = medicamentosMap;
        }

        @NonNull
        @Override
        public MedicamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicamento, parent, false);
            return new MedicamentoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MedicamentoViewHolder holder, int position) {
            String medicamentoNome = medicamentos.get(position);
            Pair<Pair<String, String>, Pair<String, String>> medicamentoInfo = medicamentosMap.get(medicamentoNome);
            holder.nomeMedicamento.setText(medicamentoNome);
            if (medicamentoInfo != null) {
                holder.nomeCientifico.setText(medicamentoInfo.first.first);
                holder.formulaQuimica.setText(medicamentoInfo.first.second);
                holder.tarjamentacao.setText(medicamentoInfo.second.first);
                holder.prescricao.setText(medicamentoInfo.second.second);

                // Define a cor padrão para evitar sobreposição indesejada
                holder.tarjamentacao.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.black));
                holder.prescricao.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.black));

                // Define cores específicas com base nos valores
                if ("Tarja Preta".equals(medicamentoInfo.second.first)) {
                    holder.tarjamentacao.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.black));
                } else if ("Sem Tarja".equals(medicamentoInfo.second.first)) {
                    holder.tarjamentacao.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.cinza_medio_escuro));
                } else {
                    holder.tarjamentacao.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_light));
                }

                if ("Requer prescrição e retenção de receita".equals(medicamentoInfo.second.second)) {
                    holder.prescricao.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow));
                } else if ("Requer prescrição".equals(medicamentoInfo.second.second)) {
                    holder.prescricao.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_200));
                } else if ("Venda livre".equals(medicamentoInfo.second.second)) {
                    holder.prescricao.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.menu_success));
                }

                // Define a cor do texto da fórmula química sempre para nome_comercial_color
                holder.formulaQuimica.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.nome_comercial_color));


            }


            ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(holder.itemView, "rotationY", 0f, 360f);
            rotateAnimator.setDuration(500);
            rotateAnimator.start();
            holder.itemView.setAlpha(0f);
            holder.itemView.animate().alpha(1f).setDuration(500).start();
        }

        @Override
        public int getItemCount() {
            return medicamentos.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void updateList(List<String> newMedicamentos) {
            this.medicamentos = newMedicamentos;
            notifyDataSetChanged();
        }

        static class MedicamentoViewHolder extends RecyclerView.ViewHolder {
            TextView nomeMedicamento, nomeCientifico, formulaQuimica, tarjamentacao, prescricao;

            MedicamentoViewHolder(@NonNull View itemView) {
                super(itemView);
                nomeMedicamento = itemView.findViewById(R.id.medicamentoNome);
                nomeCientifico = itemView.findViewById(R.id.medicamentoNomeCientifico);
                formulaQuimica = itemView.findViewById(R.id.medicamentoFormula);
                tarjamentacao = itemView.findViewById(R.id.medicamentoTarja);
                prescricao = itemView.findViewById(R.id.medicamentoPrescricao);
            }
        }
    }
}
