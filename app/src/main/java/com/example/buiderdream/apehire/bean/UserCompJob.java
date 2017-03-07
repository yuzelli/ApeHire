package com.example.buiderdream.apehire.bean;



import java.io.Serializable;

/**
 * Created by 51644 on 2017/3/1.
 */

public class UserCompJob implements Serializable {


    /**
     * companyAddress : 上地
     * companyHeadImg : http://ojterpx44.bkt.clouddn.com/13133443002_1488850235532
     * companyId : 8
     * companyIntroduce : 腾讯-------------------------------------------------------------------------------------------------------------
     * companyName : 百度
     * companyNum : 13133443002
     * companyPassword : 123
     * companyScale : 4
     */

    private CompanyBean company;
    /**
     * companyId : 8
     * jobCharge : 6
     * jobCity : 1
     * jobDetail : android---------------------------
     * jobId : 10
     * jobName : android_java
     * jobTechnology : android ------------------------
     * jobType : 1
     */

    private JobBean job;
    /**
     * company : {"companyAddress":"上地","companyHeadImg":"http://ojterpx44.bkt.clouddn.com/13133443002_1488850235532","companyId":8,"companyIntroduce":"腾讯-------------------------------------------------------------------------------------------------------------","companyName":"百度","companyNum":"13133443002","companyPassword":"123","companyScale":4}
     * job : {"companyId":8,"jobCharge":6,"jobCity":1,"jobDetail":"android---------------------------","jobId":10,"jobName":"android_java","jobTechnology":"android ------------------------","jobType":1}
     * resume_id : 0
     * userInfo : {"userAdvantage":"","userAge":0,"userDegree":0,"userExpactMonney":0,"userExperence":"","userGender":"女","userHeadImg":"","userId":24,"userPassword":"123","userPhoneNum":"13133443001","userSchool":"","userTrueName":""}
     */

    private int resume_id;
    /**
     * userAdvantage :
     * userAge : 0
     * userDegree : 0
     * userExpactMonney : 0
     * userExperence :
     * userGender : 女
     * userHeadImg :
     * userId : 24
     * userPassword : 123
     * userPhoneNum : 13133443001
     * userSchool :
     * userTrueName :
     */

    private UserInfoBean userInfo;

    public CompanyBean getCompany() {
        return company;
    }

    public void setCompany(CompanyBean company) {
        this.company = company;
    }

    public JobBean getJob() {
        return job;
    }

    public void setJob(JobBean job) {
        this.job = job;
    }

    public int getResume_id() {
        return resume_id;
    }

    public void setResume_id(int resume_id) {
        this.resume_id = resume_id;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class CompanyBean implements Serializable{
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

    public static class JobBean implements Serializable{
        private int companyId;
        private int jobCharge;
        private int jobCity;
        private String jobDetail;
        private int jobId;
        private String jobName;
        private String jobTechnology;
        private int jobType;

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
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
    }

    public static class UserInfoBean implements Serializable{
        private String userAdvantage;
        private int userAge;
        private int userDegree;
        private int userExpactMonney;
        private String userExperence;
        private String userGender;
        private String userHeadImg;
        private int userId;
        private String userPassword;
        private String userPhoneNum;
        private String userSchool;
        private String userTrueName;

        public String getUserAdvantage() {
            return userAdvantage;
        }

        public void setUserAdvantage(String userAdvantage) {
            this.userAdvantage = userAdvantage;
        }

        public int getUserAge() {
            return userAge;
        }

        public void setUserAge(int userAge) {
            this.userAge = userAge;
        }

        public int getUserDegree() {
            return userDegree;
        }

        public void setUserDegree(int userDegree) {
            this.userDegree = userDegree;
        }

        public int getUserExpactMonney() {
            return userExpactMonney;
        }

        public void setUserExpactMonney(int userExpactMonney) {
            this.userExpactMonney = userExpactMonney;
        }

        public String getUserExperence() {
            return userExperence;
        }

        public void setUserExperence(String userExperence) {
            this.userExperence = userExperence;
        }

        public String getUserGender() {
            return userGender;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public String getUserHeadImg() {
            return userHeadImg;
        }

        public void setUserHeadImg(String userHeadImg) {
            this.userHeadImg = userHeadImg;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getUserPhoneNum() {
            return userPhoneNum;
        }

        public void setUserPhoneNum(String userPhoneNum) {
            this.userPhoneNum = userPhoneNum;
        }

        public String getUserSchool() {
            return userSchool;
        }

        public void setUserSchool(String userSchool) {
            this.userSchool = userSchool;
        }

        public String getUserTrueName() {
            return userTrueName;
        }

        public void setUserTrueName(String userTrueName) {
            this.userTrueName = userTrueName;
        }
    }
}
