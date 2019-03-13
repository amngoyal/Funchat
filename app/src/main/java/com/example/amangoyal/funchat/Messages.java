package com.example.amangoyal.funchat;

public class Messages {

    private String message;
    private String type;
    private String time;
    private String seen;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }


    public Messages( String message, String type, String time, String seen) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
    }


}
