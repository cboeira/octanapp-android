package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.model.Estabelecimento;
import com.example.octanapp.popup.PopUpAvaliacaoCompleta;
import com.example.octanapp.popup.PopUpAvaliacaoEstab;
import com.example.octanapp.popup.PopUpAvaliacaoSimples;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EstabelecimentoActivity extends AppCompatActivity {

    String id_usuario, cnpj;
    Toolbar toolbar;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String urlInicioEstab;
    Estabelecimento estabelecimento = new Estabelecimento();
    ProgressDialog progressDialog;
    TextView tvRamo, tvTelefone, tvEndereco, tvInfo, tvNumAv, tvNota;
    String coordenadas;
    ImageButton btTracarRota, btAvaliar, btPromocoes;
    Bundle parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);
        Intent intentRecebedora = getIntent();

        id_usuario = intentRecebedora.getStringExtra("id_usuario");
        cnpj = intentRecebedora.getStringExtra("cnpj");
        //urlInicioEstab = "http://192.168.25.17/octanapp/retornaInicioEstabelecimento.php?cnpj="+cnpj;
        urlInicioEstab = "https://octanapp.herokuapp.com/retornaInicioEstabelecimento.php?cnpj="+cnpj;

        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(EstabelecimentoActivity.this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        tvRamo = findViewById(R.id.estab_ramo);
        tvTelefone = findViewById(R.id.estab_telefone);
        tvEndereco = findViewById(R.id.estab_endereco);
        tvInfo = findViewById(R.id.estab_info);
        tvNumAv = findViewById(R.id.estab_numAv);
        tvNota = findViewById(R.id.estab_nota);

        iniciarEstabelecimento();

        btTracarRota = findViewById(R.id.bt_tracarRota);
        btTracarRota.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String strUri = "https://www.google.com/maps/dir//"+coordenadas+"/";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                startActivity(intent);
            }
        });

        parametros = new Bundle();
        parametros.putString("cnpj", cnpj);
        parametros.putString("id_usuario", id_usuario);

        btAvaliar = findViewById(R.id.bt_avaliacaosimples);
        btAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(EstabelecimentoActivity.this, PopUpAvaliacaoEstab.class);
                it.putExtras(parametros);
                startActivityForResult(it, 2);
            }
        });

        btPromocoes = findViewById(R.id.bt_promocoes);
        btPromocoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUri = "https://www.google.com/";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                startActivity(intent);
            }
        });
    }

    private void iniciarEstabelecimento() {

        stringRequest = new StringRequest(Request.Method.GET, urlInicioEstab,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            estabelecimento.setCnpj(cnpj);
                            estabelecimento.setNumAvaliacoes(jsonObject.getInt("numeroAvaliacoes"));
                            estabelecimento.setNota(jsonObject.getDouble("notaMedia"));
                            estabelecimento.setRazaoSocial(jsonObject.getString("razaoSocial"));
                            estabelecimento.setEndereco(jsonObject.getString("endereco"));
                            estabelecimento.setTelefone(jsonObject.getString("telefone"));
                            estabelecimento.setRamo(jsonObject.getString("ramo"));
                            estabelecimento.setInformacoes(jsonObject.getString("informacoes"));
                            coordenadas = jsonObject.getString("coordenadas");
                            String nota;
                            if (estabelecimento.getNota() == 0) {
                                nota = "--";
                            } else {
                                nota = String.valueOf(estabelecimento.getNota());
                            };
                            toolbar.setTitle(estabelecimento.getRazaoSocial());
                            tvRamo.setText(estabelecimento.getRamo());
                            tvEndereco.setText(estabelecimento.getEndereco());
                            tvTelefone.setText(estabelecimento.getTelefone());
                            tvInfo.setText(estabelecimento.getInformacoes());
                            tvNumAv.setText(String.valueOf(estabelecimento.getNumAvaliacoes()));
                            tvNota.setText(nota);
                            Log.v("LogLogin", response);
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            Log.v("LogLogin", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("LogLogin", error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        progressDialog.setMessage("Recebendo informações do estabelecimento...");
        progressDialog.show();
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==2 && resultCode ==2) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
