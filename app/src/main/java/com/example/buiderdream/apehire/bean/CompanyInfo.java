package com.example.buiderdream.apehire.bean;

import java.io.Serializable;

/**
 * Created by 51644 on 2017/2/24.
 */

public class CompanyInfo implements Serializable {


    /**
     * companyAddress : qq
     * companyHeadImg : http://avatar.csdn.net/3/0/F/1_qq_30276065.jpg
     * companyId : 1
     * companyIntroduce : qq
     * companyName : qq
     * companyNum : 13133443006
     * companyPassword : lbl123123
     * companyScale : 1
     */

    private String companyAddress;
    private String companyHeadImg;
    private int companyId;
    private String companyIntroduce;
    private String companyName;
    private String companyNum;
    private String companyPassword;
    private int companyScale;

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyHeadImg() {
        return companyHeadImg;
    }

    public void setCompanyHeadImg(String companyHeadImg) {
        this.companyHeadImg = companyHeadImg;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyIntroduce() {
        return companyIntroduce;
    }

    public void setCompanyIntroduce(String companyIntroduce) {
        this.companyIntroduce = companyIntroduce;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getCompanyPassword() {
        return companyPassword;
    }

    public void setCompanyPassword(String companyPassword) {
        this.companyPassword = companyPassword;
    }

    public int getCompanyScale() {
        return companyScale;
    }

    public void setCompanyScale(int companyScale) {
        this.companyScale = companyScale;
    }
}
