package com.example.asm1.Server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.0.10:3000/"; // Thay <server_url> bằng địa chỉ server của bạn

    // Phương thức khởi tạo Retrofit singleton
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Đặt URL cơ sở của API
                    .addConverterFactory(GsonConverterFactory.create()) // Sử dụng GSON để chuyển đổi JSON
                    .build();
        }
        return retrofit;
    }
}

