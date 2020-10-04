package com.example.myapplication;

public class Hotel {

    private Integer id;
    private String htlName;
    private Integer roomCount;
    private Integer phone;
    private String email;
    private String roomtype;
    private String des;
    private String loca;

    public Hotel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHtlName() {
        return htlName;
    }

    public void setHtlName(String htlName) {
        this.htlName = htlName;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
    public String getLoca() {
        return loca;
    }

    public void setLoca(String loca) {
        this.loca = loca;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }
}
