package com.example.amangoyal.funchat;

public class Messages {

    private String message;
    private String type;
    private long time;
    private Boolean seen;

    public Messages(){

    }

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }


    public Messages(String message, String type, long time, Boolean seen) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
    }


}
