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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

  private TextView txtName;
  private TextView txtEmail;
  private Button btnLogout;
  private String TAG;
  Toolbar toolbar;
  RecyclerView mRecyclerView;
  RecyclerView.Adapter mAdapter;
  RecyclerView.LayoutManager mLayoutManager;

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.logout:
        logoutUser();
        return true;
      case R.id.item2:
        Toast.makeText(getApplicationContext(), "Cart Clicked", Toast.LENGTH_LONG).show();
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

  ListView list;
  private SQLiteHandler db;
  private SessionManager session;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TAG = "MainView";
    Log.d(TAG, "The problem is in onCreate");
    txtName = (TextView) findViewById(R.id.name);
    txtEmail = (TextView) findViewById(R.id.email);
    btnLogout = (Button) findViewById(R.id.btnLogout);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    //list = findViewById(R.id.list);

    // SqLite database handler
    db = new SQLiteHandler(getApplicationContext());

    // session manager
    session = new SessionManager(getApplicationContext());
    Log.d(TAG, "session");
    if (!session.isLoggedIn()) {
      logoutUser();
    }

    // Fetching user details from sqlite
    HashMap<String, String> user = db.getUserDetails();

    String name = user.get("name");
    String email = user.get("email");

    // Displaying the user details on the screen
    txtName.setText(name);
    txtEmail.setText(email);
    getItems();
    Log.d(TAG, "display");
    // Logout button click event
    btnLogout.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        logoutUser();
      }
    });


  }

  private void getItems() {
    // Tag used to cancel the request
    String tag_string_req = "req_login";


    StringRequest strReq = new StringRequest(Request.Method.POST,
      AppConfig.URL_GetItems, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {


        try {
          Log.d(TAG, "The retrieved sting" + response);
          JSONArray jObj = new JSONArray(response.substring(1, response.length()));
          ArrayList<product> arrayList = new ArrayList<product>();
          for (int i = 0; i < jObj.length(); i++) {
            JSONObject pro = (JSONObject) jObj.get(i);
            //String uid, String name, double price, String desc, String image_url
            arrayList.add(new product(pro.getString("uid"), pro.getString("name"), pro.getDouble("price"),
              pro.getString("description"), pro.getString("image_url")));

          }
          mRecyclerView = findViewById(R.id.recyclerView);
          mRecyclerView.setHasFixedSize(true);
          mLayoutManager = new LinearLayoutManager(getApplicationContext());
          mAdapter = new ExampleAdapter(arrayList);
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
    }) {

      @Override
      protected Map<String, String> getParams() {
        // Posting parameters to login url
        Map<String, String> params = new HashMap<String, String>();
        params.put("getItems", "any");


        return params;
      }

    };

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  /**
   * Logging out the user. Will set isLoggedIn flag to false in shared
   * preferences Clears the user data from sqlite users table
   */
  private void logoutUser() {
    session.setLogin(false);

    db.deleteUsers();

    // Launching the login activity
    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
    startActivity(intent);
    finish();
  }
}
