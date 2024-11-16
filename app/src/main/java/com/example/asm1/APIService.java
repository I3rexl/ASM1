package com.example.asm1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    String DOMAIN= "http://10.24.60.19:3000/";

    @GET("/api/list")
    Call<List<CarModel>> getCars();

    @POST("/api/add_xe")
    Call<List<CarModel>> addXe(@Body CarModel xe);

    @DELETE("/api/xoa_xe/{id}")
    Call<List<CarModel>> xoaXe(@Path("id") String id); //restful api

    @PUT("/api/update_xe")
    Call<List<CarModel>> updateXe(@Body CarModel car);

}
