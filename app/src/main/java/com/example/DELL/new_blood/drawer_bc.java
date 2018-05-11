package com.example.DELL.new_blood;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class drawer_bc extends AppCompatActivity {


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
        setContentView(R.layout.activity_drawerbc);

        //Remove line to test RTL support
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

         String email = getIntent().getStringExtra("email");
         String name= getIntent().getStringExtra("name");

           USER_ID = getIntent().getStringExtra("USER_ID");


          String[] animalsArray = name.split(":");

        String name1 = animalsArray[1].toString();
        final String id1 = animalsArray[0].toString();

       // Toast.makeText(drawer_bc.this,name1 + " : " +id1,Toast.LENGTH_LONG).show();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbars);
        toolbar.setTitle("Map");
        toolbar.setTitleTextColor(Color.WHITE);
        int color = ((ColorDrawable) toolbar.getBackground()).getColor();
        toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(Color.WHITE);
        USER_ID = id1;

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
                        new PrimaryDrawerItem().withName("Add an event").withDescription("").withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName("My history").withDescription("").withIdentifier(2).withSelectable(false)

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

                                Intent i = new Intent(drawer_bc.this, Add_event.class);
                                i.putExtra("USER_ID", USER_ID);
                                startActivity(i);


                            } else if (drawerItem.getIdentifier() == 2) {
                                //  startActivity(new Intent(MainActivity.this,Main3Activity.class));


//
                                intent = new Intent(drawer_bc.this, event_history.class);
                                intent.putExtra("USER_ID", USER_ID);
                                startActivity(intent);


                            } else if (drawerItem.getIdentifier() == 3) {

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
            transaction.replace(R.id.frame_container, new drawer_fragment_bc()).commit();


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

}
