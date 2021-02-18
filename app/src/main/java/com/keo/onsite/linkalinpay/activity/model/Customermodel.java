package com.keo.onsite.linkalinpay.activity.model;//


// Created by Rajat Saha on 23-11-2020.
// Copyright (c) 2020 HTSM. All rights reserved.
//

public class Customermodel {

    String cust_id;
    String cust_name;
    String cust_mobile;
    String cust_email;
    String cust_profile;


    public Customermodel(String cust_id, String cust_name, String cust_mobile, String cust_email, String cust_profile) {
        this.cust_id = cust_id;
        this.cust_name = cust_name;
        this.cust_mobile = cust_mobile;
        this.cust_email = cust_email;
        this.cust_profile = cust_profile;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_mobile() {
        return cust_mobile;
    }

    public void setCust_mobile(String cust_mobile) {
        this.cust_mobile = cust_mobile;
    }

    public String getCust_email() {
        return cust_email;
    }

    public void setCust_email(String cust_email) {
        this.cust_email = cust_email;
    }

    public String getCust_profile() {
        return cust_profile;
    }

    public void setCust_profile(String cust_profile) {
        this.cust_profile = cust_profile;
    }
}
