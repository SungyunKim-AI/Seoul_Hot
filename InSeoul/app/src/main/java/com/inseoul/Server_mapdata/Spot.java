package com.inseoul.Server_mapdata;

public class Spot {
    int IDNUM;
    String Spot_new;
    public Spot(int IDNUM , String Spot_new){
        this.IDNUM =  IDNUM;
        this.Spot_new = Spot_new;
    }

    public int getIDNUM() {
        return IDNUM;
    }

    public void setIDNUM(int IDNUM) {
        this.IDNUM = IDNUM;
    }

    public String getSpot_new() {
        return Spot_new;
    }

    public void setSpot_new(String spot_new) {
        Spot_new = spot_new;
    }


}
