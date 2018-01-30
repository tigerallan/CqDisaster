package com.nandi.cqdisaster.securitydao.bean;

import java.io.Serializable;

/**
 * 隐患点图片信息
 * Created by baohongyan on 2018/1/9.
 */

public class PictureInfo implements Serializable {
    /**
     * "id":43009,
     * "deal_status":1,
     * "dis_no":"5001040010010101",
     * "dis_id":43009,
     * "f_remark":"",
     * "deal_type":1,
     * "type":"1",
     * "url":"5001040010010101-Q.jpg"
     */
    private int id;
    private int deal_status;
    private String dis_no;
    private int dis_id;
    private String f_remark;
    private int deal_type;
    private String type;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeal_status() {
        return deal_status;
    }

    public void setDeal_status(int deal_status) {
        this.deal_status = deal_status;
    }

    public String getDis_no() {
        return dis_no;
    }

    public void setDis_no(String dis_no) {
        this.dis_no = dis_no;
    }

    public int getDis_id() {
        return dis_id;
    }

    public void setDis_id(int dis_id) {
        this.dis_id = dis_id;
    }

    public String getF_remark() {
        return f_remark;
    }

    public void setF_remark(String f_remark) {
        this.f_remark = f_remark;
    }

    public int getDeal_type() {
        return deal_type;
    }

    public void setDeal_type(int deal_type) {
        this.deal_type = deal_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PictureInfo{" +
                "id=" + id +
                ", deal_status=" + deal_status +
                ", dis_no='" + dis_no + '\'' +
                ", dis_id=" + dis_id +
                ", f_remark='" + f_remark + '\'' +
                ", deal_type=" + deal_type +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
