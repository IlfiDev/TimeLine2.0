package com.example.timeline20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.timeline20.adapter.NotesAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class NoteFragment extends Fragment implements View.OnClickListener {
    private LinkedList<Note> notesList = new LinkedList<Note>() {
    };
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("COCK", "Dick");
        view = inflater.inflate(R.layout.fragment_note, container, false);
        MaterialButton addNoteButton = view.findViewById(R.id.add_note_button);
        addNoteButton.setOnClickListener(this);
        System.out.println("ABOBA");
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(1, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(1, false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_note_button) {

            Intent intent = new Intent(getActivity(), ChangeNoteActivity.class);
            intent.putExtra("Title", "");
            intent.putExtra("Text", "");
            startActivityForResult(intent, 1);
        }
        else{

                startIntent(view);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Note newNote;
        if(resultCode == Activity.RESULT_FIRST_USER){
            newNote = (Note) data.getSerializableExtra("note");
            notesList.add(0, newNote);
        }
        else{
            newNote = (Note) data.getSerializableExtra("note");
            for(int i = 0; i < notesList.size(); i++){
                if(notesList.get(i).GetId() == newNote.GetId()){
                    notesList.set(i, newNote);
                    break;
                }
            }
        }

        ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.fragment_note);

        ListView listView = (ListView) layout.findViewById(R.id.notes_scrollView);
        int layout_top_margin = 100;
        NotesAdapter adapter = new NotesAdapter(this.getContext(), R.layout.note_layout, notesList);

        ListView lv;

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ChangeNoteActivity.class);
                Note note = (Note)adapterView.getAdapter().getItem(i);
                intent.putExtra("Title", note.GetLabel());
                intent.putExtra("Text", note.GetText());
                intent.putExtra("NoteObject", note);
                startActivityForResult(intent, 1);
            }
        });
//        for (int i = 0; i < notesList.size(); i++){
//
//            ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.fragment_note);
//            ScrollView scView = (ScrollView) layout.findViewById(R.id.notes_scrollView);
//            ConstraintLayout placeForNotes = (ConstraintLayout) scView.findViewById(R.id.place_for_notes);
//
//            View noteView = getLayoutInflater().inflate(R.layout.note_layout, placeForNotes, false);
//
//            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) noteView.getLayoutParams();
//            params.topMargin = (int) layout_top_margin;
//            params.height = (notesList.get(i).GetLineCount() * 500);
//            noteView.setLayoutParams(params);
//
//            layout_top_margin += 100;
//            TextView textView = (TextView) noteView.findViewById(R.id.note_label);
//            textView.setText(notesList.get(i).GetLabel());
//            placeForNotes.addView(noteView);
//            noteView.setOnClickListener(this);

//        }


        //item.refreshDrawableState();


    }
    public void startIntent(View v){

        Intent intent = new Intent(getActivity(), ChangeNoteActivity.class);
        intent.putExtra("Title", notesList.get(0).GetLabel());
        intent.putExtra("Text", notesList.get(0).GetText());
        startActivityForResult(intent, 1);

    }
}