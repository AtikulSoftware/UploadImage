package com.bdtopcoder.uploadimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailsActivty extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_activty);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://atikulislam.xyz/upload_image/listapi.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);

                for (int x =0; x<response.length(); x++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(x);
                        String name = jsonObject.getString("name");
                        String imgUrl = jsonObject.getString("images");

                        hashMap = new HashMap<>();
                        hashMap.put("name",name);
                        hashMap.put("images","https://atikulislam.xyz/upload_image/Images/"+imgUrl);
                        arrayList.add(hashMap);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                MyAdapter myAdapter = new MyAdapter();
                listView.setAdapter(myAdapter);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivty.this, "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(DetailsActivty.this);
        requestQueue.add(jsonArrayRequest);

    } // OnCreate Method End Here =============

    private class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflater.inflate(R.layout.list_item,parent,false);

            HashMap<String,String> myHashmap = arrayList.get(position);
            String name = myHashmap.get("name");
            String imgUrl = myHashmap.get("images");

            ImageView img = myView.findViewById(R.id.img);
            TextView Name = myView.findViewById(R.id.name);

            Name.setText(name);
            Picasso.get().load(imgUrl).into(img);


            return myView;
        }

    } // MyAdapter End Here ================



} // Public Class End Here ====================