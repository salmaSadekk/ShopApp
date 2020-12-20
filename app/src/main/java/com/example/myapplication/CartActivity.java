package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Models.cartItem;
import com.example.myapplication.Models.product;
import com.example.myapplication.Models.shopItem;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.example.myapplication.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.myapplication.app.AppConfig.URL_Cart;
import static com.example.myapplication.app.AppConfig.URL_deleteLoc;

public class CartActivity extends AppCompatActivity   implements cartAdapter.OnShopItemClicked{
String TAG = "CartActivity" ;
  private SQLiteHandler db;
  ArrayList<cartItem> arrayList ;
  RecyclerView.Adapter mAdapter;
  RecyclerView mRecyclerView;
  RecyclerView.LayoutManager mLayoutManager;
  String uid ;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);
    db = new SQLiteHandler(getApplicationContext());
    HashMap<String, String> user = db.getUserDetails();

    String name = user.get("name");
     uid = user.get("uid");

    GetItems() ;
  }
  private void GetItems() {

    String tag_string_req = "req_login" ;

    Log.d(TAG , "uid value"+ uid) ;
    final String url =  URL_Cart+ "?user_id="+ uid;
    StringRequest strReq = new StringRequest(Request.Method.GET,
      url, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {


        try {
          Log.d(TAG, "The retrieved sting" + response);
          JSONArray jObj = new JSONArray(response);
          arrayList = new ArrayList<cartItem>();
          for (int i = 0; i < jObj.length(); i++) {
            JSONObject cartitem = (JSONObject) jObj.get(i);
            //String uid, String name, double price, String desc, String image_url
            arrayList.add( new cartItem(cartitem.getString("uid"), cartitem.getString("productname"),
              cartitem.getString("shopname"), new Location("")));
            arrayList.get(i).getLocation().setLongitude(Double.parseDouble(cartitem.getString("long")));
            arrayList.get(i).getLocation().setLatitude(Double.parseDouble(cartitem.getString("lat")));

          }
          mRecyclerView = findViewById(R.id.recyclerView);
          mRecyclerView.setHasFixedSize(true);
          //mLayoutManager =new GridLayoutManager(MainActivity.this, 2) ;
          mLayoutManager = new LinearLayoutManager(getApplicationContext());
          mAdapter = new cartAdapter(arrayList ,CartActivity.this);

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

  @Override
  public void OnShopItemClick(int position) {

    // setup the alert builder
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("options");
    // add the buttons
    builder.setPositiveButton("Navigate to location", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

      //  Intent n = new Intent(CartActivity.this , MapsActivity.class) ;
        Double lat = arrayList.get(position).getLocation().getLatitude() ;
        Double lon = arrayList.get(position).getLocation().getLongitude() ;
        Log.d(TAG , "latitude: " + lat + "   longitude: " + lon) ;
        String q = "q= " + lat + ", " + lon;
        Uri gmmIntentUri = Uri.parse("google.navigation:" + q);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
      }
    });
    builder.setNeutralButton("Delete Location", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
       DeleteLocation(arrayList.get(position) , position) ;

      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });
    // create and show the alert dialog
    AlertDialog dialog = builder.create();
    dialog.show();

  }

  public void DeleteLocation(cartItem item , int position) {
    final String url =     URL_deleteLoc + "?id="+ item.getUid();
    Log.d(TAG , "delete url" + url) ;
    String tag_string_req = "req_delete" ;

    StringRequest strReq = new StringRequest(Request.Method.GET, url,
      new Response.Listener<String>()
      {
        @Override
        public void onResponse(String response) {
          try {
            JSONObject jobj = new JSONObject(response) ;
            Log.d(TAG , "test :"+  response) ;
            boolean error = jobj.getBoolean("error") ;
            if (!error) {
              arrayList.remove(position) ;
              mAdapter.notifyDataSetChanged();

            }
            Toast.makeText(getApplicationContext(),
              jobj.getString("message"), Toast.LENGTH_LONG).show();

          } catch (JSONException e) {
            e.printStackTrace();
          }

        }
      },
      new Response.ErrorListener()
      {
        @Override
        public void onErrorResponse(VolleyError error) {
          Log.e(TAG, "delete error: " + error.getMessage());
          Toast.makeText(getApplicationContext(),
            error.getMessage(), Toast.LENGTH_LONG).show();

        }
      }
    );
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

  }
}
