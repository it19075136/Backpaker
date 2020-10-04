package com.example.myapplication;

public class Booking {
    private Integer gusId;
    private String  gusName;
    private Integer  gusContact;
    private String  gusEmail;
    private String reqRoomType;
    private Integer roomCou;
    private Integer gusNumCou;
    private Integer nightCou;

    public Booking() {
    }

    public Integer getGusId() {
        return gusId;
    }

    public void setGusId(Integer gusId) {
        this.gusId = gusId;
    }

    public String getGusName() {
        return gusName;
    }

    public void setGusName(String gusName) {
        this.gusName = gusName;
    }

    public Integer getGusContact() {
        return gusContact;
    }

    public void setGusContact(Integer gusContact) {
        this.gusContact = gusContact;
    }

    public String getGusEmail() {
        return gusEmail;
    }

    public void setGusEmail(String gusEmail) {
        this.gusEmail = gusEmail;
    }

    public String getReqRoomType() {
        return reqRoomType;
    }

    public void setReqRoomType(String reqRoomType) {
        this.reqRoomType = reqRoomType;
    }

    public Integer getRoomCou() {
        return roomCou;
    }

    public void setRoomCou(Integer roomCou) {
        this.roomCou = roomCou;
    }

    public Integer getGusNumCou() {
        return gusNumCou;
    }

    public void setGusNumCou(Integer gusNumCou) {
        this.gusNumCou = gusNumCou;
    }

    public Integer getNightCou() {
        return nightCou;
    }

    public void setNightCou(Integer nightCou) {
        this.nightCou = nightCou;
    }
}
