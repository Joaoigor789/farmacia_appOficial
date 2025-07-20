package com.example.farmacia_app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class ChangelogManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nenhum layout necessário, apenas o menu será usado
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla o menu da Activity
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (handleMenuItemClick(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean handleMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_atualizacao) {
            // Chama o método da classe utilitária
            ChangelogUtil.exibirChangelog(this);
            return true;
        }
        return false;
    }

}