package com.example.timeline20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.example.timeline20.fragments.NoteFragment;
import com.example.timeline20.fragments.SettingsFragment;
import com.example.timeline20.fragments.CalendarFragment;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private LinkedList<Note> notesList = new LinkedList<>();

    //какой фрагмент активен сейчас
    private Integer what_fragment_active = -1;

    //фрагменты
    private final NoteFragment noteFragment = new NoteFragment();
    private final CalendarFragment calendarFragment = new CalendarFragment();
    private final SettingsFragment settingsFragment = new SettingsFragment();

    private int theme;
    int active_color;

    private SharedPreferences save_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); ЛОМАЕТ ТЕМНУЮ ТЕМУ

        save_info = getSharedPreferences("save_info", MODE_PRIVATE);

        SharedPreferences save_switch = getSharedPreferences("save_switch", MODE_PRIVATE);

        if (save_switch.getBoolean("Dick", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            theme = AppCompatDelegate.MODE_NIGHT_YES;
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            theme = AppCompatDelegate.MODE_NIGHT_NO;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        if(findViewById(R.id.fragment_container) != null) {
            switch_fragment(save_info.getInt("what_fragment_active", 1));
        }
    }

    //onClick для кнопок бара
    @SuppressLint("NonConstantResourceId")
    public void bar_buttons(View view) {
        switch(view.getId()) {
            case R.id.noteFragment_button:
                if(what_fragment_active != 1) {
                    switch_fragment(1);
                }
                break;
            case R.id.calendarFragment_button:
                if(what_fragment_active != 2) {
                    switch_fragment(2);
                }
                break;
            case R.id.settingsFragment_button:
                if (what_fragment_active != 3) {
                    switch_fragment(3);
                }
                break;

            default:
                break;
        }
    }

    private void switch_fragment(Integer fragment_id) {
        if(fragment_id == 1) {

            what_fragment_active = 1;
            Log.i("switch", "1");

            View temp_view = findViewById(R.id.note_icon);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, noteFragment);
            ft.commit();
        }

        if(fragment_id == 2) {
            what_fragment_active = 2;
            Log.i("switch", "2");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, calendarFragment);
            ft.commit();
        }

        if(fragment_id == 3) {
            what_fragment_active = 3;
            Log.i("switch", "3");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, settingsFragment);
            ft.commit();
        }
    }

    public void change_bar_icon_color(int num_icon, boolean active) {

        View temp_view;
        TextView textView;

        if (theme == AppCompatDelegate.MODE_NIGHT_NO){
            active_color = ContextCompat.getColor(getApplicationContext(), R.color.secondary);} else
                active_color = ContextCompat.getColor(getApplicationContext(), R.color.primary);

        int inactive_color = ContextCompat.getColor(getApplicationContext(), R.color.bar_text);

        Drawable active_icon;

        switch (num_icon) {
            case 1:
                temp_view = findViewById(R.id.note_icon);
                textView = findViewById(R.id.note_textview);
                if (active && theme == AppCompatDelegate.MODE_NIGHT_NO) {
                    temp_view.setBackgroundResource(R.drawable.ic_active_note);
                    textView.setTextColor(active_color);
                } else if (active && theme == AppCompatDelegate.MODE_NIGHT_YES){
                    active_icon = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_note);
                    DrawableCompat.setTint(active_icon, ContextCompat.getColor(MainActivity.this, R.color.primary));

                    temp_view.setBackground(active_icon);
                    textView.setTextColor(active_color);
                } else {
                    temp_view.setBackgroundResource(R.drawable.ic_note);
                    textView.setTextColor(inactive_color);
                }
                break;
            case 2:
                temp_view = findViewById(R.id.note_calendar);
                textView = findViewById(R.id.calendar_textview);
                if (active && theme == AppCompatDelegate.MODE_NIGHT_NO) {
                    temp_view.setBackgroundResource(R.drawable.ic_active_calendar);
                    textView.setTextColor(active_color);
                } else if (active && theme == AppCompatDelegate.MODE_NIGHT_YES){
                    active_icon = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_calendar);
                    DrawableCompat.setTint(active_icon, ContextCompat.getColor(MainActivity.this, R.color.primary));

                    temp_view.setBackground(active_icon);
                    textView.setTextColor(active_color);
                } else {
                    temp_view.setBackgroundResource(R.drawable.ic_calendar);
                    textView.setTextColor(inactive_color);
                }
                break;
            case 3:
                temp_view = findViewById(R.id.note_settings);
                textView = findViewById(R.id.settings_textview);
                if (active && theme == AppCompatDelegate.MODE_NIGHT_NO) {
                    temp_view.setBackgroundResource(R.drawable.ic_active_settings);
                    textView.setTextColor(active_color);
                } else if (active && theme == AppCompatDelegate.MODE_NIGHT_YES){
                    active_icon = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_settings);
                    DrawableCompat.setTint(active_icon, ContextCompat.getColor(MainActivity.this, R.color.primary));

                    temp_view.setBackground(active_icon);
                    textView.setTextColor(active_color);
                } else {
                    temp_view.setBackgroundResource(R.drawable.ic_settings);
                    textView.setTextColor(inactive_color);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        save_info.edit().putInt("what_fragment_active", what_fragment_active).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        save_info.edit().remove("what_fragment_active").apply();
    }
}