package fr.kerriansalaun.insight.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.kerriansalaun.insight.R;
import fr.kerriansalaun.insight.model.Sol;
import fr.kerriansalaun.insight.model.Weather;
import fr.kerriansalaun.insight.view.WindRoseView;
import org.json.JSONObject;
import java.util.List;

public class SolDetailPagerAdapter extends RecyclerView.Adapter<SolDetailPagerAdapter.SolDetailViewHolder> {
    private List<Sol> solsList;

    public SolDetailPagerAdapter(List<Sol> sols) {
        this.solsList = sols;
    }

    @NonNull
    @Override
    public SolDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_sol_detail_page, parent, false);
        return new SolDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolDetailViewHolder holder, int position) {
        Sol sol = solsList.get(position);
        Weather weather = sol.getWeather();

        holder.solNumberView.setText("Sol n°" + sol.getSolKey());
        holder.temperatureView.setText(String.format("Température : avg: %.2f min: %.2f max: %.2f",
                weather.getAverageTemperature(),
                weather.getMinTemperature(),
                weather.getMaxTemperature()));
        holder.pressureView.setText(String.format("Pression : avg: %.2f min: %.2f max: %.2f",
                weather.getAveragePressure(),
                weather.getMinPressure(),
                weather.getMaxPressure()));

        holder.windRoseView.setWindData(weather.getWindData());
    }

    @Override
    public int getItemCount() {
        return solsList.size();
    }

    static class SolDetailViewHolder extends RecyclerView.ViewHolder {
        TextView solNumberView;
        TextView temperatureView;
        TextView pressureView;
        WindRoseView windRoseView;

        public SolDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            solNumberView = itemView.findViewById(R.id.solNumberTextView);
            temperatureView = itemView.findViewById(R.id.temperatureDetailTextView);
            pressureView = itemView.findViewById(R.id.pressureDetailTextView);
            windRoseView = itemView.findViewById(R.id.windRoseView);
        }
    }
} 