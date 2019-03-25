package com.example.amangoyal.funchat.Fragments;

public class ChatModelClass {

    private String  seen;
    private String timestamp;

    public ChatModelClass(String seen, String timestamp){
        this.seen = seen;
        this.timestamp = timestamp;
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
}
