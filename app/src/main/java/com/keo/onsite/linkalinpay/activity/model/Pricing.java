package com.keo.onsite.linkalinpay.activity.model;//


// Created by Rajat Saha on 05-12-2020.
// Copyright (c) 2020 HTSM. All rights reserved.
//

public class Pricing {

    String name;
    String charge;
    String image;
    String pay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public Pricing(String name, String charge, String image, String pay) {
        this.name = name;
        this.charge = charge;
        this.image = image;
        this.pay = pay;
    }
}
