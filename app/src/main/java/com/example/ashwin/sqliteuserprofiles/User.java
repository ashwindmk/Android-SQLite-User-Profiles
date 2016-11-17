package com.example.ashwin.sqliteuserprofiles;

import android.graphics.Bitmap;

/**
 * Created by ashwin on 3/11/16.
 */

public class User {

    int id, age;
    String name, image, imageurl;
    Bitmap bitmapImage;

    public User() {
    }

    public User(int id, String name, String imageurl, Bitmap bitmapImage) {
        this.id = id;
        this.name = name;
        this.imageurl = imageurl;
        this.bitmapImage = bitmapImage;
    }

    public User(int id, String name, String image, int age, String imageurl, Bitmap bitmapImage) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.imageurl = imageurl;
        this.age = age;
        this.bitmapImage = bitmapImage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
        //sample image: http://content.fathead.com/products/96/96-96074.jpg
    }

    public String getImage() {
        return image;
    }

    public void setImageurl(String imageurl)
    {
        this.imageurl = imageurl;
    }

    public String getImageurl()
    {
        return this.imageurl;
    }

    public void setBitmapimage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public Bitmap getBitmapimage() {
        return bitmapImage;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

}
