package com.example.timeline20;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;


public class NoteFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
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
}