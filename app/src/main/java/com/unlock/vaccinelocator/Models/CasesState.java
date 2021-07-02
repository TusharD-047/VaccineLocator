package com.unlock.vaccinelocator.Models;

public class CasesState {
    private int conf;
    private int recovered;
    private int deceased;
    private int increase_conf;
    private int change_recover;
    private int change_deceased;
    private int active;
    private String date;

    public CasesState(int conf, int recovered, int deceased, int increase_conf, int change_recover, int change_deceased, int active, String date) {
        this.conf = conf;
        this.recovered = recovered;
        this.deceased = deceased;
        this.increase_conf = increase_conf;
        this.change_recover = change_recover;
        this.change_deceased = change_deceased;
        this.active = active;
        this.date = date;
    }

    public int getConf() {
        return conf;
    }

    public void setConf(int conf) {
        this.conf = conf;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getDeceased() {
        return deceased;
    }

    public void setDeceased(int deceased) {
        this.deceased = deceased;
    }

    public int getIncrease_conf() {
        return increase_conf;
    }

    public void setIncrease_conf(int increase_conf) {
        this.increase_conf = increase_conf;
    }

    public int getChange_recover() {
        return change_recover;
    }

    public void setChange_recover(int change_recover) {
        this.change_recover = change_recover;
    }

    public int getChange_deceased() {
        return change_deceased;
    }

    public void setChange_deceased(int change_deceased) {
        this.change_deceased = change_deceased;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
