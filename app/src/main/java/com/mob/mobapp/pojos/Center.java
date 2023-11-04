package com.mob.mobapp.pojos;

public class Center {

    private Integer cId;
    private Double lon;
    private Double lat;
    private String address;
    private String hours;
    private String description;
    private String img;
    public Center() {
    }

    public Center(Double lon, Double lat, String address, String hours, String description, String img) {
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.hours = hours;
        this.description = description;
        this.img = img;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }
}