package com.ugiant.job.model;


public class UserBean {

    private String passWord;
    private String address;
    private String unionID;
    private String nickName;
    private String openID;
    private String headUrl;
    private String fullName;
    private String updateTime;
    private String businessCardUrl;
    private int userID;
    private String telePhone;
    private String createTime;
    private String loginName;
    private String qrCodeUrl;
    private String company;
    private String position;
    private String email;

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnionID() {
        return unionID;
    }

    public void setUnionID(String unionID) {
        this.unionID = unionID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBusinessCardUrl() {
        return businessCardUrl;
    }

    public void setBusinessCardUrl(String businessCardUrl) {
        this.businessCardUrl = businessCardUrl;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "passWord='" + passWord + '\'' +
                ", address='" + address + '\'' +
                ", unionID='" + unionID + '\'' +
                ", nickName='" + nickName + '\'' +
                ", openID='" + openID + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", fullName='" + fullName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", businessCardUrl='" + businessCardUrl + '\'' +
                ", userID=" + userID +
                ", telePhone='" + telePhone + '\'' +
                ", createTime='" + createTime + '\'' +
                ", loginName='" + loginName + '\'' +
                ", qrCodeUrl='" + qrCodeUrl + '\'' +
                ", company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
