package com.myoffersapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SATHISH on 27-Jul-17.
 */

public class CategoryData
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

        @SerializedName("categoryid")
        @Expose
        private String categoryid;
        @SerializedName("categoryname")
        @Expose
        private String categoryname;
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
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("vendorcount")
        @Expose
        private String vendorcount;
        @SerializedName("offercount")
        @Expose
        private String offercount;

        public String getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(String categoryid) {
            this.categoryid = categoryid;
        }

        public String getCategoryname() {
            return categoryname;
        }

        public void setCategoryname(String categoryname) {
            this.categoryname = categoryname;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getVendorcount() {
            return vendorcount;
        }

        public void setVendorcount(String vendorcount) {
            this.vendorcount = vendorcount;
        }

        public String getOffercount() {
            return offercount;
        }

        public void setOffercount(String offercount) {
            this.offercount = offercount;
        }

    }


}