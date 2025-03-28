package fr.kerriansalaun.insight.service;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class NasaService {
    private static final String BASE_URL = "https://api.nasa.gov/insight_weather/";
    private static final String API_KEY = "i3eGB1v9MEcFopzlOZJITukklRINnf1b4x4Awskx";
    private final Context context;
    private final RequestQueue requestQueue;

    public NasaService(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public interface WeatherCallback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    public void getWeatherData(final WeatherCallback callback) {
        String url = BASE_URL + "?api_key=" + API_KEY + "&feedtype=json&ver=1.0";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Données téléchargées avec succès", Toast.LENGTH_SHORT).show();
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                });

        requestQueue.add(request);
    }
} 