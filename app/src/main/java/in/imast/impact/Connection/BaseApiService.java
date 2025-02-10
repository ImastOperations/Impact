/**/
package in.imast.impact.Connection;


import com.google.gson.JsonObject;

import in.imast.impact.model.CustomerModel;
import in.imast.impact.model.LoginResponse;

import java.util.Map;

import in.imast.impact.model.MasterProductReqModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


/**
 * Created by Vishwnath 01_March_2021
 */

public interface BaseApiService {

    @FormUrlEncoded
    @POST("login-user")
    Call<JsonObject> loginvalidation(@FieldMap Map<String, String> queryParams);

    @FormUrlEncoded
    @POST("login-user-verify")
    Call<LoginResponse> retailerLogin(@FieldMap Map<String, String> queryParams);

    @GET("v1/customers/{id}")
    Call<JsonObject> getCustomer(@Header("Authorization") String authorization, @Path("id") String id);

    @FormUrlEncoded
    @POST("v1/coupon-entries")
    Call<JsonObject> schemeredeemed1(@Header("Authorization") String authorization, @FieldMap Map<String, String> queryParams);

    @FormUrlEncoded
    @POST("v1/user-logout")
    Call<JsonObject> logout(@Header("Authorization") String authorization, @FieldMap Map<String, String> queryParams);

    @FormUrlEncoded
    @POST("v1/get-app-version")
    Call<JsonObject> checkVersion(@FieldMap Map<String, String> queryParams);

    @GET("v1/get-customer-type")
    Call<CustomerModel> getCustomerList(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParams);

    @GET("v1/master-product-details")
    Call<JsonObject> getProducts(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParams);

    @GET("v1/master-product-filter")
    Call<JsonObject> getCategory(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParams);

    @GET("v1/master-product-group")
    Call<JsonObject> getGroup(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParams);

    @GET("v1/master-product-group-tag")
    Call<JsonObject> getThickness(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParams);
    @Multipart
    @POST("v1/site-details/media")
    Call<JsonObject> siteMedia(@Header("Authorization") String authorization, @Part MultipartBody.Part multipartTypedOutput);

    @Multipart
    @POST("v2/native/invoice-create")
    Call<JsonObject> add_invoice(@Header("Authorization") String authorization,
                                  @Part("distributor_id") RequestBody distributor_id,
                                  @Part("asm_id") RequestBody asm_id,
                                  @Part("invoice_number") RequestBody invoice_number,
                                  @Part("invoice_date") RequestBody invoice_date,
                                  @Part MultipartBody.Part attachment);


    @GET("v2/native/asm-list")
    Call<JsonObject> get_area_sales(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParams);

    @GET("v2/native/distributors-list")
    Call<JsonObject> get_distributer(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParams);

    @Headers("Content-Type: application/json")
    @POST("v1/save-invoice")
    Call<JsonObject> saveInvoice(@Header("Authorization") String authorization, @Body MasterProductReqModel customerRedeemRequestModel);

    @GET("v2/native/get-notification")
    Call<JsonObject> getNotification(@Header("Authorization") String authorization);

    //check kyc
    @GET("v2/native/manage-kyc-modal")
    Call<JsonObject> check_kyc(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParams);

    @FormUrlEncoded
    @POST("v2/native/submit-gstin")
    Call<JsonObject> submitGstin(@Header("Authorization") String authorization, @FieldMap Map<String, String> queryParams);

    @FormUrlEncoded
    @POST("v2/native/send-aadhar-otp")
    Call<JsonObject> sendAadharOTP(@Header("Authorization") String authorization, @FieldMap Map<String, String> queryParams);

    @FormUrlEncoded
    @POST("v2/native/verify-aadhar-otp")
    Call<JsonObject> verifyAadharOtp(@Header("Authorization") String authorization, @FieldMap Map<String, String> queryParams);


    @GET("v2/native/get-pan-gstin-kyc")
    Call<JsonObject> gstin_kyc(@Header("Authorization") String authorization, @QueryMap Map<String, String> queryParams);





}
