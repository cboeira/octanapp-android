package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CadastroVeiculoActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    //String urlEfetuaCadastroVeiculo = "http://192.168.25.17/octanapp/cadastraVeiculo.php";
    String urlEfetuaCadastroVeiculo = "https://octanapp.herokuapp.com/cadastraVeiculo.php";
    //String urlRetornaMarcas = "http://192.168.25.17/octanapp/retornaMarcas.php";
    String urlRetornaMarcas = "https://octanapp.herokuapp.com/retornaMarcas.php";
    //String urlRetornaModelos;
    //String urlRetornaModelos = "http://192.168.25.17/octanapp/retornaModelos.php?marca=";
    String urlRetornaModelos = "https://octanapp.herokuapp.com/retornaModelos.php?marca=";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    EditText txtMarca;
    EditText txtModelo;
    EditText txtPlaca;
    EditText txtAno;
    EditText txtKm;
    Button btConfirmaCadastro;
    Usuario usuario;
    Bundle parametrosUsuario;

    Spinner spinner, spinnerModelos;
    private JSONArray result, resultModelos;
    ArrayList<String> marcas, modelos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_veiculo);

        Intent intentRecebedora = getIntent();
        parametrosUsuario = intentRecebedora.getExtras();


        requestQueue = Volley.newRequestQueue(this);
        txtPlaca = findViewById(R.id.cadastro_veiculo_placa);
        txtAno = findViewById(R.id.cadastro_veiculo_ano);
        txtKm = findViewById(R.id.cadastro_veiculo_km);
        btConfirmaCadastro = findViewById(R.id.bt_confirmar_cadastro_veiculo);

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

        marcas = new ArrayList<String>();


        spinner = (Spinner) findViewById(R.id.spinner_marca);
        spinnerModelos = (Spinner) findViewById(R.id.spinner_modelo);

        spinner.setOnItemSelectedListener(this);
        spinnerModelos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String modelo = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), "modelo selecionado: "+modelo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getDataMarcas();

        Toolbar toolbar = findViewById(R.id.toolbar_cadastro_veiculo);
        toolbar.setTitle("Cadastrar veículo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Pattern placaPattern = Pattern.compile("^[a-zA-Z][a-zA-Z][a-zA-Z][0-9].[0-9][0-9]");

        btConfirmaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = true;

                if (txtPlaca.getText().length() != 7) {
                    txtPlaca.setError("Favor uma placa no formato: AAA0000");
                    txtPlaca.requestFocus();
                    validado = false;
                }
                if(!placaPattern.matcher(txtPlaca.getText()).matches()) {
                    txtPlaca.setError("Favor uma placa válida no formato: AAA0000");
                    txtPlaca.requestFocus();
                    validado = false;
                }
                if (txtAno.getText().length() != 4) {
                    txtAno.setError("Favor inserir o ano no formato: AAAA");
                    txtAno.requestFocus();
                    validado = false;
                }
                Calendar cal = Calendar.getInstance();
                int anoModelo = cal.get(Calendar.YEAR)+1;
                if (Integer.valueOf(txtAno.getText().toString()) > anoModelo) {
                    txtAno.setError("O ano não pode ser maior que "+anoModelo);
                    txtAno.requestFocus();
                    validado = false;
                }
                if (txtKm.getText().length() == 0) {
                    txtKm.setError("Favor inserir a quilometragem");
                    txtKm.requestFocus();
                    validado = false;
                }

                if(validado) {
                    Toast.makeText(getApplicationContext(), "Efetuando cadastro...", Toast.LENGTH_LONG).show();
                    efetuarCadastroVeiculo();
                }
            }
        });
    }
    private void efetuarCadastroVeiculo() {
        stringRequest = new StringRequest(Request.Method.POST, urlEfetuaCadastroVeiculo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isError = jsonObject.getBoolean("erro");

                            if (isError) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_LONG).show();
                            } else {
                                String mensagem = jsonObject.getString("mensagem");
                                Intent it = new Intent(CadastroVeiculoActivity.this, MenuPrincipalActivity.class);
                                it.putExtras(parametrosUsuario);
                                startActivity(it);
                                Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
                                finish();
                            }
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
                params.put("marca", spinner.getSelectedItem().toString());
                params.put("modelo", spinnerModelos.getSelectedItem().toString());
                params.put("placa", txtPlaca.getText().toString().toUpperCase());
                params.put("ano", txtAno.getText().toString());
                params.put("kmTotal", txtKm.getText().toString());
                params.put("id_usuario", String.valueOf(usuario.getId()));
               /* params.put("genero", spinner.getSelectedItem().toString());
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate data = LocalDate.parse(txtDataNascimento.getText().toString(), formato);
                System.out.println(data.toString());
                params.put("data_nasc", data.toString());*/
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String marca = adapterView.getItemAtPosition(i).toString();

        Toast.makeText(adapterView.getContext(), "marca selecionada: "+marca, Toast.LENGTH_SHORT).show();
        modelos = new ArrayList<String>();
        getDataModelos(marca);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getDataMarcas() {
        StringRequest stringRequest = new StringRequest(urlRetornaMarcas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                try {
                    j = new JSONObject(response);

                    result = j.getJSONArray("result");

                    getMarcas(result);
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

    private void getMarcas(JSONArray j) {
        for (int i = 0; i<j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                marcas.add(json.getString("marca"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, marcas));
    }

    private void getDataModelos(String marca) {
        StringRequest stringRequest = new StringRequest(urlRetornaModelos+marca, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                try {
                    j = new JSONObject(response);

                    resultModelos = j.getJSONArray("result");

                    getModelos(resultModelos);
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

    private void getModelos(JSONArray j) {
        for (int i = 0; i<j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                modelos.add(json.getString("modelo"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(modelos);
        spinnerModelos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, modelos));
    }
}
