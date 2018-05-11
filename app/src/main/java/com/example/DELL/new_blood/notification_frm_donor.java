package com.example.DELL.new_blood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class notification_frm_donor extends AppCompatActivity {


    TextView name;
    TextView address;
    TextView phone;
    TextView blood;
    TextView image;

    float lat;
    float lng;

    Toolbar toolbar;

    Button map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_frm_donor);


        name = (TextView) findViewById(R.id.dname_text);
      //  address = (TextView) findViewById(R.id.dadress_text);
        phone = (TextView) findViewById(R.id.dphone_text);
        blood = (TextView) findViewById(R.id.dblood_text);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Donor Information");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        String name1 = getIntent().getStringExtra("name");
        String req_id = getIntent().getStringExtra("req_id");

        String phone1 = getIntent().getStringExtra("phone");

        String blood1 = getIntent().getStringExtra("blood");

        String address1 = getIntent().getStringExtra("lat")+ ":" +getIntent().getStringExtra("lng");

        String image1 = getIntent().getStringExtra("image");

        image = (TextView) findViewById(R.id.dimage_text);


        final String latt = getIntent().getStringExtra("lat");
        final String lngg = getIntent().getStringExtra("lng");
        name.setText("Name : "+name1);
      //  address.setText(address1);
        phone.setText("req_id : "+req_id);
        blood.setText("Blood Group : "+blood1);
        image.setText("Check their location on map by clicking below");
        String image_url = "http://ennovayt.com/blood/" + image1;


        // show The Image in a ImageView
        new DownloadImageTask((ImageView) findViewById(R.id.img),notification_frm_donor.this)
                .execute(image_url);


        map = (Button) findViewById(R.id.map);




        SingleShotLocationProvider.requestSingleUpdate(this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        ///Log.d("Location", "my location is " + location.toString());
                        lat = location.latitude;
                        lng = location.longitude;


//
//                        u_lat = String.valueOf(lat);
//                        u_lng = String.valueOf(lng);



                    }


                });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //                Toast.makeText(notification_activity.this,USER_ID,Toast.LENGTH_LONG).show();
              //  Toast.makeText(notification_activity.this, token,Toast.LENGTH_LONG).show();
                String uri = "http://maps.google.com/maps?daddr=" + latt + "," + lngg + " (" + "Blood donation location" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);



            }
        });



    }
}
class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    ProgressDialog pd;

    public DownloadImageTask(ImageView bmImage, Context c) {
        this.bmImage = bmImage;
        pd=new ProgressDialog(c);

    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.setTitle("Processing");
        pd.setMessage("please wait");
        pd.show();
    }

    protected void onPostExecute(Bitmap result) {
        pd.dismiss();
        bmImage.setImageBitmap(result);
    }
}
