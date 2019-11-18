package com.example.octanapp.fragments;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.octanapp.R;
import com.example.octanapp.activities.AlterarSenhaActivity;
import com.example.octanapp.activities.PostoActivity;
import com.example.octanapp.model.Usuario;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback, LocationListener,GoogleMap.OnMarkerClickListener {

    private LocationManager locationManager;
    private GoogleMap mMap;
    //String urlMapeiaPostos = "http://192.168.25.17/octanapp/mapeiaPostos.php";
    String urlMapeiaPostos = "https://octanapp.herokuapp.com/mapeiaPostos.php";
    //String urlMapeiaEstabelecimentos = "http://192.168.25.17/octanapp/mapeiaEstabelecimentos.php";
    String urlMapeiaEstabelecimentos = "https://octanapp.herokuapp.com/mapeiaEstabelecimentos.php";
    private JSONArray result;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    Usuario usuario;
    Bundle parametrosUsuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");

        usuario = new Usuario();
        parametrosUsuario = new Bundle();
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
        getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);
        mMap = googleMap;

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, urlMapeiaPostos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JSONResult" , response.toString());
                JSONObject j = null;
                try{
                    j =new JSONObject(response);
                    result = j.getJSONArray("result");
                    for(int i=0;i<result.length();i++){
                        JSONObject jsonObject1=result.getJSONObject(i);
                        String latlong = jsonObject1.getString("coordenadas");
                        String id_posto = jsonObject1.getString("id_posto");
                        String nome = jsonObject1.getString("nomeFantasia");
                        String bandeira = jsonObject1.getString("bandeira");
                        String coordenadas[] = latlong.split(Pattern.quote(","));
                        String lat_i = coordenadas[0];
                        String long_i = coordenadas[1];
                        int height = 100;
                        int width = 100;
                        Marker marker;
                        if (bandeira.equals("Shell")) {
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_shell_foreground);
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(lat_i) , Double.parseDouble(long_i)))
                                    .title(nome)
                                    .snippet("clique para acessar")
                                    .icon(smallMarkerIcon)
                            );
                        } else if (bandeira.equals("Ipiranga")) {
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_ipiranga_foreground);
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(lat_i) , Double.parseDouble(long_i)))
                                    .title(nome)
                                    .snippet("clique para acessar")
                                    .icon(smallMarkerIcon)
                            );
                        } else if (bandeira.equals("Petrobras")) {
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_petrobras_foreground);
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(lat_i) , Double.parseDouble(long_i)))
                                    .title(nome)
                                    .snippet("clique para acessar")
                                    .icon(smallMarkerIcon)
                            );
                        } else {
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_posto_foreground);
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(lat_i) , Double.parseDouble(long_i)))
                                    .title(nome)
                                    .snippet("clique para acessar")
                                    .icon(smallMarkerIcon)
                            );
                        }
                        marker.setTag(id_posto);
                        mHashMap.put(marker, i);
                       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.6850,90.3563), 6.0f));
                    }

                }catch (NullPointerException e){
                    e.printStackTrace();

                }

                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

        RequestQueue requestQueueEst= Volley.newRequestQueue(getContext());
        StringRequest stringRequestEst=new StringRequest(Request.Method.GET, urlMapeiaEstabelecimentos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JSONResult" , response.toString());
                JSONObject j = null;
                try{
                    j =new JSONObject(response);
                    result = j.getJSONArray("result");
                    for(int i=0;i<result.length();i++){
                        JSONObject jsonObject1=result.getJSONObject(i);
                        String latlong = jsonObject1.getString("coordenadas");
                        String cnpj = jsonObject1.getString("cnpj");
                        String nome = jsonObject1.getString("razaoSocial");
                        String ramo = jsonObject1.getString("ramo");
                        String coordenadas[] = latlong.split(Pattern.quote(","));
                        String lat_i = coordenadas[0];
                        String long_i = coordenadas[1];
                        int height = 100;
                        int width = 100;
                        Marker marker;
                        if (ramo.equals("Autocenter")) {
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_autocenter_foreground);
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(lat_i) , Double.parseDouble(long_i)))
                                    .title(nome)
                                    .snippet("clique para acessar")
                                    .icon(smallMarkerIcon)
                            );
                        } else if (ramo.equals("Concessionaria")) {
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_concessionaria_foreground);
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(lat_i) , Double.parseDouble(long_i)))
                                    .title(nome)
                                    .snippet("clique para acessar")
                                    .icon(smallMarkerIcon)
                            );
                        } else {
                            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_ipiranga_foreground);
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(lat_i) , Double.parseDouble(long_i)))
                                    .title(nome)
                                    .snippet("clique para acessar")
                                    .icon(smallMarkerIcon)
                            );
                        }
                        marker.setTag(cnpj);
                        mHashMap.put(marker, i);
                        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.6850,90.3563), 6.0f));
                    }

                }catch (NullPointerException e){
                    e.printStackTrace();

                }

                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        stringRequestEst.setRetryPolicy(policy);
        requestQueueEst.add(stringRequestEst);

       /* mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout ll = new LinearLayout(getContext());
                ll.setPadding(20, 20, 20, 20);
                ll.setBackgroundColor(Color.GREEN);

                TextView tv = new TextView(getContext());
                tv.setText(Html.fromHtml("<b><font color=\"#ffffff\">"+marker.getTitle()+":</font></b> "+marker.getSnippet()));
                ll.addView(tv);
                final String id_posto = (String) marker.getTag();
                Button bt = new Button(getContext());
                bt.setText("Acessar posto");
                bt.setBackgroundColor(Color.RED);
                bt.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Acessando posto com o id "+id_posto, Toast.LENGTH_SHORT).show();
                    }

                });
                ll.addView(bt);
                return ll;
            }
        }); */

       mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
           @Override
           public void onInfoWindowClick(Marker marker) {
               Intent it = new Intent(getContext(), PostoActivity.class);
               String tag = (String) marker.getTag();
               if (tag.length() == 12) {
                   Toast.makeText(getActivity(), "acessando estabelecimento.", Toast.LENGTH_SHORT).show();
               } else {
                   it.putExtra("id_usuario", String.valueOf(usuario.getId()));
                   it.putExtra("id_posto", (String) marker.getTag());
                   startActivity(it);
               }
           }
       });

        LatLng curitiba = new LatLng(-25.486569, -49.300872);

        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curitiba, 15));
    }
    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println("Marker tag: "+marker.getTag());
        return false;
    }
}


