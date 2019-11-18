package com.example.octanapp.popup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import com.example.octanapp.model.CombustivelPosto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopUpAvaliacaoCompleta extends Activity implements Spinner.OnItemSelectedListener {
    TextView txtEnviar;
    TextView txtCancelar;
    String urlCombustivelPosto;
    Spinner  spinner;
    private JSONArray result;
    ArrayList<String> combustiveis;
    TextView litragem;
    TextView kmtotal;

    RatingBar ratingBar;
    ProgressBar progressBar;

    //String urlAvaliaCompleta = "http://192.168.25.17/octanapp/realizaAvaliacaoCompleta.php";
    String urlAvaliaCompleta = "https://octanapp.herokuapp.com/realizaAvaliacaoCompleta.php";

    StringRequest stringRequest, stringRequestAv;
    RequestQueue requestQueue, requestQueueAv;
    String id_usuario = "";
    String id_posto = "";
    Bundle parametros;

    @Override
    protected void onCreate(Bundle savedInstanceCreate) {
        super.onCreate(savedInstanceCreate);

        Intent intentRecebedora = getIntent();
        parametros = intentRecebedora.getExtras();

        if (parametros != null) {
            id_usuario = parametros.getString("id_usuario");
            id_posto = parametros.getString("id_posto");
        }

        requestQueueAv = Volley.newRequestQueue(this);

        setContentView(R.layout.popwindow_avaliacaocompleta);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout((int)(width*.8),(int)(height*.5));

        progressBar = findViewById(R.id.progressBar_avCompleta);
        progressBar.setVisibility(View.GONE);

        ratingBar = findViewById(R.id.ratingBar_completa);
        ratingBar.setRating(5);

        litragem = findViewById(R.id.txtLitragem);
        kmtotal = findViewById(R.id.txtKmTotal);

        txtEnviar = findViewById(R.id.txtEnviar);


        txtCancelar = findViewById(R.id.txtCancelar);
        txtCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        urlCombustivelPosto = "https://octanapp.herokuapp.com/retornaCombustivelPosto.php?id_posto="+id_posto;

        combustiveis = new ArrayList<String>();
        spinner = findViewById(R.id.spinner_combustivel);

        spinner.setOnItemSelectedListener(this);

        getData();
        txtEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = true;
                if (ratingBar.getRating() < 1) {
                    Toast.makeText(getApplicationContext(), "Nota não pode ser menor que 1!", Toast.LENGTH_SHORT).show();
                    validado = false;
                }
                if (litragem.getText().length() == 0) {
                    litragem.setError("Favor informar a litragem.");
                    litragem.requestFocus();
                    validado = false;
                } else if (Integer.parseInt(litragem.getText().toString()) > 60) {
                    litragem.setError("Litragem muito alta.");
                    litragem.requestFocus();
                    validado = false;
                }
                if (kmtotal.getText().length() == 0) {
                    kmtotal.setError("Favor informar a quilometragem total do veículo.");
                    kmtotal.requestFocus();
                    validado = false;
                }
                if (validado) {
                    efetuaAvaliacaoCompleta();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        // Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(urlCombustivelPosto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                try {
                    j = new JSONObject(response);

                    result = j.getJSONArray("result");

                    getNomes(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getNomes(JSONArray j) {
        for (int i = 0; i<j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                combustiveis.add(json.getString("nome"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, combustiveis));
    }

    private void efetuaAvaliacaoCompleta() {
        stringRequestAv = new StringRequest(Request.Method.POST, urlAvaliaCompleta,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensagem = jsonObject.getString("mensagem");
                            Log.v("LogLogin", response);
                            progressBar.setVisibility(View.GONE);
                            boolean erro = jsonObject.getBoolean("erro");
                            if (erro) {
                                Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
                                finish();
                            }
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
                params.put("id_posto", id_posto);
                params.put("id_usuario", id_usuario);
                params.put("nota", String.valueOf(ratingBar.getRating()));
                params.put("nome_combustivel", spinner.getSelectedItem().toString());
                params.put("litragem", litragem.getText().toString());
                params.put("kmTotal", kmtotal.getText().toString());
                System.out.println(params);
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        stringRequestAv.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueueAv.add(stringRequestAv);
    }
}
