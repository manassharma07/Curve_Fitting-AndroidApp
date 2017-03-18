package com.bragitoff.curvefit_leastsquares;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Help extends AppCompatActivity {
    TextView about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        about= (TextView)findViewById(R.id.help);
        about.setText(Html.fromHtml(getString(R.string.help)));
        about.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
