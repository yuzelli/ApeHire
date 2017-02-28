package com.example.buiderdream.apehire.bean;

import java.io.Serializable;

/**
 * Created by 51644 on 2017/2/27.
 */

public class JobAndCompany implements Serializable {

    /**
     * companyAddress : 北京上地中关村
     * companyHeadImg : http://ojterpx44.bkt.clouddn.com/13133443000_1488105970188
     * companyId : 7
     * companyIntroduce : 阿里巴巴网络技术有限公司（简称：阿里巴巴集团）是以曾担任英语教师的马云为首的18人，于1999年在杭州创立，他们相信互联网能够创造公平的竞争环境，让小企业通过创新与科技扩展业务，并在参与国内或全球市场竞争时处于更有利的位置。[1] 阿里巴巴集团经营多项业务，另外也从关联公司的业务和服务中取得经营商业生态系统上的支援。业务和关联公司的业务包括：淘宝网、天猫、聚划算、全球速卖通、阿里巴巴国际交易市场、1688、阿里妈妈、阿里云、蚂蚁金服、菜鸟网络等。[2]  2014年9月19日，阿里巴巴集团在纽约证券交易所正式挂牌上市，股票代码“BABA”，创始人和董事局主席为马云。2015年全年，阿里巴巴总营收943.84亿元人民币，净利润688.44亿元人民币。2016年4月6日，阿里巴巴正式宣布已经成为全球最大的零售交易平台。
     * companyName : 阿里巴巴
     * companyNum : 13133443000
     * companyPassword : 123
     * companyScale : 3
     */

    private CompanyBean company;
    /**
     * company : {"companyAddress":"北京上地中关村","companyHeadImg":"http://ojterpx44.bkt.clouddn.com/13133443000_1488105970188","companyId":7,"companyIntroduce":"阿里巴巴网络技术有限公司（简称：阿里巴巴集团）是以曾担任英语教师的马云为首的18人，于1999年在杭州创立，他们相信互联网能够创造公平的竞争环境，让小企业通过创新与科技扩展业务，并在参与国内或全球市场竞争时处于更有利的位置。[1] 阿里巴巴集团经营多项业务，另外也从关联公司的业务和服务中取得经营商业生态系统上的支援。业务和关联公司的业务包括：淘宝网、天猫、聚划算、全球速卖通、阿里巴巴国际交易市场、1688、阿里妈妈、阿里云、蚂蚁金服、菜鸟网络等。[2]  2014年9月19日，阿里巴巴集团在纽约证券交易所正式挂牌上市，股票代码\u201cBABA\u201d，创始人和董事局主席为马云。2015年全年，阿里巴巴总营收943.84亿元人民币，净利润688.44亿元人民币。2016年4月6日，阿里巴巴正式宣布已经成为全球最大的零售交易平台。","companyName":"阿里巴巴","companyNum":"13133443000","companyPassword":"123","companyScale":3}
     * jobCharge : 1
     * jobCity : 1
     * jobDetail : ccccccccc
     * jobId : 4
     * jobName : android
     * jobTechnology : cccccccccccccccccc
     * jobType : 1
     */

    private int jobCharge;
    private int jobCity;
    private String jobDetail;
    private int jobId;
    private String jobName;
    private String jobTechnology;
    private int jobType;

    public CompanyBean getCompany() {
        return company;
    }

    public void setCompany(CompanyBean company) {
        this.company = company;
    }

    public int getJobCharge() {
        return jobCharge;
    }

    public void setJobCharge(int jobCharge) {
        this.jobCharge = jobCharge;
    }

    public int getJobCity() {
        return jobCity;
    }

    public void setJobCity(int jobCity) {
        this.jobCity = jobCity;
    }

    public String getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(String jobDetail) {
        this.jobDetail = jobDetail;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobTechnology() {
        return jobTechnology;
    }

    public void setJobTechnology(String jobTechnology) {
        this.jobTechnology = jobTechnology;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public static class CompanyBean implements Serializable {
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
}
