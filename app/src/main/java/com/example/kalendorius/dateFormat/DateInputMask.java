package com.example.kalendorius.dateFormat;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

public class DateInputMask implements TextWatcher {

    private String current = "";
    private String yyyymmdd = "YYYYMMDD";
    private Calendar cal = Calendar.getInstance();
    private EditText input;

    public DateInputMask(EditText input) {
        this.input = input;
        this.input.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals(current)) {
            return;
        }

        String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
        String cleanC = current.replaceAll("[^\\d.]|\\.", "");

        int cl = clean.length();
        int sel = cl;
        for (int i = 4; i <= cl && i < 8; i += 2) {
            sel++;
        }
        //Fix for pressing delete next to a forward slash
        if (clean.equals(cleanC)) sel--;


        if (clean.length() < 8) {
            clean = clean + yyyymmdd.substring(clean.length());

        } else {
            //This part makes sure that when we finish entering numbers
            //the date is correct, fixing it otherwise
            int year = Integer.parseInt(clean.substring(0, 4));
            int mon = Integer.parseInt(clean.substring(4, 6));
            int day = Integer.parseInt(clean.substring(6, 8));

            // mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;

            if (mon < 1) {
                mon = 1;
            } else if (mon > 12) {
                mon = 12;
            }

            cal.set(Calendar.MONTH, mon - 1);
            year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;

            cal.set(Calendar.YEAR, year);
            // ^ first set year for the line below to work correctly
            //with leap years - otherwise, date e.g. 29/02/2012
            //would be automatically corrected to 28/02/2012

            int maxDay = cal.getActualMaximum(Calendar.DATE);
            day = (day > maxDay) ? maxDay : day;

            clean = String.format("%04d%02d%02d", year, mon, day);
        }

        clean = putSlashesIntoDate(clean);

        sel = sel < 0 ? 0 : sel;
        current = clean;
        input.setText(current);
        input.setSelection(sel < current.length() ? sel : current.length());
    }

    private String putSlashesIntoDate(String clean) {
        clean = String.format("%s/%s/%s", clean.substring(0, 4),
                clean.substring(4, 6),
                clean.substring(6, 8));
        return clean;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}


//package com.example.kalendorius.dateFormat;
//
//import android.annotation.SuppressLint;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.EditText;
//
//import java.util.Calendar;
//
//public class DateInputMask implements TextWatcher {
//
//    private String current = "";
//    private String yyyymmdd = "YYYYMMDD";
//    private Calendar cal = Calendar.getInstance();
//    private EditText input;
//
//    public DateInputMask(EditText input) {
//        this.input = input;
//        this.input.addTextChangedListener(this);
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @SuppressLint("DefaultLocale")
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (s.toString().equals(current)) {
//            return;
//        }
//
//        String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
//        String cleanC = current.replaceAll("[^\\d.]|\\.", "");
//
//        int cl = clean.length();
//        int sel = cl;
//        for (int i = 2; i <= cl && i < 6; i += 2) {
//            sel++;
//        }
//        //Fix for pressing delete next to a forward slash
//        if (clean.equals(cleanC)) sel--;
//
//        if (clean.length() < 8){
//            clean = clean + yyyymmdd.substring(clean.length());
//        }else{
//            //This part makes sure that when we finish entering numbers
//            //the date is correct, fixing it otherwise
//            int year = Integer.parseInt(clean.substring(4,8));
//            int mon  = Integer.parseInt(clean.substring(2,4));
//            int day  = Integer.parseInt(clean.substring(0,2));
//
//            year = (year<1900)?1900: Math.min(year, 2200);
//            cal.set(Calendar.YEAR, year);
//            mon = mon < 1 ? 1 : Math.min(mon, 12);
//            cal.set(Calendar.MONTH, mon-1);
//
//            // ^ first set year for the line below to work correctly
//            //with leap years - otherwise, date e.g. 29/02/2012
//            //would be automatically corrected to 28/02/2012
//
//            day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
//            clean = String.format("%02d%02d%02d",year, mon, day);
//        }
//
//        clean = String.format("%s/%s/%s",
//                clean.substring(4, 8),
//                clean.substring(2, 4),
//                clean.substring(0, 2));
//
//        sel = Math.max(sel, 0);
//        current = clean;
//        input.setText(current);
//        input.setSelection(Math.min(sel, current.length()));
//    }
//
//    @Override
//    public void afterTextChanged(Editable editable) {
//
//    }
//}
