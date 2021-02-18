package com.keo.onsite.linkalinpay.activity.model;

import java.io.Serializable;

/**
 * Created by ANDROID DEV on 12/26/2017.
 */

public class cartlistmodelclass implements Serializable {

    public String cart_id;
    public String cart_token;
    public String cart_sellerid;
    public String cart_cust;
    public String cart_pid;
    public String cart_qty;
    public String product_name;
    public String product_currency;

    public String product_price;
    public String product_offerprice;
    public String product_image;



public cartlistmodelclass(String cart_id, String cart_token,
 String cart_sellerid, String cart_cust, String cart_pid, String cart_qty,
                          String product_name,String product_currency,String product_price,String product_offerprice,String product_image){
this.cart_id=cart_id;
this.cart_token=cart_token;
this.cart_sellerid=cart_sellerid;
this.cart_cust=cart_cust;
this.cart_pid=cart_pid;
this.cart_qty=cart_qty;
this.product_name=product_name;
this.product_currency=product_currency;
this.product_price=product_price;
this.product_offerprice=product_offerprice;
this.product_image=product_image;





}










}
