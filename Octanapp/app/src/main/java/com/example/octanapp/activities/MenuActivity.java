package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.model.Usuario;
import com.example.octanapp.model.VeiculoEmplacado;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {
    String urlVeiculoAtivo;
    //String urlVeiculoAtivo = "http://192.168.25.17/octanapp/verificaVeiculoAtivo.php";
    //String urlVeiculoAtivo = "https://octanapp.herokuapp.com/verificaVeiculoAtivo.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    private GoogleMap mMap;

   // TextView txtNome;
    Button btCadastrarVeiculo;
    Button btLogoff;
    Button btAlterarCadastro;
    Button btPosto1;
    Button btAlterarVeiculo;
    Button btNovaInterface;

    ProgressBar progressBar;

    Usuario usuario;
    VeiculoEmplacado veiculoEmplacado;

    Boolean ativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intentRecebedora = getIntent();
        final Bundle parametrosUsuario = intentRecebedora.getExtras();
        requestQueue = Volley.newRequestQueue(this);

        progressBar = findViewById(R.id.progressBar);

        usuario = new Usuario();
        veiculoEmplacado = new VeiculoEmplacado();
        ativo = false;



        if (parametrosUsuario != null) {
            usuario.setId(parametrosUsuario.getLong("id_usuario"));
            usuario.setNome(parametrosUsuario.getString("name"));
            usuario.setEmail(parametrosUsuario.getString("email"));
            usuario.setSenha(parametrosUsuario.getString("senha"));
            usuario.setData_nasc(parametrosUsuario.getString("data_nasc"));
            usuario.setGenero(parametrosUsuario.getString("genero"));

           // Toast.makeText(this, usuario.getId() + usuario.getNome() + usuario.getEmail() + usuario.getSenha() + usuario.getData_nasc() + usuario.getGenero(), Toast.LENGTH_LONG).show();
        }

        //urlVeiculoAtivo = "http://192.168.25.17/octanapp/verificaVeiculoAtivo.php?id_usuario="+usuario.getId();
        urlVeiculoAtivo = "https://octanapp.herokuapp.com/verificaVeiculoAtivo.php?id_usuario="+usuario.getId();


        btPosto1 = findViewById(R.id.bt_posto1);
        btPosto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, PostoActivity.class);
                startActivity(it);
            }

        });

        verificarVeiculoAtivo();

        System.out.println(veiculoEmplacado.getMarca());

        Toolbar toolbar = findViewById(R.id.toolbar_cadastro_veiculo);
        toolbar.setTitle("Bem-vindo, " + usuario.getNome());
        setSupportActionBar(toolbar);


        btCadastrarVeiculo = findViewById(R.id.bt_cadastro_veiculo);
        btCadastrarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, CadastroVeiculoActivity.class);
                it.putExtras(parametrosUsuario);
                startActivity(it);
            }
        });

        btLogoff = findViewById(R.id.bt_logoff);
        btLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(it);
                Toast.makeText(getApplicationContext(), "Logoff realizado com sucesso.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btAlterarCadastro = findViewById(R.id.bt_alterar_cadastro);
        btAlterarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, AlterarCadastroActivity.class);
                it.putExtras(parametrosUsuario);
                startActivity(it);
            }
        });

        btAlterarVeiculo = findViewById(R.id.bt_alterar_veiculo);
        btAlterarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, AlterarVeiculoActivity.class);
                it.putExtras(parametrosUsuario);
                startActivity(it);
            }
        });

        btNovaInterface = findViewById(R.id.novainterface);
        btNovaInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, MenuPrincipalActivity.class);
                it.putExtras(parametrosUsuario);
                startActivity(it);
            }
        });

        System.out.println("no main: "+veiculoEmplacado.getMarca());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng curitiba = new LatLng(-25.4284, -49.2733);
        mMap.addMarker(new MarkerOptions().position(curitiba).title("Marker in Curitiba"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curitiba, 18));
    }

    private void verificarVeiculoAtivo() {

        stringRequest = new StringRequest(Request.Method.GET, urlVeiculoAtivo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean erro = jsonObject.getBoolean("erro");
                            TextView txtNome = findViewById(R.id.txt_alterar_nome);
                            if (!erro) {
                                ativo = true;
                                veiculoEmplacado.setAno(jsonObject.getInt("ano"));
                                veiculoEmplacado.setId_veiculo(jsonObject.getInt("id_veiculo"));
                                veiculoEmplacado.setId_usuario(usuario.getId());
                                veiculoEmplacado.setPlaca(jsonObject.getString("placa"));
                                veiculoEmplacado.setMarca(jsonObject.getString("marca"));
                                veiculoEmplacado.setModelo(jsonObject.getString("modelo"));
                                veiculoEmplacado.setKmTotal(jsonObject.getInt("kmTotal"));
                                System.out.println(veiculoEmplacado.getMarca());
                                Log.v("LogLogin", "entrou no ativo = true");

                                if (ativo) {
                                    txtNome.setText(veiculoEmplacado.getMarca()+" "+veiculoEmplacado.getModelo());
                                }
                            } else {
                                txtNome.setText("SEM VEICULO ATIVO");
                            }
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
                params.put("id_usuario", String.valueOf(usuario.getId()));
                return params;
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        System.out.println(veiculoEmplacado.getMarca());
        requestQueue.add(stringRequest);
    }


}
