package com.example.myapplication.Models;

public class shopItem {
  private String uid;
  private String name ;
  private  double price;
  private  double distance ;

  public double getDistance() {
    return distance;
  }

  public double getPrice() {
    return price;
  }



  public String getName() {
    return name;
  }

  public String getUid() {
    return uid;
  }

  public shopItem(String uid, String name , double price, double distance) {
    this.uid= uid ;
    this.name= name ;
    this.price =price;
    this.distance = distance ;
  }
}
