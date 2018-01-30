package com.nandi.cqdisaster.securitydao.bean;

/**
 * Created by qingsong on 2018/1/10.
 */

public class PersonnelInfo {

    /**
     * tel : 15702312977
     * name : 冉茂林
     * url : wulong/tukanzhen/ranmaolin.jpg
     */
    private String type;
    private String imageType;
    private String tel;
    private String name;
    private String url;

    public PersonnelInfo(String type, String imageType, String tel, String name, String url) {
        this.type = type;
        this.imageType = imageType;
        this.tel = tel;
        this.name = name;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PersonnelInfo{" +
                "tel='" + tel + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
