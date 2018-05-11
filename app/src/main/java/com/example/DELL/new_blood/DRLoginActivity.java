package com.example.DELL.new_blood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * A login screen that offers login via email/password.
 */
public class DRLoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    String address = "http://ennovayt.com/blood/signin.php";
    Downloader5 d ;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drlogin);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Donors & Recepient");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        signup = (Button) findViewById(R.id.email_sign_up_button);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View view) {
                String email = mEmailView.getText().toString();
                String pass = mPasswordView.getText().toString();

                if((mEmailView.getText().toString() != null && !mEmailView.getText().toString().isEmpty()) && (mPasswordView.getText().toString() != null && !mPasswordView.getText().toString().isEmpty())) {

                    String tok = FirebaseInstanceId.getInstance().getToken();
                    d = new Downloader5(DRLoginActivity.this, mEmailView.getText().toString(), mPasswordView.getText().toString(), address,tok);
                    d.execute();
                }else{
                    Toast.makeText(DRLoginActivity.this, "One of the fields are empty",Toast.LENGTH_LONG).show();
                }

            }
        });

        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DRLoginActivity.this, DRsignupActivity.class));
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



class Downloader5 extends AsyncTask<Void,Integer,String> {

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




    public Downloader5(Context c, String email, String password, String address,String token) {
        this.c = c;
        pd=new ProgressDialog(c);
        this.address = address;

        this.email = email;
        this.password = password;
        this.token=token;


    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        pd=new ProgressDialog(c);
        pd.setTitle("Signing in");
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


            String ss = new String(s);

            if(ss.contains(":")){


                Intent i = new Intent(c,drawer.class);
                i.putExtra("USER_ID", s);
                i.putExtra("name", s);
                i.putExtra("email", email);
                c.startActivity(i);




                UtilsClipCodes.saveSharedSetting(c, "ClipCodes", "false");
                UtilsClipCodes.SharedPrefesSAVE(c, email);
                Intent ImLoggedIn = new Intent(c, drawer.class);

                SharedPreferences sharedPreferences = c.getSharedPreferences("PrefName", MODE_PRIVATE);
                sharedPreferences.edit().putString("USER_ID", s).apply();
                sharedPreferences.edit().putString("name", s).apply();
                sharedPreferences.edit().putString("email", email).apply();



            }
            else{

                Toast.makeText(c,"Wrong cradentials",Toast.LENGTH_LONG).show();
            }



        }else
        {
            Toast.makeText(c,"Unable to download data",Toast.LENGTH_SHORT).show();

        }
    }

    private String DownloadData() throws IOException {
        InputStream is = null;
        String line = null;
        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add city post method
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream OS = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data =  URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"+ URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"+ URLEncoder.encode("token", "UTF-8")+"="+URLEncoder.encode(token, "UTF-8");
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



