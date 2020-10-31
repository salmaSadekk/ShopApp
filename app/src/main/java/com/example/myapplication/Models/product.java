package com.example.myapplication.Models;

import java.io.Serializable;

public class product  implements Serializable {
  private String uid; private String name  ;private  String desc; private  String image_url ;

  public product(String uid, String name,  String desc, String image_url ) {
    this.uid = uid ;
    this.name = name;
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

  public String getUid() {
    return uid;
  }
}
