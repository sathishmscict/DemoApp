package com.myoffersapp.retrofit;

import com.myoffersapp.model.CategoryData;
import com.myoffersapp.model.DealsData;
import com.myoffersapp.model.FCMReponse;
import com.myoffersapp.model.FreeOffersData;
import com.myoffersapp.model.InsertFreeCoupon;
import com.myoffersapp.model.OffersHistoryData;
import com.myoffersapp.model.ReferralData;
import com.myoffersapp.model.ReviewData;
import com.myoffersapp.model.ReviewReponse;
import com.myoffersapp.model.SingleOfferData;
import com.myoffersapp.model.SingleUserInfo;
import com.myoffersapp.model.SlidersData;
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
    Call<SingleUserInfo> sendAndUpdateProfileInfo(@Field("type") String type, @Field("userid") String userid, @Field("name") String name, @Field("email") String email, @Field("gender") String gender, @Field("dob") String dob, @Field("mobile") String mobile);


    @POST("ViewCategoryData")
    @FormUrlEncoded
    Call<CategoryData> getAllCategoryDetailsFromServer(@Field("type") String type);


    @POST("ViewVendorData")
    @FormUrlEncoded
    Call<VendorData> getAllVendorDetailsFromServer(@Field("type") String type, @Field("categoryid") String categoyId);


    @POST("ViewAllOffersData")
    @FormUrlEncoded
    Call<DealsData> getAllDealsDetailsFromServer(@Field("type") String type, @Field("offertypeid") String categoyId, @Field("branchid") String branchid, @Field("sortingtype") String sorttype);


    @POST("ViewSingleOfferData")
    @FormUrlEncoded
    Call<SingleOfferData> getSingleDealsDetailsFromServer(@Field("type") String type, @Field("offerid") String categoyId);


    @POST("ViewReferalData")
    @FormUrlEncoded
    Call<ReferralData> getAllReferralsDetailsFromServer(@Field("type") String type, @Field("userid") String userid);


    @POST("InsertReview")
    @FormUrlEncoded
    Call<ReviewReponse> insertReviewDetailsFromServer(@Field("type") String type, @Field("userid") String userid, @Field("description") String description, @Field("rate") String rate, @Field("offerid") String offerid);


    @POST("ViewOfferHistoryData")
    @FormUrlEncoded
    Call<OffersHistoryData> getOfferHistoryDetailsFromServer(@Field("type") String type, @Field("userid") String userid);


    @POST("UpdateUserAvatar")
    @FormUrlEncoded
    Call<SingleUserInfo> sendUserAvaterToServer(@Field("type") String type, @Field("userid") String userid, @Field("imagecode") String imagecode);


    @POST("InsertFCMToken")
    @FormUrlEncoded
    Call<FCMReponse> sendFCMTokenToServer(@Field("type") String type, @Field("userid") String userid, @Field("fcmtoken") String imagecode, @Field("devicetype") String deviceType);


    @POST("ViewBanner")
    @FormUrlEncoded
    Call<SlidersData> getSlidersDataFromServer(@Field("type") String type, @Field("bannertype") String bannerType, @Field("categoryid") String categoryId, @Field("vendorid") String vendorId);


    @POST("InsertFreeCoupon")
    @FormUrlEncoded
    Call<SlidersData> insertFreeCouponDataToServer(@Field("type") String type, @Field("userid") String userID, @Field("couponid") String couponId, @Field("referalid") String referalId);


    @POST("ViewFreeOffersData")
    @FormUrlEncoded
    Call<FreeOffersData> getAllFreeOffersData(@Field("type") String type, @Field("userid") String userId);

    @POST("TransferFreeCoupon")
    @FormUrlEncoded
    Call<InsertFreeCoupon> giftOrTransferCouponToOtherUser(@Field("type") String type, @Field("mobile") String mobileNo, @Field("couponid") String couponId, @Field("userid") String userId);


    @POST("ViewReviewData")
    @FormUrlEncoded
    Call<ReviewData> getAllReviewDetailsByOfferId(@Field("type") String type, @Field("offerid") String offerid);


}
