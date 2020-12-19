package com.example.myapplication.Models;

import android.location.Location;

public class cartItem {

  private String uid; private String productname  ;private  String shopname; private Location location ;

  public String getUid() {
    return uid;
  }

  public String getShopname() {
    return shopname;
  }

  public void setShopname(String shopname) {
    this.shopname = shopname;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getProductname() {
    return productname;
  }

  public void setProductname(String productname) {
    this.productname = productname;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public cartItem(String uid, String productname, String shopname, Location location ) {
    this.uid = uid ;
    this.productname = productname;
    this.shopname = shopname;
    this.location = location ;

  }
}
