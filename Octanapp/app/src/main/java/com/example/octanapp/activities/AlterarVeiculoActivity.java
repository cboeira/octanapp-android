package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.adapters.RecyclerViewVeiculoEmplacadoAdapter;
import com.example.octanapp.model.Usuario;
import com.example.octanapp.model.VeiculoEmplacado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlterarVeiculoActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    Usuario usuario;
    String urlRetornaVeiculosEmplacados;

    private JsonArrayRequest request;
    private List<VeiculoEmplacado> listaVeiculoEmplacado;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_veiculo);

        listaVeiculoEmplacado = new ArrayList<>();

        Intent intentRecebedora = getIntent();
        final Bundle parametrosUsuario = intentRecebedora.getExtras();
        requestQueue = Volley.newRequestQueue(this);

        usuario = new Usuario();

        if (parametrosUsuario != null) {
            usuario.setId(parametrosUsuario.getLong("id_usuario"));
            usuario.setNome(parametrosUsuario.getString("name"));
            usuario.setEmail(parametrosUsuario.getString("email"));
            usuario.setSenha(parametrosUsuario.getString("senha"));
            usuario.setData_nasc(parametrosUsuario.getString("data_nasc"));
            usuario.setGenero(parametrosUsuario.getString("genero"));

            // Toast.makeText(this, usuario.getId() + usuario.getNome() + usuario.getEmail() + usuario.getSenha() + usuario.getData_nasc() + usuario.getGenero(), Toast.LENGTH_LONG).show();
        }

        urlRetornaVeiculosEmplacados = "https://octanapp.herokuapp.com/retornaVeiculosEmplacados.php?id_usuario="+usuario.getId();

        Toolbar toolbar = findViewById(R.id.toolbar_alterar_veiculo);
        toolbar.setTitle("VEÍCULOS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = findViewById(R.id.recyclerView_id);
        jsonrequest();

    }

    private void jsonrequest() {
        request = new JsonArrayRequest(urlRetornaVeiculosEmplacados, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        VeiculoEmplacado veiculoEmplacado = new VeiculoEmplacado();
                        veiculoEmplacado.setPlaca(jsonObject.getString("placa"));
                        veiculoEmplacado.setKmTotal(jsonObject.getInt("kmTotal"));
                        veiculoEmplacado.setAno(jsonObject.getInt("ano"));
                        veiculoEmplacado.setId_veiculo(jsonObject.getInt("id_veiculo"));
                        veiculoEmplacado.setModelo(jsonObject.getString("modelo"));
                        veiculoEmplacado.setMarca(jsonObject.getString("marca"));
                        veiculoEmplacado.setAtivo(jsonObject.getInt("ativo"));
                        veiculoEmplacado.setId_usuario(usuario.getId());

                        listaVeiculoEmplacado.add(veiculoEmplacado);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                
                setuprecyclerview(listaVeiculoEmplacado);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(AlterarVeiculoActivity.this);
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<VeiculoEmplacado> listaVeiculoEmplacado) {

        RecyclerViewVeiculoEmplacadoAdapter myadapter = new RecyclerViewVeiculoEmplacadoAdapter(this, listaVeiculoEmplacado);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);
    }
}
