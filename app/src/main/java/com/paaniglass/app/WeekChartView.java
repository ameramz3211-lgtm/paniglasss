package com.paaniglass.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/** 7 din ka simple bar chart; goal wali line dotted. */
public class WeekChartView extends View {

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint donePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint goalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final String[] labels = new String[7];

    private int[] values = new int[7];
    private int goal = 8;

    public WeekChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        float d = getResources().getDisplayMetrics().density;
        barPaint.setColor(Color.parseColor("#9CCBE8"));
        donePaint.setColor(Color.parseColor("#2E9BD6"));

        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurface, tv, true);
        textPaint.setColor(tv.data);
        textPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        textPaint.setTextAlign(Paint.Align.CENTER);

        goalPaint.setColor(Color.parseColor("#E07A5F"));
        goalPaint.setStrokeWidth(2 * d);
        goalPaint.setStyle(Paint.Style.STROKE);
        goalPaint.setPathEffect(new DashPathEffect(new float[]{8 * d, 6 * d}, 0));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -6);
        SimpleDateFormat fmt = new SimpleDateFormat("EEE", Locale.getDefault());
        for (int i = 0; i < 7; i++) {
            labels[i] = fmt.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    public void setData(int[] values, int goal) {
        this.values = values;
        this.goal = Math.max(1, goal);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth(), h = getHeight();
        float labelH = textPaint.getTextSize() * 2.2f;
        float chartH = h - labelH * 2;
        float top = labelH;

        int max = goal;
        for (int v : values) max = Math.max(max, v);

        float slot = w / 7f;
        float barW = slot * 0.55f;
        float radius = barW / 3f;

        for (int i = 0; i < 7; i++) {
            float cx = slot * i + slot / 2f;
            float bh = chartH * values[i] / (float) max;
            rect.set(cx - barW / 2f, top + chartH - bh, cx + barW / 2f, top + chartH);
            if (values[i] > 0) {
                canvas.drawRoundRect(rect, radius, radius, values[i] >= goal ? donePaint : barPaint);
                canvas.drawText(String.valueOf(values[i]), cx, rect.top - textPaint.getTextSize() * 0.4f, textPaint);
            }
            canvas.drawText(labels[i], cx, h - labelH * 0.5f, textPaint);
        }

        float gy = top + chartH - chartH * goal / (float) max;
        canvas.drawLine(0, gy, w, gy, goalPaint);
    }
}
