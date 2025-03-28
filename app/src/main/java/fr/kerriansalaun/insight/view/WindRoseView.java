package fr.kerriansalaun.insight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import org.json.JSONObject;

public class WindRoseView extends View {
    private Paint circlePaint;
    private Paint trianglePaint;
    private Paint backgroundPaint;
    private Paint linesPaint;
    private Paint strokePaint;
    private JSONObject windData;
    private float maxCount = 0;

    public WindRoseView(Context context) {
        super(context);
        init();
    }

    public WindRoseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(2);
        circlePaint.setAntiAlias(true);

        trianglePaint = new Paint();
        trianglePaint.setColor(Color.rgb(135, 206, 235));
        trianglePaint.setStyle(Paint.Style.FILL);
        trianglePaint.setAntiAlias(true);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.rgb(211, 211, 211));
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);

        linesPaint = new Paint();
        linesPaint.setColor(Color.GRAY);
        linesPaint.setStyle(Paint.Style.STROKE);
        linesPaint.setStrokeWidth(2);
        linesPaint.setAntiAlias(true);

        strokePaint = new Paint();
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(2);
        strokePaint.setAntiAlias(true);
    }

    public void setWindData(JSONObject data) {
        this.windData = data;
        findMaxCount();
        invalidate();
    }

    private void findMaxCount() {
        try {
            maxCount = 0;
            for (int i = 0; i < 16; i++) {
                JSONObject direction = windData.optJSONObject(String.valueOf(i));
                if (direction != null) {
                    int count = direction.optInt("ct", 0);
                    maxCount = Math.max(maxCount, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        int width = getWidth();
        int height = getHeight();
        float centerX = width / 2f;
        float centerY = height / 2f;
        float radius = Math.min(width, height) / 2f - 20;

        canvas.drawCircle(centerX, centerY, radius, backgroundPaint);
        canvas.drawCircle(centerX, centerY, radius, strokePaint);

        canvas.drawLine(centerX - radius, centerY, centerX + radius, centerY, linesPaint);
        canvas.drawLine(centerX, centerY - radius, centerX, centerY + radius, linesPaint);

        if (windData == null) return;

        for (int i = 0; i < 16; i++) {
            try {
                JSONObject direction = windData.optJSONObject(String.valueOf(i));
                if (direction != null) {
                    int count = direction.optInt("ct", 0);
                    float angle = i * 22.5f; 
                    drawDirectionTriangle(canvas, centerX, centerY, radius, angle, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void drawDirectionTriangle(Canvas canvas, float centerX, float centerY, 
                                     float maxRadius, float angle, float count) {
        float radius = (count / maxCount) * maxRadius;
        double angleRad = Math.toRadians(angle - 90); 
        
        float x = (float) (centerX + radius * Math.cos(angleRad));
        float y = (float) (centerY + radius * Math.sin(angleRad));
        
        Path path = new Path();
        path.moveTo(centerX, centerY);
        
        double angleWidth = Math.toRadians(11.25); 
        float x1 = (float) (centerX + radius * Math.cos(angleRad - angleWidth));
        float y1 = (float) (centerY + radius * Math.sin(angleRad - angleWidth));
        float x2 = (float) (centerX + radius * Math.cos(angleRad + angleWidth));
        float y2 = (float) (centerY + radius * Math.sin(angleRad + angleWidth));
        
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.close();
        
        canvas.drawPath(path, trianglePaint);
        canvas.drawPath(path, strokePaint);
    }
} 