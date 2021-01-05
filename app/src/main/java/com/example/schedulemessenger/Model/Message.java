package com.example.schedulemessenger.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_table")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int messageId;

    /** Each type of message has a number corresponding to it:
     *  1 - Sms Message
     *  2 - WhatsApp Message
     *  3 - Email
     *  4 - Instagram Message
     *  5 - Instagram Story                                  */
    @ColumnInfo(name = "message_type")
    private int messageType;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "message_content")
    private String messageText;

    /** Stores the time, in millis, from the scheduling of message till its scheduled time */
    @ColumnInfo(name = "message_send_time")
    private long timeInterval;

    /** Stores the scheduled time as a formatted String */
    @ColumnInfo(name = "time_string")
    private String timeString;

    /** Each message has a status corresponding to it:
     *  Pending - the message has not been sent yet
     *  Sent - the message has been sent             */
    @ColumnInfo(name = "message_status")
    private String messageStatus;

    @ColumnInfo(name = "instagram_username")
    private String instaUsername;

    /** To store image URI as a String, for sending images as messages, or Instagram stories  */
    @ColumnInfo(name = "image_uri_string")
    private String imageUri;

    public Message() {
    }

    /* Getter and setter methods for all data members */

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getInstaUsername() {
        return instaUsername;
    }

    public void setInstaUsername(String instaUsername) {
        this.instaUsername = instaUsername;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

}
