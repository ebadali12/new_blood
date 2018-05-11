package com.example.DELL.new_blood;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;

public class user_profile extends AppCompatActivity {

    TextView name;
    TextView email;
    TextView phone;
    TextView city;
    TextView blood;



    Button submit;
    ImageView img ;
    String USER_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        img = (ImageView) findViewById(R.id.pro);
        name = (TextView) findViewById(R.id.namex);
        email = (TextView) findViewById(R.id.emailx);
        phone = (TextView) findViewById(R.id.phonex);
        city = (TextView) findViewById(R.id.cityx);
        blood = (TextView) findViewById(R.id.bloodx);
        USER_ID = getIntent().getStringExtra("USER_ID");

        submit = (Button) findViewById(R.id.edit);




        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String url = "http://ennovayt.com/blood/get_profile.php";
        String user_id = getIntent().getStringExtra("USER_ID");

        Downloaderzz d = new Downloaderzz(user_profile.this,url,user_id,name,email,phone,city,blood,img);
        d.execute();




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(user_profile.this,update_profile.class));
                Intent i = new Intent(user_profile.this,update_profile.class);
                i.putExtra("USER_ID", USER_ID);

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




class Downloaderzz extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;
    ListView lv;
    String CITY = null;
    String selected_city;

    ProgressDialog pd;
    String all_selected_subs;

    String ID;
    String url1;
    String req_id;
    String user_id;

    TextView name;
    TextView email;
    TextView phone;
    TextView city;
    TextView blood;
    ImageView img;

    public Downloaderzz(Context c, String url,String user_id, TextView name, TextView email, TextView phone, TextView city, TextView blood, ImageView img) {
        this.c = c;
        this.user_id = user_id;
        this.lv = lv;
        this.url1 = url;


        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.blood = blood;
        this.img = img;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Fetch Data");
        pd.setMessage("Fething data.....please wait");
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
            Parserzz p=new Parserzz(c,s,name,email,phone,city,blood,img);
            p.execute();
        }else
        {
            Toast.makeText(c,"Unable to download data",Toast.LENGTH_SHORT).show();

        }
    }

    private String DownloadData() throws IOException {
        InputStream is = null;
        String line = null;
        try {
            URL url = new URL(url1);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();


            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream OS = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(user_id, "UTF-8");
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


class Parserzz extends AsyncTask <Void,Integer,Integer>{



    Context c;
    String data;
    ListView lv;
    ProgressDialog pd;

    ArrayList<String> players=new ArrayList<>();
    String ID;
    String all_selected_subs;
    ArrayList<request_history_model> data_list = new ArrayList<request_history_model>();


    TextView name;
    TextView email;
    TextView phone;
    TextView city;
    TextView blood;
    ImageView img;


    public String names;
    public String emails;
    public String phones;
    public String citys;
    public String bloods;
    public String imgs;


    public Parserzz(Context c, String data,TextView name, TextView email, TextView phone, TextView city, TextView blood, ImageView img) {
        this.c = c;
        this.data = data;
        this.lv = lv;
        this.ID=ID;
        this.all_selected_subs=all_selected_subs;
        // this.check_box_data = check_box_data;

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.blood = blood;
        this.img = img;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Parsing Data");
        pd.setMessage("Parsing.....please wait");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parser();


    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(integer==1)
        {



            name.setText(names);
            email.setText(emails);
            phone.setText(phones);
            city.setText(citys);
            blood.setText(bloods);
            new DownLoadImageTask4(img).execute("http://ennovayt.com/blood/"+imgs);





        }
        else
        {
            Toast.makeText(c,"Unable to parse data",Toast.LENGTH_SHORT).show();


        }
        pd.dismiss();
    }
    private int parser()
    {
        try
        {
            JSONArray ja=new JSONArray(data);
            JSONObject jo=null;

            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                names=jo.getString("name");
                emails= jo.getString("email");
                phones = jo.getString("phone");
                citys = jo.getString("city");
                bloods= jo.getString("blood_type");
                imgs= jo.getString("image");

;
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}


class DownLoadImageTaskc extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTaskc(ImageView imageView){
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



