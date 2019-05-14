package com.devserbyn.skemex.migration.entities;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelOrganization {
    private String name;

    @Override
    public String toString() {
        return "ExcelOrganization{" +
                "name='" + name + '\'' +
                '}';
    }
}
