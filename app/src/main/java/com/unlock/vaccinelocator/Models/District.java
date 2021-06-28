package com.unlock.vaccinelocator.Models;

import org.jetbrains.annotations.NotNull;

public class District {
    private int dis_id;
    private String dis_name;

    public District(int dis_id, String dis_name) {
        this.dis_id = dis_id;
        this.dis_name = dis_name;
    }

    public int getDis_id() {
        return dis_id;
    }

    public void setDis_id(int dis_id) {
        this.dis_id = dis_id;
    }

    public String getDis_name() {
        return dis_name;
    }

    public void setDis_name(String dis_name) {
        this.dis_name = dis_name;
    }
    @Override
    public @NotNull String toString() {
        return dis_name;
    }
}
