package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Models.product;
import com.example.myapplication.Models.shopItem;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.example.myapplication.helper.SQLiteHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DetailsActivity extends AppCompatActivity implements shopsAdapter.OnShopItemClicked {
  String TAG = "DetailsActivity" ;
  product Product;
  Location CurrentLocation ;
  Toolbar toolbar;
  Boolean issortedDist = false ;
  RecyclerView mRecyclerView;
  RecyclerView.Adapter mAdapter;
  ArrayList<shopItem> arrayList ;
  ArrayList<shopItem> Distance ;
  ArrayList<shopItem> price ;
  TextView title ;
  String uid ;
  SQLiteHandler db ;
  RecyclerView.LayoutManager mLayoutManager;
  private FusedLocationProviderClient mFusedLocationProviderClient;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    Intent i = getIntent();
    Product = (product) i.getSerializableExtra("objectval");
    ImageView image =  findViewById(R.id.images) ;
    title = findViewById(R.id.title) ;
    Picasso.get().load(Product.getImage_url()).into(image);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(Product.getName());
    price = new ArrayList<shopItem>();
    Distance = new ArrayList<shopItem>();
    db = new SQLiteHandler(getApplicationContext());
    HashMap<String, String> user = db.getUserDetails();

    String name = user.get("name");
    uid = user.get("uid");
    title.setText( Product.getName());
    Log.d(TAG , Product.getName() ) ;
    getDeviceLocation() ;


  }
  private void GetDetailsItems()
  {

    final String url =  AppConfig.URL_GetDetailItems+ "?item_id="+ Product.getUid();
    String tag_string_req = "req_login" ;

    Log.d(TAG, "GETITEMS" );
    //String url = String.format( AppConfig.URL_GetDetailItems+"?item_id=%1$s",Product.getUid());
    StringRequest strReq = new StringRequest(Request.Method.GET,
      url , new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {


        try {
          Log.d(TAG, "The retrieved sting" + response);
          JSONArray jObj = new JSONArray(response);
           arrayList = new ArrayList<shopItem>();
          for (int i = 0; i < jObj.length(); i++) {


            JSONObject shopItem = (JSONObject) jObj.get(i);
            Location loc = new Location("" ) ;
            loc.setLatitude(shopItem.getDouble("lat"));
            loc.setLongitude(shopItem.getDouble("long"));


            float distanceInMeters= loc.distanceTo(DetailsActivity.this.CurrentLocation);

            //arrayList.add(new shopItem(Product.getUid(), shopItem.getString("name"),
             // shopItem.getDouble("price"), distanceInMeters ));

            arrayList.add( new shopItem(shopItem.getString("uid"), shopItem.getString("name"),
              shopItem.getDouble("price"),  Math.round(distanceInMeters/1000) ));






          }
          price.addAll(arrayList) ;
          mRecyclerView = findViewById(R.id.recyclerView);
          mRecyclerView.setHasFixedSize(true);
          //mLayoutManager =new GridLayoutManager(MainActivity.this, 2) ;
          mLayoutManager = new LinearLayoutManager(getApplicationContext());
          mAdapter = new shopsAdapter(arrayList ,DetailsActivity.this);

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

  public void sortDist(){



    if(!issortedDist) {
      Distance = (ArrayList)arrayList.clone();

       for(int i=1 ; i< Distance.size() ; i++) {
         shopItem temp =  Distance.get(i);
         for(int j=i-1 ; j>=0 ; j--) {
           if(Distance.get(j).getDistance() >temp.getDistance()) {
             //shopItem temp =  Distance.get(i);
             Distance.set(i, Distance.get(j)) ;
             Distance.set(j,temp) ;
           }

         }
         //Log.d(TAG, "Distance array size" + Distance.size()) ;

       }
      for(int x=0 ; x<Distance.size() ;x++) {
        Log.d(TAG ,"item"+ x+"  "+String.valueOf(Distance.get(x).getDistance()) )  ;
      }
      issortedDist = true ;
    }
    Log.d(TAG , Distance.toString()) ;
    for(int i=0 ; i<Distance.size() ; i++) {
      Log.d(TAG ,"item"+ i+"  "+String.valueOf(Distance.get(i).getDistance()) )  ;
    }
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
              GetDetailsItems() ;




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
  @Override
  public void OnShopItemClick(int position) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Do you want to save this location ?");


// Set up the buttons
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG , "uid of shop" + arrayList.get(position).getUid()) ;
        saveLocation(arrayList.get(position).getUid() , Product.getUid() ) ;
      }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });

    builder.show();
  }


  public void saveLocation( String shop_id , String product_id){
    String tag_string_req = "req_savelocations";
    StringRequest strReq = new StringRequest(Request.Method.POST,
      AppConfig.URL_savelocations, new Response.Listener<String>() {

      @Override
      public void onResponse(String response) {


        try {
          JSONObject jObj = new JSONObject(response);
          boolean error = jObj.getBoolean("error");

           Log.d(TAG , jObj.getString("message")) ;
            Toast.makeText(getApplicationContext() , jObj.getString("message") , Toast.LENGTH_LONG).show() ;

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
        params.put("user_id", uid);
        params.put("item_id", product_id);
        params.put("shop_id" ,shop_id) ;

        return params;
      }

    };

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
  }



  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sortDist:
        sortDist() ;
        arrayList.clear();
        arrayList.addAll(Distance) ;
      //  arrayList = (ArrayList<shopItem>) Distance.clone() ;
        mAdapter.notifyDataSetChanged();

        return true;
      case R.id.sortPrice:
        arrayList.clear();
        arrayList.addAll(price) ;
        mAdapter.notifyDataSetChanged();
        return true;


    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.sort_menu, menu);
    return true;
  }


}
