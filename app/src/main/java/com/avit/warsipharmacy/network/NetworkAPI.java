package com.avit.warsipharmacy.network;

import com.avit.warsipharmacy.auth.OtpActivity;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.category.CategoryAdapter;
import com.avit.warsipharmacy.ui.category.CategoryItem;
import com.avit.warsipharmacy.ui.checkout.UpiDetails;
import com.avit.warsipharmacy.ui.orders.OrderItem;
import com.avit.warsipharmacy.ui.prescription.PrescriptionFragment;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkAPI {
     String BASE_URL = "http://192.168.43.85:5500/";

     @GET("/categories")
     Call<List<String>> getAllCategories();

     @GET("/categories/topSelling")
     Call<List<CategoryItem>> getTopSelling();

     @GET("/categories/category/{categoryName}")
     Call<List<CategoryItem>> getCategoryItems(@Path(value = "categoryName") String categoryName, @Query("limit") int limit,@Query("page") int page);

     @GET("/categories/all")
     Call<List<CategoryItem>> searchCategoryItem(@Query("page") int page,@Query("limit") int limit,@Query("category") String category,@Query("query") String query);

     @POST("/order/newOrder")
     Call<Boolean> createOrder(@Body OrderItem.CreateOrderData createOrderData);

     @GET("/order/deliveryPrice")
     Call<Integer> getDeliveryPrice();

     @GET("/order/upiDetails")
     Call<UpiDetails> getUpiDetails();

     @GET("/order/user/{customerId}")
     Call<List<OrderItem>> getOrderItems(@Path(value = "customerId") String customerID);

     @POST("/auth/requestOtp/{phoneNo}")
     Call<String> requestOTP(@Path(value = "phoneNo") String phoneNo);

     @POST("/auth/verifyOtp")
     Call<Boolean> verifyOTP(@Body OtpActivity.OtpVerifyData otpVerifyData);

     @Multipart
     @POST("/prescription/upload")
     Call<Boolean> sendPrescription(@Part MultipartBody.Part prescriptionImage, @Part("data") PrescriptionFragment.PrescriptionUploadData data);

}
