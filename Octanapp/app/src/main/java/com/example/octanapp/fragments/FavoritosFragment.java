package com.example.octanapp.fragments;

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
import com.example.octanapp.adapters.RecyclerViewAutonomiaAdapter;
import com.example.octanapp.adapters.RecyclerViewFavoritosAdapter;
import com.example.octanapp.adapters.RecyclerViewVeiculoEmplacadoAdapter;
import com.example.octanapp.model.AutonomiaPosto;
import com.example.octanapp.model.Posto;
import com.example.octanapp.model.PostoFavorito;
import com.example.octanapp.model.Usuario;
import com.example.octanapp.model.VeiculoEmplacado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoritosFragment extends Fragment {
    View view;

    RequestQueue requestQueue;

    Usuario usuario;
    String urlRetornaFavoritos;
    Bundle parametrosUsuario;

    private JsonArrayRequest request;
    private List<PostoFavorito> listaPostoFavorito;
    private RecyclerView recyclerView;

    public FavoritosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_veiculos, container, false);

        listaPostoFavorito = new ArrayList<>();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Favoritos");
        requestQueue = Volley.newRequestQueue(getContext());
        parametrosUsuario = new Bundle();
        usuario = new Usuario();

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

        urlRetornaFavoritos = "https://octanapp.herokuapp.com/retornaPostosFavoritos.php?id_usuario="+usuario.getId();
        //urlRetornaFavoritos = "http://192.168.25.17/octanapp/retornaPostosFavoritos.php?id_usuario="+usuario.getId();


        recyclerView = view.findViewById(R.id.recyclerView_id);

        jsonrequest();

        return view;
    }

    private void jsonrequest() {
        request = new JsonArrayRequest(urlRetornaFavoritos, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        PostoFavorito posto = new PostoFavorito();
                        posto.setId_posto(jsonObject.getInt("id_posto"));
                        posto.setNomeFantasia(jsonObject.getString("nomeFantasia"));
                        posto.setBandeira(jsonObject.getString("bandeira"));
                        posto.setId_usuario(usuario.getId());

                        listaPostoFavorito.add(posto);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setuprecyclerview(listaPostoFavorito);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<PostoFavorito> listaPostoFavorito) {

        RecyclerViewFavoritosAdapter myadapter = new RecyclerViewFavoritosAdapter(getContext(), listaPostoFavorito);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(myadapter);
    }

}
