package com.example.timeline20;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChangeNoteActivity extends AppCompatActivity {

    EditText label;
    EditText text;

    LocalDateTime time = null;
    Note noteFromOutside;
    Button deleteButton;

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
        setContentView(R.layout.activity_change_event);
        label = (EditText) findViewById(R.id.note_label_field);
        text = (EditText) findViewById(R.id.note_text_field_id);
        Bundle extras = getIntent().getExtras();
        label.setText(extras.getString("Title"));
        if(extras.getString("time") != null){
            time = LocalDateTime.parse(extras.getString("time"));

        }

        text.setText(extras.getString("Text"));

        noteFromOutside = (Note) extras.getSerializable("NoteObject");
        deleteButton = this.findViewById(R.id.delete_button);


        date_button = findViewById(R.id.set_date_event_button);
        date_button.setText(date_format.format(calendar.getTime()));

        time_button = findViewById(R.id.set_time_event_button);
        time_button.setText(time_format.format(calendar.getTime()));

        finale_time_str.append(finale_date_format.format(calendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void DeleteNote(View view){
        Intent data = new Intent();
        if(noteFromOutside == null){
            data.putExtra("note", -1);
        }
        else{
            data.putExtra("note", noteFromOutside.GetId());
            data.putExtra("noteDate", noteFromOutside.GetDateInDays());
        }
        setResult(2,data);
        finish();
    }
    public void SaveAndQuit(View view) {

        if(noteFromOutside == null){
            int lineCount = text.getLineCount();
            Note note = new Note(label.getText().toString(), text.getText().toString(), time,lineCount + 1);
            Intent data = new Intent();
            data.putExtra("note",note);
            setResult(RESULT_FIRST_USER,data);
        }
        else{
            Intent data = new Intent();
            noteFromOutside.SetNoteText(text.getText().toString());
            noteFromOutside.SetLabel(label.getText().toString());
            noteFromOutside.SetTime(time);
            data.putExtra("note", noteFromOutside);
            setResult(RESULT_OK, data);
        }

        finish();
    }
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
                        android.R.style.Theme_DeviceDefault_Dialog);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar_date = Calendar.getInstance();
                        calendar_date.set(Calendar.YEAR, year);
                        calendar_date.set(Calendar.MONTH, month);
                        calendar_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String date_str_view = date_format.format(calendar_date.getTime());
                        date_button.setText(date_str_view);

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        finale_time_str.delete(0, finale_time_str.length());
                        finale_time_str.append(finale_date_format.format(calendar.getTime()));
                        time = LocalDateTime.parse(finale_time_str);
                    }
                });
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;
            case R.id.set_time_event_button:

                Calendar time_calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(ChangeNoteActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        time_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        time_calendar.set(Calendar.MINUTE, minute);
                        String time_str_view = time_format.format(time_calendar.getTime());
                        time_button.setText(time_str_view);

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        finale_time_str.delete(0, finale_time_str.length());
                        finale_time_str.append(finale_date_format.format(calendar.getTime()));
                        time = LocalDateTime.parse(finale_time_str);
                    }
                }, time_calendar.get(Calendar.HOUR_OF_DAY), time_calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
                break;

            default:
                break;
        }
    }




}

