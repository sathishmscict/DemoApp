package com.myoffersapp.model;

/**
 * Created by SATHISH on 08-Aug-17.
 */


import android.view.ViewDebug;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookmarkData {


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

        @SerializedName("offerid")
        @Expose
        private String offerid;
        @SerializedName("img")
        @Expose
        private String img;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("discountype")
        @Expose
        private String discountype;
        @SerializedName("discountname")
        @Expose
        private String discountname;
        @SerializedName("discountdesc")
        @Expose
        private String discountdesc;
        @SerializedName("unitprice")
        @Expose
        private String unitprice;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("latitude")
        @Expose
        private String latitude;


        @SerializedName("longitude")
        @Expose
        private String longitude;


        @SerializedName("vendorname")
        @Expose
        private String vendorname;
        @SerializedName("address")
        @Expose
        private String address;

        @SerializedName("bookmarkid")
        @ViewDebug.ExportedProperty
        private String bookmarkid;

        public String getBookmarkid() {
            return bookmarkid;
        }

        public void setBookmarkid(String bookmarkid) {
            this.bookmarkid = bookmarkid;
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

        public String getOfferid() {
            return offerid;
        }

        public void setOfferid(String offerid) {
            this.offerid = offerid;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDiscountype() {
            return discountype;
        }

        public void setDiscountype(String discountype) {
            this.discountype = discountype;
        }

        public String getDiscountname() {
            return discountname;
        }

        public void setDiscountname(String discountname) {
            this.discountname = discountname;
        }

        public String getDiscountdesc() {
            return discountdesc;
        }

        public void setDiscountdesc(String discountdesc) {
            this.discountdesc = discountdesc;
        }

        public String getUnitprice() {
            return unitprice;
        }

        public void setUnitprice(String unitprice) {
            this.unitprice = unitprice;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
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

    }
}

