package com.myoffersapp.retrofit;

import com.myoffersapp.model.CategoryData;
import com.myoffersapp.model.DealsData;
import com.myoffersapp.model.ReferralData;
import com.myoffersapp.model.SingleOfferData;
import com.myoffersapp.model.SingleUserInfo;
import com.myoffersapp.model.VendorData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiInterface {

    @POST("login")
    @FormUrlEncoded
    Call<SingleUserInfo> sendUserLoginData(@Field("type") String type, @Field("mobile") String mobile, @Field("name") String name, @Field("email") String email, @Field("isactive") String isactive, @Field("deviceid") String deviceid, @Field("devicetype") String devicetype, @Field("lattitude") String lattitude, @Field("longitude") String longtitude);

    @POST("UpdateProfile")
    @FormUrlEncoded
    Call<SingleUserInfo> sendAndUpdateProfileInfo(@Field("type")String type,@Field("userid")String userid,@Field("name")String name,@Field("email")String email,@Field("gender")String gender,@Field("dob") String dob,@Field("mobile") String mobile);


    @POST("ViewCategoryData")
    @FormUrlEncoded
    Call<CategoryData> getAllCategoryDetailsFromServer(@Field("type")String type);


    @POST("ViewVendorData")
    @FormUrlEncoded
    Call<VendorData> getAllVendorDetailsFromServer(@Field("type")String type,@Field("categoryid")String  categoyId);


    @POST("ViewAllOffersData")
    @FormUrlEncoded
    Call<DealsData> getAllDealsDetailsFromServer(@Field("type")String type, @Field("offertypeid")Integer  categoyId,@Field("branchid")String branchid);


    @POST("ViewSingleOfferData")
    @FormUrlEncoded
    Call<SingleOfferData> getSingleDealsDetailsFromServer(@Field("type")String type, @Field("offerid")String  categoyId);



    @POST("ViewReferalData")
    @FormUrlEncoded
    Call<ReferralData> getAllReferralsDetailsFromServer(@Field("type")String type, @Field("userid")String  userid);



}
