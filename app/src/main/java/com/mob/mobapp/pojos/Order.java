package com.mob.mobapp.pojos;

public class Order {
    private String status;
    private String dateTime;
    private String desc;

    public Order(String status, String dateTime, String desc) {
        this.status = status;
        this.dateTime = dateTime;
        this.desc = desc;
    }

    public Order() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}