package com.example.DELL.new_blood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by DELL on 3/10/2018.
 */

public class donation_adapter extends ArrayAdapter<donation_model> {
    Context context;
    int layoutResourceId;
    ArrayList<donation_model> data=new ArrayList<donation_model>();
    String ID;
    String phone;
    String all_selected_subs;
    TextView txtTitle1;
    public donation_adapter(Context context, int layoutResourceId, ArrayList<donation_model> data) {
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

            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.textView=(TextView)row.findViewById(R.id.phonenum);
            holder.button =(Button) row.findViewById(R.id.message);
            holder.button1 =(Button) row.findViewById(R.id.call);
            holder.don_date=(TextView)row.findViewById(R.id.donation_date);
            holder.don_blood=(TextView)row.findViewById(R.id.blood_type);



            row.setTag(holder);
        }
        else
        {
            holder =  (weatherHolder)row.getTag();
        }


        holder.txtTitle.setText(data.get(position).getName());
        holder.textView.setText(data.get(position).getPhone());


         holder.don_blood.setText(data.get(position).getReq_blood_type());
         holder.don_date.setText(data.get(position).getDates());
        final View row1=row;




        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                phone = ((TextView) row1.findViewById(R.id.phonenum)).getText().toString();

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

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", phone);
                smsIntent.putExtra("sms_body","Your msg");
                context.startActivity(smsIntent);
            }
        });



        final Integer pos = position;

        return row;
    }

    static class weatherHolder
    {

        TextView txtTitle;
        TextView textView;

        Button button1;
        Button button;
        TextView don_date;
        TextView don_blood;
    }
    donation_model getProduct(int position) {
        return ((donation_model) getItem(position));
    }

}

