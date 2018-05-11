package com.example.DELL.new_blood;

/**
 * Created by DELL on 3/10/2018.
 */

public class donation_model {
    String R_id;
    String U_id;
    String user_id;
    String req_blood_type;
    String dates;
    String name;
    String phone;

    public donation_model(String R_id, String U_id, String user_id, String req_blood_type, String dates, String name,String phone){
        this.R_id = R_id;
        this.U_id = U_id;
        this.user_id = user_id;
        this.req_blood_type=req_blood_type;
        this.dates = dates;
        this.name = name;
        this.phone=phone;
    }

    public String getR_id(){
        return this.R_id;

    }
    public String getU_id(){
        return this.U_id;
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
    public String getName(){
        return this.name;
    }
    public String getPhone(){
        return this.phone;
    }
}
