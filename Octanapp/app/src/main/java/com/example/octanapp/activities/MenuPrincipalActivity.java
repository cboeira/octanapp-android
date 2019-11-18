package com.example.octanapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.fragments.AlterarCadastroFragment;
import com.example.octanapp.fragments.FavoritosFragment;
import com.example.octanapp.fragments.MapaFragment;
import com.example.octanapp.fragments.VeiculosFragment;
import com.example.octanapp.model.Usuario;
import com.example.octanapp.model.VeiculoEmplacado;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuPrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    String urlVeiculoAtivo;
    StringRequest stringRequest;
    RequestQueue requestQueue;

    ProgressBar progressBar;
    View headerView;

    TextView nome;
    TextView email;
    Usuario usuario;
    VeiculoEmplacado veiculoEmplacado;
    Bundle parametrosUsuario;

    Boolean ativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Intent intentRecebedora = getIntent();
        parametrosUsuario = intentRecebedora.getExtras();
        requestQueue = Volley.newRequestQueue(this);

        usuario = new Usuario();
        veiculoEmplacado = new VeiculoEmplacado();

        if (parametrosUsuario != null) {
            usuario.setId(parametrosUsuario.getLong("id_usuario"));
            usuario.setNome(parametrosUsuario.getString("name"));
            usuario.setEmail(parametrosUsuario.getString("email"));
            usuario.setSenha(parametrosUsuario.getString("senha"));
            usuario.setData_nasc(parametrosUsuario.getString("data_nasc"));
            usuario.setGenero(parametrosUsuario.getString("genero"));
        }
        progressBar = findViewById(R.id.progressBar);
        urlVeiculoAtivo = "https://octanapp.herokuapp.com/verificaVeiculoAtivo.php?id_usuario="+usuario.getId();
        verificarVeiculoAtivo();



        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            MapaFragment mapaFragment = new MapaFragment();
            mapaFragment.setArguments(parametrosUsuario);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mapaFragment).commit();
            navigationView.setCheckedItem(R.id.nav_mapa);
        }

        headerView = navigationView.getHeaderView(0);
        nome = headerView.findViewById(R.id.nav_header_nome);
        nome.setText(usuario.getNome());

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_mapa:
                MapaFragment mapaFragment = new MapaFragment();
                mapaFragment.setArguments(parametrosUsuario);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mapaFragment).commit();
                break;
            case R.id.nav_cadastro:
                AlterarCadastroFragment alterarCadastroFragment = new AlterarCadastroFragment();
                alterarCadastroFragment.setArguments(parametrosUsuario);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, alterarCadastroFragment).commit();
                break;
            case R.id.nav_veiculos:
                VeiculosFragment veiculosFragment = new VeiculosFragment();
                veiculosFragment.setArguments(parametrosUsuario);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, veiculosFragment).commit();
                break;
            case R.id.nav_favoritos:
                FavoritosFragment favoritosFragment = new FavoritosFragment();
                favoritosFragment.setArguments(parametrosUsuario);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, favoritosFragment).commit();
                break;
            case R.id.nav_rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/")));
                break;
            case R.id.nav_logoff:
                Intent it = new Intent(MenuPrincipalActivity.this, LoginActivity.class);
                startActivity(it);
                Toast.makeText(getApplicationContext(), "Logoff realizado com sucesso.", Toast.LENGTH_LONG).show();
                finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void verificarVeiculoAtivo() {

        stringRequest = new StringRequest(Request.Method.GET, urlVeiculoAtivo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean erro = jsonObject.getBoolean("erro");
                            email = headerView.findViewById(R.id.nav_header_veiculo);
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
                                    email.setText(veiculoEmplacado.getMarca()+" "+veiculoEmplacado.getModelo());
                                }
                            } else {
                                email.setText("SEM VEICULO ATIVO");
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
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navegacao, menu);
        menu.findItem(R.id.action_cadastrar_veiculo).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}