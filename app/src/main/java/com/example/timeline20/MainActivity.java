package com.example.timeline20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private LinkedList<Note> notesList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void CreateNoteActivity(View view) {
        Intent intent = new Intent(this, ChangeNoteActivity.class);

        startActivity(intent);

    }
}