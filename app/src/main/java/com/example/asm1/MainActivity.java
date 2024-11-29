package com.example.asm1;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm1.Adapter.CarAdapter;
import com.example.asm1.Adapter.CarFilterAdapter;
import com.example.asm1.Model.CarModel;
import com.example.asm1.Server.APIService;
import com.example.asm1.Server.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView lvMain;
    private List<CarModel> listCarModel;
    private List<CarModel> filterCarList;
    private CarAdapter carAdapter;
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private CarFilterAdapter carFilterAdapter;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(APIService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    APIService apiService= retrofit.create(APIService.class);
    Call<List<CarModel>> call= apiService.getCars();

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
        recyclerView = findViewById(R.id.recyclerView);

        String packageName = getApplicationContext().getPackageName();
        Log.d("PackageName", packageName);



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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listCarModel = new ArrayList<>();
        filterCarList= new ArrayList<>(listCarModel);

        carFilterAdapter= new CarFilterAdapter(listCarModel, filterCarList);
        recyclerView.setAdapter(carFilterAdapter);
        fetchCarsFromApi();


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



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
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

    private void fetchCarsFromApi() {
        apiService= RetrofitClient.getRetrofitInstance().create(APIService.class);

        Call<List<CarModel>> call1= apiService.getCars();
        call1.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listCarModel.addAll(response.body()); // Thêm dữ liệu vào carList
                    carAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                } else {
                    Log.e("API Error", "Response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                    Log.e("API Error", "Error fetching data", t);
            }
        });
    }

    private void showDialogAdd(){
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater= getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_add, null);
        builder.setView(view);


        AlertDialog alertDialog= builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();


        EditText edttenxe= view.findViewById(R.id.edttenxe);
        EditText edtnamsx= view.findViewById(R.id.edtnamsx);
        EditText edthang= view.findViewById(R.id.edthang);
        EditText edtgia= view.findViewById(R.id.edtgia);
        Button btnthem= view.findViewById(R.id.btnthem);
        Button btnhuy= view.findViewById(R.id.btnhuy);

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten= edttenxe.getText().toString();
                String namsx= edtnamsx.getText().toString();
                String hang= edthang.getText().toString();
                String gia= edtgia.getText().toString();
                CarModel xeMoi= new CarModel(null, ten, Integer.parseInt(namsx), hang, Double.parseDouble(gia));

                Call<List<CarModel>> callAddXe= apiService.addXe(xeMoi);

                callAddXe.enqueue(new Callback<List<CarModel>>() {
                    @Override
                    public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
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
    }
}