package com.sonusourav.merazoverflow.app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
  @GET("/2.2/search/advanced?pagesize=30&order=desc&sort=relevance&site=stackoverflow")
  Call<ApiResponse> getQuestions(@Query("page") int pageNo);
}