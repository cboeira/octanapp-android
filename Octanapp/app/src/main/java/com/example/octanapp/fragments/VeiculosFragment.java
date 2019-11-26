package com.example.octanapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.activities.AlterarVeiculoActivity;
import com.example.octanapp.activities.CadastroVeiculoActivity;
import com.example.octanapp.activities.MenuActivity;
import com.example.octanapp.activities.MenuPrincipalActivity;
import com.example.octanapp.adapters.RecyclerViewAutonomiaAdapter;
import com.example.octanapp.adapters.RecyclerViewVeiculoEmplacadoAdapter;
import com.example.octanapp.model.AutonomiaPosto;
import com.example.octanapp.model.Usuario;
import com.example.octanapp.model.VeiculoEmplacado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VeiculosFragment extends Fragment {
    View view;

    RequestQueue requestQueue;

    Usuario usuario;
    String urlRetornaVeiculosEmplacados;
    Bundle parametrosUsuario;
    FragmentTransaction ft;

    private JsonArrayRequest request;
    private List<VeiculoEmplacado> listaVeiculoEmplacado;
    private RecyclerView recyclerView;

    public VeiculosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_veiculos, container, false);

        listaVeiculoEmplacado = new ArrayList<>();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Veículos");
        requestQueue = Volley.newRequestQueue(getContext());
        parametrosUsuario = new Bundle();
        usuario = new Usuario();

        ((MenuPrincipalActivity)getActivity()).verificarVeiculoAtivo();


        if (getArguments() != null) {
            usuario.setId(getArguments().getLong("id_usuario"));
            usuario.setNome(getArguments().getString("name"));
            usuario.setEmail(getArguments().getString("email"));
            usuario.setSenha(getArguments().getString("senha"));
            usuario.setData_nasc(getArguments().getString("data_nasc"));
            usuario.setGenero(getArguments().getString("genero"));
            parametrosUsuario.putLong("id_usuario", usuario.getId());
            parametrosUsuario.putString("name", usuario.getNome());
            parametrosUsuario.putString("email", usuario.getEmail());
            parametrosUsuario.putString("senha", usuario.getSenha());
            parametrosUsuario.putString("data_nasc", usuario.getData_nasc());
            parametrosUsuario.putString("genero", usuario.getGenero());
        }

        urlRetornaVeiculosEmplacados = "https://octanapp.herokuapp.com/retornaVeiculosEmplacados.php?id_usuario="+usuario.getId();
        //urlRetornaVeiculosEmplacados = "http://192.168.25.17/octanapp/retornaVeiculosEmplacados.php?id_usuario="+usuario.getId();


        recyclerView = view.findViewById(R.id.recyclerView_id);

        jsonrequest();

        return view;
    }

    private void jsonrequest() {
        request = new JsonArrayRequest(urlRetornaVeiculosEmplacados, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        VeiculoEmplacado veiculoEmplacado = new VeiculoEmplacado();
                        veiculoEmplacado.setPlaca(jsonObject.getString("placa"));
                        veiculoEmplacado.setKmTotal(jsonObject.getInt("kmTotal"));
                        veiculoEmplacado.setAno(jsonObject.getInt("ano"));
                        veiculoEmplacado.setId_veiculo(jsonObject.getInt("id_veiculo"));
                        veiculoEmplacado.setModelo(jsonObject.getString("modelo"));
                        veiculoEmplacado.setMarca(jsonObject.getString("marca"));
                        veiculoEmplacado.setAtivo(jsonObject.getInt("ativo"));
                        veiculoEmplacado.setId_usuario(usuario.getId());

                        listaVeiculoEmplacado.add(veiculoEmplacado);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setuprecyclerview(listaVeiculoEmplacado);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<VeiculoEmplacado> listaVeiculoEmplacado) {

        RecyclerViewVeiculoEmplacadoAdapter myadapter = new RecyclerViewVeiculoEmplacadoAdapter(getContext(), listaVeiculoEmplacado);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(myadapter);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_navegacao, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cadastrar_veiculo) {
            Intent it = new Intent(getContext(), CadastroVeiculoActivity.class);
            it.putExtras(parametrosUsuario);
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK))
            // recreate your fragment here
            ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}
