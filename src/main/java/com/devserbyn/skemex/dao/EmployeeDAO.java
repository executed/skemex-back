package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.controller.search.EmployeeSearch;
import com.devserbyn.skemex.entity.Employee;

import java.util.List;

public interface EmployeeDAO extends IBaseDAO<Employee, String> {
    List<Employee> findByOrganizationId(Long id);

    List<Employee> search(EmployeeSearch employeeSearch);

    List<Employee> findAllAdminsByOffice(long organizationId);
}
