package com.example.timeline20.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.timeline20.ChangeNoteActivity;
import com.example.timeline20.MainActivity;
import com.example.timeline20.Note;
import com.example.timeline20.R;
import com.example.timeline20.adapter.EventAdapter;
import com.example.timeline20.adapter.NotesAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.LineNumberInputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;


public class CalendarFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    LinkedList<LinkedList<Note>> datesList = new LinkedList<LinkedList<Note>>();
    View view;
    SharedPreferences prefs;

    String parameterListToDelete = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        prefs = getContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        if(prefs != null){

            String serializedObject = prefs.getString("SavedEventsList", null);
            if(serializedObject != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<LinkedList<LinkedList<Note>>>(){}.getType();
                datesList = gson.fromJson(serializedObject, type);
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        Button addEventButton = view.findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(2, true);
        SetupListView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(2, false);
    }

    @Override
    public void onStop() {
        prefs = getContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedList<LinkedList<Note>>>(){}.getType();
        String json = gson.toJson(datesList, type);
        editor.putString("SavedEventsList", json).apply();

        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Note newNote;
        LinkedList<Note> notesList;
        if(resultCode == 2){
            int id = (int) data.getIntExtra("note", -1);
            if(id != -1){
                int date = (int) data.getIntExtra("noteDate", -1);
                int listIndex = findListIdByDate(date, datesList);
                notesList = datesList.get(listIndex);
                notesList.remove(findNoteIndexById(id, notesList));
                if(datesList.get(listIndex).size() == 0){
                    datesList.remove(listIndex);
                }
            }

        }
        else{
            newNote = (Note) data.getSerializableExtra("note");
            newNote.SetStrTime();
            int listIndex = findListIdByDate(newNote.GetDateInDays(), datesList);
            LinkedList<Note> tempList;
            if(listIndex == -1){
                tempList = null;
            }else{
                tempList = datesList.get(listIndex);
            }

            if(tempList == null){
                notesList = new LinkedList<Note>();
                notesList.add(0, newNote);
                datesList.add(0, notesList);
                sortDatesList(datesList);
            }
            else{
                notesList = tempList;
                if(resultCode == Activity.RESULT_FIRST_USER){

                    notesList.add(0, newNote);
                    Collections.sort(notesList);

                }
                else{
                    int itemIndex = findNoteIndexById(newNote.GetId(), notesList);
                    if(notesList.get(itemIndex).GetDateInDays() != newNote.GetDateInDays() ){
                        notesList.remove(itemIndex);
                    }
                    else{
                        notesList.set(itemIndex, newNote);
                        Collections.sort(notesList);
                    }
                }
            }

        }
        SetupListView(view);
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
    private void SetupListView(View view){
        ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.fragment_calendar);
        HorizontalScrollView scrollView = (HorizontalScrollView) layout.findViewById(R.id.events_scrollview);
        ConstraintLayout innerLayout = (ConstraintLayout) scrollView.findViewById(R.id.place_for_events);
        DisplayMetrics ds = getResources().getDisplayMetrics();
        innerLayout.removeAllViewsInLayout();

        float top_date_leftMargin = 20 * ds.density;
        float default_top_date_leftMargin = 117 * ds.density;
        float top_date_topMargin = 12 * ds.density;

        float event_leftMargin = 12 * ds.density;
        float default_event_leftMargin = 117 * ds.density;
        float event_topMargin = 48 * ds.density;

        TextView no_events = getView().findViewById(R.id.no_events_textview);
        if(datesList.size() != 0) {
            View something = new View(getContext());
            something.setBackgroundResource(R.drawable.shape_event_container);
            ConstraintLayout.LayoutParams something_params = new ConstraintLayout.LayoutParams((int) (event_leftMargin +
                    (default_event_leftMargin * (datesList.size()))), (int) (112 * ds.density));
            something_params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            something_params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            something.setLayoutParams(something_params);
            innerLayout.addView(something);
            no_events.setVisibility(View.GONE);
        } else {
            no_events.setVisibility(View.VISIBLE);
        }

        for(int i = 0; i < datesList.size(); i++){
            ListView listView = (ListView) getLayoutInflater().inflate(R.layout.inflateable_listview, null);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams((int) (106 * ds.density), ConstraintLayout.LayoutParams.WRAP_CONTENT);
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            params.topMargin = (int) event_topMargin;
            params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            params.leftMargin = (int) event_leftMargin;
            event_leftMargin += default_event_leftMargin;
            listView.setLayoutParams(params);
            EventAdapter adapter = new EventAdapter(this.getContext(), R.layout.event_layout, datesList.get(i));
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), ChangeNoteActivity.class);
                    Note note = (Note)adapterView.getAdapter().getItem(i);
                    intent.putExtra("Title", note.GetLabel());
                    intent.putExtra("Text", note.GetText());
                    intent.putExtra("time", note.GetDateTime());
                    intent.putExtra("NoteObject", note);
                    intent.putExtra("layoutNum", 1);
                    intent.putExtra("event_time", note.timeStr);
                    intent.putExtra("isNewEvent", false);
                    startActivityForResult(intent, 1);
                }
            });
            innerLayout.addView(listView);
            ConstraintLayout topDateLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.top_date_layout, null);
            ConstraintLayout.LayoutParams otherParams = new ConstraintLayout.LayoutParams((int) (90 * ds.density), (int) (24 * ds.density));
            otherParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            otherParams.topMargin = (int) top_date_topMargin;
            otherParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            otherParams.leftMargin = (int) top_date_leftMargin;
            top_date_leftMargin += default_top_date_leftMargin;

            topDateLayout.setLayoutParams(otherParams);

            topDateLayout.setOnClickListener(this);
            TextView top_date_full_date = topDateLayout.findViewById(R.id.top_date_full_date);
            top_date_full_date.setText(datesList.get(i).getFirst().GetFullFullDate());

            TextView text = (TextView) topDateLayout.getViewById(R.id.top_date_textview);
            text.setText(datesList.get(i).getFirst().GetFullDate());
            innerLayout.addView(topDateLayout);
        }

        TextView event_dates = getView().findViewById(R.id.event_dates_textview);
        String gap_event_dates;
        if (datesList.size() != 0) {
            if (datesList.size() == 1) {
                gap_event_dates = datesList.getFirst().getFirst().GetFullDate();
            } else {
                gap_event_dates = datesList.getFirst().getFirst().GetFullDate() +
                        " - " + datesList.getLast().getLast().GetFullDate();
            }
        } else {
            gap_event_dates = " - ";
        }
        event_dates.setText(gap_event_dates);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_event_button:
                Intent intent = new Intent(getActivity(), ChangeNoteActivity.class);
                intent.putExtra("Title", "");
                intent.putExtra("Text", "");
                intent.putExtra("time", LocalDateTime.now().toString());
                intent.putExtra("layoutNum", 1);
                intent.putExtra("isNewEvent", true);

                startActivityForResult(intent, 1);
                break;
            case R.id.top_date_layout:
                TextView top_date_full_date = view.findViewById(R.id.top_date_full_date);
                parameterListToDelete = top_date_full_date.getText().toString();
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
                break;

            default:
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CreateEvent(View view){
        Intent intent = new Intent(getActivity(), ChangeNoteActivity.class);
        intent.putExtra("Title", "");
        intent.putExtra("Text", "");
        intent.putExtra("time", LocalDateTime.now().toString());
        intent.putExtra("layoutNum", 1);

        startActivityForResult(intent, 1);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sortDatesList(LinkedList<LinkedList<Note>> datesList){
        for(int i = 0; i < datesList.size() - 1; i++){
            if (datesList.get(i).get(0).GetDateInDays() > datesList.get(i + 1).get(0).GetDateInDays()){
                LinkedList<Note> tempList = datesList.get(i);
                datesList.remove(i);
                datesList.add(i+1, tempList);
            }
            else{
                return;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private int findListIdByDate(int dateInDays, LinkedList<LinkedList<Note>> datesList) {
        for (int i = 0; i < datesList.size(); i++) {
            if (datesList.get(i).getFirst().GetDateInDays() == dateInDays) {
                return i;
            }
        }
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        String[] temp_data_arr;
        if(!parameterListToDelete.equals("")) {
            temp_data_arr = parameterListToDelete.split("\\.");
        } else {
            return false;
        }
        Calendar temp_calendar_date = Calendar.getInstance();


        temp_calendar_date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp_data_arr[0]));
        temp_calendar_date.set(Calendar.MONTH, Integer.parseInt(temp_data_arr[1])-1);
        temp_calendar_date.set(Calendar.YEAR, Integer.parseInt(temp_data_arr[2]));

        int dateInDays = temp_calendar_date.get(Calendar.DAY_OF_YEAR);

        int listToDelete = findListIdByDate(dateInDays, datesList);
        if (listToDelete != -1) {
            datesList.remove(listToDelete);
        }

        SetupListView(requireView());

        return false;
    }
}