# Volley with many objects

this repository shows how you can consume services on android with volleyball as http library.

**you will learn how**
- consume the api of the openweathermap.org.
- get the different elements of this api.

**the problem**
  - get this elements.
```javascript 
 "coord": {
    "lon": -74.8,
    "lat": 10.96
  },  "main": {
    
  "temp": 300.15, 
    ... 
  },  
  
  "wind": {
    "speed": 1.5,
    "deg": 320
  },  
``` 
  
 of this json 
```javascript
{
  "coord": {
    "lon": -74.8,
    "lat": 10.96
  },
  "weather": [
    {
      "id": 802,
      "main": "Clouds",
      "description": "scattered clouds",
      "icon": "03n"
    }
  ],
  "base": "cmc stations",
  "main": {
    "temp": 300.15,
    "pressure": 1011,
    "humidity": 83,
    "temp_min": 300.15,
    "temp_max": 300.15
  },
  "wind": {
    "speed": 1.5,
    "deg": 320
  },
  "clouds": {
    "all": 40
  },
  "dt": 1470117600,
  "sys": {
    "type": 1,
    "id": 4246,
    "message": 0.0061,
    "country": "CO",
    "sunrise": 1470134870,
    "sunset": 1470180170
  },
  "id": 3689147,
  "name": "Barranquilla",
  "cod": 200
}
```

**firt steep**
  - put this in graddle
  - ```compile 'com.android.volley:volley:1.0.0'```
  - put internet permissions in the manifest
  - ```<uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />```

**define objects for input and output data**
```javascript

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
                    Snackbar.make(view, "Enter a city", Snackbar.LENGTH_LONG)
                            .setAction("ok", null).show();
                }

            }
        });
    }
```

### This is the secret

**run the next method**

```javascript
runApi(ciudad.trim());
```

**that contains**

```javascript
private RequestQueue requestQueue;

public void runApi(String c) {
        // URL of api
    String url = "http://api.openweathermap.org/data/2.5/weather?q=" + c + ",colombia&APPID=64522a9f9acc53bceb67c3e9ae04fbec";
    requestQueue = Volley.newRequestQueue(this);
        // new JSONObject Request
    JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
        Request.Method.GET, url,
        null,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                        // get data of request and send to parse
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
        //  json array to the request queue is added
    requestQueue.add(jsArrayRequest);
}
```
**run the next method**

```javascript
JsonParser(response);
```
**that contains**

**for json parser**
```javascript
public void JsonParser(JSONObject response){
    Toast.makeText(MainActivity.this, "Procesando request", Toast.LENGTH_LONG).show();
        // iterate response
    for (int i = 0; i < response.length(); i++) {
        try {

            // removing the main object
            JSONObject main = response.getJSONObject("main");

            // obtaining the value of temp inside main: "temp": 300.15 ...
            temperatura = main.getString("temp");
            // cambiando el valor del EditText
            mTemperatura.setText(temperatura);

            //objects in coord ... the same is repeated
            JSONObject coord = response.getJSONObject("coord");
            coordenadas = "lon: "+coord.getString("lon")+ ", lat: " + coord.getString("lat");
            mCoordenadas.setText(coordenadas);

            // Wind objects inside: D
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
// voila
```

#Thanks for being amazing alda❣
