package com.example.kalendorius;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kalendorius.getersSeters.Clients;
import com.example.kalendorius.getersSeters.Events;

import java.util.ArrayList;

public class EventRecyclerAdaptor extends RecyclerView.Adapter<EventRecyclerAdaptor.MyViewHolder> {

    DBOpenHelper dbOpenHelper;
    Context context;
    ArrayList<Events> events;
    ArrayList<Clients> clients;

    public EventRecyclerAdaptor(Context context, ArrayList<Clients> clients, ArrayList<Events> events) {
        this.context = context;
        this.events = events;
        this.clients = clients;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final Events event = events.get(position);
        holder.DateText.setText(event.getDATE());
        holder.Event.setText(event.getEVENT());
        holder.Time.setText(event.getTIME());

        for (final Clients c: clients) {
            if(c.getID().equals(event.getCLIENTID())){

                holder.Name.setText(c.getNAME());
                holder.Surname.setText(c.getSURNAME());
                holder.Phone.setText(c.getPHONE());

                break;
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCalendarEvent(event.getEVENT(),event.getDATE(),event.getTIME(),event.getCLIENTID(), c.getNAME(), c.getSURNAME(), c.getPHONE());
                    events.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void deleteCalendarEvent(String event, String date, String time, String clientid, String name, String surname, String phone) {
        dbOpenHelper = new DBOpenHelper(context);
        dbOpenHelper.deleteEvent(event,date,time, clientid);
        dbOpenHelper.deleteClient(name,surname, phone);
        dbOpenHelper.close();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView DateText, Event, Time, Name, Surname, Phone;
        Button delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.cName);
            Surname = itemView.findViewById(R.id.cSurname);
            Phone = itemView.findViewById(R.id.cPhone);
            DateText = itemView.findViewById(R.id.date_event);
            Event = itemView.findViewById(R.id.name_event);
            Time = itemView.findViewById(R.id.time_event);
            delete = itemView.findViewById(R.id.delete_event);
        }
    }
}
