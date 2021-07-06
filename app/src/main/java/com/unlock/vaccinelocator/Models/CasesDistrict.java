package com.unlock.vaccinelocator.Models;

public class CasesDistrict {
    String Dis_name;
    int active_dis;
    int confirmed_dis;
    int dec_dis;
    int rec_dis;
    int chng_conf_dis;
    int chng_rec_dis;
    int chng_dec_dis;

    public CasesDistrict(String dis_name, int active_dis, int confirmed_dis, int dec_dis, int rec_dis, int chng_conf_dis, int chng_rec_dis, int chng_dec_dis) {
        Dis_name = dis_name;
        this.active_dis = active_dis;
        this.confirmed_dis = confirmed_dis;
        this.dec_dis = dec_dis;
        this.rec_dis = rec_dis;
        this.chng_conf_dis = chng_conf_dis;
        this.chng_rec_dis = chng_rec_dis;
        this.chng_dec_dis = chng_dec_dis;
    }

    public String getDis_name() {
        return Dis_name;
    }

    public void setDis_name(String dis_name) {
        Dis_name = dis_name;
    }

    public int getActive_dis() {
        return active_dis;
    }

    public void setActive_dis(int active_dis) {
        this.active_dis = active_dis;
    }

    public int getConfirmed_dis() {
        return confirmed_dis;
    }

    public void setConfirmed_dis(int confirmed_dis) {
        this.confirmed_dis = confirmed_dis;
    }

    public int getDec_dis() {
        return dec_dis;
    }

    public void setDec_dis(int dec_dis) {
        this.dec_dis = dec_dis;
    }

    public int getRec_dis() {
        return rec_dis;
    }

    public void setRec_dis(int rec_dis) {
        this.rec_dis = rec_dis;
    }

    public int getChng_conf_dis() {
        return chng_conf_dis;
    }

    public void setChng_conf_dis(int chng_conf_dis) {
        this.chng_conf_dis = chng_conf_dis;
    }

    public int getChng_rec_dis() {
        return chng_rec_dis;
    }

    public void setChng_rec_dis(int chng_rec_dis) {
        this.chng_rec_dis = chng_rec_dis;
    }

    public int getChng_dec_dis() {
        return chng_dec_dis;
    }

    public void setChng_dec_dis(int chng_dec_dis) {
        this.chng_dec_dis = chng_dec_dis;
    }
}
