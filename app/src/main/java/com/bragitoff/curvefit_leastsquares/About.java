package com.bragitoff.curvefit_leastsquares;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends AppCompatActivity {

    TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        about= (TextView)findViewById(R.id.about);
        about.setText(Html.fromHtml(getString(R.string.about)));
        about.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
