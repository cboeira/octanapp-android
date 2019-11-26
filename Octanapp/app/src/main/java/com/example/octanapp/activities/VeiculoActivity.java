package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.model.VeiculoEmplacado;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VeiculoActivity extends AppCompatActivity {

    TextView txtMarca;
    TextView txtModelo;
    TextView txtPlaca;
    TextView txtAno;
    TextView txtKmTotal;
    ImageView imgMarca;
    Button btAtivar;
    Button btRemover;
    Button btHistorico;
    //String urlAtivaVeiculo = "http://192.168.25.17/octanapp/ativaVeiculo.php";
    String urlAtivaVeiculo = "https://octanapp.herokuapp.com/ativaVeiculo.php";
    //String urlRemoveVeiculo = "http://192.168.25.17/octanapp/removeVeiculo.php";
    String urlRemoveVeiculo = "https://octanapp.herokuapp.com/removeVeiculo.php";

    VeiculoEmplacado veiculoEmplacado;

    StringRequest stringRequest;
    RequestQueue requestQueue;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo);

        veiculoEmplacado = new VeiculoEmplacado();

        requestQueue = Volley.newRequestQueue(this);

        veiculoEmplacado.setMarca(getIntent().getExtras().getString("marca"));
        veiculoEmplacado.setModelo(getIntent().getExtras().getString("modelo"));
        veiculoEmplacado.setPlaca(getIntent().getExtras().getString("placa").toUpperCase());
        veiculoEmplacado.setAno(getIntent().getExtras().getInt("ano"));
        veiculoEmplacado.setKmTotal(getIntent().getExtras().getInt("kmTotal"));
        veiculoEmplacado.setId_veiculo(getIntent().getExtras().getInt("id_veiculo"));
        veiculoEmplacado.setId_usuario(getIntent().getExtras().getLong("id_usuario"));
        veiculoEmplacado.setAtivo(getIntent().getExtras().getInt("ativo"));

        Toolbar toolbar = findViewById(R.id.toolbar_veiculo);
        toolbar.setTitle(veiculoEmplacado.getModelo());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        imgMarca = findViewById(R.id.veiculo_logo_marca);
        txtMarca = findViewById(R.id.txt_marca_veiculo);
        txtModelo = findViewById(R.id.txt_modelo_veiculo);
        txtPlaca = findViewById(R.id.txt_placa_veiculo);
        txtAno = findViewById(R.id.txt_ano_veiculo);
        txtKmTotal = findViewById(R.id.txt_km_veiculo);
        btAtivar = findViewById(R.id.bt_ativar);

        String stringKmTotal = String.valueOf(veiculoEmplacado.getKmTotal())+" Km rodados";
        String marca = veiculoEmplacado.getMarca();
        if (marca.equals("AUDI")) {
            imgMarca.setImageResource(R.mipmap.ic_audi_foreground);
        } else if (marca.equals("BMW")) {
            imgMarca.setImageResource(R.mipmap.ic_bmw_foreground);
        } else if (marca.equals("CITROEN")) {
            imgMarca.setImageResource(R.mipmap.ic_citroen_foreground);
        } else if (marca.equals("FIAT")) {
            imgMarca.setImageResource(R.mipmap.ic_fiat_foreground);
        } else if (marca.equals("FORD")) {
            imgMarca.setImageResource(R.mipmap.ic_ford_foreground);
        } else if (marca.equals("CHEVROLET")) {
            imgMarca.setImageResource(R.mipmap.ic_chevrolet_foreground);
        } else if (marca.equals("HONDA")) {
            imgMarca.setImageResource(R.mipmap.ic_honda_foreground);
        } else if (marca.equals("HYUNDAI")) {
            imgMarca.setImageResource(R.mipmap.ic_hyundai_foreground);
        } else if (marca.equals("MERCEDES-BENZ")) {
            imgMarca.setImageResource(R.mipmap.ic_mercedes_foreground);
        } else if (marca.equals("NISSAN")) {
            imgMarca.setImageResource(R.mipmap.ic_nissan_foreground);
        } else if (marca.equals("PEUGEOT")) {
            imgMarca.setImageResource(R.mipmap.ic_peugeot_foreground);
        } else if (marca.equals("RENAULT")) {
            imgMarca.setImageResource(R.mipmap.ic_renault_foreground);
        } else if (marca.equals("TOYOTA")) {
            imgMarca.setImageResource(R.mipmap.ic_toyota_foreground);
        } else if (marca.equals("VOLKSWAGEN")) {
            imgMarca.setImageResource(R.mipmap.ic_volkswagen_foreground);
        }

        txtMarca.setText(veiculoEmplacado.getMarca());
        txtModelo.setText(veiculoEmplacado.getModelo());
        txtPlaca.setText(veiculoEmplacado.getPlaca());
        txtAno.setText(String.valueOf(veiculoEmplacado.getAno()));
        txtKmTotal.setText(stringKmTotal);

        if (veiculoEmplacado.getAtivo() == 1) {
            btAtivar.setText("ATIVO");
        } else {
            btAtivar.setText("ATIVAR");
            btAtivar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ativarVeiculo();
                }
            });
        }

        btHistorico = findViewById(R.id.bt_historico);
        btHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(VeiculoActivity.this, HistoricoActivity.class);
                it.putExtra("placa", veiculoEmplacado.getPlaca());
                startActivity(it);
            }
        });

        btRemover = findViewById(R.id.bt_remover_veiculo);
        btRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(VeiculoActivity.this);
                if (veiculoEmplacado.getAtivo() == 1) {
                    builder.setTitle("Deseja realmente remover este veículo ativo? Será necessário ativar outro veículo.");
                } else {
                    builder.setTitle("Deseja realmente remover este veículo?");
                }
                builder.setPositiveButton("REMOVER VEÍCULO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removerVeiculo();
                        Toast.makeText(getApplicationContext(), "Removendo veículo...", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });



    }

    private void ativarVeiculo() {
        stringRequest = new StringRequest(Request.Method.POST, urlAtivaVeiculo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensagem = jsonObject.getString("mensagem");
                            Log.v("LogLogin", response);
                            Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
                            setResult(10001);
                            finish();
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
                params.put("placa", veiculoEmplacado.getPlaca());
                params.put("id_usuario", String.valueOf(veiculoEmplacado.getId_usuario()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void removerVeiculo() {
        stringRequest = new StringRequest(Request.Method.POST, urlRemoveVeiculo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensagem = jsonObject.getString("mensagem");
                            Log.v("LogLogin", response);
                            Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
                            setResult(10001);
                            finish();
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
                params.put("placa", veiculoEmplacado.getPlaca());
                params.put("id_usuario", String.valueOf(veiculoEmplacado.getId_usuario()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}
