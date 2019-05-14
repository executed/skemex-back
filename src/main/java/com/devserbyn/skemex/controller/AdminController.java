package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.exception.BadRequestException;
import com.devserbyn.skemex.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Transactional
    @PostMapping("/upload")
    public boolean submit(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            throw new BadRequestException("File was not sent");
        }
        boolean result = adminService.handleExcelFile(file);
        HttpStatus status = result ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return result;
    }
}
