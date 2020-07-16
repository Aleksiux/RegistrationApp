package com.example.kalendorius.getersSeters;

import android.app.usage.UsageEvents;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class Clients {

    String ID, NAME, SURNAME, PHONE;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSURNAME() {
        return SURNAME;
    }

    public void setSURNAME(String SURNAME) {
        this.SURNAME = SURNAME;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Clients(String NAME, String SURNAME, String PHONE, String ID) {
        this.NAME = NAME;
        this.SURNAME = SURNAME;
        this.PHONE = PHONE;
        this.ID = ID;
    }
}