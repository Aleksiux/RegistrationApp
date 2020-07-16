package com.example.kalendorius;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kalendorius.databaseStructure.ClientStructure;
import com.example.kalendorius.databaseStructure.DBStructure;
import com.example.kalendorius.getersSeters.Clients;
import com.example.kalendorius.getersSeters.Employees;
import com.example.kalendorius.getersSeters.Events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendar extends LinearLayout {
    ImageButton PirmynButton, AtgalButton;
    TextView DabartineData;
    GridView gridView;
    private static final String TAG = "CustomCalender";
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.getDefault());
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Clients> clients = new ArrayList<>();

    DBOpenHelper dbOpenHelper;


    public CustomCalendar(Context context) {
        super(context);
    }

    public CustomCalendar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        IntializeLayout();

        AtgalButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });
        PirmynButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(getContext()).inflate(R.layout.naujas_klientas, null);
                final EditText EventName = addView.findViewById(R.id.eventname);
                final EditText cName = addView.findViewById(R.id.vardas);
                final EditText cSurname = addView.findViewById(R.id.pavarde);
                final EditText cPhone = addView.findViewById(R.id.tel_nr);
//                final EditText eEmail = addView.findViewById(R.id.etEmail);
//                final EditText eName = addView.findViewById(R.id.Name);
//                final EditText eSurname = addView.findViewById(R.id.Surname);
//                final EditText eBirthday = addView.findViewById(R.id.BirthDay);
//                final EditText ePassword = addView.findViewById(R.id.etPassword);
//                final AutoCompleteTextView searchDoctor = addView.findViewById(R.id.searchDoctor);
                final TextView EventTime = addView.findViewById(R.id.event_time);
                ImageButton SetTime = addView.findViewById(R.id.set_event_time);
                Button AddEvent = addView.findViewById(R.id.add_event);
                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());


                                SimpleDateFormat hformate = new SimpleDateFormat("HH 'val.': mm 'min.'", Locale.getDefault());


                                //              1-12 valandų
                                //          13 - 24 valandos


//                                int hours = c.getTime().getHours();
//                                int minutes = c.getTime().getMinutes();

                                //  formatDateString(hours, minutes);

                                String event_time = hformate.format(c.getTime()); //;
                                EventTime.setText(event_time);
                            }
                        }, hours, minute, true);
                        timePickerDialog.show();
                    }
                });
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year, cName.getText().toString(),
                                cSurname.getText().toString(),
                                cPhone.getText().toString()
//                                eEmail.getText().toString(),
//                                eName.getText().toString(),
//                                eSurname.getText().toString(),
//                                eBirthday.getText().toString(),
//                                searchDoctor.getText().toString(),
                              //  ePassword.getText().toString()
                        );
                        SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String date = eventDateFormat.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRv);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);

                EventRecyclerAdaptor eventRecyclerAdaptor = new EventRecyclerAdaptor(showView.getContext(), CollectClient(date), CollectEventByDate(date));

                recyclerView.setAdapter(eventRecyclerAdaptor);
                eventRecyclerAdaptor.notifyDataSetChanged();
                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    private ArrayList<Events> CollectEventByDate(String date) {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String Date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            String clientId = cursor.getString(cursor.getColumnIndex(DBStructure.CLIENTID));
            Events events = new Events(event, time, Date, month, Year, clientId);
            arrayList.add(events);
        }
        return arrayList;
    }


    public CustomCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent(String event, String time, String date, String month, String year, String name, String surname, String phone/*, String email, String Name, String Surname, String birthday, String doctor, String password*/) {
        dbOpenHelper = new DBOpenHelper(context);

        dbOpenHelper.SaveClientAndEvent(event, time, date, month, year, name, surname, phone/*, email, Name, Surname, birthday, doctor, password*/);

        dbOpenHelper.close();
        Toast.makeText(context, "Klientas išsaugotas", Toast.LENGTH_SHORT).show();

    }

    private void IntializeLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.kalendorius, this);
        PirmynButton = view.findViewById(R.id.pirmynBtn);
        AtgalButton = view.findViewById(R.id.atgalBtn);
        DabartineData = view.findViewById(R.id.dabartineData);
        gridView = view.findViewById(R.id.grid_view);
    }

    public void SetUpCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        DabartineData.setText(currentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 2;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);
        List<Events> eventsList = CollectEventPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        //CollectClientPerMonth();

        while (dates.size() < MAX_CALENDAR_DAYS) {

            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList, clients);
        gridView.setAdapter(myGridAdapter);
    }


    private List<Events> CollectEventPerMonth(String Month, String Year) {
        List<Events> eventsList = new ArrayList<>();

        dbOpenHelper = new DBOpenHelper(context);

        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month, Year);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            String clientId = cursor.getString(cursor.getColumnIndex(DBStructure.CLIENTID));
            Events events = new Events(event, time, date, month, year, clientId);
            eventsList.add(events);
        }
        return eventsList;
    }

    private ArrayList<Clients> CollectClient(String date) {
        ArrayList<Clients> clientsArrayList = new ArrayList<>();

        dbOpenHelper = new DBOpenHelper(context);

        Cursor cursor = dbOpenHelper.readClient();
        while (cursor.moveToNext()) {
            String Name = cursor.getString(cursor.getColumnIndex(ClientStructure.NAME));
            String surname = cursor.getString(cursor.getColumnIndex(ClientStructure.SURNAME));
            String phone = cursor.getString(cursor.getColumnIndex(ClientStructure.PHONE));
            String id = cursor.getString(cursor.getColumnIndex(ClientStructure.ID));
            Clients clients = new Clients(Name, surname, phone, id);


            clientsArrayList.add(clients);
        }
        return clientsArrayList;
    }


//    public ArrayList<Cursor> ClientsWithEvents(String date) {
//
//        ArrayList<Cursor> clientsArrayList = new ArrayList<>();
//        Cursor readClient = dbOpenHelper.readClient();
//        clientsArrayList.add(readClient);
//
//        ArrayList<Cursor> eventsArrayList = new ArrayList<>();
//        Cursor readEvents = dbOpenHelper.ReadEvents(date);
//        eventsArrayList.add(readEvents);
//
//
//        clientsArrayList.addAll(eventsArrayList);
//        readClient.close();
//        dbOpenHelper.close();
//        return clientsArrayList;
//    }


}


