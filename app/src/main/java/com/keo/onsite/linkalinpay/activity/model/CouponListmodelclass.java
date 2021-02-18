package com.keo.onsite.linkalinpay.activity.model;

public class CouponListmodelclass {
    public String id;
    public String seller_id;
    public String name;
    public String code;
    public String type;
    public String amount;
    public String limit;
    public String expirydate;
    public String email;
    public String status;


    public CouponListmodelclass(String id, String seller_id, String name,
                                String code, String type, String amount,
                                String limit, String expirydate, String email, String status) {



           this.id=id;
           this.seller_id=seller_id;
           this.name=name;
           this.code=code;
           this.type=type;
           this.amount=amount;
           this.limit=limit;
           this.expirydate=expirydate;
           this.email=email;
           this.status=status;

    }
}













