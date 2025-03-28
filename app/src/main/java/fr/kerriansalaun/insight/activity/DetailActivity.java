package fr.kerriansalaun.insight.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import fr.kerriansalaun.insight.R;
import org.json.JSONObject;
import fr.kerriansalaun.insight.view.WindRoseView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;
import fr.kerriansalaun.insight.adapter.SolDetailPagerAdapter;
import fr.kerriansalaun.insight.model.Sol;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        ArrayList<String> solsList = getIntent().getStringArrayListExtra("sols_list");
        String currentSolKey = getIntent().getStringExtra("sol_key");
        
        List<Sol> solsData = new ArrayList<>();
        try {
            for (String solJson : solsList) {
                JSONObject jsonObject = new JSONObject(solJson);
                Sol sol = new Sol(jsonObject);
                solsData.add(sol);
            }
            
            SolDetailPagerAdapter adapter = new SolDetailPagerAdapter(solsData);
            viewPager.setAdapter(adapter);
            
            for (int i = 0; i < solsData.size(); i++) {
                if (solsData.get(i).getSolKey().equals(currentSolKey)) {
                    viewPager.setCurrentItem(i, false);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.error_data_processing), Toast.LENGTH_SHORT).show();
        }
    }
} 