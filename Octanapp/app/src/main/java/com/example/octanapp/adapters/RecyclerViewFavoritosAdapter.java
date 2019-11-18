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
import com.example.octanapp.activities.PostoActivity;
import com.example.octanapp.activities.VeiculoActivity;
import com.example.octanapp.model.PostoFavorito;
import com.example.octanapp.model.VeiculoEmplacado;

import java.util.List;

public class RecyclerViewFavoritosAdapter extends RecyclerView.Adapter<RecyclerViewFavoritosAdapter.MyViewHolder> {

    private Context mContext;
    private List<PostoFavorito> mData;

    public RecyclerViewFavoritosAdapter(Context mContext, List<PostoFavorito> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.row_item_favoritos, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, PostoActivity.class);
                i.putExtra("id_posto", String.valueOf(mData.get(viewHolder.getAdapterPosition()).getId_posto()));
                i.putExtra("id_usuario", String.valueOf(mData.get(viewHolder.getAdapterPosition()).getId_usuario()));
                mContext.startActivity(i);

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNomeFantasia.setText(mData.get(position).getNomeFantasia());
        holder.tvBandeira.setText(mData.get(position).getBandeira());
        String bandeira = mData.get(position).getBandeira();
        if (bandeira.equals("Shell")) {
            holder.imgBandeira.setImageResource(R.mipmap.ic_shell_foreground);
        } else if (bandeira.equals("Ipiranga")) {
            holder.imgBandeira.setImageResource(R.mipmap.ic_ipiranga_foreground);
        } else if (bandeira.equals("Petrobras")) {
            holder.imgBandeira.setImageResource(R.mipmap.ic_petrobras_foreground);
        } else {
            holder.imgBandeira.setImageResource(R.mipmap.ic_posto_foreground);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNomeFantasia;
        TextView tvBandeira;
        ImageView imgBandeira;
        RelativeLayout view_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view_container = itemView.findViewById(R.id.container);
            tvNomeFantasia = itemView.findViewById(R.id.favorito_nomePosto);
            tvBandeira = itemView.findViewById(R.id.favorito_bandeira);
            imgBandeira = itemView.findViewById(R.id.favorito_imagembandeira);


        }
    }
}
