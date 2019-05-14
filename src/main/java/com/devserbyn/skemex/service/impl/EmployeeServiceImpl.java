package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.mapping.EmployeeMapper;
import com.devserbyn.skemex.service.UserService;
import com.devserbyn.skemex.controller.dto.EmployeeDTO;
import com.devserbyn.skemex.controller.search.EmployeeSearch;
import com.devserbyn.skemex.dao.EmployeeDAO;
import com.devserbyn.skemex.dao.OrganizationDAO;
import com.devserbyn.skemex.entity.Employee;
import com.devserbyn.skemex.exception.BadRequestException;
import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final EmployeeMapper mapper;
    private final OrganizationDAO organizationDAO;
    private final UserService userService;

    @Autowired
    public EmployeeServiceImpl(EmployeeDAO employeeDAO, EmployeeMapper mapper, OrganizationDAO organizationDAO, UserService userService) {
        this.employeeDAO = employeeDAO;
        this.mapper = mapper;
        this.organizationDAO = organizationDAO;
        this.userService = userService;
    }

    @Override
    public Optional<EmployeeDTO> findDTOById(String nickname) {
        return employeeDAO.findById(nickname).map(mapper::toDTO);
    }

    @Override
    public boolean existsById(String nickname) {
        if (nickname == null)
            return false;
        return employeeDAO.existsById(nickname);
    }

    @Override
    public void deactivation(String nickname) {
        Employee employee = employeeDAO.findById(nickname).orElseThrow((() -> new NotFoundException(String.format("Employee '%s' doesn't exist", nickname))));
        employee.setActive(false);
        employeeDAO.save(employee);
    }

    @Override
    public Employee findEntityById(String nickname) {
        return employeeDAO.findById(nickname).orElseThrow((() -> new NotFoundException(String.format("Employee '%s' doesn't exist", nickname))));
    }

    @Override
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee employee = Employee.builder().firstName(employeeDTO.getFirstName()).lastName(employeeDTO.getLastName())
                .active(true)
                .nickname(employeeDTO.getFirstName() + employeeDTO.getLastName())
                .organization(organizationDAO.findByName(employeeDTO.getOrganizationName()).orElseThrow(() -> new BadRequestException("-")))
                .build();
        Employee employeeNew = employeeDAO.save(employee);
        userService.createByEmployee(employeeNew);
        return mapper.toDTO(employeeNew);
    }

    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    @Override
    public List<Employee> findAllAdminsByOffice(long organizationId) {
        return employeeDAO.findAllAdminsByOffice(organizationId);
    }

    @Override
    public List<EmployeeDTO> findByOrganizationId(Long id) {
        return mapper.toDTO(employeeDAO.findByOrganizationId(id));
    }

    @Override
    public List<EmployeeDTO> search(EmployeeSearch employeeSearch) {
        return mapper.toDTO(employeeDAO.search(employeeSearch));
    }

}
