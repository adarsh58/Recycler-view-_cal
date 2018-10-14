package com.adarsh.recycler;

public class dataitem {

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public dataitem(int number) {
        this.number = number;
    }

    private int number;

    public String getTotalEfforts() {
        return totalEfforts;
    }

    public void setTotalEfforts(String totalEfforts) {
        this.totalEfforts = totalEfforts;
    }

    private String totalEfforts;
}
