package com.example.amangoyal.funchat.Fragments;

class FriendRequestModelClass {
    private String name;
    private String image;
    private String uid;
    private String date;
    private String thumb_image;
    private String currentUserUid;



    public FriendRequestModelClass(String name, String image, String status, String thumb_image, String uid,String currentUserUid) {
        this.uid = uid;
        this.name = name;
        this.date = status;
        this.thumb_image = thumb_image;
        this.currentUserUid = currentUserUid;
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

    public String getCurrentUserUid() {
        return currentUserUid;
    }

    public void setCurrentUserUid(String currentUserUid) {
        this.currentUserUid = currentUserUid;
    }
}
