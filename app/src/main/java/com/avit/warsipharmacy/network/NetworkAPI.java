package com.avit.warsipharmacy.network;

import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
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


}
