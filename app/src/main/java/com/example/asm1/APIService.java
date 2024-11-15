package com.example.asm1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    String DOMAIN= "http://192.168.0.10:3000/";

    @GET("/api/list")
    Call<List<CarModel>> getCars();

}
