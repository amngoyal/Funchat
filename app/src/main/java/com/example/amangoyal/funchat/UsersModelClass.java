package com.example.amangoyal.funchat;


public class UsersModelClass {
    private String name;
    private String image;

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;
    private String status;
    private String thumb_image;

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    @Override
    public String toString() {
        return "UsersModelClass{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", uid='" + uid + '\'' +
                ", status='" + status + '\'' +
                ", thumb_image='" + thumb_image + '\'' +
                '}';
    }

    public UsersModelClass(String name,String image, String status, String thumb_image, String uid) {
        this.uid = uid;
        this.name = name;
        this.status = status;
        this.image = image;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }
}
