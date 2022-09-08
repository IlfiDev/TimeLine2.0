package com.example.timeline20;

import java.io.Serializable;
import java.sql.Time;

public class Note implements Serializable {
    private String label;
    private String noteText;
    private Time date_time = null;

    public Note(String label, String noteText, Time time){
        this.label = label;
        this.noteText = noteText;
        this.date_time = time;
    }
    public Note(String label, String noteText){
        this.label = label;
        this.noteText = noteText;
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
}
