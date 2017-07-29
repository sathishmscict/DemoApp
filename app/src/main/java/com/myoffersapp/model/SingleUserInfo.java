package com.myoffersapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SATHISH on 27-Jul-17.
 */

public class SingleUserInfo
{

    @SerializedName("MESSAGE")
    @Expose
    private String mESSAGE;
    @SerializedName("ORIGINAL_ERROR")
    @Expose
    private String oRIGINALERROR;
    @SerializedName("ERROR_STATUS")
    @Expose
    private Boolean eRRORSTATUS;
    @SerializedName("RECORDS")
    @Expose
    private Boolean rECORDS;
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;

    public String getMESSAGE() {
        return mESSAGE;
    }

    public void setMESSAGE(String mESSAGE) {
        this.mESSAGE = mESSAGE;
    }

    public String getORIGINALERROR() {
        return oRIGINALERROR;
    }

    public void setORIGINALERROR(String oRIGINALERROR) {
        this.oRIGINALERROR = oRIGINALERROR;
    }

    public Boolean getERRORSTATUS() {
        return eRRORSTATUS;
    }

    public void setERRORSTATUS(Boolean eRRORSTATUS) {
        this.eRRORSTATUS = eRRORSTATUS;
    }

    public Boolean getRECORDS() {
        return rECORDS;
    }

    public void setRECORDS(Boolean rECORDS) {
        this.rECORDS = rECORDS;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("verificationstatus")
    @Expose
    private String verificationstatus;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("createat")
    @Expose
    private String createat;
    @SerializedName("updateat")
    @Expose
    private String updateat;
    @SerializedName("deleteat")
    @Expose
    private String deleteat;
    @SerializedName("useravatar")
    @Expose
    private String useravatar;
    @SerializedName("referalcode")
    @Expose
    private String referalcode;
    @SerializedName("verificationcode")
    @Expose
    private String verificationcode;
    @SerializedName("devicetype")
    @Expose
    private String devicetype;
    @SerializedName("deviceid")
    @Expose
    private String deviceid;
    @SerializedName("isactive")
    @Expose
    private String isactive;
    @SerializedName("isfirstbill")
    @Expose
    private String isfirstbill;
    @SerializedName("lattitude")
    @Expose
    private String lattitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("isrefered")
    @Expose
    private String isrefered;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerificationstatus() {
        return verificationstatus;
    }

    public void setVerificationstatus(String verificationstatus) {
        this.verificationstatus = verificationstatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCreateat() {
        return createat;
    }

    public void setCreateat(String createat) {
        this.createat = createat;
    }

    public String getUpdateat() {
        return updateat;
    }

    public void setUpdateat(String updateat) {
        this.updateat = updateat;
    }

    public String getDeleteat() {
        return deleteat;
    }

    public void setDeleteat(String deleteat) {
        this.deleteat = deleteat;
    }

    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }

    public String getReferalcode() {
        return referalcode;
    }

    public void setReferalcode(String referalcode) {
        this.referalcode = referalcode;
    }

    public String getVerificationcode() {
        return verificationcode;
    }

    public void setVerificationcode(String verificationcode) {
        this.verificationcode = verificationcode;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getIsfirstbill() {
        return isfirstbill;
    }

    public void setIsfirstbill(String isfirstbill) {
        this.isfirstbill = isfirstbill;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsrefered() {
        return isrefered;
    }

    public void setIsrefered(String isrefered) {
        this.isrefered = isrefered;
    }

}
}


