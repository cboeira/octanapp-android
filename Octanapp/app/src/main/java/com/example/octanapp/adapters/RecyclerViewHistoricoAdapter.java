package com.example.octanapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.octanapp.R;
import com.example.octanapp.activities.PostoActivity;
import com.example.octanapp.activities.VeiculoActivity;
import com.example.octanapp.model.AvaliacaoCompleta;
import com.example.octanapp.model.PostoFavorito;
import com.example.octanapp.model.VeiculoEmplacado;

import java.util.List;

public class RecyclerViewHistoricoAdapter extends RecyclerView.Adapter<RecyclerViewHistoricoAdapter.MyViewHolder> {

    private Context mContext;
    private List<AvaliacaoCompleta> mData;

    public RecyclerViewHistoricoAdapter(Context mContext, List<AvaliacaoCompleta> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_item_historico, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNomeFantasia.setText(mData.get(position).getNomeFantasia());
        holder.tvBandeira.setText(mData.get(position).getBandeira());
        holder.tvHorario.setText(mData.get(position).getHorario());
        holder.tvLitragem.setText(String.valueOf(mData.get(position).getLitros())+" L");
        holder.tvKmTotal.setText(String.valueOf(mData.get(position).getKmTotal())+ " km");
        holder.tvAutonomia.setText(String.valueOf(mData.get(position).getAutonomia())+" km/L");
        holder.tvCombustivel.setText(mData.get(position).getCombustivel());
        holder.ratingBar.setRating(mData.get(position).getNota());
        holder.ratingBar.setFocusable(false);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNomeFantasia;
        TextView tvBandeira;
        TextView tvHorario;
        TextView tvLitragem;
        TextView tvKmTotal;
        TextView tvAutonomia;
        TextView tvCombustivel;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeFantasia = itemView.findViewById(R.id.historico_nomePosto);
            tvBandeira = itemView.findViewById(R.id.historico_bandeira);
            tvHorario = itemView.findViewById(R.id.historico_horario);
            tvLitragem = itemView.findViewById(R.id.historico_litragem);
            tvKmTotal = itemView.findViewById(R.id.historico_kmtotal);
            tvAutonomia = itemView.findViewById(R.id.historico_autonomia);
            tvCombustivel = itemView.findViewById(R.id.historico_combustivel);
            ratingBar = itemView.findViewById(R.id.ratingBar_historico);

        }
    }
}
