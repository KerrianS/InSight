package fr.kerriansalaun.insight.model;

import org.json.JSONObject;
import java.util.List;

public class Weather {
    private List<Sol> sols;
    private double averageTemperature;
    private double minTemperature;
    private double maxTemperature;
    private double averagePressure;
    private double minPressure;
    private double maxPressure;
    private JSONObject windData;

    public Weather(JSONObject data) {
        try {
            JSONObject AT = data.getJSONObject("AT");
            JSONObject PRE = data.getJSONObject("PRE");
            
            this.averageTemperature = AT.getDouble("av");
            this.minTemperature = AT.getDouble("mn");
            this.maxTemperature = AT.getDouble("mx");
            
            this.averagePressure = PRE.getDouble("av");
            this.minPressure = PRE.getDouble("mn");
            this.maxPressure = PRE.getDouble("mx");
            
            this.windData = data.getJSONObject("WD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getAverageTemperature() { return averageTemperature; }
    public double getMinTemperature() { return minTemperature; }
    public double getMaxTemperature() { return maxTemperature; }
    public double getAveragePressure() { return averagePressure; }
    public double getMinPressure() { return minPressure; }
    public double getMaxPressure() { return maxPressure; }
    public JSONObject getWindData() { return windData; }
    public List<Sol> getSols() { return sols; }

    public void setSols(List<Sol> sols) { this.sols = sols; }

    public JSONObject toJSON() {
        try {
            JSONObject json = new JSONObject();
            JSONObject at = new JSONObject();
            at.put("av", averageTemperature);
            at.put("mn", minTemperature);
            at.put("mx", maxTemperature);
            json.put("AT", at);

            JSONObject pre = new JSONObject();
            pre.put("av", averagePressure);
            pre.put("mn", minPressure);
            pre.put("mx", maxPressure);
            json.put("PRE", pre);

            if (windData != null) {
                json.put("WD", windData);
            }

            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
} 