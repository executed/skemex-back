package com.devserbyn.skemex.migration.entities;

import com.devserbyn.skemex.entity.Employee;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelEmployee {
    private String firstName;
    private String lastName;
    private ExcelOrganization organization;

    @Override
    public String toString() {
        return "ExcelEmployee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", organization=" + organization +
                '}';
    }

    public boolean matchNames(Employee currentEmployee) {
        return Objects.equals(currentEmployee.getFirstName(), this.getFirstName())
                && Objects.equals(currentEmployee.getLastName(), this.getLastName());
    }
}
