package com.bragitoff.curvefit_leastsquares;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;


public class Expo_Fit extends AppCompatActivity {

    TextView c_text;
    TextView a_text;
    TextView answer;
    GraphView graph;
    PointsGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> series2;
    EditText interpolate;
    Button interpolate_button;
    TextView interpolate_answer;
    double xsum=0;
    double ysum=0;
    double xysum=0;
    double x2sum=0;
    double a,b,c;
    public static RelativeLayout content;
    //Layout_to_Image layout_to_image;  //Create Object of Layout_to_Image Class


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expo__fit);
        content=(RelativeLayout)findViewById(R.id.exp_layout);
        c_text=(TextView)findViewById(R.id.c);
        a_text=(TextView)findViewById(R.id.a);
        answer=(TextView)findViewById(R.id.textView6);
        interpolate_answer=(TextView)findViewById(R.id.inter_ans_exp);
        interpolate_button=(Button)findViewById(R.id.interpolate_exp);
        interpolate=(EditText)findViewById(R.id.editText_inter_exp);
        graph = (GraphView) findViewById(R.id.graph_expo);
        series= new PointsGraphSeries<>(data());
        graph.addSeries(series);
        series.setShape(PointsGraphSeries.Shape.POINT);
        series.setSize(5);

        int n=MainActivity.x_axis.size();
        double y[]=new double[n];
        for (int i=0;i<n;i++){
            y[i]=Math.log(Double.parseDouble(MainActivity.y_axis.get(i)));
        }
        for(int i=0;i<n;i++){
            xsum=Double.parseDouble(MainActivity.x_axis.get(i))+xsum;
            ysum=y[i]+ysum;
            xysum=Double.parseDouble(MainActivity.x_axis.get(i))*y[i]+xysum;
            x2sum=Double.parseDouble(MainActivity.x_axis.get(i))*Double.parseDouble(MainActivity.x_axis.get(i))+x2sum;

        }
        a=(n*xysum-xsum*ysum)/(n*x2sum-xsum*xsum);
        b=(x2sum*ysum-xsum*xysum)/(x2sum*n-xsum*xsum);
        c=Math.exp(b);
        c_text.setText("Constant, c = ");
        c_text.append(Double.toString(c));
        a_text.setText("Power, a = ");
        a_text.append(Double.toString(a));
        answer.setText("Exponential Fit; y = c*exp(a*x)");//+new DecimalFormat("*.####").format(c)+"exp("+Double.toString(a)+"x)");

        series2=new LineGraphSeries<>();
        graph.addSeries(series2);
        series2.setThickness(1);

        double x_array[]=new double[n];
        double y_array[]=new double[n];
        for (int i=0;i<n;i++){
            x_array[i]=Double.parseDouble(MainActivity.x_axis.get(i));
            y_array[i]=Double.parseDouble(MainActivity.y_axis.get(i));
        }

        double min_x = min(x_array);
        double max_x = max(x_array);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(min_x);
        graph.getViewport().setMaxX(max_x);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);



        for (double x=min_x;x<=max_x;x=x+(max_x-min_x)/500){
            series2.appendData(new DataPoint(x,c*Math.exp(a*x)),true,10000000);
        }
    }
    public DataPoint[] data(){
        int n=MainActivity.x_axis.size();
        DataPoint[] values = new DataPoint[n];
        for(int i=0;i<n;i++){
            DataPoint v = new DataPoint(Double.parseDouble(MainActivity.x_axis.get(i)),Double.parseDouble(MainActivity.y_axis.get(i)));
            values[i] = v;
        }
        return values;
    }
    public DataPoint[] data2(){
        int n=MainActivity.x_axis.size();
        double[] yfit=new double[n];
        for(int i=0;i<n;i++){
            double p=a*Double.parseDouble(MainActivity.x_axis.get(i));
            yfit[i]=c*Math.exp(p);
        }
        int minIndex = MainActivity.x_axis.indexOf(Collections.min(MainActivity.x_axis));
        int maxIndex = MainActivity.x_axis.indexOf(Collections.max(MainActivity.x_axis));
        double n1=((Double.parseDouble(MainActivity.x_axis.get(maxIndex))-Double.parseDouble(MainActivity.x_axis.get(minIndex)))/0.1);
        int n2= (int) n1+1;
        DataPoint[] values = new DataPoint[n2];
        Double x=Double.parseDouble(MainActivity.x_axis.get(minIndex));
        //for (double x=Double.parseDouble(MainActivity.x_axis.get(minIndex));x<Double.parseDouble(MainActivity.x_axis.get(maxIndex));x=x+0.1){
        for (int i=0;i<n2;i++){
            //int i=0;
            DataPoint v = new DataPoint(x,c*Math.exp(a*x));
            values[i] = v;
            //i++;
            x=x+0.1;
        }
        /*
        for(int i=0;i<n;i++){
            DataPoint v = new DataPoint(Double.parseDouble(MainActivity.x_axis.get(i)),yfit[i]);
            values[i] = v;
        }
        */
        return values;

    }
    private static double min(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    private static double max(double[] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    private static double min_diff(double[] array){
        double min=array[0];
        for (int i=0; i<array.length-1; i++){
            if (Math.abs(array[i]-array[i+1])<min){
                min=Math.abs(array[i]-array[i+1]);
            }
        }

        int n=MainActivity.x_axis.size();

        double x_array[]=new double[n];
        double y_array[]=new double[n];
        for (int i=0;i<n;i++){
            x_array[i]=Double.parseDouble(MainActivity.x_axis.get(i));
            y_array[i]=Double.parseDouble(MainActivity.y_axis.get(i));
        }

        double min_x = min(x_array);
        double max_x = max(x_array);

        //if (min>=(max_x-min_x)/100)
        return min;
    }
    public void save_img(View view) {
        //layout_to_image=new Layout_to_Image(Expo_Fit.this,content);
        //Bitmap bitmap;
        //bitmap=layout_to_image.convert_layout();
        /*content.setDrawingCacheEnabled(true);
        content.buildDrawingCache(true);
        bitmap = Bitmap.createBitmap(content.getDrawingCache());
        content.setDrawingCacheEnabled(false);*/
        if (isStoragePermissionGranted()) {
            String h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
            File root = new File(Environment.getExternalStorageDirectory(), "Curve Fitter IMG");
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, h + ".jpg"); // file path to save
            FileOutputStream ostream = null;
            try {
                ostream = new FileOutputStream(filepath);
                // Saving screenshot to file
//                Falcon.takeScreenshot(Expo_Fit.this,filepath);
// Take bitmap and do whatever you want
  //              Bitmap bitmap = Falcon.takeScreenshotBitmap(Expo_Fit.this);
    //            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

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
    public void interpolate_exp(View view){
        if (interpolate.getText().toString()==null || interpolate.getText().toString().trim().equals("")){
            Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(interpolate);
        }
        else {
            double interpolated_value = c * Math.exp(a * Double.parseDouble(interpolate.getText().toString()));
            interpolate_answer.setText("The corresponding y-value is : " + Double.toString(interpolated_value));
        }
    }
}
