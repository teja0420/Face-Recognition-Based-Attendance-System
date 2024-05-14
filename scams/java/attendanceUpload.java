package com.example.scams;

import android.util.Log;

public class attendanceUpload {

    private String userName;
    private String imageURL;
    private String subject;
    private String regNo;
    private String date;

    public attendanceUpload(){


    }

    public attendanceUpload(String imageurl, String regno, String sub, String username, String Date ){

        this.userName = username;
        this.imageURL = imageurl;
        this.subject = sub;
        Log.i("inner class",this.subject);
        //Log.i("blap",subject+" "+userName+"_"+ regNo+" end");
        this.regNo = regno;
        this.date=Date;

    }


    public String getuserName(){

        return this.userName;

    }

    public void setuserName(String username){

        userName = username;

    }

    public String getImageURL(){

        return this.imageURL;

    }

    public void setImageURL(String imageurl){

        imageURL = imageurl;

    }

    public String getSubject(){

        return this.subject;

    }

    public void setSubject(String sub) {
        subject = sub;
    }

    public String getRegistrationNumber(){

        return this.regNo;

    }

    public void setRegistrationNumber(String regno){

        regNo = regno;

    }

    public String getdate(){

        return this.date;

    }

    public void setdate(String Date){

        date = Date;

    }

}
