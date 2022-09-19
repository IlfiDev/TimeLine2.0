package com.example.timeline20;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ChangeNoteActivity extends AppCompatActivity {

    EditText label;
    EditText text;

    LocalDateTime time = null;
    Note noteFromOutside;
    Button deleteButton;

    boolean isNewEvent = true;

    StringBuilder finale_time_str = new StringBuilder();

    private Button date_button, time_button;

    private final SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private final SimpleDateFormat time_format = new SimpleDateFormat("HH:mm", Locale.US);
    private final  SimpleDateFormat finale_date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:'00.000'", Locale.US);

    Calendar calendar = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras.getInt("layoutNum") == 0){
            setContentView(R.layout.activity_change_note);
        }else{
            setContentView(R.layout.activity_change_event);

        }
        label = (EditText) findViewById(R.id.note_label_field);
        text = (EditText) findViewById(R.id.note_text_field_id);

        label.setText(extras.getString("Title"));
        if(extras.getString("time") != null){
            time = LocalDateTime.parse(extras.getString("time"));

        }

        text.setText(extras.getString("Text"));

        noteFromOutside = (Note) extras.getSerializable("NoteObject");
        deleteButton = this.findViewById(R.id.delete_button);

        if(extras.getInt("layoutNum") == 1) {
            String time_str = extras.getString("event_time", "");

            if(!time_str.equals("")) {
                isNewEvent = false;
                String[] temp_str = time_str.split("T");

                String[] date = temp_str[0].split("-");

                calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));

                String[] time = temp_str[1].split(":");
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            }

            date_button = findViewById(R.id.set_date_event_button);
            date_button.setText(date_format.format(calendar.getTime()));

            time_button = findViewById(R.id.set_time_event_button);
            time_button.setText(time_format.format(calendar.getTime()));

            finale_time_str.append(finale_date_format.format(calendar.getTime()));

            TextView change_event_layout_title = findViewById(R.id.change_event_layout_title);
            if(extras.getBoolean("isNewEvent")) {
                change_event_layout_title.setText(R.string.create_event);
            }
        } else {
            TextView change_note_layout_title = findViewById(R.id.change_note_layout_title);
            if (extras.getBoolean("isNewNote")) {
                change_note_layout_title.setText(R.string.create_note);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void DeleteNote(View view){
        Intent data = new Intent();
        if(noteFromOutside == null){
            data.putExtra("note", -1);
        }
        else{
            data.putExtra("note", noteFromOutside.GetId());
            if(noteFromOutside.timeStr != null){
                data.putExtra("noteDate", noteFromOutside.GetDateInDays());
            }

        }
        setResult(2,data);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveAndQuit(View view) {

        if(noteFromOutside == null){
            int lineCount = text.getLineCount();
            Note note;
            if(time == null){
                note = new Note(label.getText().toString(), text.getText().toString(),lineCount + 1);

            }
            else{
                note = new Note(label.getText().toString(), text.getText().toString(), time.toString(),lineCount + 1);
            }
            Intent data = new Intent();
            data.putExtra("note",note);
            setResult(RESULT_FIRST_USER,data);
        }
        else{
            Intent data = new Intent();
            int currDate = -1;
            if(noteFromOutside.timeStr == null) {
                noteFromOutside.SetNoteText(text.getText().toString());
                noteFromOutside.SetLabel(label.getText().toString());

                data.putExtra("note", noteFromOutside);
            }else{
                noteFromOutside.SetNoteText(text.getText().toString());
                noteFromOutside.SetLabel(label.getText().toString());
                noteFromOutside.SetTime(time.toString());
                data.putExtra("note", noteFromOutside);

                data.putExtra("date", currDate);
            }



            setResult(RESULT_OK, data);
        }

        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            SaveAndQuit(findViewById(R.id.note_label_field));
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeDateTime(View view) {
        switch(view.getId()) {
            case R.id.set_date_event_button:

                DatePickerDialog datePickerDialog = new DatePickerDialog(ChangeNoteActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String date_str_view = date_format.format(calendar.getTime());
                        date_button.setText(date_str_view);

                        eventInThePast();

                        finale_time_str.delete(0, finale_time_str.length());
                        finale_time_str.append(finale_date_format.format(calendar.getTime()));
                        time = LocalDateTime.parse(finale_time_str);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();
                break;
            case R.id.set_time_event_button:

                TimePickerDialog timePickerDialog = new TimePickerDialog(ChangeNoteActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        eventInThePast();


                        String time_str_view = time_format.format(calendar.getTime());
                        time_button.setText(time_str_view);


                        finale_time_str.delete(0, finale_time_str.length());
                        finale_time_str.append(finale_date_format.format(calendar.getTime()));
                        time = LocalDateTime.parse(finale_time_str);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                timePickerDialog.show();
                break;

            default:
                break;
        }
    }


    private void eventInThePast() {

        Calendar temp_calendar = Calendar.getInstance();
        int now_year = temp_calendar.get(Calendar.YEAR);
        int now_month = temp_calendar.get(Calendar.MONTH);
        int now_dayOfMonth = temp_calendar.get(Calendar.DAY_OF_MONTH);

        int now_hourOfDay = temp_calendar.get(Calendar.HOUR_OF_DAY);
        int now_minites = temp_calendar.get(Calendar.MINUTE);

        if ((calendar.get(Calendar.YEAR) == now_year) && (calendar.get(Calendar.MONTH) == now_month) &&
                (calendar.get(Calendar.DAY_OF_MONTH) == now_dayOfMonth)) {

            if (calendar.get(Calendar.HOUR_OF_DAY) < now_hourOfDay) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Toast.makeText(ChangeNoteActivity.this, R.string.event_in_the_past, Toast.LENGTH_SHORT).show();
            } else {
                if ((calendar.get(Calendar.HOUR_OF_DAY) == now_hourOfDay) &&
                        (calendar.get(Calendar.MINUTE) < now_minites)) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    Toast.makeText(ChangeNoteActivity.this, R.string.event_in_the_past, Toast.LENGTH_SHORT).show();
                }
            }

        }

        String date_str_view = date_format.format(calendar.getTime());
        date_button.setText(date_str_view);
    }




}

