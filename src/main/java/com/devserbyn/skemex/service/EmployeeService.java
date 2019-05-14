package com.devserbyn.skemex.service;


import com.devserbyn.skemex.controller.dto.EmployeeDTO;
import com.devserbyn.skemex.controller.search.EmployeeSearch;
import com.devserbyn.skemex.entity.Employee;

import java.util.Optional;
import java.util.List;

public interface EmployeeService {

    Optional<EmployeeDTO> findDTOById(String nickname);

    boolean existsById(String nickname);

    void deactivation(String nickname);

    List<EmployeeDTO> findByOrganizationId(Long id);

    List<EmployeeDTO> search(EmployeeSearch employeeSearch);

    Employee findEntityById(String nickname);

    EmployeeDTO save(EmployeeDTO employee);

    Employee save(Employee employee);

    List<Employee> findAllAdminsByOffice(long organizationId);
}
