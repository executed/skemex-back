package com.devserbyn.skemex.migration.entities;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelWorkspace {
    private Integer number;
    private ExcelEmployee employee;

    @Override
    public String toString() {
        return "ExcelWorkspace{" +
                "number=" + number +
                ", employee=" + employee +
                '}';
    }
}
