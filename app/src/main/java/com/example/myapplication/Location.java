package com.example.myapplication;

public class Location {
    public String locationName;
    private String route;
    private String note;
    private String accommondation;
    private String NOofDays;
    private String roadCondition;
    private String Weather;
    private String permission;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }




    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAccommondation() {
        return accommondation;
    }

    public void setAccommondation(String accommondation) {
        this.accommondation = accommondation;
    }

    public String getNOofDays() {
        return NOofDays;
    }

    public void setNOofDays(String NOofDays) {
        this.NOofDays = NOofDays;
    }

    public String getRoadCondition() {
        return roadCondition;
    }

    public void setRoadCondition(String roadCondition) {
        this.roadCondition = roadCondition;
    }

    public String getWeather() {
        return Weather;
    }

    public void setWeather(String weather) {
        Weather = weather;
    }

    public Location() {
    }
}
