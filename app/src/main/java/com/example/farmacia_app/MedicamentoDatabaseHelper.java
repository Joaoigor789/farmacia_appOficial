package com.example.farmacia_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicamentoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medicamentos.db";
    private static final int DATABASE_VERSION = 1;

    public MedicamentoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medicamentos (nome TEXT PRIMARY KEY, descricao TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS medicamentos");
        onCreate(db);
    }

    public void inserirMedicamento(String nome, String descricao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("descricao", descricao);
        db.insertWithOnConflict("medicamentos", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public String buscarDescricao(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("medicamentos", new String[]{"descricao"},
                "nome = ?", new String[]{nome}, null, null, null);

        if (cursor.moveToFirst()) {
            String descricao = cursor.getString(0);
            cursor.close();
            db.close();
            return descricao;
        }

        cursor.close();
        db.close();
        return null;
    }
}

