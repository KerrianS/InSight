package fr.kerriansalaun.insight.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.kerriansalaun.insight.R;
import fr.kerriansalaun.insight.activity.DetailActivity;
import fr.kerriansalaun.insight.model.Sol;
import fr.kerriansalaun.insight.model.Weather;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SolAdapter extends ArrayAdapter<Sol> {

    public SolAdapter(Context context, List<Sol> sols) {
        super(context, 0, sols);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sol, parent, false);
        }

        final Sol sol = getItem(position);
        Weather weather = sol.getWeather();
        TextView solNumberView = convertView.findViewById(R.id.solNumberTextView);
        TextView temperatureView = convertView.findViewById(R.id.temperatureTextView);
        TextView pressureView = convertView.findViewById(R.id.pressureTextView);

        try {
            solNumberView.setText("Sol n°" + sol.getSolKey());
            temperatureView.setText(String.format("Température : %.2f", weather.getAverageTemperature()));
            pressureView.setText(String.format("Pression : %.2f", weather.getAveragePressure()));

            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                
                intent.putExtra("sol_key", sol.getSolKey());
                                ArrayList<String> solsList = new ArrayList<>();
                for (int i = 0; i < getCount(); i++) {
                    solsList.add(getItem(i).toString());
                }
                intent.putStringArrayListExtra("sols_list", solsList);
                
                getContext().startActivity(intent);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
} 