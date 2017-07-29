package com.myoffersapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SATHISH on 28-Jul-17.
 */

public class VendorData {



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
    public class Datum
    {

        @SerializedName("vendorid")
        @Expose
        private String vendorid;
        @SerializedName("categoryid")
        @Expose
        private String categoryid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("noofbranch")
        @Expose
        private String noofbranch;
        @SerializedName("isactive")
        @Expose
        private String isactive;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("createat")
        @Expose
        private String createat;
        @SerializedName("updateat")
        @Expose
        private String updateat;
        @SerializedName("deleteat")
        @Expose
        private String deleteat;
        @SerializedName("verificationstatus")
        @Expose
        private String verificationstatus;
        @SerializedName("commision")
        @Expose
        private String commision;
        @SerializedName("branchid")
        @Expose
        private String branchid;
        @SerializedName("img")
        @Expose
        private String img;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("branchisactive")
        @Expose
        private String branchisactive;
        @SerializedName("branchcreateat")
        @Expose
        private String branchcreateat;
        @SerializedName("branchupdateat")
        @Expose
        private String branchupdateat;
        @SerializedName("branchdeleteat")
        @Expose
        private String branchdeleteat;
        @SerializedName("fcmtoken")
        @Expose
        private String fcmtoken;
        @SerializedName("branchmobile")
        @Expose
        private String branchmobile;
        @SerializedName("gstno")
        @Expose
        private String gstno;

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoofbranch() {
        return noofbranch;
    }

    public void setNoofbranch(String noofbranch) {
        this.noofbranch = noofbranch;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getVerificationstatus() {
        return verificationstatus;
    }

    public void setVerificationstatus(String verificationstatus) {
        this.verificationstatus = verificationstatus;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBranchisactive() {
        return branchisactive;
    }

    public void setBranchisactive(String branchisactive) {
        this.branchisactive = branchisactive;
    }

    public String getBranchcreateat() {
        return branchcreateat;
    }

    public void setBranchcreateat(String branchcreateat) {
        this.branchcreateat = branchcreateat;
    }

    public String getBranchupdateat() {
        return branchupdateat;
    }

    public void setBranchupdateat(String branchupdateat) {
        this.branchupdateat = branchupdateat;
    }

    public String getBranchdeleteat() {
        return branchdeleteat;
    }

    public void setBranchdeleteat(String branchdeleteat) {
        this.branchdeleteat = branchdeleteat;
    }

    public String getFcmtoken() {
        return fcmtoken;
    }

    public void setFcmtoken(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }

    public String getBranchmobile() {
        return branchmobile;
    }

    public void setBranchmobile(String branchmobile) {
        this.branchmobile = branchmobile;
    }

    public String getGstno() {
        return gstno;
    }

    public void setGstno(String gstno) {
        this.gstno = gstno;
    }

}

}
