package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.adapters.RecyclerViewHistoricoAdapter;
import com.example.octanapp.adapters.RecyclerViewPrecosAdapter;
import com.example.octanapp.model.AvaliacaoCompleta;
import com.example.octanapp.model.CombustivelPosto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    String urlRetornaHistorico;
    String placa;

    private JsonArrayRequest request;
    private List<AvaliacaoCompleta> listaAvaliacao;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        placa = getIntent().getStringExtra("placa");

        Toolbar toolbar = findViewById(R.id.toolbar_historico);
        toolbar.setTitle(placa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listaAvaliacao = new ArrayList<>();
        urlRetornaHistorico = "https://octanapp.herokuapp.com/retornaHistoricoVeiculo.php?placa="+placa;
        //urlRetornaHistorico = "http://192.168.25.17/octanapp/retornaHistoricoVeiculo.php?placa="+placa;

        recyclerView = findViewById(R.id.recyclerView_id);
        jsonrequest();
    }

    private void jsonrequest() {
        request = new JsonArrayRequest(urlRetornaHistorico, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        AvaliacaoCompleta avaliacao = new AvaliacaoCompleta();
                        avaliacao.setNomeFantasia(jsonObject.getString("nomeFantasia"));
                        avaliacao.setLitros(jsonObject.getDouble("litros"));
                        avaliacao.setNota((float)jsonObject.getDouble("nota"));
                        avaliacao.setHorario(jsonObject.getString("horario"));
                        avaliacao.setAutonomia(jsonObject.getDouble("autonomia"));
                        avaliacao.setBandeira(jsonObject.getString("bandeira"));
                        avaliacao.setCombustivel(jsonObject.getString("nomeCombustivel"));
                        avaliacao.setKmTotal(jsonObject.getInt("kmTotal"));
                        avaliacao.setPlaca(placa);
                        listaAvaliacao.add(avaliacao);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setuprecyclerview(listaAvaliacao);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<AvaliacaoCompleta> listaAvaliacao) {

        RecyclerViewHistoricoAdapter myadapter = new RecyclerViewHistoricoAdapter(this, listaAvaliacao);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);
    }

}
