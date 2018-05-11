package com.example.DELL.new_blood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class event_activity extends AppCompatActivity {

    TextView name1, msg1, address1, notif1;
    Button button;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_activity);

        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String msg = getIntent().getStringExtra("msg");
        String img = getIntent().getStringExtra("img");


        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event information");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageView = (ImageView) findViewById(R.id.image1);
        name1 = (TextView) findViewById(R.id.name1);
        msg1 = (TextView) findViewById(R.id.msg1);
        address1 = (TextView) findViewById(R.id.address1);
        notif1 = (TextView) findViewById(R.id.notif1);


        name1.setText(name);
        msg1.setText(msg);
        address1.setText(address);


        new DownLoadImageTask5(imageView).execute("http://ennovayt.com/blood/"+img);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



class DownLoadImageTask5 extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTask5(ImageView imageView){
        this.imageView = imageView;
    }

    /*
        doInBackground(Params... params)
            Override this method to perform a computation on a background thread.
     */
    protected Bitmap doInBackground(String...urls){
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
            logo = BitmapFactory.decodeStream(is);
        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return logo;
    }

    /*
        onPostExecute(Result result)
            Runs on the UI thread after doInBackground(Params...).
     */
    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);
    }
}
