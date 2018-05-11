package com.example.DELL.new_blood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
 * Created by DELL on 3/9/2018.
 */


public class notif_adapter extends ArrayAdapter<donor_model> {

    Context context;
    int layoutResourceId;
    ArrayList<donor_model> data=new ArrayList<donor_model>();
    String ID;
    String all_selected_subs;

    public notif_adapter(Context context, int layoutResourceId, ArrayList<donor_model> data) {
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



            row.setTag(holder);
        }
        else
        {
            holder = (weatherHolder)row.getTag();
        }


        holder.txtTitle.setText(data.get(position).getName());



        Uri myUri = Uri.parse(data.get(position).getImage().toString());


        //String picture = "https://sikandariqbal.net/Rahnuma/images/pic1.jpg";
        String picture = "http://ennovayt.com/blood/"+data.get(position).getImage().toString();
        new DownLoadImageTask2(holder.imgIcon).execute(picture);

        final Integer pos = position;

        return row;
    }

    static class weatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView textView;
        Button button;
    }
    donor_model getProduct(int position) {
        return ((donor_model) getItem(position));
    }






}

class DownLoadImageTask3 extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTask3(ImageView imageView){
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
