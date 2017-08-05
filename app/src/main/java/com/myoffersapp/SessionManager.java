package com.myoffersapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Lincoln on 05/05/16.
 */
public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "offersapp";

    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Check For Activation
    public static final String KEY_CODE = "code", KEY_SMSURL = "smsurl",
            KEY_RECEIVECODE = "reccode";


    public static final String KEY_USER_AVATAR_URL = "UserAvatarURL", KEY_USER_EMAIL = "UserEmail", KEY_USER_NAME = "UserName";

    public static final String KEY_OFFFER_TYPE_ID =  "OfferTypeId";

    public static final String KEY_ENODEDED_STRING = "Encoded_string";
    public static final String KEY_USER_ID = "UserId", KEY_USER_VERIFICATION_STATUS = "VerificationStatus", KEY_USER_GENDER = "Gender", KEY_USER_DOB = "DOB", KEY_USER_REFERAL_CODE = "ReferralCode", KEY_USER_DEVICE_TYPE = "DeviceType", KEY_USER_IS_FIRST_BILL = "IsFirstBill", KEY_USER_IS_ACTIVE = "IsActive", KEY_USER_IS_REFERRED = "IsReferred", KEY_USER_MOBILE = "UserMobile";

    public static final String KEY_CATEGORYID = "categoryId", KEY_CATEGORY_NAME = "categoryName";
    public static final String KEY_VENDORID = "VendorId", KEY_BRANCHIID = "BranchId";
    public static final String KEY_OFFERID = "OfferId",KEY_OFFER_TITLE = "offerTitle";

    public static final String KEY_REFERALID = "referalId";

    public static final String KEY_LATTITUDE = "Lattitude", KEY_LONGTITUDE = "Longtitude",  KEY_COMPANYNAME = "CompanyName";

    public static final String KEY_DISTANCE_INTERVAL_IN_KM="DistanceInKm";



    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setGPSLocations(String lattitude, String longtitude, String companyname) {




        editor.putString(KEY_LATTITUDE, lattitude);
        editor.putString(KEY_LONGTITUDE, longtitude);
        editor.putString(KEY_COMPANYNAME, companyname);


        editor.commit();
    }


    public void CheckSMSVerificationActivity(String reccode, String actstatus) {

        editor.putString(KEY_RECEIVECODE, reccode);
        editor.putString(KEY_USER_VERIFICATION_STATUS, actstatus);
        editor.commit();

    }

    public  void  setDistanceInterval(String distanceKM)
    {
        editor.putString(KEY_DISTANCE_INTERVAL_IN_KM , distanceKM);
        editor.commit();
    }



    public void setUserImageUrl(String imgURL) {

        editor.putString(KEY_USER_AVATAR_URL, imgURL);
    }

    public void setOfferTypeId(int offerTypeId)
    {
        editor.putString(KEY_OFFFER_TYPE_ID , String.valueOf(offerTypeId));
        editor.commit();
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getSessionDetails() {


        HashMap<String, String> user = new HashMap<String, String>();



        user.put(KEY_REFERALID  , pref.getString(KEY_REFERALID,"0"));


        user.put(KEY_OFFFER_TYPE_ID , pref.getString(KEY_OFFFER_TYPE_ID , "1"));
        user.put(KEY_DISTANCE_INTERVAL_IN_KM , pref.getString(KEY_DISTANCE_INTERVAL_IN_KM , "10"));

        user.put(KEY_OFFER_TITLE , pref.getString(KEY_OFFER_TITLE , ""));

        user.put(KEY_COMPANYNAME, pref.getString(KEY_COMPANYNAME, ""));

        user.put(KEY_LATTITUDE, pref.getString(KEY_LATTITUDE, "0"));

        user.put(KEY_LONGTITUDE, pref.getString(KEY_LONGTITUDE, "0"));



        user.put(KEY_OFFERID, pref.getString(KEY_OFFERID, "0"));


        user.put(KEY_BRANCHIID, pref.getString(KEY_BRANCHIID, "0"));

        user.put(KEY_VENDORID, pref.getString(KEY_VENDORID, "0"));
        user.put(KEY_CATEGORY_NAME, pref.getString(KEY_CATEGORY_NAME, ""));
        user.put(KEY_CATEGORYID, pref.getString(KEY_CATEGORYID, ""));
        user.put(KEY_ENODEDED_STRING, pref.getString(KEY_ENODEDED_STRING, ""));
        // user.put(KEY_ORDERID, pref.getString(KEY_ORDERID, "0"));
        user.put(KEY_RECEIVECODE, pref.getString(KEY_RECEIVECODE, "0"));
        user.put(KEY_CODE, pref.getString(KEY_CODE, "0"));
        user.put(KEY_SMSURL, pref.getString(KEY_SMSURL, ""));

        user.put(KEY_USER_VERIFICATION_STATUS, pref.getString(KEY_USER_VERIFICATION_STATUS, "0"));
        user.put(KEY_USER_AVATAR_URL, pref.getString(KEY_USER_AVATAR_URL, ""));

        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, ""));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, ""));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, "0"));
        user.put(KEY_USER_GENDER, pref.getString(KEY_USER_GENDER, ""));
        user.put(KEY_USER_DOB, pref.getString(KEY_USER_DOB, ""));
        user.put(KEY_USER_REFERAL_CODE, pref.getString(KEY_USER_REFERAL_CODE, ""));
        user.put(KEY_USER_DEVICE_TYPE, pref.getString(KEY_USER_DEVICE_TYPE, ""));
        user.put(KEY_USER_IS_FIRST_BILL, pref.getString(KEY_USER_IS_FIRST_BILL, "0"));
        user.put(KEY_USER_IS_ACTIVE, pref.getString(KEY_USER_IS_ACTIVE, "1"));
        user.put(KEY_USER_IS_REFERRED, pref.getString(KEY_USER_IS_REFERRED, "0"));

        user.put(KEY_USER_MOBILE, pref.getString(KEY_USER_MOBILE, "0"));


        return user;
    }

    public void setEncodedImage(String encodeo_image) {


        editor.putString(KEY_ENODEDED_STRING, encodeo_image);

        editor.commit();
    }


    public void createUserSendSmsUrl(String code, String websiteurl) {

        editor.putString(KEY_CODE, code);
        editor.putString(KEY_SMSURL, websiteurl);// http://radiant.dnsitexperts.com/JSON_Data.aspx?type=otp&mobile=9825681802&code=7692
        editor.commit();

    }


    public void setUserDetails(String username, String email) {

        editor.putString(KEY_USER_NAME, username);
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();


    }

    public void setUserDetails(String name, String email, String userId, String verificationStatus, String gender, String dob, String useravatar, String referalcode, String devicetype, String isActive, String isFirstBill, String isreferred, String usermobile) {


        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_VERIFICATION_STATUS, verificationStatus);
        editor.putString(KEY_USER_GENDER, gender);
        editor.putString(KEY_USER_DOB, dob);
        editor.putString(KEY_USER_AVATAR_URL, useravatar);
        editor.putString(KEY_USER_REFERAL_CODE, referalcode);
        editor.putString(KEY_USER_DEVICE_TYPE, devicetype);
        editor.putString(KEY_USER_IS_FIRST_BILL, isFirstBill);
        editor.putString(KEY_USER_IS_ACTIVE, isActive);
        editor.putString(KEY_USER_IS_REFERRED, isreferred);
        editor.putString(KEY_USER_MOBILE, usermobile);

        editor.commit();


    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void setCategoryDetails(String categoryId, String categoryName) {


        editor.putString(KEY_CATEGORYID, categoryId);
        editor.putString(KEY_CATEGORY_NAME, categoryName);

        editor.commit();
    }

    public void setVendorDetails(String branchId, String vendorId) {


        editor.putString(KEY_VENDORID, vendorId);
        editor.putString(KEY_BRANCHIID, branchId);


        editor.commit();
    }

    public void setOfferDetails(String offerid,String offerTitle) {
        editor.putString(KEY_OFFERID, offerid);
        editor.putString(KEY_OFFER_TITLE, offerTitle);

        editor.commit();
    }


    public void setReferalID(String referalid) {

        editor.putString(KEY_REFERALID , referalid);
        editor.commit();
    }
}
