package com.devserbyn.skemex.migration.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ExcelRoom {
    private final String title;
    private final List<ExcelWorkspace> workspaces = new ArrayList<>();

    public ExcelRoom(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ExcelRoom{" +
                "title='" + title + '\'' +
                ", workspaces=" + workspaces +
                '}';
    }
}
