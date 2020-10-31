package com.example.myapplication;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Models.product;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.example.myapplication.helper.SQLiteHandler;
import com.example.myapplication.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ExampleAdapter.OnItemClicked {
  private String TAG;
  Toolbar toolbar;
  RecyclerView mRecyclerView;
  RecyclerView.Adapter mAdapter;
  RecyclerView.LayoutManager mLayoutManager;
  ArrayList<product> arrayList ;
  private SQLiteHandler db;
  private SessionManager session;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TAG = "MainView";
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // SqLite database handler
    db = new SQLiteHandler(getApplicationContext());

    // session manager
    session = new SessionManager(getApplicationContext());
    Log.d(TAG, "session");
    if (!session.isLoggedIn()) {
      logoutUser();
    }

    GetItems();
  }

  private void GetItems() {

    //final String url =  AppConfig.URL_GetDetailItems+ "?getItems=9";
    String tag_string_req = "req_login" ;

    Log.d(TAG, "GETITEMS" );
    String url = String.format( AppConfig.URL_GetItems+"?getItems=%1$s",9);
    //AppConfig.URL_GetDetailItems+ "?getItems=9"
    StringRequest strReq = new StringRequest(Request.Method.GET,
     url , new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {


        try {
          Log.d(TAG, "The retrieved sting" + response);
          JSONArray jObj = new JSONArray(response);
           arrayList = new ArrayList<product>();
          for (int i = 0; i < jObj.length(); i++) {
            JSONObject pro = (JSONObject) jObj.get(i);
            //String uid, String name, double price, String desc, String image_url
            arrayList.add(new product(pro.getString("uid"), pro.getString("name"),
              pro.getString("description"), pro.getString("image_url") ));

          }
          mRecyclerView = findViewById(R.id.recyclerView);
          mRecyclerView.setHasFixedSize(true);
          //mLayoutManager =new GridLayoutManager(MainActivity.this, 2) ;
          mLayoutManager = new LinearLayoutManager(getApplicationContext());
          mAdapter = new ExampleAdapter(arrayList ,MainActivity.this);
          mRecyclerView.setLayoutManager(mLayoutManager);
          mRecyclerView.setAdapter(mAdapter);


        } catch (JSONException e) {
          // JSON error
          e.printStackTrace();
          Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "Login Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
          error.getMessage(), Toast.LENGTH_LONG).show();

      }
    }) ;
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
  }



  private void logoutUser() {
    session.setLogin(false);

    db.deleteUsers();

    // Launching the login activity
    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void onItemClick(int position) {
    Log.d(TAG , "Click" +position) ;
    Intent intent = new Intent( MainActivity.this , DetailsActivity.class) ;
    intent.putExtra("objectval", arrayList.get(position));
    startActivity(intent);
  }

  //ToolBar


  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.logout:
        logoutUser();
        return true;
      case R.id.item2:
        Toast.makeText(getApplicationContext(), "Cart Clicked", Toast.LENGTH_LONG).show();
        return true;
      case R.id.map:
        Intent n = new Intent(getApplicationContext(), MapsActivity.class) ;
        startActivity(n);
        return true;

    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.example_menu, menu);
    return true;
  }

}
