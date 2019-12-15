package com.hateos.demo.model;

import org.springframework.hateoas.ResourceSupport;

public class Vehicle extends ResourceSupport {
    private long vehicleId;
    private String mark;
    private String model;
    private Color color;

    public Vehicle() {
    }

    public Vehicle(long vehicleId, String mark, String model, Color color) {
        this.vehicleId = vehicleId;
        this.mark = mark;
        this.model = model;
        this.color = color;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
