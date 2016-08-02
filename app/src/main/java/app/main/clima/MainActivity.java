package app.main.clima;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String ciudad, temperatura, coordenadas, speed, deg;
    EditText mCiudad;
    TextView mTemperatura, mCoordenadas, mSpeedViento, mDegViento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCiudad = (EditText) findViewById(R.id.ciudad);
        mTemperatura = (TextView) findViewById(R.id.temperatura);
        mCoordenadas = (TextView) findViewById(R.id.coordenadas);
        mSpeedViento = (TextView) findViewById(R.id.speed);
        mDegViento = (TextView) findViewById(R.id.deg);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ciudad = mCiudad.getText().toString();
                if (ciudad.trim().length() > 0) {
                    runApi(ciudad.trim());
                } else {
                    Snackbar.make(view, "Escriba una ciudad", Snackbar.LENGTH_LONG)
                            .setAction("ok", null).show();
                }

            }
        });
    }

    private RequestQueue requestQueue;

    public void runApi(String c) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + c + ",colombia&APPID=64522a9f9acc53bceb67c3e9ae04fbec";
        requestQueue = Volley.newRequestQueue(this);
        // Nueva petición JSONObject
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JsonParser(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JSON ERROR : ", "Error Respuesta en JSON: " + error.getMessage());
                    }
                }
        );
        // se agrega el array json a la cola de peticiones
        requestQueue.add(jsArrayRequest);
    }

    public void JsonParser(JSONObject response){
        Toast.makeText(MainActivity.this, "Procesando request", Toast.LENGTH_LONG).show();
        // for para recorrer el response y extraer los datos
        for (int i = 0; i < response.length(); i++) {
            try {

                // sacando el objeto main
                JSONObject main = response.getJSONObject("main");

                // obteniendo el valor de temp dentro de main : "temp": 300.15 ...
                temperatura = main.getString("temp");
                // cambiando el valor del EditText
                mTemperatura.setText(temperatura);

                // objetos dentro de coord ... se repite lo mismo
                JSONObject coord = response.getJSONObject("coord");
                coordenadas = "lon: "+coord.getString("lon")+ ", lat: " + coord.getString("lat");
                mCoordenadas.setText(coordenadas);

                // objetos dentro de viento :D
                JSONObject wind = response.getJSONObject("wind");
                speed = wind.getString("speed");
                mSpeedViento.setText(speed);
                deg = wind.getString("deg");
                mDegViento.setText(deg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
