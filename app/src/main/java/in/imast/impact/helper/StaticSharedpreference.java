package in.imast.impact.helper;

import android.content.Context;
import android.content.SharedPreferences;

import in.imast.impact.R;


/**
 * Created by akram on 3/21/2016.
 */
public class StaticSharedpreference {

    public static String saveInfo(String key, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Aludecor", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

        return key;
    }

    public static String getInfo(String key, Context context) {
        String value = "";
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Aludecor", context.MODE_PRIVATE);
            value = sharedPreferences.getString(key, "");
            return value;

        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static void deleteSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Aludecor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    public static String saveInfoForgot(String key, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Aludecor unclear", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        return key;
    }

    public static String getInfoForgot(String key, Context context) {
        String value = "";
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Aludecor unclear", context.MODE_PRIVATE);
            value = sharedPreferences.getString(key, "");

            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static String saveInt(String key, int value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Aludecor", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();

        return key;
    }

    public static int getInt(String key, Context context) {
        int value = 0;
        try {

            SharedPreferences sharedPreferences = context.getSharedPreferences("Aludecor", context.MODE_PRIVATE);

            value = sharedPreferences.getInt(key, 0);

            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static void removeKey(String key, Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
 }
