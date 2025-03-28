package fr.kerriansalaun.insight.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import fr.kerriansalaun.insight.R;
import fr.kerriansalaun.insight.adapter.SolAdapter;
import fr.kerriansalaun.insight.service.NasaService;
import fr.kerriansalaun.insight.model.Sol;
import fr.kerriansalaun.insight.model.Weather;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView solCountTextView;
    private ListView solsListView;
    private NasaService nasaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        solCountTextView = findViewById(R.id.solCountTextView);
        solsListView = findViewById(R.id.solsListView);
        nasaService = new NasaService(this);

        loadMarsWeatherData();
    }

    private void loadMarsWeatherData() {
        nasaService.getWeatherData(new NasaService.WeatherCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray solKeys = response.getJSONArray("sol_keys");
                    int nbSols = solKeys.length();
                    solCountTextView.setText("NB SOLS : " + nbSols);

                    List<Sol> solsList = new ArrayList<>();
                    for (int i = 0; i < solKeys.length(); i++) {
                        String solKey = solKeys.getString(i);
                        JSONObject solData = response.getJSONObject(solKey);
                        solData.put("sol_key", solKey);
                        Sol sol = new Sol(solData);
                        solsList.add(sol);
                    }

                    SolAdapter adapter = new SolAdapter(HomeActivity.this, solsList);
                    solsListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, 
                        getString(R.string.error_data_processing), 
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(HomeActivity.this, 
                    getString(R.string.error_api, error), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
} 