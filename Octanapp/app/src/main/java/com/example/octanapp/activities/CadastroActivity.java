package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.popup.PopUpTermos;
import com.example.octanapp.R;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CadastroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //String urlEfetuaCadastro = "http://192.168.25.17/octanapp/cadastraUsuario.php";
    String urlEfetuaCadastro = "https://octanapp.herokuapp.com/cadastraUsuario.php";


    StringRequest stringRequest;
    RequestQueue requestQueue;

    private static final String TAG = "CadastroActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListerner;

    EditText txtNome;
    EditText txtEmail;
    EditText txtSenha;
    EditText txtConfirmaSenha;
    TextView txtDataNascimento;
    Button btTermosDeUso;
    Button btFinalizaCadastro;
    Switch swTermos;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar = findViewById(R.id.toolbar_cadastro);
        toolbar.setTitle("Cadastro");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtNome = findViewById(R.id.txt_cadastro_nome);
        txtEmail = findViewById(R.id.txt_cadastro_email);
        txtSenha = findViewById(R.id.txt_cadastro_senha);
        txtConfirmaSenha = findViewById(R.id.txt_cadastro_confirmasenha);
        txtDataNascimento = findViewById(R.id.txt_cadastro_datanascimento);
        swTermos = findViewById(R.id.sw_termos_agreed);
        spinner = findViewById(R.id.spinner_genero);

        btTermosDeUso = findViewById(R.id.bt_cadastro_termos);
        btFinalizaCadastro = findViewById(R.id.bt_finaliza_cadastro);

        mDisplayDate = (TextView) findViewById(R.id.txt_cadastro_datanascimento);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CadastroActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListerner,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListerner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                String auxDay = "";
                String auxMonth = "";
                String date;
                if (month < 10) {
                    auxMonth = "0";
                }
                if (day < 10) {
                    auxDay = "0";
                }
                date = auxDay + day + "/" + auxMonth + month + "/" + year;

                mDisplayDate.setText(date);
            }
        };

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.generos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btTermosDeUso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUri = "https://octanapp.herokuapp.com/termosdeuso.html";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                startActivity(intent);
            }
        });

        final Pattern nomePattern = Pattern.compile("^[a-zA-Z]* .*");


        btFinalizaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = true;

                if (txtNome.getText().length() == 0) {
                    txtNome.setError("Campo nome completo obrigatório");
                    txtNome.requestFocus();
                    validado = false;
                }

                if(!nomePattern.matcher(txtNome.getText()).matches()) {
                    txtNome.setError("Campo nome completo obrigatório, não simples");
                    txtNome.requestFocus();
                    validado = false;
                }

                if (txtEmail.getText().length() == 0) {
                    txtEmail.setError("Campo e-mail obrigatório");
                    txtEmail.requestFocus();
                    validado = false;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
                    txtEmail.setError("Favor inserir um email válido");
                    txtEmail.requestFocus();
                    validado = false;
                }

                if (txtSenha.getText().length() == 0) {
                    txtSenha.setError("Campo senha obrigatório");
                    txtSenha.requestFocus();
                    validado = false;
                }

                if (txtSenha.getText().length() < 6) {
                    txtSenha.setError("Senha com no mínimo 6 caracteres");
                    txtSenha.requestFocus();
                    validado = false;
                }

                if (txtConfirmaSenha.getText().length() == 0) {
                    txtConfirmaSenha.setError("Campo confirma senha obrigatório");
                    txtConfirmaSenha.requestFocus();
                    validado = false;
                }

                if (!txtSenha.getText().toString().equals(txtConfirmaSenha.getText().toString())) {
                    txtConfirmaSenha.setError("Senhas diferentes");
                    txtConfirmaSenha.requestFocus();
                    validado = false;
                }

                //validar se a senha respeita letras e numeros e tamanho

                if (txtDataNascimento.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Favor inserir a data de nascimento", Toast.LENGTH_SHORT).show();
                    validado = false;
                }

              /* Teste para transformar a string data para armazenar no sql
                if (txtDataNascimento.getText().length() > 0) {
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate data = LocalDate.parse(txtDataNascimento.getText().toString(), formato);
                   // System.out.println(data);
                    Toast.makeText(getApplicationContext(), data.toString() , Toast.LENGTH_SHORT).show();
                    validado = false;
                }*/

                if (spinner.getSelectedItem().toString().equals("GÊNERO")) {
                    Toast.makeText(getApplicationContext(), "Favor inserir seu gênero", Toast.LENGTH_SHORT).show();
                    validado = false;
                }

                if (!swTermos.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Para realizar o cadastro, é necessário concordar com os termos de uso.", Toast.LENGTH_LONG).show();
                    validado = false;
                }

                if (validado) {
                  //  Toast.makeText(getApplicationContext(), "Cadastro efetuado com sucesso. Por favor, efetue login.", Toast.LENGTH_LONG).show();
                   // Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
                   // startActivity(it);
                    efetuarCadastro();
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

    private void efetuarCadastro() {
        stringRequest = new StringRequest(Request.Method.POST, urlEfetuaCadastro,
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
                                Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
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
                char[] letras = txtNome.getText().toString().toCharArray();
                for (int i = 0; i < letras.length; i++) {
                    if (i == 0 || !Character.isLetterOrDigit(letras[i-1])){
                        letras[i] = Character.toUpperCase(letras[i]);
                    }
                }
                String nomeCorreto = new String(letras);
                params.put("name", nomeCorreto);
                params.put("email", txtEmail.getText().toString());
                params.put("senha", txtSenha.getText().toString());
                params.put("genero", spinner.getSelectedItem().toString());
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate data = LocalDate.parse(txtDataNascimento.getText().toString(), formato);
                System.out.println(data.toString());
                params.put("data_nasc", data.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}
