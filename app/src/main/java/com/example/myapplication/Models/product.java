package com.example.myapplication.Models;

import java.io.Serializable;

public class product  implements Serializable {
  private String uid; private String name ; private double price ;private  String desc; private  String image_url ;
  private Double distance ;
  public product(String uid, String name, double price, String desc, String image_url , double distance) {
    this.uid = uid ;
    this.name = name;
    this.price =price ;
    this.desc = desc;
    this.image_url = image_url ;
    this.distance = distance ;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
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
