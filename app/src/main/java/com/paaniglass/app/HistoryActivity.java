package com.paaniglass.app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

/** Page 2: pichle 7 din ka chart, streak aur average. */
public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        EdgeHelper.applyPadding(findViewById(R.id.root));

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        WaterStore store = new WaterStore(this);
        int[] week = store.lastSevenDays();
        int goal = store.getGoal();

        WeekChartView chart = findViewById(R.id.chart);
        chart.setData(week, goal);

        int total = 0, hitDays = 0;
        for (int v : week) {
            total += v;
            if (v >= goal) hitDays++;
        }
        float avg = total / 7f;
        int litres10 = Math.round(total * store.getGlassMl() / 100f); // litre x10

        ((TextView) findViewById(R.id.streakValue)).setText(String.valueOf(store.streak()));
        ((TextView) findViewById(R.id.avgValue)).setText(String.format(java.util.Locale.US, "%.1f", avg));
        ((TextView) findViewById(R.id.goalDaysValue)).setText(getString(R.string.of_seven, hitDays));
        ((TextView) findViewById(R.id.litreValue)).setText(
                String.format(java.util.Locale.US, "%.1f L", litres10 / 10f));
    }
}
