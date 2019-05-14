package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.EmployeeService;
import com.devserbyn.skemex.controller.dto.EmployeeDTO;
import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.controller.search.EmployeeSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/{nickname}")
    @ResponseStatus(HttpStatus.OK)
    public Response<EmployeeDTO> findEmployee(@PathVariable("nickname") String nickname) {
        EmployeeDTO employee = employeeService.findDTOById(nickname)
                .orElseThrow(() -> new NotFoundException(String.format("Employee with nickname: %s not found",
                        nickname)));
        return Response.success(employee);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO){
        return Response.success(employeeService.save(employeeDTO));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{nickname}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable String nickname){
        employeeService.deactivation(nickname);
    }

    @GetMapping(value = "/isPresent/{nickname}")
    public Response<Boolean> isNicknamePresent(@PathVariable("nickname") String nickname){
        return Response.success(employeeService.existsById(nickname));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/search")
    public Response<List<EmployeeDTO>> searchEmployees(@RequestBody EmployeeSearch employeeSearch){
        return Response.success(employeeService.search(employeeSearch));
    }

    @GetMapping(value = "/byProject")
    public Response<List<EmployeeDTO>> findByOrganisation(@RequestParam("projectId") Long id) {
        return Response.success(employeeService.findByOrganizationId(id));
    }
}
