package com.devserbyn.skemex.migration.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ExcelOffice {

    private final String city;
    private final List<ExcelRoom> rooms = new ArrayList<>();

    public ExcelOffice(String city) {
        this.city = city;
    }

    public void addRoom(ExcelRoom room) {
        this.rooms.add(room);
    }

    @Override
    public String toString() {
        return "ExcelOffice{" +
                "city='" + city + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
