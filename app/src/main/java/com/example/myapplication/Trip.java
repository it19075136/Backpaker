package com.example.myapplication;

public class Trip {
    String Location, Destination, VehicleType, FuelType, Drivetrain, travelTime, Uid;
    Integer Id;
    Double distance,FuelCost;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public Double getFuelCost() {
        return FuelCost;
    }

    public void setFuelCost(Double fuelCost) {
        FuelCost = fuelCost;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Trip() {
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String fuelType) {
        FuelType = fuelType;
    }

    public String getDrivetrain() {
        return Drivetrain;
    }

    public void setDrivetrain(String drivetrain) {
        Drivetrain = drivetrain;
    }

}
