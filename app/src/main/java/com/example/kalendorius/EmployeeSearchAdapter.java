package com.example.kalendorius;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kalendorius.databaseStructure.DBemployee;
import com.example.kalendorius.getersSeters.Employees;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSearchAdapter extends ArrayAdapter<Employees> {


    private Context context;
    private int Limit = 5;
    private List<Employees> employees;

    public EmployeeSearchAdapter(Context context, List<Employees> employees) {
        super(context, R.layout.employee_search_layout, employees);
        this.context = context;
        this.employees = employees;
    }

    @Override
    public int getCount() {
        return Math.min(Limit, employees.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_search_layout, null);
        Employees employee = employees.get(position);
        TextView searchTextViewDoctor = view.findViewById(R.id.searchDoctor);
        searchTextViewDoctor.setText(employee.getDOCTOR());
        TextView searchTextViewDoctorName = view.findViewById(R.id.searchEmployeeName);
        searchTextViewDoctorName.setText(employee.getNAME());
        TextView searchTextViewDoctorSurname = view.findViewById(R.id.searchEmployeeSurname);
        searchTextViewDoctorSurname.setText(employee.getDOCTOR());
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new EmployeeFilter(this, context);
    }

    private class EmployeeFilter extends Filter {

        private EmployeeSearchAdapter employeeSearchAdapter;
        private Context context;

        public EmployeeFilter(EmployeeSearchAdapter employeeSearchAdapter, Context context) {
            super();
            this.employeeSearchAdapter = employeeSearchAdapter;
            this.context = context;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            employeeSearchAdapter.employees.clear();
            FilterResults filterResults = new FilterResults();
            if(charSequence == null || charSequence.length() == 0){
                filterResults.values = new ArrayList<Employees>();
                filterResults.count = 0;
            }
            else {
                DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
                List<Employees> employees = dbOpenHelper.search(charSequence.toString());
                filterResults.values = employees;
                filterResults.count = employees.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            employeeSearchAdapter.employees.clear();
            employeeSearchAdapter.employees.addAll((List<Employees>)filterResults.values);
            employeeSearchAdapter.notifyDataSetChanged();

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Employees employees =  (Employees) resultValue;
            return employees.getDOCTOR();
        }
    }
}
