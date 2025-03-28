package fr.kerriansalaun.insight.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import fr.kerriansalaun.insight.R;
import fr.kerriansalaun.insight.service.RobotControlService;

public class ControlActivity extends AppCompatActivity {
    private static final int MAX_RECONNECTION_ATTEMPTS = 3;
    private static final int RECONNECTION_DELAY = 5000;
    private static final String CMD_START = "START";
    private static final String CMD_STOP = "STOP";
    
    private RobotControlService robotService;
    private Button startButton;
    private Button leftButton;
    private Button frontButton;
    private Button rightButton;
    private boolean isStarted = false;
    private int reconnectionAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        initializeViews();
        setupButtons();
        connectToRobot();
    }

    private void initializeViews() {
        robotService = new RobotControlService();
        startButton = findViewById(R.id.startButton);
        leftButton = findViewById(R.id.leftButton);
        frontButton = findViewById(R.id.frontButton);
        rightButton = findViewById(R.id.rightButton);
    }

    private void setupButtons() {
        startButton.setOnClickListener(v -> {
            String command = isStarted ? CMD_STOP : CMD_START;
            robotService.sendCommand(command, new RobotControlService.CommandCallback() {
                @Override
                public void onResponse(String response) {
                    isStarted = !isStarted;
                    updateButtonStates();
                    updateStartButtonAppearance();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ControlActivity.this, "Erreur: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        leftButton.setOnClickListener(v -> sendDirectionCommand("DIRECT_LEFT"));
        frontButton.setOnClickListener(v -> sendDirectionCommand("DIRECT_FRONT"));
        rightButton.setOnClickListener(v -> sendDirectionCommand("DIRECT_RIGHT"));
    }

    private void sendDirectionCommand(String command) {
        robotService.sendCommand(command, new RobotControlService.CommandCallback() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ControlActivity.this, "Commande exécutée", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ControlActivity.this, "Erreur: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateButtonStates() {
        leftButton.setEnabled(isStarted);
        frontButton.setEnabled(isStarted);
        rightButton.setEnabled(isStarted);
    }

    private void updateStartButtonAppearance() {
        startButton.setText(isStarted ? R.string.btn_stop : R.string.btn_start);
        startButton.setBackgroundTintList(getColorStateList(
            isStarted ? android.R.color.holo_red_light : android.R.color.holo_green_light
        ));
    }

    private void connectToRobot() {
        robotService.connect(new RobotControlService.CommandCallback() {
            @Override
            public void onResponse(String response) {
                runOnUiThread(() -> {
                    Toast.makeText(ControlActivity.this, R.string.success_connected, Toast.LENGTH_SHORT).show();
                    startButton.setEnabled(true);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ControlActivity.this, 
                        getString(R.string.error_connection, error), 
                        Toast.LENGTH_SHORT).show();
                    if (reconnectionAttempts < MAX_RECONNECTION_ATTEMPTS) {
                        startButton.postDelayed(() -> connectToRobot(), RECONNECTION_DELAY);
                        reconnectionAttempts++;
                    } else {
                        Toast.makeText(ControlActivity.this, 
                            R.string.error_max_attempts, 
                            Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        robotService.disconnect();
    }
} 