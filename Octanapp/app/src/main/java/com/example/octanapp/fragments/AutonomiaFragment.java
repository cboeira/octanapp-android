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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.adapters.RecyclerViewAutonomiaAdapter;
import com.example.octanapp.model.AutonomiaPosto;
import com.example.octanapp.model.VeiculoEmplacado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutonomiaFragment extends Fragment {
    View view;

    RequestQueue requestQueue;

    String urlRetornaAutonomia, urlVeiculoAtivo;

    StringRequest stringRequest;
    ProgressBar progressBar;
    VeiculoEmplacado veiculoEmplacado;

    TextView veiculoAtivo;

    private JsonArrayRequest request;
    private List<AutonomiaPosto> listaAutonomia;
    private RecyclerView recyclerView;

    String id_usuario, id_posto;

    public AutonomiaFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_autonomia, container, false);
        id_posto = "";
        id_usuario = "";
        if (getArguments() != null) {
            id_posto = getArguments().getString("id_posto");
            id_usuario = getArguments().getString("id_usuario");
        }
        listaAutonomia = new ArrayList<>();

        urlRetornaAutonomia = "https://octanapp.herokuapp.com/retornaAutonomiaPostoVeiculo.php?id_posto="+id_posto+"&id_usuario="+id_usuario;
        //urlRetornaAutonomia = "http://192.168.25.17/octanapp/retornaAutonomiaPostoVeiculo.php?id_posto="+id_posto+"&id_usuario="+id_usuario;
        urlVeiculoAtivo = "https://octanapp.herokuapp.com/verificaVeiculoAtivo.php?id_usuario="+id_usuario;

        veiculoAtivo = view.findViewById(R.id.autonomia_veiculo);
        progressBar = view.findViewById(R.id.progressBar_autonomia);
        veiculoEmplacado = new VeiculoEmplacado();
        requestQueue = Volley.newRequestQueue(getContext());

        verificarVeiculoAtivo();

        recyclerView = view.findViewById(R.id.recyclerView_id);
        jsonrequest();


        return view;
    }

    private void jsonrequest() {
        request = new JsonArrayRequest(urlRetornaAutonomia, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        AutonomiaPosto autonomia = new AutonomiaPosto();
                        autonomia.setNumAvaliacoes(jsonObject.getInt("numeroAvaliacoes"));
                        autonomia.setAutonomia(jsonObject.getDouble("autonomia"));
                        autonomia.setPrecoKm(jsonObject.getDouble("precokm"));
                        autonomia.setNome(jsonObject.getString("nome"));
                        listaAutonomia.add(autonomia);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setuprecyclerview(listaAutonomia);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<AutonomiaPosto> listaPrecos) {

        RecyclerViewAutonomiaAdapter myadapter = new RecyclerViewAutonomiaAdapter(getContext(), listaAutonomia);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(myadapter);
    }

    public void verificarVeiculoAtivo() {

        stringRequest = new StringRequest(Request.Method.GET, urlVeiculoAtivo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean ativo;
                            boolean erro = jsonObject.getBoolean("erro");
                            if (!erro) {
                                ativo = true;
                                veiculoEmplacado.setAno(jsonObject.getInt("ano"));
                                veiculoEmplacado.setId_veiculo(jsonObject.getInt("id_veiculo"));
                                veiculoEmplacado.setId_usuario(Integer.parseInt(id_usuario));
                                veiculoEmplacado.setPlaca(jsonObject.getString("placa"));
                                veiculoEmplacado.setMarca(jsonObject.getString("marca"));
                                veiculoEmplacado.setModelo(jsonObject.getString("modelo"));
                                veiculoEmplacado.setKmTotal(jsonObject.getInt("kmTotal"));
                                System.out.println(veiculoEmplacado.getMarca());
                                Log.v("LogLogin", "entrou no ativo = true");

                                if (ativo) {
                                    veiculoAtivo.setText(veiculoEmplacado.getMarca()+" "+veiculoEmplacado.getModelo());
                                }
                            } else {
                                veiculoAtivo.setText("SEM VEICULO ATIVO");
                            }
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
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", id_usuario);
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        requestQueue.add(stringRequest);
    }

}
