package com.example.DELL.new_blood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;


import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

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


public class drawer extends AppCompatActivity implements LocationListener {
    //  private GoogleMap mMap;
    private static final int PROFILE_SETTING = 100000;


    //data
    public String guide_first_name;
    public String guide_last_name;
    public String guide_id;

    String total1, total2;

    public String USER_ID;

    public double lat, lng;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;


    String ID;
    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);


        SharedPreferences sharedPreferences = getSharedPreferences("PrefName", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);

        String[] animalsArray = name.split(":");
        String name1 = animalsArray[1].toString();
        final String id1 = animalsArray[0].toString();




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



                        save_loc ss = new save_loc(drawer.this,id1,total1,total2);
                        ss.execute();



                    }


                });


        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbars);
        toolbar.setTitle("Home");

        toolbar.setTitleTextColor(Color.WHITE);




        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName(name1).withEmail(email).withIcon("https://sikandariqbal.net/Rahnuma/guides_images/sam.jpg").withIdentifier(100);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)

                .withHeaderBackground(R.drawable.header).withTextColor(Color.WHITE)
                .addProfiles(
                        profile


                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)

                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(

                        new PrimaryDrawerItem().withName("Request blood").withIcon(R.drawable.b3).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName("My Profile").withIcon(R.drawable.man_icon).withIdentifier(5).withSelectable(false),
                        new PrimaryDrawerItem().withName("Request History").withIcon(R.drawable.hammad).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName("Donation History").withIcon(R.drawable.hammad).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName("Events").withIcon(R.drawable.delivery_date).withIdentifier(6).withSelectable(false),
                        new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.icon1).withIdentifier(4).withSelectable(false)

                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
//                                intent = new Intent(MainActivity.this, Main2Activity.class);
//                                intent.putExtra("EXTRA_SESSION_ID", ID);
//                                startActivity(intent);
//
//                                 startActivity(new Intent(drawer.this,request_blood.class));

                                Intent i = new Intent(drawer.this,request_blood.class);
                                i.putExtra("USER_ID", id1);
                                startActivity(i);


                            } else if (drawerItem.getIdentifier() == 2) {

                                Intent i = new Intent(drawer.this,request_history.class);
                           i.putExtra("USER_ID", id1);
                                startActivity(i);


//
//                                intent = new Intent(MainActivity.this, Main3Activity.class);
//                                intent.putExtra("EXTRA_SESSION_ID", ID);
//                                startActivity(intent);


                            } else if (drawerItem.getIdentifier() == 3) {
                                Intent i = new Intent(drawer.this,donation_history.class);
                                i.putExtra("USER_ID", id1);
                                startActivity(i);





                            } else if (drawerItem.getIdentifier() == 4) {



                                UtilsClipCodes.saveSharedSetting(drawer.this, "ClipCodes", "true");
                                UtilsClipCodes.SharedPrefesSAVE(getApplicationContext(), "");
                                Intent LogOut = new Intent(getApplicationContext(), DRLoginActivity.class);
                                startActivity(LogOut);
                                finish();

                            }
                            else if (drawerItem.getIdentifier() == 5) {

                                Intent i = new Intent(drawer.this,user_profile.class);
                                i.putExtra("USER_ID", id1);
                                startActivity(i);



                            }
                            else if (drawerItem.getIdentifier() == 6) {

                                Intent i = new Intent(drawer.this,event_user.class);
                               // i.putExtra("USER_ID", id1);
                                startActivity(i);



                            }

                            if (intent != null) {

                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
//                .withShowDrawerUntilDraggedOpened(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        //RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);
        // new RecyclerViewCacheUtil<IDrawerItem>().withCacheSize(2).apply(result.getRecyclerView(), result.getDrawerItems());

        //only set the active selection or active profile if we do not recreate the activity




        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11

//tour list
            FragmentManager manager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
      transaction.replace(R.id.frame_container, new map_fragment()).commit();






            result.setSelection(21, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

        result.updateBadge(4, new StringHolder(10 + ""));
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}


class save_loc extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;

    ///ArrayList<String> check_box_data = new ArrayList<String>();
    ProgressDialog pd;

    String name;
    String email;
    String password;
    String phone;
    String city;

    String lat;
    String lng;

    String blood_type;


    String USER_ID;
    // publlistAdapter boxAdapter;

    public save_loc(Context c, String USER_ID, String lat, String lng) {
        this.c = c;
        this.address = address;
this.USER_ID = USER_ID;
        this.email = email;
        this.password = password;
        this.lat = lat;
        this.lng = lng;


    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        pd=new ProgressDialog(c);
//        pd.setTitle("Fetch Data");
//        pd.setMessage("Fething data.....please wait");
//        pd.show();
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

//        pd.dismiss();
        if (s!=null)
        {
//            Parser3 p=new Parser3(c,s,firstname,username,email,mobile,city,image);
//            p.execute();
            // Toast.makeText(c,"Updated"+s,Toast.LENGTH_SHORT).show();

//            if(!s.equals("0")){
              //  Toast.makeText(c,"Address saved : "+s,Toast.LENGTH_LONG).show();
//                c.startActivity(new Intent(c,drawer.class));
//                Intent i = new Intent(c,drawer.class);
//                i.putExtra("USER_ID", s);
//                c.startActivity(i);

//            }
//            else{
//                Toast.makeText(c,"Wrong cradentials",Toast.LENGTH_LONG).show();
//            }




//            new AlertDialog.Builder(c)
//                    .setTitle("Your Alert")
//                    .setMessage("Your Message")
//                    .setCancelable(false)
//                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Whatever...
//                        }
//                    }).show();




        }else
        {
            Toast.makeText(c,"Unable to download data",Toast.LENGTH_SHORT).show();

        }
    }

    private String DownloadData() throws IOException {
        InputStream is = null;
        String line = null;
        String urls = "http://ennovayt.com/blood/add_location.php";
        try {
            URL url = new URL(urls);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add city post method
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream OS = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =  URLEncoder.encode("USER_ID", "UTF-8")+"="+URLEncoder.encode(USER_ID, "UTF-8")+"&"+ URLEncoder.encode("lat", "UTF-8")+"="+URLEncoder.encode(lat, "UTF-8")+"&"+ URLEncoder.encode("lng", "UTF-8")+"="+URLEncoder.encode(lng, "UTF-8");
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



