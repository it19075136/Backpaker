package com.example.myapplication;

public class campingGear {

    private String locationName;
    private String email;
    private String lamps;
    private String cupsPlates;
    private String malletHammer;
    private String tables;
    private String gas;
    private String date;
    private String tentSize;
    private String tentNumber;
    private String campId;

    public campingGear() {
    }

    public campingGear(String campId,String locationName, String email, String lamps, String cupsPlates, String malletHammer, String tables, String gas, String date, String tentSize, String tentNumber) {

        this.campId = campId;
        this.locationName = locationName;
        this.email = email;
        this.lamps = lamps;
        this.cupsPlates = cupsPlates;
        this.malletHammer = malletHammer;
        this.tables = tables;
        this.gas = gas;
        this.date = date;
        this.tentSize = tentSize;
        this.tentNumber = tentNumber;
    }


    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getTentSize() {
        return tentSize;
    }

    public void setTentSize(String tentSize) {
        this.tentSize = tentSize;
    }

    public String getTentNumber() {
        return tentNumber;
    }

    public void setTentNumber(String tentNumber) {
        this.tentNumber = tentNumber;
    }



    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLamps() {
        return lamps;
    }

    public void setLamps(String lamps) {
        this.lamps = lamps;
    }

    public String getCupsPlates() {
        return cupsPlates;
    }

    public void setCupsPlates(String cupsPlates) {
        this.cupsPlates = cupsPlates;
    }

    public String getMalletHammer() {
        return malletHammer;
    }

    public void setMalletHammer(String malletHammer) {
        this.malletHammer = malletHammer;
    }

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
