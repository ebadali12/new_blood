package com.example.DELL.new_blood;

/**
 * Created by DELL on 3/9/2018.
 */

public class donor_model {
    String user_id;
    String name;
    String email;
    String phone;
    String blood;
    String city;
    String image;
    String token;
    String lat;
    String lng;


    public donor_model(String user_id, String name, String email, String phone, String blood, String city, String image, String token, String lat, String lng){
        this.user_id = user_id;
        this.name =name;
        this.email = email;
        this.phone = phone;
        this.blood = blood;
        this.city = city;
        this.image = image;
        this.token = token;
        this.lat = lat;
        this.lng = lng;
    }

    public String getUser_id(){
        return this.user_id;
    }
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPhone(){
        return this.phone;
    }
    public String getBlood(){
        return this.blood;
    }
    public String getCity(){
        return this.city;
    }
    public String getImage(){
        return this.image;
    }
    public String getToken(){
        return this.token;
    }
    public String getLat(){
        return this.lat;
    }
    public String getLng(){
        return this.lng;
    }
}
