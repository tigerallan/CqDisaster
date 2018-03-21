package com.nandi.cqdisaster.examine.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ChenPeng on 2017/3/7.
 */
@Entity
public class BaseInfoBean {
    @Id(autoincrement = true)
    private Long id;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 灾害点名称
     */
    private String baseInfoName;
    /**
     * 地址
     */
    private String baseInfoAddress;
    /**
     * 调查人
     */
    private String baseInfoLookPerson;
    /**
     * 联系人
     */
    private String baseInfoContact;
    /**
     * 联系人电话
     */
    private String baseInfoMobile;
    /**
     * 发生时间
     */
    private String baseInfoHappenTime;
    /**
     * 调查时间
     */
    private String baseInfoLookTime;
    public String getBaseInfoLookTime() {
        return this.baseInfoLookTime;
    }
    public void setBaseInfoLookTime(String baseInfoLookTime) {
        this.baseInfoLookTime = baseInfoLookTime;
    }
    public String getBaseInfoHappenTime() {
        return this.baseInfoHappenTime;
    }
    public void setBaseInfoHappenTime(String baseInfoHappenTime) {
        this.baseInfoHappenTime = baseInfoHappenTime;
    }
    public String getBaseInfoMobile() {
        return this.baseInfoMobile;
    }
    public void setBaseInfoMobile(String baseInfoMobile) {
        this.baseInfoMobile = baseInfoMobile;
    }
    public String getBaseInfoContact() {
        return this.baseInfoContact;
    }
    public void setBaseInfoContact(String baseInfoContact) {
        this.baseInfoContact = baseInfoContact;
    }
    public String getBaseInfoLookPerson() {
        return this.baseInfoLookPerson;
    }
    public void setBaseInfoLookPerson(String baseInfoLookPerson) {
        this.baseInfoLookPerson = baseInfoLookPerson;
    }
    public String getBaseInfoAddress() {
        return this.baseInfoAddress;
    }
    public void setBaseInfoAddress(String baseInfoAddress) {
        this.baseInfoAddress = baseInfoAddress;
    }
    public String getBaseInfoName() {
        return this.baseInfoName;
    }
    public void setBaseInfoName(String baseInfoName) {
        this.baseInfoName = baseInfoName;
    }
    public String getTaskId() {
        return this.taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1610890069)
    public BaseInfoBean(Long id, String taskId, String baseInfoName,
            String baseInfoAddress, String baseInfoLookPerson,
            String baseInfoContact, String baseInfoMobile,
            String baseInfoHappenTime, String baseInfoLookTime) {
        this.id = id;
        this.taskId = taskId;
        this.baseInfoName = baseInfoName;
        this.baseInfoAddress = baseInfoAddress;
        this.baseInfoLookPerson = baseInfoLookPerson;
        this.baseInfoContact = baseInfoContact;
        this.baseInfoMobile = baseInfoMobile;
        this.baseInfoHappenTime = baseInfoHappenTime;
        this.baseInfoLookTime = baseInfoLookTime;
    }
    @Generated(hash = 139291923)
    public BaseInfoBean() {
    }
}
