package com.example.timeline20;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.timeline20.Additional.IdMaker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.chrono.MinguoChronology;

public class Note implements Serializable,Comparable<Note> {
    private int id;
    private String label;
    private String noteText;
    public String timeStr;
    private int lineCount;

    public Note(String label, String noteText, String time, int lineCount){
        this.id = IdMaker.getId();
        this.label = label;
        this.noteText = noteText;
        this.lineCount = lineCount;
        this.timeStr = time;

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

        LocalDateTime time = LocalDateTime.parse(timeStr);
        return time.getMinute() + time.getHour() * 60;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int GetDateInDays(){
        LocalDateTime time = LocalDateTime.parse(timeStr);
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
    public String GetDateTime(){

        return timeStr;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String GetTime(){

        return timeStr.split("T")[1].split(":")[0] + ":" + timeStr.split("T")[1].split(":")[1];
    }
    public void SetTime(String time){
        this.timeStr = time;
//        this.timeStr = this.time.toString();
    }
    public void SetStrTime(){
        //this.timeStr = this.time.toString();
    }
    public String GetDate(){
        return timeStr.split("T")[0].split("-")[2] + "." + timeStr.split("T")[0].split("-")[1];
    }

}
