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
 * Created by DELL on 3/21/2018.
 */

public class weatherAdapter3 extends ArrayAdapter<weather1> {

    Context context;
    int layoutResourceId;
    ArrayList<weather1> data=new ArrayList<weather1>();
    String ID;
    String all_selected_subs;

    public weatherAdapter3(Context context, int layoutResourceId, ArrayList<weather1> data) {
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


        holder.txtTitle.setText(data.get(position).getName() +"\n"+data.get(position).getAddress()+"\n"+ data.get(position).getDate_time());



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
    }
    weather1 getProduct(int position) {
        return ((weather1)getItem(position));
    }






}


class DownLoadImageTask6 extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;

    public DownLoadImageTask6(ImageView imageView){
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
