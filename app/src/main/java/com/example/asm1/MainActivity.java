package com.example.asm1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView lvMain;
    List<CarModel> listCarModel;
    CarAdapter carAdapter;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvMain= findViewById(R.id.lvMain);
        btnAdd= findViewById(R.id.btnAdd);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService= retrofit.create(APIService.class);
        Call<List<CarModel>> call= apiService.getCars();



        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if(response.isSuccessful()){
                    listCarModel = response.body();
                    if (listCarModel == null || listCarModel.isEmpty()) {
                        Log.e("Error", "Danh sách xe null hoặc trống");
                        return;
                    }
                    carAdapter = new CarAdapter(getApplicationContext(), listCarModel);
                    lvMain.setAdapter(carAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarModel xeMoi= new CarModel(null, "Xe moi 2", 2020, "Huyndai", 1000);

                Call<List<CarModel>> callAddXe= apiService.addXe(xeMoi);

                callAddXe.enqueue(new Callback<List<CarModel>>() {
                    @Override
                    public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                        if(response.isSuccessful()){
                            listCarModel.clear();
                            listCarModel.addAll(response.body());
                            carAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CarModel>> call, Throwable t) {

                    }
                });
            }
        });







        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CarModel xeCanXoa= listCarModel.get(i);

                Call<List<CarModel>> callXoaXe= apiService.xoaXe(xeCanXoa.get_id());

                Log.d("Debug", "ID cần xóa: " + xeCanXoa.get_id());


                callXoaXe.enqueue(new Callback<List<CarModel>>() {
                    @Override
                    public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                        if (response.isSuccessful()){
                            listCarModel.clear();
                            listCarModel.addAll(response.body());
                            carAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CarModel>> call, Throwable t) {

                    }
                });


                return true;
            }
        });



    }
}