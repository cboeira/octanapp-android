package com.example.octanapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;

import org.json.JSONObject;

public class InformacoesFragment extends Fragment {
    View view;

    TextView txtRazao;
    TextView txtEndereco;
    TextView txtTelefone;
    TextView txtRamo;
    TextView txtInformacoes;
    TextView txtBandeira;
    TextView txtFormaPagamento;

    String urlInformacoesPosto;

    StringRequest stringRequest;
    RequestQueue requestQueue;

    ProgressBar progressBar;

    public InformacoesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_informacoes, container, false);
        String id_posto = "";
        if (getArguments() != null) {
            id_posto = getArguments().getString("id_posto");
        }

        requestQueue = Volley.newRequestQueue(getContext());
        progressBar = view.findViewById(R.id.progressBar);
        urlInformacoesPosto = "https://octanapp.herokuapp.com/retornaInformacoesPosto.php?id_posto="+id_posto;

        retornarInformacoesPosto();

        return view;

    }

    private void retornarInformacoesPosto() {

        stringRequest = new StringRequest(Request.Method.GET, urlInformacoesPosto,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            txtRazao = view.findViewById(R.id.informacoes_razaoSocial);
                            txtEndereco = view.findViewById(R.id.informacoes_endereco);
                            txtTelefone = view.findViewById(R.id.informacoes_telefone);
                            txtRamo = view.findViewById(R.id.informacoes_ramo);
                            txtInformacoes = view.findViewById(R.id.informacoes_informacoes);
                            txtBandeira = view.findViewById(R.id.informacoes_bandeira);
                            txtFormaPagamento = view.findViewById(R.id.informacoes_forma_pagamento);
                            txtRazao.setText(jsonObject.getString("razaoSocial"));
                            txtEndereco.setText("Endereço: "+jsonObject.getString("endereco"));
                            txtTelefone.setText("Telefone: "+jsonObject.getString("telefone"));
                            txtRamo.setText("Ramo: "+jsonObject.getString("ramo"));
                            txtInformacoes.setText("Informações: "+jsonObject.getString("informacoes"));
                            txtBandeira.setText("Bandeira: "+jsonObject.getString("bandeira"));
                            txtFormaPagamento.setText("Formas de Pagamento: "+jsonObject.getString("formaPagamento"));
                            Log.v("LogLogin", response);
                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Log.v("LogLogin", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("LogLogin", error.getMessage());

                    }
                }) {
        };
        progressBar.setVisibility(View.VISIBLE);
        requestQueue.add(stringRequest);
    }

}