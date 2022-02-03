package com.example.lepton;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LeptonAppUtil {

    public static String getMacAddress(Context ctx) {
        String address = "";
        if(ctx == null)return address;
       /* WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        @SuppressLint("HardwareIds") address = info.getMacAddress();*/
        address = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

        //TelephonyManager telephonyManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        // telephonyManager.getDeviceId();

        return address;
    }

    public static String getAppVersion(Context context) {
        if(context == null)return "";
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException ignored) {
            throw new RuntimeException("Could not get package name: " + ignored);
        }
        assert info != null;
        return info.versionName;//String.format(context.getString(R.string.version), info.versionName);
    }

    public static boolean isNetworkConnected(Context ctx) {
        if(ctx == null)return false;
        ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return  activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public static boolean isMobileDataConnected(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return  isConnected;
  /*      if (!isConnected)
        {
            return tr;
        }else
            return false;*/

    }


    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }



    public static long getInternalSpace(){
        long megAvailable=0;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable;
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        }
        else {
            bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        }
        megAvailable = bytesAvailable / (1024 * 1024);
        Log.e("","Available MB : "+megAvailable);
        return megAvailable;
    }

    public static String getFormatDate(String dateString, String dateStringDateFormat,String desiredDateFormate) {
        if (dateString == null || dateString.isEmpty()) return "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateStringDateFormat, Locale.ENGLISH);
            SimpleDateFormat output = new SimpleDateFormat(desiredDateFormate, Locale.ENGLISH);
            Date d = sdf.parse(dateString);
            return output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }

    }

    public static String getCurrentYear(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy",Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return formatter.format(calendar.getTime());
    }


    public static double calculationByDistance(LatLng start, LatLng end) {
        if(start==null)
            return 0.00;
        int Radius = 6371;// radius of earth in Km
        double lat1 = start.latitude;
        double lat2 = end.latitude;
        double lon1 = start.longitude;
        double lon2 = end.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c * 1000;
        if (Double.isNaN(valueResult)) {
            valueResult = 0.0;
        }
        return valueResult;
    }

    public static int parseInt(String intValue) {
        try {
            if (intValue!=null&& !intValue.isEmpty())
                return Integer.parseInt(intValue);
            else
                return 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static double parseDouble(String intValue) {
        try {
            return Double.parseDouble(intValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String formatDouble(double intValue) {
        try {
            return new DecimalFormat("#0.00").format(intValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String formatDoubleuptoSix(double intValue) {
        try {
            return new DecimalFormat("#0.000000").format(intValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long parseLong(String longValue) {
        if (longValue==null || longValue.isEmpty() )
            return 0;
        try {
            return Long.parseLong(longValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        String formattedStr = "";
        String [] camelCaseArray = original.split("_");
        for(int i = 0; i < camelCaseArray.length; i++){
            if(camelCaseArray[i].length() > 0) {
                formattedStr = formattedStr + " "+ camelCaseArray[i].substring(0, 1).toUpperCase() + camelCaseArray[i].substring(1);
            }
        }
        return formattedStr;
    }

    public static String[] splitStringFromSpecificChar(String origin,String regex){
        if(origin == null || origin.isEmpty())return null;
        if(origin.contains(regex)){
            int length = origin.length();
            int regexIndex = origin.indexOf(regex);
            String prefix = origin.substring(0,regexIndex);
            String suffix = origin.substring(regexIndex,length);
            return new String[]{prefix,suffix};
        }
        return null;

    }

    public static double formatDecimalValue(double value){
        DecimalFormat doubleFormatter = new DecimalFormat("####0.000000");
        double parsedValue =  parseDouble(doubleFormatter.format(value));
        if(parsedValue == 0)return value;
        else return parsedValue;
    }



}
