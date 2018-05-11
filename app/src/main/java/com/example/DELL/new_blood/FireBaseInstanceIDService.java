package com.example.DELL.new_blood;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by DELL on 2/24/2018.
 */


public class FireBaseInstanceIDService extends FirebaseInstanceIdService {

    public static String token;
    @Override
    public void onTokenRefresh() {



        token = FirebaseInstanceId.getInstance().getToken();

        registerToken(token);
    }

    public String getToken(){
        return this.token;
    }
    private void registerToken(String token) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .build();

        Request request = new Request.Builder()
                .url("http://ennovayt.com/blood/add_token.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
