package com.paaniglass.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.DateFormat;
import java.util.Date;

/** Page 1: aaj ka glass. Glass ya button tap karo, paani barhe. */
public class MainActivity extends AppCompatActivity {

    private WaterStore store;
    private GlassView glass;
    private TextView countText, mlText, cheerText;
    private LinearProgressIndicator progress;

    private static final int[] CHEERS = {
            R.string.cheer_0, R.string.cheer_1, R.string.cheer_2, R.string.cheer_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeHelper.applyPadding(findViewById(R.id.root));

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        store = new WaterStore(this);
        glass = findViewById(R.id.glass);
        countText = findViewById(R.id.countText);
        mlText = findViewById(R.id.mlText);
        cheerText = findViewById(R.id.cheerText);
        progress = findViewById(R.id.progress);
        TextView dateText = findViewById(R.id.dateText);
        dateText.setText(DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));

        MaterialButton addBtn = findViewById(R.id.addButton);
        MaterialButton undoBtn = findViewById(R.id.undoButton);

        glass.setOnClickListener(v -> addOne(v));
        addBtn.setOnClickListener(v -> addOne(v));
        undoBtn.setOnClickListener(v -> {
            store.removeGlass();
            refresh(true);
        });
    }

    private void addOne(android.view.View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        store.addGlass();
        refresh(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh(false);   // settings badli hon ya din badal gaya ho
    }

    private void refresh(boolean animate) {
        int count = store.getToday();
        int goal = store.getGoal();
        int ml = count * store.getGlassMl();

        glass.setLevel(count / (float) goal, animate);
        countText.setText(getString(R.string.count_format, count, goal));
        mlText.setText(getString(R.string.ml_format, ml));
        progress.setMax(goal);
        progress.setProgressCompat(Math.min(count, goal), animate);

        int stage;
        if (count >= goal) stage = 3;
        else if (count >= goal / 2) stage = 2;
        else if (count > 0) stage = 1;
        else stage = 0;
        cheerText.setText(CHEERS[stage]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
