package com.bragitoff.curvefit_leastsquares;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Choose_fit extends AppCompatActivity {
    public static RelativeLayout content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_fit);
        content=(RelativeLayout)findViewById(R.id.choose_layout);
    }
    public void exponential(View view) {
        Intent intent = new Intent(this, Expo_Fit.class);
        startActivity(intent);
        //MainActivity.url=MainActivity.errorurl;
        //MainActivity.BragitOff.loadUrl(errorurl);
    }
    public void linear(View view) {
        Intent intent = new Intent(this, Linear_Fit.class);
        startActivity(intent);
        //MainActivity.url=MainActivity.errorurl;
        //MainActivity.BragitOff.loadUrl(errorurl);
    }
    public void polynomial(View view) {
        Intent intent = new Intent(this, Poly_Fit.class);
        startActivity(intent);
        //MainActivity.url=MainActivity.errorurl;
        //MainActivity.BragitOff.loadUrl(errorurl);
    }
    public void save_img1(View view) {
        Bitmap bitmap;
        content.setDrawingCacheEnabled(true);
        content.buildDrawingCache(true);
        bitmap = Bitmap.createBitmap(content.getDrawingCache());
        content.setDrawingCacheEnabled(false);
        if (isStoragePermissionGranted()) {
            String h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
            File root = new File(Environment.getExternalStorageDirectory(), "Curve Fitter IMG");
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, h + ".png"); // file path to save
            FileOutputStream ostream = null;
            try {
                ostream = new FileOutputStream(filepath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            try {
                ostream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(getBaseContext(), "Please enable storage permission from settings", Toast.LENGTH_LONG).show();
        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}
