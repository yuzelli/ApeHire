package com.example.buiderdream.apehire.bean;

import java.io.Serializable;

/**
 * Created by 51644 on 2017/2/26.
 */

public class CompanyPics implements Serializable {


    /**
     * companyId : 2
     * pictureId : 5
     * pictureURL : qqqqqqqqqqqqqqqqqqqq
     */

    private int companyId;
    private int pictureId;
    private String pictureURL;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
