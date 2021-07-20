package com.unlock.vaccinelocator.Models;

public class AlertList {

    String vacBrand,date,minAge,pincode,distr,state;

    public AlertList(String vacBrand, String date, String minAge, String pincode, String distr, String state) {
        this.vacBrand = vacBrand;
        this.date = date;
        this.minAge = minAge;
        this.pincode = pincode;
        this.distr = distr;
        this.state = state;
    }

    public String getVacBrand() {
        return vacBrand;
    }

    public String getDate() {
        return date;
    }

    public String getMinAge() {
        return minAge;
    }

    public String getPincode() {
        return pincode;
    }

    public String getDistr() {
        return distr;
    }

    public String getState() {
        return state;
    }
}
