package com.example.DELL.new_blood;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class request_blood extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;

    Spinner blood;
    EditText msg;
    Button submit;
    TextView address;
    Button location;
    private String[] arraySpinner;
    EditText name;
    public String USER_ID;
    Downloader6 d;
    Downloader8 d2 ;
    String request_ID;
    String total1, total2;
    String lat2,lng2;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    String address1;
    public double lat,lng;
    EditText radius;
    private static final String TAG = "request_blood";
    String radius_km;
    TextView location1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);
        location1=(TextView)findViewById(R.id.address1);

        blood = (Spinner) findViewById(R.id.blood_type);
        msg = (EditText) findViewById(R.id.msg1);
        submit = (Button) findViewById(R.id.submit);
        location=(Button)findViewById(R.id.location);
        address = (TextView) findViewById(R.id.address);
        name = (EditText) findViewById(R.id.nameText);
        radius = (EditText) findViewById(R.id.radius);
        this.arraySpinner = new String[]{
                "A+", "A-", "B+", "B-", "O-", "O+"
        };

        USER_ID = getIntent().getStringExtra("USER_ID");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        blood.setAdapter(adapter);

        SingleShotLocationProvider.requestSingleUpdate(this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        ///Log.d("Location", "my location is " + location.toString());
                        lat = location.latitude;
                        lng = location.longitude;
                        total1 = String.valueOf(lat);
                        total2 = String.valueOf(lng);

                        String add = total1 + " : " + total2;


                        String lat1 = String.valueOf(lat);
                        String lng2 = String.valueOf(lng);
                        address1 =lat1+" :  " + lng2;
                        address.setText(lat1+" :  " + lng2);

                    }


                });


        final String msgs = msg.getText().toString();
        final String name1 = name.getText().toString();


        radius_km = radius.getText().toString();

        USER_ID = getIntent().getStringExtra("USER_ID");
        final String live = getIntent().getStringExtra("LIVE");

        lat2 = getIntent().getStringExtra("lat2");
        lng2 = getIntent().getStringExtra("lng2");


        final String  token = FirebaseInstanceId.getInstance().getToken();



        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request Blood");
        toolbar.setTitleTextColor(Color.WHITE);

        if(isServicesOK()) {
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(request_blood.this,MapActivity.class);
                    intent.putExtra("user_id", USER_ID);
                    intent.putExtra("token",token);
                    startActivity(intent);


                }
            });


        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("Request Blood");
        }


        if(live != null && !live.isEmpty()) {

            location1.setText("New location set");
        }

        else{
            location1.setText("OR current user location");

        }



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean fieldsOK = validate(new EditText[] { name, msg,radius });
                if(live != null && !live.isEmpty()) {
                    if(fieldsOK) {
                        if(Integer.parseInt(radius.getText().toString()) < 20) {
                            d = new Downloader6(request_blood.this, name.getText().toString(), lat2, lng2, blood.getSelectedItem().toString(), msg.getText().toString(), token, USER_ID, radius.getText().toString());
                            d.execute();
                        }
                        else{
                            Toast.makeText(request_blood.this, "Radius too big",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(request_blood.this,"Some of the fields missing",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if(fieldsOK) {
                        if(Integer.parseInt(radius.getText().toString()) < 20) {
                        d = new Downloader6(request_blood.this, name.getText().toString(), total1, total2, blood.getSelectedItem().toString(), msg.getText().toString(), token, USER_ID, radius.getText().toString());
                        d.execute();
                    }
                        else{
                            Toast.makeText(request_blood.this, "Radius too big",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(request_blood.this,"Some of the fields missing",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });



    }


    private boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                return false;
            }
        }
        return true;
    }



    public boolean isServicesOK(){

        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(request_blood.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(request_blood.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


class Downloader6 extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;


    ProgressDialog pd;

    String name;
    String email;
    String password;
    String phone;
    String city;
    String blood_type;
    String token;
    String lat;
    String lng;
    String msg;
    String USER_ID;

    String radius;
    public Downloader6(Context c,String name,  String lat,String lng,String blood_type,String msg,String token,String USER_ID,String radius) {
        this.c = c;
        this.token=token;
        this.msg = msg;
        this.address = address;
        this.name = name;
        this.radius = radius;
        this.phone = phone;
        this.USER_ID = USER_ID;
        this.blood_type = blood_type;
        this.lat = lat;
        this.lng = lng;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Processing");
        pd.setMessage("please wait");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        String data= null;
        try {
            data = DownloadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

       pd.dismiss();
        if (s!=null)
        {

          Toast.makeText(c,"Request Sent",Toast.LENGTH_SHORT).show();





        }else
        {
            Toast.makeText(c,"Unable to download data",Toast.LENGTH_SHORT).show();

        }
    }

    private String DownloadData() throws IOException {
        InputStream is = null;
        String line = null;
        String url1 = "http://ennovayt.com/blood/push_notification.php";
        try {
            URL url = new URL(url1);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add city post method
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream OS = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"+ URLEncoder.encode("blood", "UTF-8")+"="+URLEncoder.encode(blood_type, "UTF-8")+"&"+ URLEncoder.encode("message", "UTF-8")+"="+URLEncoder.encode(msg, "UTF-8")+"&"+ URLEncoder.encode("token", "UTF-8")+"="+URLEncoder.encode(token, "UTF-8")+"&"+ URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(USER_ID, "UTF-8")+"&"+ URLEncoder.encode("lat", "UTF-8")+"="+URLEncoder.encode(lat, "UTF-8")+"&"+ URLEncoder.encode("lng", "UTF-8")+"="+URLEncoder.encode(lng, "UTF-8")+"&"+ URLEncoder.encode("radius", "UTF-8")+"="+URLEncoder.encode(radius, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();


            //getting list of sub citites
            is = new BufferedInputStream(con.getInputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            if (br != null) {
                while ((line = br.readLine()) != null) {
                    sb.append((line+"\n"));
                }

            }
            else {
                return null;
            }
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null)
            {
                is.close();
            }
        }
        return null;
    }
}



class Downloader8 extends AsyncTask<Void,Integer,String> {

    Context c;


    ///ArrayList<String> check_box_data = new ArrayList<String>();
    ProgressDialog pd;

    String blood_type;
    String token;

    String USER_ID;
    String address;

    String lat;
    String lng;
    String msg;

  public static  String request_ID;


    public String getRequest_ID(){
        return this.request_ID;
    }

    public Downloader8(Context c,String USER_ID,  String blood_type,String lat,String lng) {
        this.c = c;
        this.USER_ID = USER_ID;
        this.blood_type = blood_type;
        this.address = address;

        this.lat = lat;
        this.lng = lng;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... params) {
        String data= null;
        try {
            data = DownloadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        if (s!=null)
        {

                request_ID = s;
              Toast.makeText(c,"Request Sent",Toast.LENGTH_SHORT).show();





        }else
        {
            Toast.makeText(c,"Unable to download data",Toast.LENGTH_SHORT).show();

        }
    }

    private String DownloadData() throws IOException {
        InputStream is = null;
        String line = null;
        String url1 = "http://ennovayt.com/blood/request_blood.php";
        try {
            URL url = new URL(url1);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add city post method
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream OS = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("USER_ID", "UTF-8")+"="+URLEncoder.encode(USER_ID, "UTF-8")+"&"+ URLEncoder.encode("blood", "UTF-8")+"="+URLEncoder.encode(blood_type, "UTF-8")+"&"+ URLEncoder.encode("lat", "UTF-8")+"="+URLEncoder.encode(lat, "UTF-8")+"&"+ URLEncoder.encode("lng", "UTF-8")+"="+URLEncoder.encode(lng, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();


            //getting list of sub citites
            is = new BufferedInputStream(con.getInputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            if (br != null) {
                while ((line = br.readLine()) != null) {
                    sb.append((line+"\n"));
                }

            }
            else {
                return null;
            }
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null)
            {
                is.close();
            }
        }
        return null;
    }
}


