package com.example.DELL.new_blood;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class donor_profile extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Button call;
    Button Location;
    Button msg;
    TextView donorBlood;
    TextView donorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);

        imageView = (ImageView) findViewById(R.id.dimage);
        textView = (TextView) findViewById(R.id.profile);
        Location =(Button)findViewById(R.id.location);
        donorPhone=(TextView) findViewById(R.id.donor_phone);
        donorBlood=(TextView)findViewById(R.id.donor_blood);

        call = (Button) findViewById(R.id.call);
        msg = (Button) findViewById(R.id.msg);

        String image = getIntent().getStringExtra("image");
        final String name = getIntent().getStringExtra("name");
        String blood = getIntent().getStringExtra("blood");
        final String phone = getIntent().getStringExtra("phone");
        final String lat=getIntent().getStringExtra("lat");
        final String lng=getIntent().getStringExtra("long");

        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Donor Information");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textView.setText(name);
        donorBlood.setText(blood);
        donorPhone.setText(phone);

        String picture = "http://ennovayt.com/blood/" + image;
        new DownLoadImageTask4(imageView).execute(picture);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(donor_profile.this, "Calling : "+phone,Toast.LENGTH_LONG).show();

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String p = "tel:"+phone ;
                callIntent.setData(Uri.parse(p));
                if (ActivityCompat.checkSelfPermission(donor_profile.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
//                       public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                              int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    startActivity(callIntent);
                    return;
                }



            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", phone);
                smsIntent.putExtra("sms_body","Your msg");
                startActivity(smsIntent);
            }
        });

        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               Intent i = new Intent(donor_profile.this,Donor_onMap.class);
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                i.putExtra("phone", phone);
               i.putExtra("name", name);
               startActivity(i);
            }
        });
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


class DownLoadImageTask4 extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTask4(ImageView imageView){
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
