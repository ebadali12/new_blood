package com.example.DELL.new_blood;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DELL on 3/10/2018.
 */

public class UtilsClipCodes {

    public static String FileName = "MyFileName";
    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

   public static void SharedPrefesSAVE(Context ctx,String Name){
        SharedPreferences prefs = ctx.getSharedPreferences("NAME", 0);
        SharedPreferences.Editor prefEDIT = prefs.edit();
        prefEDIT.putString("Name", Name);
        prefEDIT.commit();
    }
}
