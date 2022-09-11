package com.example.timeline20;

import com.example.timeline20.Additional.IdMaker;

import java.io.Serializable;
import java.sql.Time;

public class Note implements Serializable {
    private int id;
    private String label;
    private String noteText;
    private Time date_time = null;
    private int lineCount;

    public Note(String label, String noteText, Time time, int lineCount){
        this.id = IdMaker.getId();
        this.label = label;
        this.noteText = noteText;
        this.date_time = time;
        this.lineCount = lineCount;
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
}
