package com.example.uber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder>  {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Model> models;
    public myAdapter(Context context, ArrayList<Model> models, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.models = models;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public myAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new myAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(models.get(position).getName());
        holder.tvVehicle.setText(models.get(position).getVehicle());
        holder.tvRating.setText(String.valueOf(models.get(position).getRating()));
        holder.tvPrice.setText(String.valueOf(models.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvVehicle, tvRating, tvPrice;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tvName=itemView.findViewById(R.id.ime);
            tvVehicle=itemView.findViewById(R.id.vozilo);
            tvRating=itemView.findViewById(R.id.rejting);
            tvPrice=itemView.findViewById(R.id.cena);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}