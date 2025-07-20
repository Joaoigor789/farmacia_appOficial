package com.example.farmacia_app;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BancodeDados extends SQLiteOpenHelper {
    private static final String TAG = "BancodeDados";
    private static final String DB_NAME_FARMACIA = "farmacia_app.db";
    private static final int DB_VERSION = 1;

    public BancodeDados(MainActivity context) {
        super(context, DB_NAME_FARMACIA, null, DB_VERSION);
        Log.d(TAG, "Banco de dados inicializado");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando tabela farmacia...");
        db.execSQL("CREATE TABLE IF NOT EXISTS farmacia ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome_dispositivo TEXT, "
                + "nome_usuario TEXT);");
        Log.d(TAG, "Tabela criada com sucesso");
    }

    public void inserirDadosAutomaticos() {
        SQLiteDatabase db = this.getWritableDatabase();
        String nomeDispositivo = android.os.Build.MODEL;
        String nomeUsuario = System.getProperty("user.name");

        Log.d(TAG, "Inserindo dados no banco: " + nomeDispositivo + ", " + nomeUsuario);

        db.execSQL("INSERT INTO farmacia (nome_dispositivo, nome_usuario) VALUES (?, ?);",
                new String[]{nomeDispositivo, nomeUsuario});
        db.close();

        Log.d(TAG, "Dados inseridos com sucesso");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Atualizando banco de dados...");
        db.execSQL("DROP TABLE IF EXISTS farmacia");
        onCreate(db);
    }
}
