package com.example.electrictime;

public class DataModel {

    String transport;
    String version;
    double speed;
    int range;
    int id_;

    public DataModel(String transport, String version, double speed, int range, int id_) {
        this.transport = transport;
        this.version = version;
        this.speed = speed;
        this.range = range;
        this.id_ = id_;
    }

    public String getTransport() {
        return transport;
    }

    public String getVersion() { return version;}

    public double getSpeed() {
        return speed;
    }

    public int getRange() {
        return range;
    }

    public int getId() {
        return id_;
    }
}