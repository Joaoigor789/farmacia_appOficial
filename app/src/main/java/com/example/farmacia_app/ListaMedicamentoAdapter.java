package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaMedicamentoAdapter extends RecyclerView.Adapter<ListaMedicamentoAdapter.ViewHolder> {

    private List<VisionOCRActivity.Medicamento> listaCompleta;
    private List<VisionOCRActivity.Medicamento> listaFiltrada;

    public ListaMedicamentoAdapter(List<VisionOCRActivity.Medicamento> medicamentos) {
        this.listaCompleta = new ArrayList<>(medicamentos);
        this.listaFiltrada = medicamentos; // referência direta usada na Activity
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_medicamento, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VisionOCRActivity.Medicamento medicamento = listaFiltrada.get(position);
        holder.txtNome.setText(medicamento.getNome());
        holder.txtDescricao.setText(medicamento.getDescricao());
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filtrar(String texto) {
        texto = texto.toLowerCase().trim();
        listaFiltrada.clear();

        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaCompleta);
        } else {
            for (VisionOCRActivity.Medicamento m : listaCompleta) {
                if (m.getNome().toLowerCase().contains(texto) ||
                        m.getDescricao().toLowerCase().contains(texto)) {
                    listaFiltrada.add(m);
                }
            }
        }

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNome;
        public TextView txtDescricao;
        public CardView cardView;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(View view) {
            super(view);
            txtNome = view.findViewById(R.id.txtNomeMedicamento);
            txtDescricao = view.findViewById(R.id.txtDescricaoMedicamento);
            cardView = (CardView) view;

            // Adiciona listener para efeito 3D touch
            cardView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float centerX = v.getWidth() / 2f;
                    float centerY = v.getHeight() / 2f;
                    float x = event.getX();
                    float y = event.getY();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            float deltaX = (x - centerX) / centerX; // -1 a 1
                            float deltaY = (centerY - y) / centerY; // -1 a 1, invertido para efeito natural

                            float maxRotation = 10f; // graus maximos de rotação

                            v.setRotationY(maxRotation * deltaX);
                            v.setRotationX(maxRotation * deltaY);

                            v.setElevation(20f); // aumenta sombra no toque
                            break;

                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            v.animate()
                                    .rotationX(0)
                                    .rotationY(0)
                                    .setInterpolator(new DecelerateInterpolator())
                                    .setDuration(300)
                                    .start();

                            v.setElevation(10f); // sombra padrão
                            break;
                    }
                    return false; // deixa o card responder normalmente ao clique
                }
            });
        }
    }
}
