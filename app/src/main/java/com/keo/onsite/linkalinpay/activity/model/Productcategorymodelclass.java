package com.keo.onsite.linkalinpay.activity.model;

public class Productcategorymodelclass {
    public String category_id;
    public String category_sellerid;
    public String category_name;
    public String category_link;
    public String category_desc;
    public String category_featured;
    public String category_status;
    public String category_date;
    public String category_ip;
    public String category_subid;
    public String parent_categoryname;


public Productcategorymodelclass(String category_id,String category_sellerid,String category_name,
                                 String category_link,String category_desc,
                                 String category_featured,String category_status,String category_date,
                                 String category_ip,String category_subid,String parent_categoryname){


    this.category_id=category_id;
    this.category_sellerid=category_sellerid;
    this.category_name=category_name;
    this.category_link=category_link;
    this.category_desc=category_desc;
    this.category_featured=category_featured;
    this.category_status=category_status;
    this.category_date=category_date;
    this.category_ip=category_ip;
    this.category_subid=category_subid;
    this.parent_categoryname=parent_categoryname;
}





}
