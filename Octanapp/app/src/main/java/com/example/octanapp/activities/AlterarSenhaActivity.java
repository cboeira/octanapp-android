package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.model.Usuario;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AlterarSenhaActivity extends AppCompatActivity {

    Usuario usuario;
    Bundle parametrosUsuario;
    //String urlAlteraSenha = "http://192.168.25.17/octanapp/alteraSenha.php";
    String urlAlteraSenha = "https://octanapp.herokuapp.com/alteraSenha.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button btAlteraSenha;
    EditText txtSenhaAtual;
    EditText txtSenhaNova;
    EditText txtConfirmaSenhaNova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        requestQueue = Volley.newRequestQueue(this);

        Intent intentRecebedora = getIntent();
        parametrosUsuario = intentRecebedora.getExtras();
        usuario = new Usuario();
        if (parametrosUsuario != null) {
            usuario.setId(parametrosUsuario.getLong("id_usuario"));
            usuario.setNome(parametrosUsuario.getString("name"));
            usuario.setEmail(parametrosUsuario.getString("email"));
            usuario.setSenha(parametrosUsuario.getString("senha"));
            usuario.setData_nasc(parametrosUsuario.getString("data_nasc"));
            usuario.setGenero(parametrosUsuario.getString("genero"));
        }

        txtSenhaAtual = findViewById(R.id.txt_senha_atual);
        txtSenhaNova = findViewById(R.id.txt_senha_nova);
        txtConfirmaSenhaNova = findViewById(R.id.txt_senha_nova_conf);
        btAlteraSenha = findViewById(R.id.bt_alterar_senha);

        Toolbar toolbar = findViewById(R.id.toolbar_alterar_senha);
        toolbar.setTitle("Alterar senha");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        btAlteraSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = true;

                if (!txtSenhaAtual.getText().toString().equals(usuario.getSenha())) {
                    txtSenhaAtual.setError("Senha não confere");
                    txtSenhaAtual.requestFocus();
                    validado = false;
                }
                if (txtSenhaNova.getText().length() == 0) {
                    txtSenhaNova.setError("Campo senha obrigatório");
                    txtSenhaNova.requestFocus();
                    validado = false;
                }

                if (txtSenhaNova.getText().length() < 6) {
                    txtSenhaNova.setError("Senha com no mínimo 6 caracteres");
                    txtSenhaNova.requestFocus();
                    validado = false;
                }

                if (txtSenhaNova.getText().toString().equals(usuario.getSenha())) {
                    txtSenhaNova.setError("Senha nova não pode ser igual a antiga");
                    txtSenhaNova.requestFocus();
                    validado = false;
                }

                if (txtConfirmaSenhaNova.getText().length() == 0) {
                    txtConfirmaSenhaNova.setError("Campo confirma senha obrigatório");
                    txtConfirmaSenhaNova.requestFocus();
                    validado = false;
                }

                if (!txtSenhaNova.getText().toString().equals(txtConfirmaSenhaNova.getText().toString())) {
                    txtConfirmaSenhaNova.setError("Senhas diferentes");
                    txtConfirmaSenhaNova.requestFocus();
                    validado = false;
                }

                if (validado) {
                    Toast.makeText(getApplicationContext(), "Alterando senha...", Toast.LENGTH_LONG).show();
                    parametrosUsuario.putString("senha", txtSenhaNova.getText().toString());
                    alterarSenha();

                }
            }
        });
    }
    private void alterarSenha() {

        stringRequest = new StringRequest(Request.Method.POST, urlAlteraSenha,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensagem = jsonObject.getString("mensagem");
                            Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
                            Intent it = new Intent(AlterarSenhaActivity.this, MenuPrincipalActivity.class);
                            it.putExtras(parametrosUsuario);
                            startActivity(it);
                            finish();
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
                params.put("id_usuario", String.valueOf(usuario.getId()));
                params.put("senha_nova", txtSenhaNova.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
