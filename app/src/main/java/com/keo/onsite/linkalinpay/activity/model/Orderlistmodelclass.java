package com.keo.onsite.linkalinpay.activity.model;

public class Orderlistmodelclass {
public String id;
public String oid;
public String seller_id;
public String product_id;
public String product_name;
public String product_price;
public String product_offerprice;
public String product_image;
public String sbtotal;
public String qty;
public String ftotal;
public String order_id;
public String total_amount;
public String orderstatus;
public String payment_type;
public String order_date;

public Orderlistmodelclass(String oid,String product_id,String product_name,String product_price,String product_offerprice,
                           String product_image,String sbtotal,String qty,
                           String ftotal,String order_id,String total_amount,
                           String orderstatus,String payment_type,String order_date){



    this.oid=oid;
   // this.seller_id=seller_id;
    this.product_id=product_id;
    this.product_name=product_name;
    this.product_price=product_price;
    this.product_offerprice=product_offerprice;
    this.product_image=product_image;
    this.sbtotal=sbtotal;
    this.qty=qty;
    this.ftotal=ftotal;
    this.order_id=order_id;
    this.total_amount=total_amount;
    this.orderstatus=orderstatus;
    this.payment_type=payment_type;
    this.order_date=order_date;



}
















}
