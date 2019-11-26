package com.example.octanapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.regex.Pattern;

public class InformacoesFragment extends Fragment {
    View view;

    TextView txtRazao;
    TextView txtEndereco;
    TextView txtTelefone;
    TextView txtRamo;
    TextView txtInformacoes;
    TextView txtBandeira;
    TextView txtFormaPagamento;
    ImageView debito, dinheiro, credito, cheque, aleloauto, ticketcar, semparar, abasteceai;

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
                            //txtFormaPagamento = view.findViewById(R.id.informacoes_forma_pagamento);
                            txtRazao.setText(jsonObject.getString("razaoSocial"));
                            txtEndereco.setText(jsonObject.getString("endereco"));
                            txtTelefone.setText(jsonObject.getString("telefone"));
                            txtRamo.setText(jsonObject.getString("ramo"));
                            txtInformacoes.setText(jsonObject.getString("informacoes"));
                            txtBandeira.setText(jsonObject.getString("bandeira"));
                            String formasPagamento = jsonObject.getString("formaPagamento");
                            String formas[] = formasPagamento.split(Pattern.quote(","));
                            for (String dados : formas) {
                                if (dados.equals("debito")) {
                                    debito = view.findViewById(R.id.forma_debito);
                                    debito.setVisibility(View.VISIBLE);
                                }
                                if (dados.equals("dinheiro")) {
                                    dinheiro = view.findViewById(R.id.forma_dinheiro);
                                    dinheiro.setVisibility(View.VISIBLE);
                                }
                                if (dados.equals("credito")) {
                                    credito = view.findViewById(R.id.forma_credito);
                                    credito.setVisibility(View.VISIBLE);
                                }
                                if (dados.equals("cheque")) {
                                    cheque = view.findViewById(R.id.forma_cheque);
                                    cheque.setVisibility(View.VISIBLE);
                                }
                                if (dados.equals("semparar")) {
                                    semparar = view.findViewById(R.id.forma_semparar);
                                    semparar.setVisibility(View.VISIBLE);
                                }
                                if (dados.equals("ticketcar")) {
                                    ticketcar = view.findViewById(R.id.forma_ticketcar);
                                    ticketcar.setVisibility(View.VISIBLE);
                                }
                                if (dados.equals("abasteceai")) {
                                    abasteceai = view.findViewById(R.id.forma_abasteceai);
                                    abasteceai.setVisibility(View.VISIBLE);
                                }
                                if (dados.equals("aleloauto")) {
                                    aleloauto = view.findViewById(R.id.forma_aleloauto);
                                    aleloauto.setVisibility(View.VISIBLE);
                                }
                            }
                            //txtFormaPagamento.setText(aux);
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