package in.imast.impact.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.imast.impact.R;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by CodeHunter on 25/3/17.
 */

public class Utilities {

    private ProgressDialog loadingDialog;
    public Activity activity;

    public static int INTENTCAMERA = 4, INTENTGALLERY = 5;
    public static File cameraFile;


    public Utilities(Activity activity) {
        this.activity = activity;

    }




    public  boolean isValidMobileNumber(String s)
    {
        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    public  boolean isValidPANNo(String str)
    {
        // Regex to check valid
        // PAN number
        String regex = "[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher()
        // method to find the matching
        // between the given string
        // and the regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    public  boolean isValidFirm(String str)
    {
        // Regex to check valid
        // PAN number
        String regex = "[a-zA-Z]";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher()
        // method to find the matching
        // between the given string
        // and the regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    public boolean isGSTValid(String  email) {
        boolean isValid = false;

        //  val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        String expression = "^([0-9]{2}[a-zA-Z]{4}([a-zA-Z]{1}|[0-9]{1})[0-9]{4}[a-zA-Z]{1}([a-zA-Z]|[0-9]){3}){0,15}$";

        Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(email);
        if (m.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        Toast.makeText(activity, activity.getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        return false;
    }


    public void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }


    public boolean isValidEmail(String address) {

        if (address != null || !address.equals("")) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(address).matches();
        } else {
            return false;
        }
    }

    public boolean isValidPincode(String pincode) {
        if (pincode == null) {
            return false;
        } else {
            String PINCODE_PATTERN = "^[0-9]{6}$";
            Pattern pattern = Pattern.compile(PINCODE_PATTERN);
            Matcher matcher = pattern.matcher(pincode);
            return matcher.matches();
        }
    }


    public boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public boolean isValidPassword(String password) {
        Pattern p = Pattern.compile("((?!\\s)\\A)(\\s|(?<!\\s)\\S){6,20}\\Z");
        if (password == null) {
            return false;
        } else {
            Matcher m = p.matcher(password);
            return m.matches();
        }
    }


    public boolean isValidName(String firstname) {
        Pattern p = Pattern.compile("^[ a-zA-Z]{3,20}$");   //Will accept any string with a-z or A-Z of 3-20 characters length
        if (firstname == null) {
            return false;
        } else {
            Matcher m = p.matcher(firstname);
            return m.matches();
        }
    }

    public boolean isValidAge(String age) {
        Pattern p = Pattern.compile("^[1-9]{1,3}$");    //will accept all numbers from 1-9 with 1-3 digits (1-999)
        if (age == null || age.equals("")) {
            return false;
        } else {
            Matcher m = p.matcher(age);
            return m.matches();
        }
    }

    public static void datePicker(final EditText edt , Context ctx){


        final Calendar c = Calendar.getInstance();
     int    mYear = c.get(Calendar.YEAR);
        int    mMonth = c.get(Calendar.MONTH);
        int   mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);

        String myDate = "2019/04/01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();

        datePickerDialog.getDatePicker().setMinDate(millis);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);


        datePickerDialog.show();

    }

    public String getDate(long milliSeconds, String dateFormat) {
        /*Sample for data formatter "dd/MM/yyyy hh:mm:ss.SSS"*/
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getDateMonthYear(){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return df.format(c);

    }


    public boolean isEmpty(CharSequence chars) {
        return TextUtils.isEmpty(chars);
    }

    public boolean isEmptyEdittext(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString().trim());
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File thumbimg = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return thumbimg;
    }

    public String getPathFromUri(Uri uri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
             cursor.close();

        return path;
    }

    public String getVideoPathFromUri(Uri uri, Activity activity) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;

    }


    public static File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MP4_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES);

        File thumbimg = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return thumbimg;
    }

    static Random random = new Random();

    public static String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append("ABCDEFGHIJKLMNOP".
                    charAt(random.nextInt("ABCDEFGHIJKLMNOP".length())));

            i++;
        }
        return stringBuilder.toString();
    }

    public static void datepicker(Context context, final EditText editText){


        final Calendar c = Calendar.getInstance();
        int   mYear = c.get(Calendar.YEAR);
        final int  mMonth = c.get(Calendar.MONTH);
        int  mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int yearPick, int monthPick, int dayPick) {

                        int month =monthPick+1;

                        String day,monthStr;

                        if(dayPick<10){
                            day ="0"+dayPick;
                        }else{
                            day =""+dayPick;
                        }

                        if (month < 10) {
                            monthStr = "0" + month;
                        } else {
                            monthStr = "" + month;
                        }

                        editText.setText(day+"-"+monthStr+"-"+yearPick);

                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }
    public static void datepicker(Context context, final TextView editText){


        final Calendar c = Calendar.getInstance();
        int   mYear = c.get(Calendar.YEAR);
        final int  mMonth = c.get(Calendar.MONTH);
        int  mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int yearPick, int monthPick, int dayPick) {

                        int month =monthPick+1;

                        String day,monthStr;

                        if(dayPick<10){
                            day ="0"+dayPick;
                        }else{
                            day =""+dayPick;
                        }

                        if (month < 10) {
                            monthStr = "0" + month;
                        } else {
                            monthStr = "" + month;
                        }

                        editText.setText(yearPick +"-"+monthStr+"-"+day);

                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

}
