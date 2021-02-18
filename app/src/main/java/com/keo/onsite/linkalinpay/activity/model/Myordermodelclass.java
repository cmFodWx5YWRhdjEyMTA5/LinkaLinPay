package com.keo.onsite.linkalinpay.activity.model;

public class Myordermodelclass {
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

public Myordermodelclass(String orderstatus,String order_date,String currency,String total_amount,
 String order_id,String couponamount,String couponcode,
                         String couponname,String product_name,
                         String product_price,String product_offerprice,String product_image ){


         this.orderstatus=orderstatus;
         this.order_date=order_date;
         this.currency=currency;
         this.total_amount=total_amount;
         this.order_id=order_id;
         this.couponamount=couponamount;
         this.couponcode=couponcode;
         this.couponname=couponname;
         this.product_name=product_name;
         this.product_price=product_price;
         this.product_offerprice=product_offerprice;
         this.product_image=product_image;


}











}
