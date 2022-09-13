package com.example.timeline20.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.timeline20.ChangeNoteActivity;
import com.example.timeline20.MainActivity;
import com.example.timeline20.Note;
import com.example.timeline20.R;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;


public class CalendarFragment extends Fragment {
    LinkedList<LinkedList<Note>> datesList = new LinkedList<LinkedList<Note>>();
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note, container, false);

        Button addEventButton = view.findViewById(R.id.add_event_button);


        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(2, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(2, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Note newNote;
        LinkedList<Note> notesList;
        if(resultCode == 2){
            int id = (int) data.getIntExtra("note", -1);
            int date = (int) data.getIntExtra("noteDate", -1);

        }
        else{
            newNote = (Note) data.getSerializableExtra("note");

            if(datesList.get(newNote.GetDateInDays()) == null){
                notesList = new LinkedList<Note>();
            }
            else{
                notesList = datesList.get(newNote.GetDateInDays());
            }
            if(resultCode == Activity.RESULT_FIRST_USER){

                notesList.add(0, newNote);
                Collections.sort(notesList);
                datesList.set(newNote.GetDateInDays(), notesList);
            }
            else{
                notesList = datesList.get(newNote.GetDateInDays());
                notesList.set(findNoteIndexById(newNote.GetId(), notesList), newNote);
            }
        }
    }
    private int findNoteIndexById(int id, LinkedList<Note> list){
        if(list.size() != 0){
            for(int i = 0; i < list.size(); i++){
                if(list.get(i).GetId() == id){
                    return i;

                }
            }
        }
        return -1;
    }
}