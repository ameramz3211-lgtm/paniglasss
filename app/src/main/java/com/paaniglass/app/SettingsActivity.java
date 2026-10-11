package com.paaniglass.app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;

/** Page 3: daily goal, glass ka size, aur aaj ka reset. */
public class SettingsActivity extends AppCompatActivity {

    private static final int[] SIZES = {150, 200, 250, 300, 500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EdgeHelper.applyPadding(findViewById(R.id.root));

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        WaterStore store = new WaterStore(this);

        // Goal slider
        TextView goalLabel = findViewById(R.id.goalLabel);
        Slider goalSlider = findViewById(R.id.goalSlider);
        goalSlider.setValue(Math.max(4, Math.min(16, store.getGoal())));
        goalLabel.setText(getString(R.string.goal_label, store.getGoal()));
        goalSlider.addOnChangeListener((s, value, fromUser) -> {
            int g = Math.round(value);
            store.setGoal(g);
            goalLabel.setText(getString(R.string.goal_label, g));
        });

        // Glass size chips
        ChipGroup group = findViewById(R.id.sizeGroup);
        int current = store.getGlassMl();
        for (int ml : SIZES) {
            Chip chip = new Chip(this);
            chip.setText(getString(R.string.ml_chip, ml));
            chip.setCheckable(true);
            chip.setChecked(ml == current);
            chip.setTag(ml);
            group.addView(chip);
        }
        group.setOnCheckedStateChangeListener((g, ids) -> {
            if (ids.isEmpty()) return;
            Chip c = g.findViewById(ids.get(0));
            store.setGlassMl((Integer) c.getTag());
        });

        // Reset today
        MaterialButton reset = findViewById(R.id.resetButton);
        reset.setOnClickListener(v -> new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.reset_title)
                .setMessage(R.string.reset_msg)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.reset, (d, w) -> {
                    store.resetToday();
                    Toast.makeText(this, R.string.reset_done, Toast.LENGTH_SHORT).show();
                })
                .show());

        TextView version = findViewById(R.id.versionText);
        version.setText(getString(R.string.version_text, BuildConfigHelper.versionName(this)));
    }
}
