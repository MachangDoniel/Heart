package com.example.cardiacrecorder;

public class Details {
    public int heartrate, systolic, diastolic, id;
    public String  comment, name, date, time;
//    Date date;
//    Time time;

    public Details(){}

    public Details(int heartrate, int systolic, int diastolic, int id, String comment, String name, String date, String time) {
        this.heartrate = heartrate;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.id = id;
        this.comment = comment;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public int getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(int heartrate) {
        this.heartrate = heartrate;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
