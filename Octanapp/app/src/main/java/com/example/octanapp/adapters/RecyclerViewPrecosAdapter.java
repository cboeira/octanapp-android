package com.example.octanapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.octanapp.R;
import com.example.octanapp.model.CombustivelPosto;

import java.util.List;

public class RecyclerViewPrecosAdapter extends RecyclerView.Adapter<RecyclerViewPrecosAdapter.MyViewHolder> {

    private Context mContext;
    private List<CombustivelPosto> mData;

    public RecyclerViewPrecosAdapter(Context mContext, List<CombustivelPosto> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_item_precos, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNome.setText(mData.get(position).getNome());
        holder.tvPreco.setText("R$ "+String.valueOf(mData.get(position).getPreco()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNome;
        TextView tvPreco;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNome = itemView.findViewById(R.id.precos_nome);
            tvPreco = itemView.findViewById(R.id.precos_preco);


        }
    }
}
