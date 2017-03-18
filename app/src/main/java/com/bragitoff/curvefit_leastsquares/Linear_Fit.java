package com.bragitoff.curvefit_leastsquares;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;


public class Linear_Fit extends AppCompatActivity {

    TextView slope;
    TextView intercept;
    TextView eqn;
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
    double m,c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear__fit);
        slope=(TextView)findViewById(R.id.m_slope);
        intercept=(TextView)findViewById(R.id.c_intercept);
        eqn=(TextView)findViewById(R.id.textView4);
        interpolate_answer=(TextView)findViewById(R.id.inter_ans_lin);
        interpolate_button=(Button)findViewById(R.id.interpolate_lin);
        interpolate=(EditText)findViewById(R.id.editText_inter_lin);
        graph = (GraphView) findViewById(R.id.graph_lin);
        series= new PointsGraphSeries<>(data());
        graph.addSeries(series);
        series.setShape(PointsGraphSeries.Shape.POINT);
        series.setSize(5);
        /*
        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        */


        int n=MainActivity.x_axis.size();
        for(int i=0;i<n;i++){
            xsum=Double.parseDouble(MainActivity.x_axis.get(i))+xsum;
            ysum=Double.parseDouble(MainActivity.y_axis.get(i))+ysum;
            xysum=Double.parseDouble(MainActivity.x_axis.get(i))*Double.parseDouble(MainActivity.y_axis.get(i))+xysum;
            x2sum=Double.parseDouble(MainActivity.x_axis.get(i))*Double.parseDouble(MainActivity.x_axis.get(i))+x2sum;

        }
        m=(n*xysum-xsum*ysum)/(n*x2sum-xsum*xsum);
        c=(x2sum*ysum-xsum*xysum)/(x2sum*n-xsum*xsum);
        slope.setText("Slope, m=");
        slope.append(Double.toString(m));
        intercept.setText("Intercept, c=");
        intercept.append(Double.toString(c));
        eqn.setText("Linear Fit line: y=");
        eqn.append(Double.toString(m));
        eqn.append("x + ");
        eqn.append(Double.toString(c));

        series2=new LineGraphSeries<>(data2());
        graph.addSeries(series2);
        series2.setThickness(1);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);





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
        //double yfit[]=new double[n];
        //for(int i=0;i<n;i++){
          //  yfit[i]=m*Double.parseDouble(MainActivity.x_axis.get(i))+c;
        //}
        double x_array[]=new double[n];
        double y_array[]=new double[n];
        for (int i=0;i<n;i++){
            x_array[i]=Double.parseDouble(MainActivity.x_axis.get(i));
            y_array[i]=Double.parseDouble(MainActivity.y_axis.get(i));
        }
        double min_x = min(x_array);
        double max_x = max(x_array);
        DataPoint[] values = new DataPoint[2];
        //for(int i=0;i<n;i++){
            DataPoint v0 = new DataPoint(min_x,m*min_x+c);
            values[0] = v0;
            DataPoint v1 = new DataPoint(max_x,m*max_x+c);
            values[1] = v1;
        //}
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
    public void interpolate_lin(View view){
        if (interpolate.getText().toString()==null || interpolate.getText().toString().trim().equals("")){
            Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(interpolate);
        }
        else{
            double interpolated_value= m*Double.parseDouble(interpolate.getText().toString())+c;
            interpolate_answer.setText("The corresponding y-value is : "+Double.toString(interpolated_value));
        }

    }

}
