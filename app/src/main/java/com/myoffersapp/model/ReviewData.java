package com.myoffersapp.model;

/**
 * Created by SATHISH on 05-Aug-17.
 */


import com.google.gson.annotations.SerializedName;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewData {

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
    @SerializedName("AVERAGERATE")
    @Expose
    private String aVERAGERATE;
    @SerializedName("TOTALREVIEW")
    @Expose
    private String tOTALREVIEW;
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

    public String getAVERAGERATE() {
        return aVERAGERATE;
    }

    public void setAVERAGERATE(String aVERAGERATE) {
        this.aVERAGERATE = aVERAGERATE;
    }

    public String getTOTALREVIEW() {
        return tOTALREVIEW;
    }

    public void setTOTALREVIEW(String tOTALREVIEW) {
        this.tOTALREVIEW = tOTALREVIEW;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("useravatar")
        @Expose
        private String useravatar;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("date")
        @Expose
        private String date;

        public String getUseravatar() {
            return useravatar;
        }

        public void setUseravatar(String useravatar) {
            this.useravatar = useravatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

}


