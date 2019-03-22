package com.example.amangoyal.funchat.Fragments;

public class FriendsModelClass {
    private String name;
    private String image;
    private String uid;
    private String date;
    private String thumb_image;
    private String onlineStatus;


    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public FriendsModelClass(String name, String image, String status, String thumb_image, String uid, String onlineStatus) {
        this.uid = uid;
        this.name = name;
        this.date = status;
        this.image = image;
        this.onlineStatus = onlineStatus;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "UsersModelClass{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", uid='" + uid + '\'' +
                ", date='" + date + '\'' +
                ", thumb_image='" + thumb_image + '\'' +
                '}';
    }

}

