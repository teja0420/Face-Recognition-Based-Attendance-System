package com.example.scams;

public class Upload {

    private String mname;
    private String mImageUrl;
    private String mRegistrationNumber;
    private String mSmartCard;
    private String mClass;
    private String mEmailID;

    public Upload(){


    }

    public Upload(String emailID, String name, String imageUrl,String registrationNumber,String smartCard,String classs) {

        if (name.trim().equals("") || registrationNumber.trim().equals("") || smartCard.trim().equals("") || classs.trim().equals("") ) {
            name = "No Name";
            registrationNumber="No Registration Number";
            smartCard = "No Smart Card";
            classs= "No Class";

        }

        mEmailID = emailID;
        mname = name;
        mImageUrl = imageUrl;
        mRegistrationNumber=registrationNumber;
        mSmartCard = smartCard;
        mClass = classs;

    }

    public String getEmailid(){

        return mEmailID;

    }

    public void setEmailid(String emailId){

        mEmailID = emailId;

    }

    public String getName(){

        return mname;

    }

    public void setName(String name){

        mname = name;

    }
    public String getImageUrl(){

        return mImageUrl;

    }

    public void setImageUrl(String imageUrl){

        mImageUrl = imageUrl;

    }

    public String getRegistrationNumber(){

        return mRegistrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber){

        mRegistrationNumber = registrationNumber;

    }

    public String getSmartCard(){

        return mSmartCard;

    }

    public void setSmartCard(String smartCard){

        mSmartCard=smartCard;

    }

    public String getClasss(){

        return mClass;

    }

    public void setClasss(String classs){

        mClass = classs;

    }
}
