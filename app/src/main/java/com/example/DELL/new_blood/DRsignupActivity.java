package com.example.DELL.new_blood;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DRsignupActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText password;
    EditText phone;

    Spinner Btype;
    Spinner spinnerCity;
    ImageView image;
    Button add_image;
    Button submit;
    private String[] arraySpinner;
    private String[] citySpinner;
    private static final String TAG = "myApp";


    String names ;
    String emails ;
    String passwords ;
    String phones ;
    String cities;
    String selected ;
    String citySelected;

    public static final String UPLOAD_URL = "http://ennovayt.com/blood/upload_image.php";
Downloader4 d;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    String address = "http://ennovayt.com/blood/signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drsignup);

        name = (EditText) findViewById(R.id.nameText);
        email = (EditText) findViewById(R.id.EmailText);
        password = (EditText) findViewById(R.id.PasswordText);
        phone = (EditText) findViewById(R.id.PhoneText);
        image = (ImageView) findViewById(R.id.imageView);

        add_image = (Button) findViewById(R.id.ImageButton);
        submit = (Button) findViewById(R.id.signup);

        Btype = (Spinner) findViewById(R.id.Btype);
        spinnerCity=(Spinner)findViewById(R.id.Btype1);

        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Requesting storage permission
        requestStoragePermission();
        //setting spinner values (main city)

        //citySpinner
        this.arraySpinner = new String[] {
                "A+", "A-", "B+", "B-", "O-","O+"
        };

        this.citySpinner = new String[] {
                "Islamabad", "Rawalpindi", "Karachi", "Lahore", "Faisalabad","Multan","Peshawar","Quetta","Sialkot","Abbottabad", "Gwadar", "Bahawalpur", "Gujranwala", "Khairpur","Jhelum"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);

        Btype.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, citySpinner);

        spinnerCity.setAdapter(adapter1);

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        names = name.getText().toString();
        emails = email.getText().toString();
        passwords = password.getText().toString();
        phones = phone.getText().toString();

        selected = Btype.getSelectedItem().toString();
        citySelected=spinnerCity.getSelectedItem().toString();
        final Boolean[] res = {false};

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






            if(emailValidator(email.getText().toString())) {
                boolean fieldsOK = validate(new EditText[]{name, email, password, phone});

                if ((filePath != null) && fieldsOK) {
                    FirebaseMessaging.getInstance().subscribeToTopic("test");
                    String tok = FirebaseInstanceId.getInstance().getToken();
                    d = new Downloader4(DRsignupActivity.this, name.getText().toString(), email.getText().toString(), password.getText().toString(), phone.getText().toString(), spinnerCity.getSelectedItem().toString(), Btype.getSelectedItem().toString(), address, tok);
                    d.execute();
                    uploadMultipart();
                }
                else {
                    Toast.makeText(DRsignupActivity.this, "Some of the fields are missing", Toast.LENGTH_LONG).show();
                }
            }
            else{

            Toast.makeText(DRsignupActivity.this,"Email not correct", Toast.LENGTH_LONG).show();

            }


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


    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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
    /*
    * This is the method responsible for image upload
    * We need the full image path and the name for the image in this method
    * */
    public void uploadMultipart() {
        //getting name for the image
        String names = name.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", email.getText().toString()) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {

            Log.w(TAG,exc.getStackTrace().toString());
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }






    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}

class Downloader4 extends AsyncTask<Void,Integer,String> {

    Context c;
    String address;

    ///ArrayList<String> check_box_data = new ArrayList<String>();
    ProgressDialog pd;

    String name;
    String email;
    String password;
    String phone;
    String city;
    String blood_type;
    String token;


    // publlistAdapter boxAdapter;

    public Downloader4(Context c,String name, String email, String password, String phone, String city, String bood,String address,String token) {
        this.c = c;
        this.token = token;
this.address = address;
//pd= new ProgressDialog(c);
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.blood_type = bood;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       pd=new ProgressDialog(c);
        pd.setTitle("Signing up");
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
//            Parser3 p=new Parser3(c,s,firstname,username,email,mobile,city,image);
//            p.execute();
            Toast.makeText(c,s,Toast.LENGTH_SHORT).show();
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
        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add city post method
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            OutputStream OS = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"+ URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"+ URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"+ URLEncoder.encode("phone", "UTF-8")+"="+URLEncoder.encode(phone, "UTF-8")+"&"+ URLEncoder.encode("city", "UTF-8")+"="+URLEncoder.encode(city, "UTF-8")+"&"+ URLEncoder.encode("blood", "UTF-8")+"="+URLEncoder.encode(blood_type, "UTF-8")+"&"+ URLEncoder.encode("token", "UTF-8")+"="+URLEncoder.encode(token, "UTF-8");
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

