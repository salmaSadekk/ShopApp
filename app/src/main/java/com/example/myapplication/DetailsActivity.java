package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Models.product;
import com.example.myapplication.Models.shopItem;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DetailsActivity extends AppCompatActivity {
  String TAG = "DetailsActivity" ;
  product Product;
  Location CurrentLocation ;
  Toolbar toolbar;
  private FusedLocationProviderClient mFusedLocationProviderClient;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    Intent i = getIntent();
    Product = (product) i.getSerializableExtra("objectval");
    ImageView image =  findViewById(R.id.images) ;
    Picasso.get().load(Product.getImage_url()).into(image);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(Product.getName());


     Log.d(TAG , Product.getName() ) ;
    getDeviceLocation() ;
    GetDetailsItems() ;

  }
  private void GetDetailsItems()
  {

    //final String url =  AppConfig.URL_GetDetailItems+ "?getItems=9";
    String tag_string_req = "req_login" ;

    Log.d(TAG, "GETITEMS" );
    String url = String.format( AppConfig.URL_GetDetailItems+"?item_id=%1$s",Product.getUid());
    StringRequest strReq = new StringRequest(Request.Method.GET,
      url , new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {


        try {
          Log.d(TAG, "The retrieved sting" + response);
          JSONArray jObj = new JSONArray(response);
          ArrayList<shopItem> arrayList = new ArrayList<shopItem>();
          for (int i = 0; i < jObj.length(); i++) {


            JSONObject shopItem = (JSONObject) jObj.get(i);
            Location loc = new Location("" ) ;
            loc.setLatitude(shopItem.getDouble("lat"));
            loc.setLongitude(shopItem.getDouble("long"));


            float distanceInMeters= loc.distanceTo(DetailsActivity.this.CurrentLocation);

            arrayList.add(new shopItem(Product.getUid(), shopItem.getString("name"),
              shopItem.getDouble("price"), distanceInMeters ));






          }



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

  private void getDeviceLocation(){
    Log.d(TAG, "getDeviceLocation: getting the devices current location");

    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    try{


      final Task location = mFusedLocationProviderClient.getLastLocation();
      location.addOnCompleteListener(new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
          if(task.isSuccessful()){
            Log.d(TAG, "onComplete: found location!");
            Location currentLocation = (Location) task.getResult();
            if(currentLocation!= null){

              //Toast.makeText(MainActivity.this, "Please open the location", Toast.LENGTH_SHORT).show();

              DetailsActivity.this.CurrentLocation =currentLocation ;




            }
            else{
              Toast.makeText(DetailsActivity.this, "Open the Location Please!", Toast.LENGTH_SHORT).show();
            }



          }else{
            Log.d(TAG, "onComplete: current location is null");
            Toast.makeText(DetailsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
          }
        }
      });

    }catch (SecurityException e){
      Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
    }
  }

}
