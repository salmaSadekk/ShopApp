package com.example.myapplication.Models;

public class product {
  private String uid; private String name ; private double price ;private  String desc; private  String image_url ;
  public product(String uid, String name, double price, String desc, String image_url) {
    this.uid = uid ;
    this.name = name;
    this.price =price ;
    this.desc = desc;
    this.image_url = image_url ;
  }

  public String getName() {
    return name;
  }

  public String getImage_url() {
    return image_url;
  }

  public String getDesc() {
    return desc;
  }

  public double getPrice() {
    return price;
  }

  public String getUid() {
    return uid;
  }
}
