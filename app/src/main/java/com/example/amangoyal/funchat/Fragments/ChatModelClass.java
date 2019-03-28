package com.example.amangoyal.funchat.Fragments;

public class ChatModelClass {

    private String seen;
    private long timestamp;
    private String name;
    private String thumb_image;
    private String uid;
    private String last_message;

    public ChatModelClass(){

    }

    public ChatModelClass(String seen, long timestamp, String name, String thumbImage, String uid, String lastMessage) {
        this.seen = seen;
        this.timestamp = timestamp;
        this.name = name;
        this.thumb_image = thumbImage;
        this.uid = uid;
        this.last_message = lastMessage;

    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
