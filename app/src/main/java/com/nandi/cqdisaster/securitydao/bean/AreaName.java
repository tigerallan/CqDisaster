package com.nandi.cqdisaster.securitydao.bean;

/**
 * Created by qingsong on 2018/1/9.
 */

public class AreaName {
    /**
     * id : 2
     * lon : 108.426777
     * level : 3
     * area_parent : 1
     * area_code : 500101
     * area_name : 万州区
     * unit_id : 2
     * lat : 30.714754
     */
    private int id;
    private double lon;
    private int level;
    private int area_parent;
    private int area_code;
    private String area_name;
    private int unit_id;
    private double lat;


    public AreaName(int id, double lon, int level, int area_parent, int area_code, String area_name, int unit_id, double lat) {
        this.id = id;
        this.lon = lon;
        this.level = level;
        this.area_parent = area_parent;
        this.area_code = area_code;
        this.area_name = area_name;
        this.unit_id = unit_id;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getArea_parent() {
        return area_parent;
    }

    public void setArea_parent(int area_parent) {
        this.area_parent = area_parent;
    }

    public int getArea_code() {
        return area_code;
    }

    public void setArea_code(int area_code) {
        this.area_code = area_code;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
