package com.example.timeline20;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ChangeNoteActivity extends AppCompatActivity {

    EditText label;
    EditText text;

    LocalDateTime time = null;
    Note noteFromOutside;
    Button deleteButton;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_note);
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

    }
    public void DeleteNote(View view){
        Intent data = new Intent();
        if(noteFromOutside == null){
            data.putExtra("note", -1);
        }
        else{
            data.putExtra("note", noteFromOutside.GetId());

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
}

