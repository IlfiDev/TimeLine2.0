package com.example.timeline20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.sql.Time;

public class ChangeNoteActivity extends AppCompatActivity {

    EditText label;
    EditText text;
    Time time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_note);
        label = (EditText) findViewById(R.id.note_label_field);
        text = (EditText) findViewById(R.id.note_field);
        Bundle extras = getIntent().getExtras();
        label.setText(extras.getString("Title"));
        text.setText(extras.getString("Text"));
    }

    public void SaveAndQuit(View view) {
        int lineCount = text.getLineCount();
        Note note = new Note(label.getText().toString(), text.getText().toString(), lineCount + 1);
        Intent data = new Intent();
        data.putExtra("note",note);

        setResult(RESULT_OK,data);
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

