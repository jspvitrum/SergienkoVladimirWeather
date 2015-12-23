package com.sergienko.weather.detail.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.sergienko.weather.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Vladimir on 29.11.2015.
 */
public class DetailActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        TextView textView = (TextView) findViewById(R.id.pressure);

        String pressure = getIntent().getStringExtra("pressure");

        textView.setText(pressure);

        TextView textView2 = (TextView) findViewById(R.id.temp);

        String temp = getIntent().getStringExtra("temp");

        textView2.setText(temp);

        TextView textView3 = (TextView) findViewById(R.id.humidity);

        String humidity = getIntent().getStringExtra("humidity");

        textView3.setText(humidity);

        TextView textView4 = (TextView) findViewById(R.id.speed);

        String speed = getIntent().getStringExtra("speed");

        textView4.setText(speed);

        TextView textView5 = (TextView) findViewById(R.id.dt_txt);

        String dt_txt = getIntent().getStringExtra("dt_txt");

        textView5.setText(dt_txt);

        String icon = getIntent().getStringExtra("icon");

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
        Picasso.with(this).load(iconUrl).resize(90,90).into(imageView);

    }

}
