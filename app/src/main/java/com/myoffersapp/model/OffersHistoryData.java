package com.myoffersapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SATHISH on 02-Aug-17.
 */

public class OffersHistoryData {



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

        @SerializedName("offerhistoryid")
        @Expose
        private String offerhistoryid;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("offerid")
        @Expose
        private String offerid;
        @SerializedName("isgivefreecoupon")
        @Expose
        private String isgivefreecoupon;
        @SerializedName("billamount")
        @Expose
        private String billamount;
        @SerializedName("createat")
        @Expose
        private String createat;
        @SerializedName("updateat")
        @Expose
        private String updateat;
        @SerializedName("deleteat")
        @Expose
        private String deleteat;
        @SerializedName("qty")
        @Expose
        private String qty;
        @SerializedName("offername")
        @Expose
        private String offername;
        @SerializedName("vendorname")
        @Expose
        private String vendorname;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("offerimage")
        @Expose
        private String offerimage;

        public String getOfferhistoryid() {
            return offerhistoryid;
        }

        public void setOfferhistoryid(String offerhistoryid) {
            this.offerhistoryid = offerhistoryid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getOfferid() {
            return offerid;
        }

        public void setOfferid(String offerid) {
            this.offerid = offerid;
        }

        public String getIsgivefreecoupon() {
            return isgivefreecoupon;
        }

        public void setIsgivefreecoupon(String isgivefreecoupon) {
            this.isgivefreecoupon = isgivefreecoupon;
        }

        public String getBillamount() {
            return billamount;
        }

        public void setBillamount(String billamount) {
            this.billamount = billamount;
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

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getOffername() {
            return offername;
        }

        public void setOffername(String offername) {
            this.offername = offername;
        }

        public String getVendorname() {
            return vendorname;
        }

        public void setVendorname(String vendorname) {
            this.vendorname = vendorname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getOfferimage() {
            return offerimage;
        }

        public void setOfferimage(String offerimage) {
            this.offerimage = offerimage;
        }

    }


}



