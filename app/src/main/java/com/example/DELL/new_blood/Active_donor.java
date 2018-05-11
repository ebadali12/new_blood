package com.example.DELL.new_blood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

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

public class Active_donor extends AppCompatActivity {

    PullRefreshLayout layout;
    Button refresh;
    ListView listView;

    int i=65;
    int j=90;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_donor);


        listView = (ListView) findViewById(R.id.list);


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbars);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Donors");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final String req_id = getIntent().getStringExtra("req_id");


        String url = "http://ennovayt.com/blood/get_donors.php";

        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
// listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                String url = "http://ennovayt.com/blood/get_donors.php";
                //Toast.makeText(Active_donor.this,req_id,Toast.LENGTH_LONG).show();
                Downloader3 d = new Downloader3(Active_donor.this, listView,url,req_id,layout);
                d.execute();

            }
        });


// refresh complete


        //Toast.makeText(Active_donor.this,req_id,Toast.LENGTH_LONG).show();
        Downloader3 d = new Downloader3(Active_donor.this, listView,url,req_id,layout);
        d.execute();

//
//        final String latt = getIntent().getStringExtra("lat");
//        final String lngg = getIntent().getStringExtra("lng");

//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                if(i!=90) {
////                    char chars = (char) i;
////                    i++;
////                    String s = "" + chars;
////                    arrayList.add(s);
////                    arrayAdapter.notifyDataSetChanged();
////                }
//
//            }
//        });
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





class Downloader3 extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;
    ListView lv;
    String CITY = null;
    PullRefreshLayout layout;
    String selected_city;
    ///ArrayList<String> check_box_data = new ArrayList<String>();
    ProgressDialog pd;
    String all_selected_subs;
    // publlistAdapter boxAdapter;
    String ID;
    String url1;
    String req_id;
    public Downloader3(Context c,ListView lv, String url,String req_id, PullRefreshLayout layout ) {
        this.c = c;
        this.layout = layout;
        this.req_id = req_id;
        this.lv = lv;
        this.url1 = url;
        // this.check_box_data = check_box_data;

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
            Parser3 p=new Parser3(c,s,lv,layout);
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

            //add city post method
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream OS = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("req_id", "UTF-8")+"="+URLEncoder.encode(req_id, "UTF-8");
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


class Parser3 extends AsyncTask <Void,Integer,Integer>{

//    public listAdapter boxAdapter;

    Context c;
    String data;
    ListView lv;
    ProgressDialog pd;
    PullRefreshLayout layout;
    //    ArrayList<String> check_box_data = new ArrayList<String>();
    ArrayList<String> players=new ArrayList<>();
    String ID;
    String all_selected_subs;
    ArrayList<donor_model> data_list = new ArrayList<donor_model>();

    //  public listAdapter get_adapter(){
//        return this.boxAdapter;
//    }

    public Parser3(Context c,  String data, ListView lv, PullRefreshLayout layout) {
        this.c = c;
        this.layout = layout;
        this.data = data;
        this.lv = lv;
        this.ID=ID;
        this.all_selected_subs=all_selected_subs;
        // this.check_box_data = check_box_data;
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
//            ArrayAdapter<String> adapter=new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,players);
//            lv.setAdapter(adapter);

            notif_adapter Wadapter = new notif_adapter(c, R.layout.row6,data_list);
            lv.setAdapter(Wadapter);
            layout.setRefreshing(false);
//
//           boxAdapter = new listAdapter(c, data_list);
//
//            lv.setAdapter(boxAdapter);


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(c,"HELLOOO",Toast.LENGTH_LONG); }
            });

//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position,
//                                        long id) {
//
//                    final long[] checkedIds = lv.getCheckItemIds();
//                    for (int i=0;i<checkedIds.length;i++)
//                        Toast.makeText(c,checkedIds.toString(),Toast.LENGTH_LONG);
//
//                }
//            });






            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                    String shit = data_list.get(pos).getName();
//          String guideid1 = data.get(pos).getID();
                    Intent myintent=new Intent(c, donor_profile.class).putExtra("name", shit);
                    myintent.putExtra("phone",data_list.get(pos).getPhone());
                    myintent.putExtra("blood",data_list.get(pos).getBlood());
                    myintent.putExtra("image",data_list.get(pos).getImage());
                    myintent.putExtra("lat",data_list.get(pos).getLat());
                    myintent.putExtra("long",data_list.get(pos).getLng());
                    c.startActivity(myintent);

                    //final String lat=getIntent().getStringExtra("lat");
                    //final String lng=getIntent().getStringExtra("long");

                }
            });
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
            //    players.clear();
            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                String name=jo.getString("name");
                String email = jo.getString("email");
                String phone = jo.getString("phone");
                String city = jo.getString("city");
                String image = jo.getString("image");
                String token = jo.getString("token");

                String lat = jo.getString("lat");
                String lng = jo.getString("lng");
                String user_id = jo.getString("user_id");
                String blood = jo.getString("blood_type");
                // players.add(name);
                data_list.add(new donor_model(user_id,name,email,phone,blood,city,image,token,lat,lng));
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
