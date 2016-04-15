package com.sergienko.weather.main.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sergienko.weather.R;
import com.sergienko.weather.base.DataBase;
import com.sergienko.weather.detail.activity.DetailActivity;
import com.sergienko.weather.service.ServiceWeather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "MyDBName.db";
    ArrayList<HashMap<String, String>> contactList;
    String json;
    DataBase dbHelper;
    ServiceWeather.MyBinder binder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        binder = new ServiceWeather.MyBinder();

       setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final CheckBox checkBox=(CheckBox) findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if (checkBox.isChecked()) {

                  Intent intent = new Intent(MainActivity.this, ServiceWeather.class);
                  bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
              }


        if (checkBox.isChecked()==false) {

             Intent intent = new Intent(MainActivity.this, ServiceWeather.class);
            unbindService(mConnection);}
    }
});

        dbHelper = new DataBase(this, DATABASE_NAME, null, 1);

        contactList = new ArrayList<HashMap<String, String>>();
        connect con = new connect();
        con.execute();
        try {
            contactList = con.get();
            Toast.makeText(MainActivity.this, "Количество данных: "+Integer.toString(contactList.size()), Toast.LENGTH_LONG).show();
            if (contactList.size() == 0) { Toast.makeText(MainActivity.this, "Интернет связь отсутствует! Сведения о погоде  загружены с базы данных", Toast.LENGTH_SHORT).show();
                contactList = dbHelper.getAllCotacts();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ListView lvCats = (ListView) findViewById(R.id.listView);
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, contactList,
                R.layout.adapter, new String[]{"dt_txt", "temp", "pressure"}, new int[]{R.id.data, R.id.temp, R.id.pressure});
        lvCats.setAdapter(adapter);

        lvCats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoActivity_lesson_1(id);
            }
        });

    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(MainActivity.this, ServiceWeather.class);
//        unbindService(mConnection);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class connect extends AsyncTask<Void, Void, ArrayList> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(Void... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJson = "";
            URL url;

            try {
                url = new URL("http://api.openweathermap.org/data/2.5/forecast/city?id=710791&APPID=7ecc1ea3a34dd7e4677a252c9ddafa8e");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();
                json = resultJson;
                inputStream.close();

                JSONObject mainJsonObject = new JSONObject(json);
                JSONArray listJsonArray = mainJsonObject.getJSONArray("list");
                dbHelper.onDelete();
                for (int i = 0; i < listJsonArray.length(); i++) {
                    JSONObject c = listJsonArray.getJSONObject(i);
                    HashMap<String, String> hashMap_AsyncTask = new HashMap<String, String>();

                    JSONObject main = c.getJSONObject("main");

                    double celsius_temp = main.getDouble("temp") - 273;
                    hashMap_AsyncTask.put("temp", String.format("%.2f° C", celsius_temp));
                    hashMap_AsyncTask.put("humidity", main.getString("humidity"));
                    hashMap_AsyncTask.put("pressure", main.getString("pressure"));
                    hashMap_AsyncTask.put("dt_txt", listJsonArray.getJSONObject(i).getString("dt_txt"));
                    hashMap_AsyncTask.put("icon", listJsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
                    hashMap_AsyncTask.put("speed", listJsonArray.getJSONObject(i).getJSONObject("wind").getString("speed"));

                    dbHelper.insertContact(String.format("%.2f° C", celsius_temp), main.getString("humidity"), main.getString("pressure"), listJsonArray.getJSONObject(i).getString("dt_txt"),
                            listJsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"), listJsonArray.getJSONObject(i).getJSONObject("wind").getString("speed"));
                    contactList.add(hashMap_AsyncTask);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            return contactList;
        }
    }

    public void gotoActivity_lesson_1(long id) {
        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra("id", id);
        HashMap<String, String> d = contactList.get((int) id);
        String temp = d.get("temp");
        String pressure = d.get("pressure");
        String humidity = d.get("humidity");
        String dt_txt = d.get("dt_txt");
        String icon = d.get("icon");
        String speed = d.get("speed");

        intent.putExtra("dt_txt", dt_txt);
        intent.putExtra("temp", temp);
        intent.putExtra("pressure", pressure);
        intent.putExtra("humidity", humidity);
        intent.putExtra("icon", icon);
        intent.putExtra("speed", speed);

        startActivity(intent);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (ServiceWeather.MyBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}



