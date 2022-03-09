package com.example.apidotnet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView result;
    ListView lv1;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // lv1=(ListView)findViewById(R.id.lv1);
        result=(TextView) findViewById(R.id.result);
        MyAsyncTask asyncTask =new MyAsyncTask();
        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(image);

       asyncTask.execute("http://172.17.36.60:45457/api/students");
    }

    class MyAsyncTask extends AsyncTask<String, String, String> {
        String newData="";
        JSONObject Jobj;
        String name;
        ArrayList arr;

        @Override
        protected void onPostExecute(String s) {
            result.setText(name);
            //lv1.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,arr));

        }

        @Override
        protected String doInBackground(String... strings) {
            publishProgress("Open connection.");
            String s="";
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();
                publishProgress("start reading!!");
                InputStream in=new BufferedInputStream(urlConnection.getInputStream());
                s=Stream2String(in);

                /*StringReader fis = new StringReader(newData);
                JSONObject myJson=new JSONObject(newData);
                JSONArray arrayJ=myJson.getJSONArray("items");
                JSONObject Jobj1=arrayJ.getJSONObject(2);
                Jobj=Jobj1.getJSONObject("media");*/
                //publishProgress(newData);
               // newData=Stream2String(in);

                //StringReader fis = new StringReader(newData);
                //JSONObject myJson=new JSONObject(newData);
                JSONArray myarray=new JSONArray(s);
                // JSONArray arrayJ=myJson.getJSONArray("items");
                // JSONObject Jobj1=arrayJ.getJSONObject(2);
                //JSONObject obj1 = myarray.getJSONObject(1);
               // name = obj1.getString("name");
                name="";
                for(int i=0;i<myarray.length();i++) {
                    JSONObject obj1 = myarray.getJSONObject(i);
                    name += obj1.getString("name")+"\n";
                    //arr.add(name);
                }
                lv1.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,arr));
                //publishProgress(newData);

            }catch (Exception exp){
                publishProgress("cannot connect to server");
            }
            return s;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            result.setText(values[0]);
            //lv1.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arr));
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPreExecute() {
            newData="";
            publishProgress("connecting attempt ongoing please wait !.");
        }
        public String Stream2String(InputStream in){
            BufferedReader buReader=new BufferedReader(new InputStreamReader(in));
            String text="",line;
            try {
                while ((line=buReader.readLine())!=null){
                    text+=line;
                }
            }catch (Exception exp){}
            return text;
        }
    }
}