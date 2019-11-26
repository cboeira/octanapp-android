package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
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

public class LoginActivity extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    //String urlValidaLogin = "http://192.168.25.17/octanapp/validaLogin.php";
    String urlValidaLogin = "https://octanapp.herokuapp.com/validaLogin.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;
    Usuario usuario;

    Button btInscrever;
    Button btEntrar;
    Button btRedefinir;
    EditText txtEmail;
    EditText txtSenha;
    Switch swLembrar;
    ProgressDialog progressDialog;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        txtEmail = findViewById(R.id.txt_login_email);
        txtSenha = findViewById(R.id.txt_login_senha);
        btEntrar = findViewById(R.id.bt_entrar);
        btInscrever = findViewById(R.id.bt_inscrever);
        btRedefinir = findViewById(R.id.bt_redefinir);
        swLembrar = findViewById(R.id.sw_login_lembrar);
        progressDialog = new ProgressDialog(LoginActivity.this);

        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            swLembrar.setChecked(true);
        } else {
            swLembrar.setChecked(false);
        }

        txtEmail.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        txtSenha.setText(sharedPreferences.getString(KEY_PASS, ""));

        txtEmail.addTextChangedListener(this);
        txtSenha.addTextChangedListener(this);
        swLembrar.setOnCheckedChangeListener(this);

        usuario = new Usuario();

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = true;

                if (txtEmail.getText().length() == 0) {
                    txtEmail.setError("Campo e-mail obrigatório");
                    txtEmail.requestFocus();
                    validado = false;
                }

                if (txtSenha.getText().length() == 0) {
                    txtSenha.setError("Campo senha obrigatório");
                    txtSenha.requestFocus();
                    validado = false;
                }

                if (validado) {
                    //Toast.makeText(getApplicationContext(), "Validando dados...", Toast.LENGTH_LONG).show();
                    validarLogin();
                   // Intent it = new Intent(LoginActivity.this, MenuActivity.class);
                   // startActivity(it);
                }
            }
        });

        btInscrever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, CadastroActivity.class);
               startActivity(it);
            }
        });

        btRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, RedefinirSenhaActivity.class);
                startActivity(it);
            }
        });


    }

    private void validarLogin() {
        stringRequest = new StringRequest(Request.Method.POST, urlValidaLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isError = jsonObject.getBoolean("erro");

                            if (isError) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_LONG).show();
                            } else {
                                usuario.setId(jsonObject.getLong("id_usuario"));
                                usuario.setNome(jsonObject.getString("name"));
                                usuario.setEmail(jsonObject.getString("email"));
                                usuario.setSenha(jsonObject.getString("senha"));
                                usuario.setData_nasc(jsonObject.getString("data_nasc"));
                                usuario.setGenero(jsonObject.getString("genero"));
                                Bundle parametrosUsuario = new Bundle();
                                parametrosUsuario.putLong("id_usuario", usuario.getId());
                                parametrosUsuario.putString("name", usuario.getNome());
                                parametrosUsuario.putString("email", usuario.getEmail());
                                parametrosUsuario.putString("senha", usuario.getSenha());
                                parametrosUsuario.putString("data_nasc", usuario.getData_nasc());
                                parametrosUsuario.putString("genero", usuario.getGenero());

                                /*SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                                SharedPreferences.Editor ed = preferences.edit();
                                ed.putLong("id_usuario", usuario.getId());
                                ed.putString("name", usuario.getNome());
                                ed.putString("email", usuario.getEmail());
                                ed.putString("senha", usuario.getSenha());
                                ed.putString("data_nasc", usuario.getData_nasc());
                                ed.putString("genero", usuario.getGenero());
                                ed.apply();*/
                                progressDialog.dismiss();
                                Intent it = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
                                it.putExtras(parametrosUsuario);
                                startActivity(it);
                                Toast.makeText(getApplicationContext(), "Bem vindo, " + usuario.getNome() + ".", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            //Log.v("LogLogin", response);
                        } catch (Exception e) {
                            Log.v("LogLogin", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                        progressDialog.dismiss();
                        //Log.e("LogLogin", error.getMessage());
                        Toast.makeText(getApplicationContext(), "Falha de comunicação com o servidor. Tente novamente mais tarde", Toast.LENGTH_SHORT).show();

                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", txtEmail.getText().toString());
                params.put("senha", txtSenha.getText().toString());
                return params;
            }
        };
        progressDialog.setMessage("Validando dados...");
        progressDialog.show();
        requestQueue.add(stringRequest);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }

    private void managePrefs() {
        if(swLembrar.isChecked()) {
            editor.putString(KEY_USERNAME, txtEmail.getText().toString().trim());
            editor.putString(KEY_PASS, txtSenha.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);
            editor.remove(KEY_USERNAME);
            editor.apply();
        }
    }
}

