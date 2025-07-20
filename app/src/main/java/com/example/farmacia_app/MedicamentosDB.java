package com.example.farmacia_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MedicamentosDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Medicamento.db";
    private static final int DATABASE_VERSION = 2;  // Atualizando a versão para refletir a mudança
    private static final String TABLE_NAME = "medicamentos";
    private static final String COLUMN_NAME = "nome";
    private static final String COLUMN_FORMULA = "formula";
    private static final String COLUMN_CIENTIFICO = "nome_cientifico"; // Nova coluna

    public MedicamentosDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_NAME + " TEXT PRIMARY KEY, "
                + COLUMN_CIENTIFICO + " TEXT NOT NULL, "  // Adicionando a coluna nome_cientifico
                + COLUMN_FORMULA + " TEXT NOT NULL)";
        db.execSQL(createTableSQL);

        // Inserir dados iniciais
        insertMedicamentos(db);
    }

    private void insertMedicamentos(SQLiteDatabase db) {
        String[][] medicamentos = {
                {"Abacavir", "Abacavir", "C14H18N6O"},
                {"Aciclovir", "Aciclovir", "C8H11N5O3"},
                {"Adenosina", "Adenosine", "C10H13N5O4"},
                {"Albuterol", "Albuterol", "C13H21NO3"},
                {"Amoxicilina", "Amoxicillin", "C16H19N3O5S"},
                {"Amlodipina", "Amlodipine", "C20H25ClN2O5"},
                {"Ampicilina", "Ampicillin", "C16H19N3O4S"},
                {"Anlodipina", "Anlodipine", "C20H25ClN2O5"},
                {"Aripiprazol", "Aripiprazole", "C23H27Cl2N3O2"},
                {"Atorvastatina", "Atorvastatin", "C33H35FNO2"},
                {"Azitromicina", "Azithromycin", "C20H28N2O2"},
                {"Benzonatato", "Benzonatate", "C30H53NO11"},
                {"Bisoprolol", "Bisoprolol", "C18H31NO4"},
                {"Budesonida", "Budesonide", "C25H34O6"},
                {"Captopril", "Captopril", "C9H15NO3S"},
        };

        try {
            db.beginTransaction();
            for (String[] medicamento : medicamentos) {
                db.execSQL("INSERT OR IGNORE INTO " + TABLE_NAME + " VALUES (?, ?, ?)", medicamento);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MedicamentosDB", "Erro ao inserir medicamentos: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualizar a tabela para adicionar a nova coluna 'nome_cientifico'
        if (oldVersion < 2) {
            String alterTableSQL = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_CIENTIFICO + " TEXT NOT NULL DEFAULT ''";
            db.execSQL(alterTableSQL);
        }
    }
}
