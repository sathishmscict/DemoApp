package com.myoffersapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SATHISH on 28-Jul-17.
 */


public class ReferralData {

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

        @SerializedName("referalid")
        @Expose
        private String referalid;
        @SerializedName("referaluserid")
        @Expose
        private String referaluserid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("createat")
        @Expose
        private String createat;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("isfirstbill")
        @Expose
        private String isfirstbill;
        @SerializedName("isfreecouponselected")
        @Expose
        private String isfreecouponselected;

        public String getReferalid() {
            return referalid;
        }

        public void setReferalid(String referalid) {
            this.referalid = referalid;
        }

        public String getReferaluserid() {
            return referaluserid;
        }

        public void setReferaluserid(String referaluserid) {
            this.referaluserid = referaluserid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreateat() {
            return createat;
        }

        public void setCreateat(String createat) {
            this.createat = createat;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIsfirstbill() {
            return isfirstbill;
        }

        public void setIsfirstbill(String isfirstbill) {
            this.isfirstbill = isfirstbill;
        }

        public String getIsfreecouponselected() {
            return isfreecouponselected;
        }

        public void setIsfreecouponselected(String isfreecouponselected) {
            this.isfreecouponselected = isfreecouponselected;
        }

    }

}
