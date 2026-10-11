package com.paaniglass.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/** Sara data phone ke andar SharedPreferences mein. Har din ki glass ginti alag key mein. */
public class WaterStore {

    private static final String PREFS = "paani_data";
    private static final String KEY_GOAL = "goal";
    private static final String KEY_SIZE = "glass_ml";

    private final SharedPreferences prefs;

    public WaterStore(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static String dayKey(Date date) {
        return "d_" + new SimpleDateFormat("yyyyMMdd", Locale.US).format(date);
    }

    public int getCount(Date date) {
        return prefs.getInt(dayKey(date), 0);
    }

    public int getToday() {
        return getCount(new Date());
    }

    public int addGlass() {
        int now = getToday() + 1;
        prefs.edit().putInt(dayKey(new Date()), now).apply();
        return now;
    }

    public int removeGlass() {
        int now = Math.max(0, getToday() - 1);
        prefs.edit().putInt(dayKey(new Date()), now).apply();
        return now;
    }

    public void resetToday() {
        prefs.edit().remove(dayKey(new Date())).apply();
    }

    public int getGoal() {
        return prefs.getInt(KEY_GOAL, 8);
    }

    public void setGoal(int goal) {
        prefs.edit().putInt(KEY_GOAL, goal).apply();
    }

    public int getGlassMl() {
        return prefs.getInt(KEY_SIZE, 250);
    }

    public void setGlassMl(int ml) {
        prefs.edit().putInt(KEY_SIZE, ml).apply();
    }

    /** Pichle 7 din (purane se naye tak) ki ginti. */
    public int[] lastSevenDays() {
        int[] out = new int[7];
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -6);
        for (int i = 0; i < 7; i++) {
            out[i] = getCount(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return out;
    }

    /** Lagatar kitne din goal pura hua (aaj adhoora ho to kal se ginti). */
    public int streak() {
        int goal = getGoal();
        Calendar cal = Calendar.getInstance();
        if (getCount(cal.getTime()) < goal) cal.add(Calendar.DAY_OF_YEAR, -1);
        int days = 0;
        while (days < 3650 && getCount(cal.getTime()) >= goal) {
            days++;
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }
        return days;
    }
}
