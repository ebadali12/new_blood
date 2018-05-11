package com.example.DELL.new_blood;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 4/18/2017.
 */

public class weather1 implements Parcelable {

    String name;
    String msg;
    String img;
    String address;
    String ID;
    String date_time;



    weather1(String name, String msg, String img, String address, String ID,String date_time) {
        this.name = name;
        this.date_time = date_time;
        this.msg = msg;
        this.img = img;
        this.address = address;
        this.ID = ID;

    }
    public String getID(){
        return this.ID;
    }

    public String getName(){
        return this.name;

    }
    public String getMsg(){
        return this.msg;
    }
    public String getImg(){
        return this.img;
    }
    public String getAddress(){
        return this.address;
    }
    public String getDate_time() {
        return this.date_time;
    }


    protected weather1(Parcel in) {
        name = in.readString();
        msg = in.readString();
        img = in.readString();
        address = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(msg);
        dest.writeString(img);
        dest.writeString((address));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<weather> CREATOR = new Parcelable.Creator<weather>() {
        @Override
        public weather createFromParcel(Parcel in) {
            return new weather(in);
        }

        @Override
        public weather[] newArray(int size) {
            return new weather[size];
        }
    };
}
