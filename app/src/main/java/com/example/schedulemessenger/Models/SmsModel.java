package com.example.schedulemessenger.Models;

//Class which provides model for each sms
public class SmsModel {

    private String phoneNumber; //Stores phone number of recipient
    private String smsMessage; //Stores message typed out by user
    private long time; //Stores time selected by user

    public SmsModel(String phoneNumber, String smsMessage, long time) {
        this.phoneNumber = phoneNumber;
        this.smsMessage = smsMessage;
        this.time = time;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
