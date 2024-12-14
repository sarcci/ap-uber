package com.example.uber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HAdapter extends RecyclerView.Adapter<HAdapter.MyViewHolder>  {

    private final HRecyclerViewInterface hRecyclerViewInterface;

    Context context;
    ArrayList<HModel> hmodels;
    public HAdapter(Context context, ArrayList<HModel> hmodels, HRecyclerViewInterface hRecyclerViewInterface) {
        this.context = context;
        this.hmodels = hmodels;
        this.hRecyclerViewInterface = hRecyclerViewInterface;
    }

    @NonNull
    @Override
    public HAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_row, parent, false);
        return new HAdapter.MyViewHolder(view, hRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HAdapter.MyViewHolder holder, int position) {
        holder.name.setText(hmodels.get(position).getName());
        holder.datum.setText(hmodels.get(position).getDatum());
    }

    @Override
    public int getItemCount() {
        return hmodels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, datum;

        public MyViewHolder(@NonNull View itemView, HRecyclerViewInterface hRecyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.ime);
            datum = itemView.findViewById(R.id.datum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            hRecyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}