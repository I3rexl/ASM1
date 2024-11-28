package com.example.asm1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm1.Model.CarModel;
import com.example.asm1.R;

import java.util.List;

public class CarFilterAdapter extends RecyclerView.Adapter<CarFilterAdapter.CarViewHolder>{
    private List<CarModel> carList;
    private List<CarModel> carListFull;

    public CarFilterAdapter(List<CarModel> carList, List<CarModel> carListFull) {
        this.carList = carList;
        this.carListFull = carListFull;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);

        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarModel carModel= carList.get(position);
        holder.tvTen.setText(carModel.getTen());
        holder.tvHang.setText(carModel.getHang());
        holder.tvGia.setText(String.valueOf(carModel.getGia()));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public void filter(String query){
        carList.clear();
        if (query.isEmpty()){
            carList.addAll(carListFull);
        }else {
            String filterPattern= query.toLowerCase().trim();
            for (CarModel carModel: carListFull){
                if (carModel.getTen().toLowerCase().contains(filterPattern)){
                    carList.add(carModel);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder{
        TextView tvTen, tvHang, tvGia;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen= itemView.findViewById(R.id.tvName);
            tvHang= itemView.findViewById(R.id.tvHang);
            tvGia= itemView.findViewById(R.id.tvGia);
        }
    }
}
