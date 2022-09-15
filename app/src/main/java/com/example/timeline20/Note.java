package com.example.timeline20;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.timeline20.Additional.IdMaker;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Note implements Serializable,Comparable<Note> {
    private int id;
    private String label;
    private String noteText;
    private LocalDateTime time;

    private int lineCount;

    public Note(String label, String noteText, LocalDateTime time, int lineCount){
        this.id = IdMaker.getId();
        this.label = label;
        this.noteText = noteText;
        this.lineCount = lineCount;
        this.time = time;
    }
    public Note(String label, String noteText, int lineCount){
        this.id = IdMaker.getId();
        this.label = label;
        this.noteText = noteText;
        this.lineCount = lineCount;
    }

    public void SetLabel(String label){
        this.label = label;
    }
    public void SetNoteText(String text){
        this.noteText = text;
    }
    public String GetLabel(){
        return label;
    }
    public String GetText(){
        return noteText;
    }
    public int GetLineCount(){return lineCount;}
    public int GetId(){
        return id;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int GetTimeInMinutes(){
        return time.getMinute() + time.getHour() * 60;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int GetDateInDays(){

        int a = time.getDayOfYear();
        return a;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compareTo(Note note) {
        if(this.GetTimeInMinutes() > note.GetTimeInMinutes()){
            return 1;
        }
        else if(this.GetTimeInMinutes() == note.GetTimeInMinutes()){
            return 0;
        }
        else{
            return -1;
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String GetTime(){
        int hours = time.getHour();
        int minutes = time.getMinute();
        return String.valueOf(hours) + ":" + String.valueOf(minutes);
    }
}
