package com.example.myapplication.app;

public class AppConfig {
  public static String  urlpart ="http://192.168.1.9:8080" ;
  // Server user login url
  public static   String URL_LOGIN = urlpart+"/android_login_api/login.php";

  // Server user register url
  public static String URL_REGISTER = urlpart+"/android_login_api/register.php";
  public static  String URL_GetItems = urlpart+ "/android_login_api/getItems.php";
  public static  String URL_GetDetailItems = urlpart+"/android_login_api/get_Details.php";
  public static  String URL_savelocations =   urlpart+"/android_login_api/savelocation.php" ;
  public static  String URL_Cart =   urlpart+"/android_login_api/getlocations.php" ;
  public static  String URL_deleteLoc =   urlpart+"/android_login_api/deletelocation.php" ;


}
