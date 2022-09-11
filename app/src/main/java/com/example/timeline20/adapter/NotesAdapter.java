package com.example.timeline20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.example.timeline20.Note;
import com.example.timeline20.R;

import java.util.List;

public class NotesAdapter extends BaseAdapter {
    private List<Note> list;
    private LayoutInflater layoutInflater;

    public NotesAdapter(Context context, List<Note> list){
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;
        if(newView == null){
            newView = layoutInflater.inflate(R.layout.note_layout, viewGroup, false);

        }
        EditText edit_title = (EditText) newView.findViewById(R.id.note_label_field);
        //EditText edit_content = (EditText) newView.findViewById((R.id.note))
        edit_title.setText(list.get(i).GetLabel());
        return newView;
    }
}
