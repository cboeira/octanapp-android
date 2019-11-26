package com.example.octanapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.octanapp.R;
import com.example.octanapp.model.AutonomiaPosto;

import java.util.List;

public class RecyclerViewAutonomiaAdapter extends RecyclerView.Adapter<RecyclerViewAutonomiaAdapter.MyViewHolder> {

    private Context mContext;
    private List<AutonomiaPosto> mData;

    public RecyclerViewAutonomiaAdapter(Context mContext, List<AutonomiaPosto> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_item_autonomia, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNome.setText(mData.get(position).getNome());
        holder.tvAutonomiaAvaliacao.setText(String.valueOf(mData.get(position).getAutonomia())+
                " km/L ("+String.valueOf(mData.get(position).getNumAvaliacoes()+" avaliações)"));
        holder.tvPrecoKm.setText("R$ "+String.valueOf(mData.get(position).getPrecoKm())+"/km");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNome;
        TextView tvAutonomiaAvaliacao;
        TextView tvPrecoKm;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNome = itemView.findViewById(R.id.autonomia_nome);
            tvAutonomiaAvaliacao = itemView.findViewById(R.id.autonomia_aut_av);
            tvPrecoKm = itemView.findViewById(R.id.autonomia_precokm);


        }
    }
}