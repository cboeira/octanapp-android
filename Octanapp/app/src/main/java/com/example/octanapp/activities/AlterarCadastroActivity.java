package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AlterarCadastroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    Usuario usuario;
    Bundle parametrosUsuario;

    private static final String TAG = "AlterarCadastroActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListerner;

    //String urlAlteraCadastro = "http://192.168.25.17/octanapp/alteraCadastro.php";
    String urlAlteraCadastro = "https://octanapp.herokuapp.com/alteraCadastro.php";

    //String urlExcluiConta = "http://192.168.25.17/octanapp/removeUsuario.php";
    String urlExcluiConta = "https://octanapp.herokuapp.com/removeUsuario.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;
    EditText txtNome;
    EditText txtEmail;
    Spinner spinner;
    Button btConfirmaAtualizacao;
    Button btAlteraSenha;
    Button btExcluiConta;
    AlertDialog dialog;
    AlertDialog.Builder builder;


    boolean alteraNome;
    boolean alteraEmail;
    boolean alteraData;
    boolean alteraGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_cadastro);

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

        Toolbar toolbar = findViewById(R.id.toolbar_alterar_cadastro);
        toolbar.setTitle("ALTERAR CADASTRO");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtNome = findViewById(R.id.txt_alterar_nome);
        txtEmail = findViewById(R.id.txt_alterar_email);

        spinner = findViewById(R.id.spinner_alterar_genero);

        txtNome.setText(usuario.getNome());
        txtEmail.setText(usuario.getEmail());

        mDisplayDate = (TextView) findViewById(R.id.txt_alterar_datanascimento);

        mDisplayDate.setText(usuario.getData_nasc());

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AlterarCadastroActivity.this,
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
        int generos = 0;
        if (usuario.getGenero().equals("Masculino")) generos=1;
        if (usuario.getGenero().equals("Feminino")) generos=2;
        if (usuario.getGenero().equals("Outro")) generos=3;
        spinner.setSelection(generos);
        spinner.setOnItemSelectedListener(this);

        final Pattern nomePattern = Pattern.compile("^[a-zA-Z]* .*");

        btConfirmaAtualizacao = findViewById(R.id.bt_confirma_atualizacao);
        btConfirmaAtualizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = true;
                alteraNome = false;
                alteraEmail = false;
                alteraData = false;
                alteraGenero = false;

                if (txtNome.getText().length() == 0) {
                    txtNome.setError("Campo nome completo obrigatório");
                    txtNome.requestFocus();
                    validado = false;
                }

                if(!txtNome.getText().toString().equals(usuario.getNome())) {
                    //txtNome.setError("Nome alterado");
                    //txtNome.requestFocus();
                    alteraNome = true;
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

                if(!txtEmail.getText().toString().equals(usuario.getEmail())) {
                    //txtEmail.setError("Email alterado");
                    //txtEmail.requestFocus();
                    alteraEmail = true;
                }

                if (mDisplayDate.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Favor inserir a data de nascimento", Toast.LENGTH_SHORT).show();
                    validado = false;
                }

                /*DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate data = LocalDate.parse(mDisplayDate.getText().toString(), formato);*/

                if (!mDisplayDate.getText().toString().equals(usuario.getData_nasc())){
                    //Toast.makeText(getApplicationContext(), "Data de Nascimento alterada", Toast.LENGTH_SHORT).show();
                    alteraData = true;
                }

                if (spinner.getSelectedItem().toString().equals("GÊNERO")) {
                    Toast.makeText(getApplicationContext(), "Favor inserir seu gênero", Toast.LENGTH_SHORT).show();
                    validado = false;
                }
                if (!spinner.getSelectedItem().toString().equals(usuario.getGenero())){
                    //Toast.makeText(getApplicationContext(), "Genero alterado", Toast.LENGTH_SHORT).show();
                    alteraGenero = true;
                }

                if (!alteraNome && !alteraEmail && !alteraGenero && !alteraData) {
                    Toast.makeText(getApplicationContext(), "Nenhum dado para alterar.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Alterando dados...", Toast.LENGTH_SHORT).show();
                    alterarCadastro();

                }

            }
        });

        btExcluiConta = findViewById(R.id.bt_excluir_conta);
        btExcluiConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder = new AlertDialog.Builder(AlterarCadastroActivity.this);
                builder.setTitle("Deseja realmente excluir sua conta?");
                builder.setPositiveButton("EXCLUIR CONTA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removerUsuario();
                        Toast.makeText(getApplicationContext(), "Excluindo conta...", Toast.LENGTH_SHORT).show();
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

        btAlteraSenha = findViewById(R.id.bt_alterar_senha);
        btAlteraSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AlterarCadastroActivity.this, AlterarSenhaActivity.class);
                it.putExtras(parametrosUsuario);
                startActivity(it);
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

    private void alterarCadastro() {
        stringRequest = new StringRequest(Request.Method.POST, urlAlteraCadastro,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Bundle parametrosUsuarioAlterado = new Bundle();
                            parametrosUsuarioAlterado.putLong("id_usuario", usuario.getId());
                            parametrosUsuarioAlterado.putString("senha", usuario.getSenha());

                            boolean alteraNome = jsonObject.getBoolean("nome_alterado");
                            boolean alteraEmail = jsonObject.getBoolean("email_alterado");
                            boolean alteraData = jsonObject.getBoolean("data_alterada");
                            boolean alteraGenero = jsonObject.getBoolean("genero_alterado");

                            if (alteraNome) {
                                parametrosUsuarioAlterado.putString("name", txtNome.getText().toString());
                            } else {
                                parametrosUsuarioAlterado.putString("name", usuario.getNome());
                            }
                            if (alteraEmail) {
                                parametrosUsuarioAlterado.putString("email", txtEmail.getText().toString());
                            } else {
                                parametrosUsuarioAlterado.putString("email", usuario.getEmail());
                            }
                            if (alteraData) {
                                parametrosUsuarioAlterado.putString("data_nasc", mDisplayDate.getText().toString());
                            } else {
                                parametrosUsuarioAlterado.putString("data_nasc", usuario.getData_nasc());
                            }
                            if (alteraData) {
                                parametrosUsuarioAlterado.putString("genero", spinner.getSelectedItem().toString());
                            } else {
                                parametrosUsuarioAlterado.putString("genero", usuario.getGenero());
                            }
                            Intent it = new Intent(AlterarCadastroActivity.this, MenuActivity.class);
                            it.putExtras(parametrosUsuarioAlterado);
                            startActivity(it);
                            //Toast.makeText(getApplicationContext(), "Bem vindo, " + usuario.getNome() + ".", Toast.LENGTH_SHORT).show();
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
                if (alteraNome) {
                    params.put("altera_nome", "true");
                    char[] letras = txtNome.getText().toString().toCharArray();
                    for (int i = 0; i < letras.length; i++) {
                        if (i == 0 || !Character.isLetterOrDigit(letras[i-1])){
                            letras[i] = Character.toUpperCase(letras[i]);
                        }
                    }
                    String nomeCorreto = new String(letras);
                    params.put("name", nomeCorreto);
                } else {
                    params.put("altera_nome", "false");
                }
                if (alteraEmail) {
                    params.put("altera_email", "true");
                    params.put("email", txtEmail.getText().toString());
                } else {
                    params.put("altera_email", "false");
                }
                if (alteraData) {
                    params.put("altera_data", "true");
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate data = LocalDate.parse(mDisplayDate.getText().toString(), formato);
                    System.out.println(data.toString());
                    params.put("data_nasc", data.toString());
                } else {
                    params.put("altera_data", "false");
                }
                if (alteraGenero) {
                    params.put("altera_genero", "true");
                    params.put("genero", spinner.getSelectedItem().toString());
                } else {
                    params.put("altera_genero", "false");
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void removerUsuario() {

        stringRequest = new StringRequest(Request.Method.POST, urlExcluiConta,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean veiculo_removido = jsonObject.getBoolean("veiculo_removido");
                            if (veiculo_removido) {
                                Toast.makeText(getApplicationContext(), "Veículos e conta removidos.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Conta removida.", Toast.LENGTH_LONG).show();
                            }
                            Intent it = new Intent(AlterarCadastroActivity.this, LoginActivity.class);
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
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
