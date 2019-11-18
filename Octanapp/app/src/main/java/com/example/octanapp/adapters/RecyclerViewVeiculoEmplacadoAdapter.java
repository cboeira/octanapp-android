package com.example.octanapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.octanapp.R;
import com.example.octanapp.activities.VeiculoActivity;
import com.example.octanapp.model.VeiculoEmplacado;

import java.util.List;

public class RecyclerViewVeiculoEmplacadoAdapter extends RecyclerView.Adapter<RecyclerViewVeiculoEmplacadoAdapter.MyViewHolder> {

    private Context mContext;
    private List<VeiculoEmplacado> mData;

    public RecyclerViewVeiculoEmplacadoAdapter(Context mContext, List<VeiculoEmplacado> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_item_veiculoemplacado, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, VeiculoActivity.class);
                i.putExtra("marca", mData.get(viewHolder.getAdapterPosition()).getMarca());
                i.putExtra("modelo", mData.get(viewHolder.getAdapterPosition()).getModelo());
                i.putExtra("ano", mData.get(viewHolder.getAdapterPosition()).getAno());
                i.putExtra("ativo", mData.get(viewHolder.getAdapterPosition()).getAtivo());
                i.putExtra("placa", mData.get(viewHolder.getAdapterPosition()).getPlaca());
                i.putExtra("kmTotal", mData.get(viewHolder.getAdapterPosition()).getKmTotal());
                i.putExtra("id_veiculo", mData.get(viewHolder.getAdapterPosition()).getId_veiculo());
                i.putExtra("id_usuario", mData.get(viewHolder.getAdapterPosition()).getId_usuario());
                mContext.startActivity(i);

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvMarca.setText(mData.get(position).getMarca());
        holder.tvModelo.setText(mData.get(position).getModelo());
        holder.tvAno.setText(String.valueOf(mData.get(position).getAno()));
        if (mData.get(position).getAtivo() == 1) {
            holder.imgAtivo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMarca;
        TextView tvModelo;
        TextView tvAno;
        ImageView imgAtivo;
        RelativeLayout view_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view_container = itemView.findViewById(R.id.container);
            tvMarca = itemView.findViewById(R.id.veiculoemplacado_marca);
            tvModelo = itemView.findViewById(R.id.veiculoemplacado_modelo);
            tvAno = itemView.findViewById(R.id.veiculoemplacado_ano);
            imgAtivo = itemView.findViewById(R.id.veiculoemplacado_imagem_ativo);


        }
    }
}
