package com.example.farmacia_app;

import android.annotation.SuppressLint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.MedicamentoViewHolder> {
    private List<String> medicamentosList;

    public MedicamentosAdapter(Map<String, Pair<Pair<String, String>, String>> medicamentosMap) {
        this.medicamentosList = new ArrayList<>(medicamentosMap.keySet());
    }

    @NonNull
    @Override
    public MedicamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new MedicamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentoViewHolder holder, int position) {
        String medicamento = medicamentosList.get(position);
        holder.textView.setText(medicamento);
    }

    @Override
    public int getItemCount() {
        return medicamentosList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<String> newMedicamentosList) {
        this.medicamentosList = newMedicamentosList;
        notifyDataSetChanged();
    }

    public static class MedicamentoViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MedicamentoViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}

