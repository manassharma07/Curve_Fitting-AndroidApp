package com.bragitoff.curvefit_leastsquares;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collections;

public class Poly_Fit extends AppCompatActivity {

    EditText n_deg;
    Button submit_n;
    ListView list_poly;
    TextView poly_coeff;
    GraphView graph;
    PointsGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> series2;
    EditText interpolate;
    Button interpolate_button;
    TextView interpolate_answer;
    int n, N;
    public static ArrayList<String> coeff=new ArrayList<String>();
    //int n = Integer.parseInt(n_deg.getText().toString());
    //double a1[]=new double[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poly__fit);
        n_deg=(EditText)findViewById(R.id.n_deg);
        submit_n=(Button)findViewById(R.id.submit_n);
        poly_coeff=(TextView)findViewById(R.id.poly_ans);
        interpolate_answer=(TextView)findViewById(R.id.inter_ans_poly);
        interpolate_button=(Button)findViewById(R.id.interpolate_poly);
        interpolate=(EditText)findViewById(R.id.editText_inter_poly);
        graph = (GraphView) findViewById(R.id.graph_poly);
        series= new PointsGraphSeries<>(data());

        graph.addSeries(series);
        series.setShape(PointsGraphSeries.Shape.POINT);
        series.setSize(5);

        n_deg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    submit_n.performClick();
                }
                return false;
            }
        });
    }
    public void calc_pfit(View v){
        if (n_deg.getText().toString()==null || n_deg.getText().toString().trim().equals("")){
            Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(n_deg);
        }
        else if(Integer.parseInt(n_deg.getText().toString())>MainActivity.x_axis.size()){
            Toast.makeText(getBaseContext(),"Degree of polynomial can't be greater than the no. of data points", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(n_deg);
        }
        else {
            //int n, N;
            N = MainActivity.x_axis.size();
            double x[] = new double[N];
            double y[] = new double[N];
            for (int i = 0; i < N; i++) {
                x[i] = Double.parseDouble(MainActivity.x_axis.get(i));
                y[i] = Double.parseDouble(MainActivity.y_axis.get(i));
            }
            n = Integer.parseInt(n_deg.getText().toString());
            double X[] = new double[2 * n + 1];
            for (int i = 0; i < 2 * n + 1; i++) {
                X[i] = 0;
                for (int j = 0; j < N; j++)
                    X[i] = X[i] + Math.pow(x[j], i);        //consecutive positions of the array will store N,sigma(xi),sigma(xi^2),sigma(xi^3)....sigma(xi^2n)
            }
            double B[][] = new double[n + 1][n + 2], a[] = new double[n + 1];            //B is the Normal matrix(augmented) that will store the equations, 'a' is for value of the final coefficients
            for (int i = 0; i <= n; i++)
                for (int j = 0; j <= n; j++)
                    B[i][j] = X[i + j];            //Build the Normal matrix by storing the corresponding coefficients at the right positions except the last column of the matrix
            double Y[] = new double[n + 1];                    //Array to store the values of sigma(yi),sigma(xi*yi),sigma(xi^2*yi)...sigma(xi^n*yi)
            for (int i = 0; i < n + 1; i++) {
                Y[i] = 0;
                for (int j = 0; j < N; j++)
                    Y[i] = Y[i] + Math.pow(x[j], i) * y[j];        //consecutive positions will store sigma(yi),sigma(xi*yi),sigma(xi^2*yi)...sigma(xi^n*yi)
            }
            for (int i = 0; i <= n; i++)
                B[i][n + 1] = Y[i];                //load the values of Y as the last column of B(Normal Matrix but augmented)
            n = n + 1;
            for (int i = 0; i < n; i++)                    //From now Gaussian Elimination starts(can be ignored) to solve the set of linear equations (Pivotisation)
                for (int k = i + 1; k < n; k++)
                    if (B[i][i] < B[k][i])
                        for (int j = 0; j <= n; j++) {
                            double temp = B[i][j];
                            B[i][j] = B[k][j];
                            B[k][j] = temp;
                        }

            for (int i = 0; i < n - 1; i++)            //loop to perform the gauss elimination
                for (int k = i + 1; k < n; k++) {
                    double t = B[k][i] / B[i][i];
                    for (int j = 0; j <= n; j++)
                        B[k][j] = B[k][j] - t * B[i][j];    //make the elements below the pivot elements equal to zero or elimnate the variables
                }
            for (int i = n - 1; i >= 0; i--)                //back-substitution
            {                        //x is an array whose values correspond to the values of x,y,z..
                a[i] = B[i][n];                //make the variable to be calculated equal to the rhs of the last equation
                for (int j = 0; j < n; j++)
                    if (j != i)            //then subtract all the lhs values except the coefficient of the variable whose value                                   is being calculated
                        a[i] = a[i] - B[i][j] * a[j];
                a[i] = a[i] / B[i][i];            //now finally divide the rhs by the coefficient of the variable to be calculated
            }

            for (int i=0;i<n;i++){
                coeff.add(i,Double.toString(a[i]));
            }

            poly_coeff.setText(Double.toString(a[0]));
            poly_coeff.append("x^0");
            for (int i = 1; i < n; i++) {
                poly_coeff.append(" + (");
                poly_coeff.append(Double.toString(a[i]));
                poly_coeff.append(")");
                poly_coeff.append("x^");
                poly_coeff.append(Integer.toString(i));

            }
            series2=new LineGraphSeries<>();
            graph.addSeries(series2);
            series2.setThickness(1);

            double min_x = min(x);
            double max_x = max(x);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(min_x);
            graph.getViewport().setMaxX(max_x);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScalableY(true);



            for (double xp=min_x;xp<=max_x;xp=xp+(max_x-min_x)/500){
                double yp=a[0];
                for (int j=1;j<n;j++){
                    yp=yp+Math.pow(xp,j)*a[j];
                }
                series2.appendData(new DataPoint(xp,yp),true,10000000);
            }
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
    public DataPoint[] data2(double a1[]){
        int n=MainActivity.x_axis.size();
        //double[] yfit=new double[n];
        /*
        for(int i=0;i<n;i++){
            double p=a*Double.parseDouble(MainActivity.x_axis.get(i));
            yfit[i]=c*Math.exp(p);
        }
        */
        int ndeg=Integer.parseInt(n_deg.getText().toString());

        int minIndex = MainActivity.x_axis.indexOf(Collections.min(MainActivity.x_axis));
        int maxIndex = MainActivity.x_axis.indexOf(Collections.max(MainActivity.x_axis));
        //double n1=((Double.parseDouble(MainActivity.x_axis.get(maxIndex))-Double.parseDouble(MainActivity.x_axis.get(minIndex)))/0.01);
        //int n2= (int) n1;
        DataPoint[] values = new DataPoint[1];
        //Double x=Double.parseDouble(MainActivity.x_axis.get(minIndex));
        int i=0;
        for (double x=Double.parseDouble(MainActivity.x_axis.get(minIndex));x<=Double.parseDouble(MainActivity.x_axis.get(maxIndex));x=x+0.01){
        //for (int i=0;i<n2;i++){
            //int i=0;
            double y=a1[0];
            for (int j=1;j<ndeg+1;j++){
                y=y+Math.pow(x,j)*a1[j];
            }
            DataPoint v = new DataPoint(x,y);
            values[i] = v;
            series2.appendData(new DataPoint(x, y), true,10000);
            i++;
            //x=x+0.1;
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
    public void interpolate_poly(View view){
        if (interpolate.getText().toString()==null || interpolate.getText().toString().trim().equals("")){
            Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(interpolate);
        }
        else {
            double interpolated_value = Double.parseDouble(coeff.get(0));
            for (int j = 1; j < n; j++) {
                interpolated_value = interpolated_value + Math.pow(Double.parseDouble(interpolate.getText().toString()), j) * Double.parseDouble(coeff.get(j));
            }
            interpolate_answer.setText("The corresponding y-value is : " + Double.toString(interpolated_value));
        }
    }
}
