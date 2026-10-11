package com.paaniglass.app;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/** Apna banaya hua glass: neeche se paani bharta hai aur upar halki lehar chalti hai. */
public class GlassView extends View {

    private final Paint glassPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint waterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path glassPath = new Path();
    private final Path waterPath = new Path();

    private float level = 0f;      // 0..1 abhi dikh raha
    private float wavePhase = 0f;
    private ValueAnimator levelAnim;
    private final ValueAnimator waveAnim;

    public GlassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        float d = getResources().getDisplayMetrics().density;

        glassPaint.setStyle(Paint.Style.STROKE);
        glassPaint.setStrokeWidth(5 * d);
        glassPaint.setStrokeJoin(Paint.Join.ROUND);
        glassPaint.setColor(Color.parseColor("#7FA7C9"));

        waterPaint.setStyle(Paint.Style.FILL);
        waterPaint.setColor(Color.parseColor("#2E9BD6"));

        bubblePaint.setStyle(Paint.Style.FILL);
        bubblePaint.setColor(Color.argb(90, 255, 255, 255));

        waveAnim = ValueAnimator.ofFloat(0f, (float) (Math.PI * 2));
        waveAnim.setDuration(2200);
        waveAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveAnim.setInterpolator(new LinearInterpolator());
        waveAnim.addUpdateListener(a -> {
            wavePhase = (float) a.getAnimatedValue();
            invalidate();
        });
    }

    public void setLevel(float target, boolean animate) {
        target = Math.max(0f, Math.min(1f, target));
        if (levelAnim != null) levelAnim.cancel();
        if (!animate) {
            level = target;
            invalidate();
            return;
        }
        levelAnim = ValueAnimator.ofFloat(level, target);
        levelAnim.setDuration(700);
        levelAnim.setInterpolator(new DecelerateInterpolator());
        levelAnim.addUpdateListener(a -> {
            level = (float) a.getAnimatedValue();
            invalidate();
        });
        levelAnim.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        waveAnim.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        waveAnim.cancel();
        if (levelAnim != null) levelAnim.cancel();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth(), h = getHeight();
        float pad = glassPaint.getStrokeWidth();
        float topL = pad, topR = w - pad;
        float botL = w * 0.18f, botR = w * 0.82f;
        float top = pad, bottom = h - pad;

        glassPath.reset();
        glassPath.moveTo(topL, top);
        glassPath.lineTo(botL, bottom);
        glassPath.lineTo(botR, bottom);
        glassPath.lineTo(topR, top);

        if (level > 0.001f) {
            float waterTop = bottom - (bottom - top) * level * 0.94f;
            float amp = (h * 0.018f) * (level < 0.98f ? 1f : 0.4f);

            waterPath.reset();
            // glass ki tirchi deewaron par paani ki chaurai
            float leftAtTop = lerp(botL, topL, (bottom - waterTop) / (bottom - top));
            float rightAtTop = lerp(botR, topR, (bottom - waterTop) / (bottom - top));
            waterPath.moveTo(botL, bottom);
            waterPath.lineTo(leftAtTop, waterTop);
            int steps = 24;
            for (int i = 0; i <= steps; i++) {
                float x = lerp(leftAtTop, rightAtTop, i / (float) steps);
                float y = waterTop + (float) Math.sin(wavePhase + i * 0.5f) * amp;
                waterPath.lineTo(x, y);
            }
            waterPath.lineTo(botR, bottom);
            waterPath.close();
            canvas.drawPath(waterPath, waterPaint);

            // chhote bulbule
            float span = bottom - waterTop;
            for (int i = 0; i < 4; i++) {
                float t = ((wavePhase / (float) (Math.PI * 2)) + i * 0.25f) % 1f;
                float by = bottom - span * t;
                float bx = lerp(botL, botR, 0.25f + 0.17f * i);
                canvas.drawCircle(bx, by, w * 0.018f, bubblePaint);
            }
        }
        canvas.drawPath(glassPath, glassPaint);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
