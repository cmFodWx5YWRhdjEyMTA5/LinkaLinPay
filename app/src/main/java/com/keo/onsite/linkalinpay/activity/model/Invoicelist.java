package com.keo.onsite.linkalinpay.activity.model;

public class Invoicelist {
public String totalrecords;
public String inv_id;
public String seller_id;
public String inv_name;
public String inv_email;
public String inv_mobile;
public String inv_description;
public String inv_amount_type;
public String inv_amount;
public String inv_refno;
public String inv_status;
public String inv_date;
public String InvcURl;
public String BKYTrackUID;
public String pay_date;


public Invoicelist( String totalrecords,     String inv_id,String seller_id,String inv_name,String inv_email,
                   String inv_mobile,String inv_description,String inv_amount_type,String inv_amount,
                   String inv_refno,String inv_status,String inv_date,String InvcURl,
                   String BKYTrackUID,String pay_date){
                   this.totalrecords=totalrecords;
                   this.inv_id=inv_id;
                   this.seller_id=seller_id;
                   this.inv_name=inv_name;
                   this.inv_email=inv_email;
                   this.inv_mobile=inv_mobile;
                   this.inv_description=inv_description;
                   this.inv_amount_type=inv_amount_type;
                   this.inv_amount=inv_amount;
                   this.inv_refno=inv_refno;
                   this.inv_status=inv_status;
                   this.inv_date=inv_date;
                   this.InvcURl=InvcURl;
                   this.BKYTrackUID=BKYTrackUID;
                   this.pay_date=pay_date;


}









}
