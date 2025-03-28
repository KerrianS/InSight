package fr.kerriansalaun.insight.model;

import org.json.JSONObject;
import java.util.Iterator;

public class Sol {
    private String solKey;
    private Weather weather;

    public Sol(JSONObject data) {
        try {
            this.solKey = data.getString("sol_key");
            this.weather = new Weather(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSolKey() {
        return solKey;
    }

    public Weather getWeather() {
        return weather;
    }

    @Override
    public String toString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sol_key", solKey);
            if (weather != null) {
                JSONObject weatherData = weather.toJSON();
                Iterator<String> keys = weatherData.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    jsonObject.put(key, weatherData.get(key));
                }
            }
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public static class Temperature {
        private double av;  
        private double mn; 
        private double mx;  
        private int ct;     

        public double getAv() { return av; }
        public double getMn() { return mn; }
        public double getMx() { return mx; }
        public int getCt() { return ct; }
    }

    public static class Pressure {
        private double av;
        private double mn;
        private double mx;
        private int ct;

        public double getAv() { return av; }
        public double getMn() { return mn; }
        public double getMx() { return mx; }
        public int getCt() { return ct; }
    }

    public static class WindData {
        private WindDirection[] directions;
        
        public static class WindDirection {
            private double compass_degrees;
            private String compass_point;
            private double compass_right;
            private double compass_up;
            private int ct;

            public double getCompassDegrees() { return compass_degrees; }
            public String getCompassPoint() { return compass_point; }
            public double getCompassRight() { return compass_right; }
            public double getCompassUp() { return compass_up; }
            public int getCt() { return ct; }
        }

        public WindDirection[] getDirections() { return directions; }
    }
} 