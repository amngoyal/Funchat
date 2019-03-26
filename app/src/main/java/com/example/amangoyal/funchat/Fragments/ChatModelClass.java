package com.example.amangoyal.funchat.Fragments;

public class ChatModelClass {

    private String seen;
    private String timestamp;
    private String name;
    private String thumbImage;

    public ChatModelClass(String seen, String timestamp,String name,String thumbImage ) {
        this.seen = seen;
        this.timestamp = timestamp;
        this.name = name;
        this.thumbImage = thumbImage;
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
}
