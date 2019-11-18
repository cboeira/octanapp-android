package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.model.Posto;
import com.example.octanapp.popup.PopUpAvaliacaoSimples;
import com.example.octanapp.R;
import com.example.octanapp.adapters.ViewPagerAdapter;
import com.example.octanapp.fragments.AutonomiaFragment;
import com.example.octanapp.fragments.InformacoesFragment;
import com.example.octanapp.fragments.PrecosFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PostoActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImageButton btTracarRota;
    private ImageButton btAvaliar;
    private ImageButton btSalvar;

    private TextView txtSalvo;

    String urlInicioPosto, urlAdicionaFavorito, urlRemoveFavorito;
    StringRequest stringRequest;
    RequestQueue requestQueue;

    String id_usuario;
    String id_posto;
    Posto posto = new Posto();
    ProgressBar progressBar;
    Toolbar toolbar;
    String coordenadas;
    boolean favorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posto);
        Intent intentRecebedora = getIntent();

        id_usuario = intentRecebedora.getStringExtra("id_usuario");
        id_posto = intentRecebedora.getStringExtra("id_posto");

        progressBar = findViewById(R.id.progressBar_Posto);
        requestQueue = Volley.newRequestQueue(this);

        //urlInicioPosto = "http://192.168.25.17/octanapp/retornaInicioPosto.php?id_posto="+id_posto+"&id_usuario="+id_usuario;
        //urlAdicionaFavorito = "http://192.168.25.17/octanapp/adicionaFavorito.php";
        //urlRemoveFavorito = "http://192.168.25.17/octanapp/removeFavorito.php";

        urlInicioPosto = "https://octanapp.herokuapp.com/retornaInicioPosto.php?id_posto="+id_posto+"&id_usuario="+id_usuario;
        urlAdicionaFavorito = "https://octanapp.herokuapp.com/adicionaFavorito.php";
        urlRemoveFavorito = "https://octanapp.herokuapp.com/removeFavorito.php";
       // posto = new Posto();


        toolbar = findViewById(R.id.toolbar_posto);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        txtSalvo = findViewById(R.id.posto_salvar);

        iniciarPosto();

        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 3);

        final Bundle parametros = new Bundle();
        parametros.putString("id_posto", id_posto);
        parametros.putString("id_usuario", id_usuario);


        InformacoesFragment informacoesFragment = new InformacoesFragment();
        informacoesFragment.setArguments(parametros);
        AutonomiaFragment autonomiaFragment = new AutonomiaFragment();
        autonomiaFragment.setArguments(parametros);
        PrecosFragment precosFragment = new PrecosFragment();
        precosFragment.setArguments(parametros);
        adapter.AddFragment(autonomiaFragment,"AUTONOMIA");
        adapter.AddFragment(precosFragment, "PREÇOS");
        adapter.AddFragment(informacoesFragment, "INFORMAÇÕES");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        btTracarRota = findViewById(R.id.bt_tracarRota);
        btTracarRota.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String strUri = "https://www.google.com/maps/dir//"+coordenadas+"/";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                startActivity(intent);
            }
        });

        btAvaliar = findViewById(R.id.bt_avaliacaosimples);
        btAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(PostoActivity.this, PopUpAvaliacaoSimples.class);
                it.putExtras(parametros);
                startActivity(it);
            }
        });


        btSalvar = findViewById(R.id.bt_favorito);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!favorito) {
                    //Toast.makeText(getApplicationContext(), "Posto salvo nos favoritos.", Toast.LENGTH_SHORT).show();
                    favoritarPosto();
                    txtSalvo.setText("SALVO");
                    favorito = true;
                } else {
                   // Toast.makeText(getApplicationContext(), "Posto removido dos favoritos.", Toast.LENGTH_SHORT).show();
                    desfavoritarPosto();
                    txtSalvo.setText("SALVAR");
                    favorito = false;
                }
            }
        });
    }

    private void iniciarPosto() {

        stringRequest = new StringRequest(Request.Method.GET, urlInicioPosto,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            posto.setId_posto(jsonObject.getInt("id_posto"));
                            posto.setNomeFantasia(jsonObject.getString("nomeFantasia"));
                            posto.setBandeira(jsonObject.getString("bandeira"));
                            posto.setNotaMedia(jsonObject.getDouble("notaMedia"));
                            posto.setNumAvaliacoes(jsonObject.getInt("numeroAvaliacoes"));
                            posto.setCoordenadas(jsonObject.getString("coordenadas"));
                            coordenadas = posto.getCoordenadas();
                            favorito = jsonObject.getBoolean("favorito");
                            if (favorito) {
                                txtSalvo.setText("SALVO");
                            } else {
                                txtSalvo.setText("SALVAR");
                            }
                            String nota;
                            if (posto.getNotaMedia() == 0) {
                                nota = "Sem nota";
                            } else {
                                nota = String.valueOf(posto.getNotaMedia());
                            }
                            String titulo = posto.getNomeFantasia()+" ("+nota+")";
                            toolbar.setTitle(titulo);
                            String numAv = String.valueOf(posto.getNumAvaliacoes());
                            String subTitulo = numAv+" avaliações";
                            toolbar.setSubtitle(subTitulo);
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
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        requestQueue.add(stringRequest);
    }

    private void favoritarPosto() {
        stringRequest = new StringRequest(Request.Method.POST, urlAdicionaFavorito,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensagem = jsonObject.getString("mensagem");
                            Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
                            Log.v("LogLogin", response);
                        } catch (Exception e) {
                            Log.v("LogLogin", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                        Log.e("LogLogin", error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_posto", id_posto);
                params.put("id_usuario", id_usuario);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void desfavoritarPosto() {
        stringRequest = new StringRequest(Request.Method.POST, urlRemoveFavorito,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensagem = jsonObject.getString("mensagem");
                            Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
                            Log.v("LogLogin", response);
                        } catch (Exception e) {
                            Log.v("LogLogin", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                        Log.e("LogLogin", error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_posto", id_posto);
                params.put("id_usuario", id_usuario);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
