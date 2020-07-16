package com.example.kalendorius.getersSeters;

public class Employees {


    String NAME;
    String SURNAME;
    String DOCTOR;
    String EMAIL;
    String PASSWORD;
    String BIRTHDAY;
    int ID;

    public Employees() {
        this.ID = ID;
        this.NAME = NAME;
        this.SURNAME = SURNAME;
        this.DOCTOR = DOCTOR;
        this.EMAIL = EMAIL;
        this.PASSWORD = PASSWORD;
        this.BIRTHDAY = BIRTHDAY;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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

    public String getDOCTOR() {
        return DOCTOR;
    }

    public void setDOCTOR(String DOCTOR) {
        this.DOCTOR = DOCTOR;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getBIRTHDAY() {
        return BIRTHDAY;
    }

    public void setBIRTHDAY(String BIRTHDAY) {
        this.BIRTHDAY = BIRTHDAY;
    }
}