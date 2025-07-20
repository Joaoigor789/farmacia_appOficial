package com.example.farmacia_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> mensagens;

    public ChatAdapter(Context context, ArrayList<String> mensagens) {
        super(context, R.layout.chat_item, mensagens);
        this.context = context;
        this.mensagens = mensagens;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_item, parent, false);
        }

        // Configuração do layout para exibir a mensagem
        TextView textMensagem = convertView.findViewById(R.id.textMensagem);
        textMensagem.setText(mensagens.get(position));

        return convertView;
    }
}

