package com.example.DELL.new_blood;

/**
 * Created by DELL on 3/10/2018.
 */

public class request_history_model {

    String req_id;
    String user_id;
    String req_blood_type;
    String dates;
    String lat;
    String lng;


    public request_history_model(String req_id, String user_id, String req_blood_type, String dates, String lat, String lng){
        this.req_id = req_id;
        this.user_id = user_id;
        this.req_blood_type= req_blood_type;
        this.dates = dates;
        this.lat = lat;
        this.lng = lng;
    }

    public String getReq_id(){
        return this.req_id;
    }

    public String getUser_id(){
        return this.user_id;
    }
    public String getReq_blood_type(){
        return this.req_blood_type;
    }
    public String getDates(){
        return this.dates;
    }
    public String getLat(){
        return this.lat;
    }
    public String getLng(){
        return this.lng;
    }

}
