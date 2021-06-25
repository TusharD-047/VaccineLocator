package com.unlock.vaccinelocator.Models;

public class Doses {
    private String centre_name;
    private String address;
    private String vac_name;
    private String slot1;
    private String slot2;
    private String cost;
    private String age;

    public Doses(String centre_name, String address, String vac_name, String slot1, String slot2, String cost,String age) {
        this.centre_name = centre_name;
        this.address = address;
        this.vac_name = vac_name;
        this.slot1 = slot1;
        this.slot2 = slot2;
        this.cost = cost;
        this.age = age;
    }

    public String getCentre_name() {
        return centre_name;
    }

    public void setCentre_name(String centre_name) {
        this.centre_name = centre_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVac_name() {
        return vac_name;
    }

    public void setVac_name(String vac_name) {
        this.vac_name = vac_name;
    }

    public String getSlot1() {
        return slot1;
    }

    public void setSlot1(String slot1) {
        this.slot1 = slot1;
    }

    public String getSlot2() {
        return slot2;
    }

    public void setSlot2(String slot2) {
        this.slot2 = slot2;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
