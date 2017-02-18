package com.example.buiderdream.apehire.entity;

import java.io.Serializable;

/**
 * 职位实体类
 * Created by 文捷 on 2017/2/18.
 */

public class JobInfo implements Serializable{
    private int JobId;//职位ID
    private String JobName;//职位名称
    private String JobDetail;//职位详情
    private int JobType;//职位种类
    private int JobCity;//职位所在城市
    private int JobCharge;//职位薪资范围
    private int CompanyId;//职位所属公司ID
    private String JobTechnology;//职位技能要求

    public JobInfo() {
        super();
    }

    public JobInfo(int jobId, String jobName, String jobDetail, int jobType, int jobCity, int jobCharge, int companyId, String jobTechnology) {
        JobId = jobId;
        JobName = jobName;
        JobDetail = jobDetail;
        JobType = jobType;
        JobCity = jobCity;
        JobCharge = jobCharge;
        CompanyId = companyId;
        JobTechnology = jobTechnology;
    }

    public int getJobId() {
        return JobId;
    }

    public void setJobId(int jobId) {
        JobId = jobId;
    }

    public String getJobName() {
        return JobName;
    }

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public String getJobDetail() {
        return JobDetail;
    }

    public void setJobDetail(String jobDetail) {
        JobDetail = jobDetail;
    }

    public int getJobType() {
        return JobType;
    }

    public void setJobType(int jobType) {
        JobType = jobType;
    }

    public int getJobCity() {
        return JobCity;
    }

    public void setJobCity(int jobCity) {
        JobCity = jobCity;
    }

    public int getJobCharge() {
        return JobCharge;
    }

    public void setJobCharge(int jobCharge) {
        JobCharge = jobCharge;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    public String getJobTechnology() {
        return JobTechnology;
    }

    public void setJobTechnology(String jobTechnology) {
        JobTechnology = jobTechnology;
    }
}
