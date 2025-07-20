package com.example.farmacia_app.desativados;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.farmacia_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.BarcodeView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private BarcodeView barcodeView;
    private TextView medicamentoInfo;

    private DatabaseReference medicamentosRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        barcodeView = findViewById(R.id.barcode_view);
        medicamentoInfo = findViewById(R.id.medicamento_info);

        // Inicializar o Firebase
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        medicamentosRef = mDatabase.getReference("medicamentos");

        // Verifica se a permissão da câmera já foi concedida
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            startScanning();
        }
    }

    @SuppressLint("SetTextI18n")
    private void startScanning() {
        barcodeView.decodeContinuous(result -> {
            if (result.getText() != null) {
                String codigoBarras = result.getText();
                Toast.makeText(CameraActivity.this, "Código de Barras: " + codigoBarras, Toast.LENGTH_SHORT).show();

                // Buscar no JSON as informações do medicamento
                String medicamento = buscarMedicamentoPorCodigo(CameraActivity.this, codigoBarras);

                if (medicamento == null) {
                    // Caso o medicamento não seja encontrado, sugerir o registro
                    medicamentoInfo.setText("Medicamento não encontrado. Escaneando QR Code para bula...");
                    scanQRCode(codigoBarras);  // Iniciar o scan do QR Code para bula
                } else {
                    // Exibir na tela o medicamento encontrado
                    medicamentoInfo.setText(medicamento);
                }
            }
        });

        barcodeView.resume();
    }

    private void scanQRCode(String codigoBarras) {
        // Simulação do escaneamento de QR Code (você precisaria implementar um scanner de QR code para essa funcionalidade)
        String qrCodeInfo = "QR Code info for medication"; // Exemplo, em um caso real você processaria o QR code

        // Agora que temos as informações do QR code, podemos adicionar ao JSON e Firebase
        adicionarMedicamentoNoJSON(codigoBarras, qrCodeInfo);
        adicionarMedicamentoNoFirebase(codigoBarras, qrCodeInfo);
    }

    @SuppressLint("SetTextI18n")
    private void adicionarMedicamentoNoJSON(String codigoBarras, String qrCodeInfo) {
        try {
            // Carregar o JSON existente
            InputStream inputStream = getAssets().open("medicamentos.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            // Parse JSON existente
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());

            // Criar um novo objeto de medicamento com as informações coletadas
            JSONObject novoMedicamento = new JSONObject();
            novoMedicamento.put("nome", "Medicamento Teste");
            novoMedicamento.put("codigo_barras", codigoBarras);
            novoMedicamento.put("registro", qrCodeInfo);  // Usando a informação do QR Code como "registro"

            // Adicionar o novo medicamento ao array
            jsonArray.put(novoMedicamento);

            // Salvar o JSON atualizado de volta no arquivo
            FileOutputStream outputStream = openFileOutput("medicamentos.json", Context.MODE_PRIVATE);
            outputStream.write(jsonArray.toString().getBytes());
            outputStream.close();

            // Exibir sucesso
            medicamentoInfo.setText("Medicamento adicionado com sucesso!");
        } catch (IOException | org.json.JSONException e) {
            Log.e("CameraActivity", "Erro ao adicionar medicamento", e);
            medicamentoInfo.setText("Erro ao adicionar medicamento.");
        }
    }

    private void adicionarMedicamentoNoFirebase(String codigoBarras, String qrCodeInfo) {
        // Criar um novo objeto de medicamento para enviar ao Firebase
        Medicamento medicamento = new Medicamento("Medicamento Teste", codigoBarras, qrCodeInfo);

        // Adicionar o medicamento ao Firebase
        medicamentosRef.push().setValue(medicamento)
                .addOnSuccessListener(aVoid -> {
                    // Sucesso
                    Toast.makeText(CameraActivity.this, "Medicamento adicionado ao Firebase", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Falha
                    Toast.makeText(CameraActivity.this, "Erro ao adicionar no Firebase", Toast.LENGTH_SHORT).show();
                });
    }

    private String buscarMedicamentoPorCodigo(Context context, String codigoBarras) {
        try {
            InputStream inputStream = context.getAssets().open("medicamentos.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            // Parse JSON
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject medicamento = jsonArray.getJSONObject(i);
                if (medicamento.getString("codigo_barras").equals(codigoBarras)) {
                    return "Medicamento: " + medicamento.getString("nome") + "\nRegistro: " + medicamento.getString("registro");
                }
            }

        } catch (IOException | org.json.JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                Toast.makeText(this, "Permissão da câmera negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeView != null) {
            barcodeView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeView != null) {
            barcodeView.pause();
        }
    }

    // Classe para representar o medicamento no Firebase
    public static class Medicamento {
        private String nome;
        private String codigo_barras;
        private String registro;

        public Medicamento() {
            // Necessário para o Firebase
        }

        public Medicamento(String nome, String codigo_barras, String registro) {
            this.nome = nome;
            this.codigo_barras = codigo_barras;
            this.registro = registro;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCodigo_barras() {
            return codigo_barras;
        }

        public void setCodigo_barras(String codigo_barras) {
            this.codigo_barras = codigo_barras;
        }

        public String getRegistro() {
            return registro;
        }

        public void setRegistro(String registro) {
            this.registro = registro;
        }
    }
}
