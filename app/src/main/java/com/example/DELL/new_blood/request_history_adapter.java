package com.example.DELL.new_blood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by DELL on 3/10/2018.
 */
public class request_history_adapter extends ArrayAdapter<request_history_model> {

    Context context;
    int layoutResourceId;
    ArrayList<request_history_model> data=new ArrayList<request_history_model>();
    String ID;
    String all_selected_subs;

    public request_history_adapter(Context context, int layoutResourceId, ArrayList<request_history_model> data) {
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
           // holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtDate=(TextView)row.findViewById(R.id.date);

//            holder.button =(Button) row.findViewById(R.id.choose);
//             holder.checkBox = (CheckBox) row.findViewById(R.id.checkBox);


            row.setTag(holder);
        }
        else
        {
            holder = (weatherHolder)row.getTag();
        }

        // weather weather = data[position];
        holder.txtTitle.setText(data.get(position).getReq_blood_type());
        holder.txtDate.setText(data.get(position).getDates());

//        holder.checkBox.setOnCheckedChangeListener(myCheckChangList);
        //      holder.checkBox.setTag(position);
        // holder.checkBox.setChecked(p.box);



//        Uri myUri = Uri.parse(data.get(position).getImage().toString());
//
//
//        //String picture = "https://sikandariqbal.net/Rahnuma/images/pic1.jpg";
//        String picture = "http://ennovayt.com/blood/"+data.get(position).getImage().toString();
//        new DownLoadImageTask2(holder.imgIcon).execute(picture);

        final Integer pos = position;
//        holder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String shit = data.get(pos).getReq_id();
////                String guideid1 = data.get(pos).getID();
//                Intent myintent=new Intent(context, Active_donor.class).putExtra("req_id", shit);
//
//                context.startActivity(myintent);
//            }
//        });
        //  holder.imgIcon.setImageBitmap(mIcon_val);

        // holder.imgIcon.setImageResource();

        return row;
    }

    static class weatherHolder
    {
       // ImageView imgIcon;
        TextView txtTitle;
        TextView txtDate;
        Button button;
    }
    request_history_model getProduct(int position) {
        return ((request_history_model) getItem(position));
    }

//    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
//        public void onCheckedChanged(CompoundButton buttonView,
//                                     boolean isChecked) {
//            getProduct((Integer) buttonView.getTag()).box = isChecked;
//        }
//    };





}