package com.example.admin.eatserver.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.admin.eatserver.Model.Request;
import com.example.admin.eatserver.Model.User;
import com.example.admin.eatserver.Remote.APIService;
import com.example.admin.eatserver.Remote.FCMRetroFitClient;
import com.example.admin.eatserver.Remote.IGeoCoordinates;
import com.example.admin.eatserver.Remote.RetrofitClient;

import java.util.Calendar;
import java.util.Locale;

public class Common {
    public static final String SHIPPERS_TABLE ="Shippers" ;
    public static final String ORDER_NEED_SHIP_TABLE ="OrderNeedShip" ;
    public static String topicName = "News";
    public static User currentUser;
    public  static Request currentRequest;

    public  static String UPDATE="Update";

    public  static String DELETE="Delete";
    public  static String PHONE="phone";

    public  static final String baseUri = "https://maps.googleapis.com";
    public  static final String fcmUri = "https://fcm.googleapis.com/";
    //public  static final String baseUri = "https://maps.googleapis.com/maps/api/place";
    public static final int PICK_IMAGE_REQUEST = 71;
    public static String convertCodeToStatus(String code)
    {
        if(code.equals("0"))
            return "Placed";
        else if(code.equals("1"))
            return "On my way";
        else
            return "Shipping";
    }

    public static IGeoCoordinates getGeoCodeService()
    {
        return RetrofitClient.getClient(baseUri).create(IGeoCoordinates.class);
    }

    public static APIService getFCMClient()
    {
        return FCMRetroFitClient.getClient(fcmUri).create(APIService.class);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight)
    {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);

        float scaleX = newWidth/(float)bitmap.getWidth();
        float scaleY = newHeight/(float)bitmap.getHeight();
        float pivotX = 0, pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public static String getDate(long time)
    {
        Calendar calendar = Calendar.getInstance( Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(
                android.text.format.DateFormat.format( "dd-MM-yyyy HH:mm",calendar ).toString()
        );
        return date.toString();
    }

}
