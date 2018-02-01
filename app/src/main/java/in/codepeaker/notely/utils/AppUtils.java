package in.codepeaker.notely.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by github.com/codepeaker on 1/2/18.
 */

public class AppUtils {
    public static void setSharedPreferences(Context context, String key, String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, string);
        editor.apply();
    }

    public static void setSharedPreferences(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public static String getStringpreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static int getIntpreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }
}
