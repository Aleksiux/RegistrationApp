package com.example.kalendorius;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kalendorius.getersSeters.Clients;

import java.util.ArrayList;

public class ClientRecyclerAdaptor extends RecyclerView.Adapter<ClientRecyclerAdaptor.MyViewHolder> {


    Context context;
    ArrayList<Clients> arrayList;

    public ClientRecyclerAdaptor(Context context, ArrayList<Clients> clientsArrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ClientRecyclerAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout, parent, false);
        return new ClientRecyclerAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientRecyclerAdaptor.MyViewHolder holder, int position) {
        Clients clients = arrayList.get(position);
        holder.Name.setText(clients.getNAME());
        holder.Surname.setText(clients.getSURNAME());
        holder.Phone.setText(clients.getPHONE());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Surname, Phone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.cName);
            Surname = itemView.findViewById(R.id.cSurname);
            Phone = itemView.findViewById(R.id.cPhone);
        }
    }
}


