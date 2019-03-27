package com.example.amangoyal.funchat.Fragments;

public class ChatModelClass {

    private String seen;
    private String timestamp;
    private String name;
    private String thumbImage;
    private String uid;
    private String lastMessage;

    public ChatModelClass(String seen, String timestamp, String name, String thumbImage, String uid, String lastMessage) {
        this.seen = seen;
        this.timestamp = timestamp;
        this.name = name;
        this.thumbImage = thumbImage;
        this.uid = uid;
        this.lastMessage = lastMessage;

    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
