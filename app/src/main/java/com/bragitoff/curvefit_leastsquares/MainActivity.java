package com.bragitoff.curvefit_leastsquares;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText x_values;
    EditText y_values;
    Button submit_x;
    Button submit_y;
    Button update_x;
    Button insert_x;
    Button update_y;
    Button insert_y;
    ListView show_x;
    ListView show_y;
    public static int i;
    public static ArrayList<String> x_axis=new ArrayList<String>();
    public static ArrayList<String> y_axis=new ArrayList<String>();
    TextView dispx;
    public static String appPackageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x_values=(EditText)findViewById(R.id.x);
        y_values=(EditText)findViewById(R.id.y);
        submit_x=(Button)findViewById(R.id.submit_x);
        submit_y=(Button)findViewById(R.id.submit_y);
        update_x=(Button)findViewById(R.id.updatex);
        update_y=(Button)findViewById(R.id.updatey);
        show_x=(ListView)findViewById(R.id.x_axis);
        registerForContextMenu(show_x);
        show_y=(ListView)findViewById(R.id.y_axis);
        registerForContextMenu(show_y);

        appPackageName=getApplicationContext().getPackageName();// getPackageName() from Context or Activity



        x_values.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    submit_x.performClick();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(x_values, InputMethodManager.SHOW_FORCED);
                }
                return false;
            }
        });
        y_values.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    submit_y.performClick();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(y_values, InputMethodManager.SHOW_FORCED);
                }
                return false;
            }
        });

        submit_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.x[i]=Double.parseDouble(x_values.getText().toString());
                //x_axis.add(i,Double.toString(MainActivity.x[i]));
                if (x_values.getText().toString()==null || x_values.getText().toString().trim().equals("")){
                    Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(1)
                            .playOn(x_values);
                }
                else if (x_axis.contains(x_values.getText().toString())){
                    Toast.makeText(getBaseContext(),"You've already entered that..", Toast.LENGTH_LONG).show();
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(1)
                            .playOn(x_values);
                }
                else {
                    x_axis.add(x_values.getText().toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
                    show_x.setAdapter(adapter);
                    ((EditText) findViewById(R.id.x)).setText("");
                }

            }
        });
        submit_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (y_values.getText().toString()==null || y_values.getText().toString().trim().equals("")){
                    Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(1)
                            .playOn(y_values);
                }
                else {
                    y_axis.add(y_values.getText().toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
                    show_y.setAdapter(adapter);
                    ((EditText) findViewById(R.id.y)).setText("");
                }
            }
        });
        show_x.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        show_y.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }
    public void calc_fit(View view) {
        if (y_axis.size()!=x_axis.size()){
            Toast.makeText(getBaseContext(),"Sizes of x and y don't match", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Pulse)
                    .duration(700)
                    .repeat(1)
                    .playOn(show_x);
            YoYo.with(Techniques.Pulse)
                    .duration(700)
                    .repeat(1)
                    .playOn(show_y);
        }
        else if(y_axis.size()<2 && x_axis.size()<2){
            Toast.makeText(getBaseContext(),"Please enter at least 2 data points", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(this, Choose_fit.class);
            startActivity(intent);
        }
    }
    public void clear(View view) {
        x_axis.clear();
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
        show_x.setAdapter(adapter1);
        ((EditText)findViewById(R.id.x)).setText("");
        y_axis.clear();
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
        show_y.setAdapter(adapter2);
        ((EditText)findViewById(R.id.y)).setText("");
    }
    public void save_csv(View view) {

        if (isStoragePermissionGranted()){

            if (y_axis.size()!=x_axis.size()){
                Toast.makeText(getBaseContext(),"Sizes of x and y don't match", Toast.LENGTH_LONG).show();
            }
            else if (y_axis.size()==0 && x_axis.size()==0){
                    Toast.makeText(getBaseContext(),"There is no data", Toast.LENGTH_LONG).show();
            }
            else{
                String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                String h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
                File root = new File(Environment.getExternalStorageDirectory(), "Curve Fitter CSV");
                if (!root.exists()) {
                    root.mkdirs(); // this will create folder.
                }
                File filepath = new File(root, h + ".csv"); // file path to save
                CSVWriter writer = null;
                try {
                    writer = new CSVWriter(new FileWriter(filepath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                List<String[]> data = new ArrayList<String[]>();
                //String[] data =new String[x_axis.size()];
                for (int i=0;i<x_axis.size();i++){
                    data.add(new String[] {x_axis.get(i), y_axis.get(i)});
                    //data[i]=x_axis.get(i);

                }
                //writer.writeNext(data);
                writer.writeAll(data);
                Toast.makeText(getBaseContext(),"File created inside storage/Curve Fitter CSV/"+h, Toast.LENGTH_LONG).show();

                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        else if(Build.VERSION.SDK_INT < 23){
            Toast.makeText(getBaseContext(),"Please enable storage permission from settings",Toast.LENGTH_LONG).show();
        }
    }
    public void read_csv(View view) {
        //clear_ the data first
        x_axis.clear();
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
        show_x.setAdapter(adapter1);
        ((EditText)findViewById(R.id.x)).setText("");
        y_axis.clear();
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
        show_y.setAdapter(adapter2);
        ((EditText)findViewById(R.id.y)).setText("");

        //filepath
        File root = new File(Environment.getExternalStorageDirectory(), "Curve Fitter CSV");
        String h ="1";
        File filepath = new File(root, h + ".csv"); // file path to read

        //read
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(filepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            List data = csvReader.readAll(); //read the contents of the csv and store them in an arraylist data
            String[][] w = (String[][])data.toArray(new String[data.size()][]); //convert the list into an array w of two dimensions
            //String[][] w = data.toArray(new String[][] {});
            for (int i=0;i<data.size();i++){
                x_axis.add(i,w[i][0]);
                y_axis.add(i,w[i][1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
        show_x.setAdapter(adapter1);
        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
        show_y.setAdapter(adapter2);
        ((EditText) findViewById(R.id.y)).setText("");

    }
    public void read_csv2(View view){
        if (isStoragePermissionGranted()){
            //clear_ the data first
            x_axis.clear();
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
            show_x.setAdapter(adapter1);
            ((EditText)findViewById(R.id.x)).setText("");
            y_axis.clear();
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
            show_y.setAdapter(adapter2);
            ((EditText)findViewById(R.id.y)).setText("");

            /*
            Intent intent = new Intent();
            intent.setType("text/csv");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent,"Select CSV "), 33);
            */
            new MaterialFilePicker()
                    .withActivity(this)
                    .withRequestCode(1)
                    .withFilter(Pattern.compile(".*\\.csv$")) // Filtering files and directories by file name using regexp
                    //.withFilterDirectories(true) // Set directories filterable (false by default)
                    //.withHiddenFiles(true) // Show hidden files and folders
                    .withRootPath(Environment.getExternalStorageDirectory().toString())
                    .start();

        }
        else if(Build.VERSION.SDK_INT < 23){
            Toast.makeText(getBaseContext(),"Please enable storage permission from settings",Toast.LENGTH_LONG).show();
        }

    }
    /*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String filePath = null, fileName;
            if (requestCode == 33) {
                Uri selectedMediaUri = data.getData();
                String filemanagerstring = selectedMediaUri.getPath();
                String selectedMediaPath = getPath(selectedMediaUri);
                if (!selectedMediaPath.equals("")) {
                    filePath = selectedMediaPath;
                } else if (!filemanagerstring.equals("")) {
                    filePath = filemanagerstring;
                }
                File filepath= new File(filePath);
                //read
                CSVReader csvReader = null;
                try {
                    csvReader = new CSVReader(new FileReader(filepath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    List dataset = csvReader.readAll();
                    String[][] w = (String[][])dataset.toArray(new String[dataset.size()][]);
                    //String[][] w = data.toArray(new String[][] {});
                    for (int i=0;i<dataset.size();i++){
                        x_axis.add(i,w[i][0]);
                        y_axis.add(i,w[i][1]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
                show_x.setAdapter(adapter1);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
                show_y.setAdapter(adapter2);
                ((EditText) findViewById(R.id.y)).setText("");

                //filepath is your file's path
            }
        }
    }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            //read
            CSVReader csvReader = null;
            try {
                csvReader = new CSVReader(new FileReader(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                List dataset = csvReader.readAll();
                String[][] w = (String[][])dataset.toArray(new String[dataset.size()][]);
                //String[][] w = data.toArray(new String[][] {});
                for (int i=0;i<dataset.size();i++){
                    x_axis.add(i,w[i][0]);
                    y_axis.add(i,w[i][1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
            show_x.setAdapter(adapter1);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
            show_y.setAdapter(adapter2);
            ((EditText) findViewById(R.id.y)).setText("");

            //filepath is your file's path
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return "";
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
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
            File root = new File(Environment.getExternalStorageDirectory(), "Curve Fitter CSV");
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, h + ".csv"); // file path to save
            CSVWriter writer = null;
            try {
                writer = new CSVWriter(new FileWriter(filepath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String[]> data = new ArrayList<String[]>();
            //String[] data =new String[x_axis.size()];
            for (int i=0;i<x_axis.size();i++){
                data.add(new String[] {x_axis.get(i), y_axis.get(i)});
                //data[i]=x_axis.get(i);

            }
            //writer.writeNext(data);
            writer.writeAll(data);

            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings :
                Intent intent = new Intent(this, Help.class);
                startActivity(intent);
                return true;
            case R.id.action_about :
                intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            case R.id.action_rate:
                Uri uri_playstore=Uri.parse("market://details?id="+appPackageName);
                Uri uri_browser=Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
                try {
                    Intent rateintent = new Intent(Intent.ACTION_VIEW, uri_playstore);
                    startActivity(rateintent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    Intent rateintent = new Intent(Intent.ACTION_VIEW, uri_browser);
                    startActivity(rateintent);
                }
                return true;
            case R.id.action_shareapp:
                String uri = "https://play.google.com/store/apps/details?id=" + appPackageName;
                Intent share_app_intent = new Intent(Intent.ACTION_SEND);
                share_app_intent.putExtra(Intent.EXTRA_TEXT, "Loving this app. Download it from: "+uri);
                share_app_intent.setType("text/plain");
                startActivity(Intent.createChooser(share_app_intent, getResources().getText(R.string.share_app)));
                //startActivity(share_app_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //list.setAdapter(null);
        //updateMyList();
        //adapter=new LazyAdapter(this, ((String[])names.toArray(new String[0])),
          //      ((String[])status.toArray(new String[0])));
        //list.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
        show_x.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
        show_y.setAdapter(adapter2);
        ((EditText) findViewById(R.id.y)).setText("");
    }

    /**
     * MENU
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.x_axis) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.edit_list, menu);
        }
        else if (v.getId()==R.id.y_axis) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                // edit stuff here
                final int pos =info.position;
                //edit_xi(pos);
                x_values.setText(x_axis.get(pos));
                update_x=(Button)findViewById(R.id.updatex);
                update_x.setVisibility(View.VISIBLE);
                submit_x.setVisibility(View.INVISIBLE);
                update_x.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //MainActivity.x[i]=Double.parseDouble(x_values.getText().toString());
                        //x_axis.add(i,Double.toString(MainActivity.x[i]));
                        if (x_values.getText().toString()==null || x_values.getText().toString().trim().equals("")){
                            Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
                        }
                        else if (x_axis.contains(x_values.getText().toString())){
                            Toast.makeText(getBaseContext(),"You've already entered that..", Toast.LENGTH_LONG).show();
                        }
                        else {
                            x_axis.set(pos, x_values.getText().toString());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
                            show_x.setAdapter(adapter);
                            ((EditText) findViewById(R.id.x)).setText("");
                            update_x.setVisibility(View.INVISIBLE);
                            submit_x.setVisibility(View.VISIBLE);
                        }

                    }
                });
                return true;
            case R.id.delete:
                // remove stuff here
                int pos1 =info.position;
                x_axis.remove(pos1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
                show_x.setAdapter(adapter);
                ((EditText) findViewById(R.id.x)).setText("");
                return true;
            case R.id.insert:
                //insert element
                x_axis.add(info.position,"");
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
                show_x.setAdapter(adapter1);
                ((EditText) findViewById(R.id.x)).setText("");
                insert_x=(Button)findViewById(R.id.insertx);
                insert_x.setVisibility(View.VISIBLE);
                submit_x.setVisibility(View.INVISIBLE);
                insert_x.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //MainActivity.x[i]=Double.parseDouble(x_values.getText().toString());
                        //x_axis.add(i,Double.toString(MainActivity.x[i]));
                        if (x_values.getText().toString()==null || x_values.getText().toString().trim().equals("")){
                            Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
                        }
                        else if (x_axis.contains(x_values.getText().toString())){
                            Toast.makeText(getBaseContext(),"You've already entered that..", Toast.LENGTH_LONG).show();
                        }
                        else {
                            x_axis.set(info.position, x_values.getText().toString());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, x_axis);
                            show_x.setAdapter(adapter);
                            ((EditText) findViewById(R.id.x)).setText("");
                            insert_x.setVisibility(View.INVISIBLE);
                            submit_x.setVisibility(View.VISIBLE);
                        }

                    }
                });

                return true;
            //y_axis list view
            case R.id.edity:
                // edit stuff here
                final int posy =info.position;
                //edit_yi(pos);
                y_values.setText(y_axis.get(posy));
                y_values.requestFocus();
                update_y=(Button)findViewById(R.id.updatey);
                update_y.setVisibility(View.VISIBLE);
                submit_y.setVisibility(View.INVISIBLE);
                update_y.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //MainActivity.x[i]=Double.parseDouble(x_values.getText().toString());
                        //x_axis.add(i,Double.toString(MainActivity.x[i]));
                        if (y_values.getText().toString()==null || y_values.getText().toString().trim().equals("")){
                            Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
                        }
                        else {
                            y_axis.set(posy, y_values.getText().toString());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
                            show_y.setAdapter(adapter);
                            ((EditText) findViewById(R.id.y)).setText("");
                            update_y.setVisibility(View.INVISIBLE);
                            submit_y.setVisibility(View.VISIBLE);
                        }

                    }
                });
                return true;
            case R.id.deletey:
                // remove stuff here
                y_axis.remove(info.position);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
                show_y.setAdapter(adapter3);
                ((EditText) findViewById(R.id.y)).setText("");
                return true;
            case R.id.inserty:
                //insert element
                y_values.requestFocus();
                y_axis.add(info.position,"");
                ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
                show_y.setAdapter(adapter4);
                ((EditText) findViewById(R.id.y)).setText("");
                insert_y=(Button)findViewById(R.id.inserty);
                insert_y.setVisibility(View.VISIBLE);
                submit_y.setVisibility(View.INVISIBLE);
                insert_y.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //MainActivity.x[i]=Double.parseDouble(x_values.getText().toString());
                        //x_axis.add(i,Double.toString(MainActivity.x[i]));
                        if (y_values.getText().toString()==null || y_values.getText().toString().trim().equals("")){
                            Toast.makeText(getBaseContext(),"Input Field is Empty", Toast.LENGTH_LONG).show();
                        }
                        else {
                            y_axis.set(info.position, y_values.getText().toString());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, y_axis);
                            show_y.setAdapter(adapter);
                            ((EditText) findViewById(R.id.y)).setText("");
                            insert_y.setVisibility(View.INVISIBLE);
                            submit_y.setVisibility(View.VISIBLE);
                        }

                    }
                });

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

}
