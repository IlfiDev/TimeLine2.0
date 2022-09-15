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
    public LocalDateTime time;
    private String timeStr;
    private int lineCount;

    public Note(String label, String noteText, LocalDateTime time, int lineCount){
        this.id = IdMaker.getId();
        this.label = label;
        this.noteText = noteText;
        this.lineCount = lineCount;
        this.time = time;
        if(this.time != null){
            this.timeStr = time.toString();
        }
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
        time = LocalDateTime.parse(timeStr);
        return time.getMinute() + time.getHour() * 60;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int GetDateInDays(){
        time = LocalDateTime.parse(timeStr);
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

//        time = LocalDateTime.parse(timeStr);
//        int hours = time.getHour();
//        String hourstr;
//        if(hours / 10 <= 0){
//            hourstr = "0" + String.valueOf(hours);
//        }
//        else{
//            hourstr = String.valueOf(hours);
//        }
//
//        int minutes = time.getMinute();
//        String minutesstr;
//        if(minutes / 10 <= 0){
//            minutesstr = "0" + String.valueOf(minutes);
//        }
//        else{
//            minutesstr = String.valueOf(minutes);
//        }
//        return hourstr + ":" + minutesstr;
        return timeStr.split("T")[1].split(":")[0] + ":" + timeStr.split("T")[1].split(":")[1];
    }
    public void SetTime(LocalDateTime time){
        this.time = time;
//        this.timeStr = this.time.toString();
    }
    public void SetStrTime(){
        //this.timeStr = this.time.toString();
    }

}
