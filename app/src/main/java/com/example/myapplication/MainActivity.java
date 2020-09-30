package com.example.myapplication;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.example.myapplication.helper.SQLiteHandler;
import com.example.myapplication.helper.SessionManager;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

  private TextView txtName;
  private TextView txtEmail;
  private Button btnLogout;
  private String TAG ;

  private SQLiteHandler db;
  private SessionManager session;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TAG= "MainView" ;
    txtName = (TextView) findViewById(R.id.name);
    txtEmail = (TextView) findViewById(R.id.email);
    btnLogout = (Button) findViewById(R.id.btnLogout);

    // SqLite database handler
    db = new SQLiteHandler(getApplicationContext());

    // session manager
    session = new SessionManager(getApplicationContext());

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
    getItems() ;

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


    StringRequest strReq = new StringRequest(Request.Method.GET,
      AppConfig.URL_GetItems, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {


        try {
          JSONArray jObj = new JSONArray(response);



          Log.d(TAG, "Length of Array" + jObj.length()) ;
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
        params.put("email", "any");


        return params;
      }

    };

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
  }

  /**
   * Logging out the user. Will set isLoggedIn flag to false in shared
   * preferences Clears the user data from sqlite users table
   * */
  private void logoutUser() {
    session.setLogin(false);

    db.deleteUsers();

    // Launching the login activity
    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
    startActivity(intent);
    finish();
  }
}
