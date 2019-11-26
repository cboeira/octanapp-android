package com.example.octanapp.popup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.activities.PostoActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PopUpAvaliacaoSimples extends Activity {
    TextView txtEnviar;
    TextView txtCancelar;
    Button btAvaliacaoCompleta;

    RatingBar ratingBar;
    ProgressBar progressBar;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    //String urlAvaliaSimples = "http://192.168.25.17/octanapp/realizaAvaliacaoSimples.php";
    String urlAvaliaSimples = "https://octanapp.herokuapp.com/realizaAvaliacaoSimples.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;
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

        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.popwindow_avaliacaosimples);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout((int)(width*.9),(int)(height*.4));


        progressBar = findViewById(R.id.progressBar_avSimples);
        progressBar.setVisibility(View.GONE);

        ratingBar = findViewById(R.id.ratingBar_simples);
        ratingBar.setRating(5);

        txtEnviar = findViewById(R.id.txtEnviar);
        txtEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = true;
                if (ratingBar.getRating() < 1) {
                    Toast.makeText(getApplicationContext(), "Nota não pode ser menor que 1!", Toast.LENGTH_SHORT).show();
                    validado = false;
                }
                if (validado) {
                    efetuaAvaliacaoSimples();
                }

            }
        });

        txtCancelar = findViewById(R.id.txtCancelar);
        txtCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(10);
                finish();
       /*         Intent it = new Intent(PopUpAvaliacaoSimples.this, PostoActivity.class);
                it.putExtra("id_usuario", id_usuario);
                it.putExtra("id_posto", id_posto);
                startActivity(it);*/
            }
        });

        btAvaliacaoCompleta = findViewById(R.id.bt_avaliacaoCompleta);
        btAvaliacaoCompleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent it = new Intent(PopUpAvaliacaoSimples.this, PopUpAvaliacaoCompleta.class );
                //it.putExtras(parametros);
                setResult(3);
                finish();
                //startActivity(it);

            }
        });
    }
    private void efetuaAvaliacaoSimples() {
        stringRequest = new StringRequest(Request.Method.POST, urlAvaliaSimples,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensagem = jsonObject.getString("mensagem");
                            Log.v("LogLogin", response);
                            progressBar.setVisibility(View.GONE);
                            builder = new AlertDialog.Builder(PopUpAvaliacaoSimples.this);
                            builder.setTitle("Avaliação realizada com sucesso!");
                            builder.setPositiveButton("VOLTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setResult(2);
                                    finish();
                                    Intent it = new Intent(PopUpAvaliacaoSimples.this, PostoActivity.class);
                                    it.putExtra("id_usuario", id_usuario);
                                    it.putExtra("id_posto", id_posto);
                                    startActivity(it);
                                }
                            });
                            dialog = builder.create();
                            dialog.show();
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
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        requestQueue.add(stringRequest);

    }


}
