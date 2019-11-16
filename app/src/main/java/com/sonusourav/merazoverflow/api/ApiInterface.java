package com.sonusourav.merazoverflow.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
  @GET("/2.2/search/advanced?pagesize=30&order=desc&sort=relevance&site=stackoverflow")
  Call<ApiResponse> getQuestions(@Query("page") int pageNo);

  @GET("/2.2/search?pagesize=30&order=desc&sort=popular&site=stackoverflow")
  Call<ApiResponse> searchQuestions(@Query("intitle") String questionTitle);

}