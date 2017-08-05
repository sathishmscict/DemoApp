package com.myoffersapp.model;

/**
 * Created by SATHISH on 05-Aug-17.
 */


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class FreeOffersData
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

            @SerializedName("freecouponid")
            @Expose
            private String freecouponid;
            @SerializedName("istrasfered")
            @Expose
            private String istrasfered;
            @SerializedName("trasfereduserid")
            @Expose
            private String trasfereduserid;
            @SerializedName("isused")
            @Expose
            private String isused;
            @SerializedName("isgifted")
            @Expose
            private String isgifted;
            @SerializedName("offerid")
            @Expose
            private String offerid;
            @SerializedName("img")
            @Expose
            private String img;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("validfromdate")
            @Expose
            private String validfromdate;
            @SerializedName("validtodate")
            @Expose
            private String validtodate;
            @SerializedName("quantity")
            @Expose
            private String quantity;
            @SerializedName("remaining")
            @Expose
            private String remaining;
            @SerializedName("vendorid")
            @Expose
            private String vendorid;
            @SerializedName("islimited")
            @Expose
            private String islimited;
            @SerializedName("createat")
            @Expose
            private String createat;
            @SerializedName("updateat")
            @Expose
            private String updateat;
            @SerializedName("deleteat")
            @Expose
            private String deleteat;
            @SerializedName("isactive")
            @Expose
            private String isactive;
            @SerializedName("discountype")
            @Expose
            private String discountype;
            @SerializedName("discountypename")
            @Expose
            private String discountypename;
            @SerializedName("discountdesc")
            @Expose
            private String discountdesc;

            @SerializedName("unitprice")
            @Expose
            private String unitprice;


            @SerializedName("rating")
            @Expose
            private String rating;

            @SerializedName("fromrate")
            @Expose
            private String fromrate;
            @SerializedName("torate")
            @Expose
            private String torate;
            @SerializedName("termsandcondition")
            @Expose
            private String termsandcondition;
            @SerializedName("latitude")
            @Expose
            private String latitude;
            @SerializedName("longitude")
            @Expose
            private String longitude;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("giftedby")
            @Expose
            private String giftedby;

            public String getFreecouponid() {
                return freecouponid;
            }

            public void setFreecouponid(String freecouponid) {
                this.freecouponid = freecouponid;
            }

            public String getIstrasfered() {
                return istrasfered;
            }

            public void setIstrasfered(String istrasfered) {
                this.istrasfered = istrasfered;
            }

            public String getTrasfereduserid() {
                return trasfereduserid;
            }

            public void setTrasfereduserid(String trasfereduserid) {
                this.trasfereduserid = trasfereduserid;
            }

            public String getIsused() {
                return isused;
            }

            public void setIsused(String isused) {
                this.isused = isused;
            }

            public String getIsgifted() {
                return isgifted;
            }

            public void setIsgifted(String isgifted) {
                this.isgifted = isgifted;
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

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getValidfromdate() {
                return validfromdate;
            }

            public void setValidfromdate(String validfromdate) {
                this.validfromdate = validfromdate;
            }

            public String getValidtodate() {
                return validtodate;
            }

            public void setValidtodate(String validtodate) {
                this.validtodate = validtodate;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getRemaining() {
                return remaining;
            }

            public void setRemaining(String remaining) {
                this.remaining = remaining;
            }

            public String getVendorid() {
                return vendorid;
            }

            public void setVendorid(String vendorid) {
                this.vendorid = vendorid;
            }

            public String getIslimited() {
                return islimited;
            }

            public void setIslimited(String islimited) {
                this.islimited = islimited;
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

            public String getIsactive() {
                return isactive;
            }

            public void setIsactive(String isactive) {
                this.isactive = isactive;
            }

            public String getDiscountype() {
                return discountype;
            }

            public void setDiscountype(String discountype) {
                this.discountype = discountype;
            }

            public String getDiscountypename() {
                return discountypename;
            }

            public void setDiscountypename(String discountypename) {
                this.discountypename = discountypename;
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

            public String getFromrate() {
                return fromrate;
            }

            public void setFromrate(String fromrate) {
                this.fromrate = fromrate;
            }

            public String getTorate() {
                return torate;
            }

            public void setTorate(String torate) {
                this.torate = torate;
            }

            public String getTermsandcondition() {
                return termsandcondition;
            }

            public void setTermsandcondition(String termsandcondition) {
                this.termsandcondition = termsandcondition;
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

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getGiftedby() {
                return giftedby;
            }

            public void setGiftedby(String giftedby) {
                this.giftedby = giftedby;
            }

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }

        }


    }