package com.example.DELL.new_blood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DELL on 3/18/2018.
 */

public class weatherAdapter2 extends ArrayAdapter<weather> {

    Context context;
    int layoutResourceId;
    ArrayList<weather> data=new ArrayList<weather>();
    String ID;
    String all_selected_subs;
    String phone;

    public weatherAdapter2(Context context, int layoutResourceId, ArrayList<weather> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.ID=ID;
        this.all_selected_subs=all_selected_subs;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
           weatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new weatherHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.textView=(TextView)row.findViewById(R.id.txtTitle1);
            holder.textView1=(TextView)row.findViewById(R.id.txtTitle3);
            holder.button =(Button) row.findViewById(R.id.blood_call);
            holder.eventAddress=(TextView)row.findViewById(R.id.event_address);
            holder.eventdate=(TextView)row.findViewById(R.id.event_date);
            holder.eventmessage=(TextView)row.findViewById(R.id.message);


         

            row.setTag(holder);
        }
        else
        {
            holder = (weatherHolder)row.getTag();
        }


        holder.txtTitle.setText(data.get(position).getName());
        holder.textView.setText(data.get(position).getCenter_a());
        holder.textView1.setText(data.get(position).getCenter_phone());
        holder.eventdate.setText(data.get(position).getDate_time());
        holder.eventAddress.setText(data.get(position).getAddress());
        holder.eventmessage.setText(data.get(position).getMsg());



        final View row1=row;

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phone = ((TextView) row1.findViewById(R.id.txtTitle3)).getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String p = "tel:"+phone ;
                callIntent.setData(Uri.parse(p));
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
//                       public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                              int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    context.startActivity(callIntent);
                    return;
                }



            }
        });





        Uri myUri = Uri.parse(data.get(position).getImg().toString());


        String picture = "http://ennovayt.com/blood/"+data.get(position).getImg().toString();
        new DownLoadImageTask2(holder.imgIcon).execute(picture);

        final Integer pos = position;

        return row;
    }

    static class weatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        Button button;
        TextView textView;
        TextView textView1;
        TextView eventAddress;
        TextView eventdate;
        TextView eventmessage;
    }
    weather getProduct(int position) {
        return ((weather) getItem(position));
    }





}


class DownLoadImageTask2 extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTask2(ImageView imageView){
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
