package com.example.timeline20.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.timeline20.ChangeNoteActivity;
import com.example.timeline20.MainActivity;
import com.example.timeline20.Note;
import com.example.timeline20.R;
import com.example.timeline20.adapter.NotesAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Objects;


public class NoteFragment extends Fragment implements View.OnClickListener {
    private LinkedList<Note> notesList = new LinkedList<Note>() {
    };
    SharedPreferences prefs;
    View view;


//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if(savedInstanceState != null){
//            notesList = (LinkedList<Note>) savedInstanceState.getSerializable("SavedNOtesList");
//
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        if(prefs != null){

            String serializedObject = prefs.getString("SavedNotesList", null);
            if(serializedObject != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<LinkedList<Note>>(){}.getType();
                notesList = gson.fromJson(serializedObject, type);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_note, container, false);
        MaterialButton addNoteButton = view.findViewById(R.id.add_note_button);
        addNoteButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(1, true);
        ListView listView = (ListView) getActivity().findViewById(R.id.notes_scrollView);
        NotesAdapter adapter = new NotesAdapter(this.getContext(), R.layout.note_layout, notesList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ChangeNoteActivity.class);
                Note note = (Note)adapterView.getAdapter().getItem(i);
                intent.putExtra("Title", note.GetLabel());
                intent.putExtra("Text", note.GetText());
                intent.putExtra("NoteObject", note);
                intent.putExtra("layoutNum", 0);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onDestroy() {


        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(1, false);
        super.onDestroy();

    }

    @Override
    public void onStop() {

        prefs = getContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedList<Note>>(){}.getType();
        String json = gson.toJson(notesList, type);
        editor.putString("SavedNotesList", json).apply();
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_note_button) {

            Intent intent = new Intent(getActivity(), ChangeNoteActivity.class);
            intent.putExtra("Title", "");
            intent.putExtra("Text", "");
            startActivityForResult(intent, 1);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Note newNote;
        if(resultCode == 2){
            int id = (int) data.getIntExtra("note", -1);
            int itemIndex = findNoteIndexById(id, notesList);
            if(itemIndex != -1){
                notesList.remove();
            }


        }
        else{
            if(resultCode == Activity.RESULT_FIRST_USER){
                newNote = (Note) data.getSerializableExtra("note");
                notesList.add(0, newNote);
            }
            else{
                newNote = (Note) data.getSerializableExtra("note");
                notesList.set(findNoteIndexById(newNote.GetId(), notesList), newNote);
            }

        }
        ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.fragment_note);

        ListView listView = (ListView) layout.findViewById(R.id.notes_scrollView);
        NotesAdapter adapter = new NotesAdapter(this.getContext(), R.layout.note_layout, notesList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ChangeNoteActivity.class);
                Note note = (Note)adapterView.getAdapter().getItem(i);
                intent.putExtra("Title", note.GetLabel());
                intent.putExtra("Text", note.GetText());
                intent.putExtra("NoteObject", note);
                intent.putExtra("layoutNum", 0);
                startActivityForResult(intent, 1);
            }
        });
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