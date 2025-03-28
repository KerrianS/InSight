package fr.kerriansalaun.insight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import fr.kerriansalaun.insight.R;
import fr.kerriansalaun.insight.service.MockServerService;

public class AccueilActivity extends AppCompatActivity {
    
    private Button historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        startService(new Intent(this, MockServerService.class));

        historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccueilActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        Button controlButton = findViewById(R.id.controlButton);
        controlButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, ControlActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MockServerService.class));
    }
} 