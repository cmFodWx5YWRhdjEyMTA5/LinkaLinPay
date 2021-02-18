package com.keo.onsite.linkalinpay.activity.model;

public class Custdetailmodelclass {
public String orderstatus;
public String order_date;
public String currency;

public String total_amount;
public String order_id;
public String couponamount;
public String couponcode;
public String couponname;
public String product_name;
public String product_price;
public String product_offerprice;
public String product_image;
public String qty;
public String sbtotal;
public String ftotal;

public Custdetailmodelclass (String product_name,
                             String product_price,String product_offerprice,
                             String product_image,String qty,String sbtotal,String ftotal ){



                          this.product_name=product_name;
                          this.product_price=product_price;
                          this.product_offerprice=product_offerprice;
                          this.product_image=product_image;
                          this.qty=qty;
                          this.sbtotal=sbtotal;
                          this.ftotal=ftotal;


}









}
