package com.devserbyn.skemex.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminService {
    boolean handleExcelFile(MultipartFile file) throws IOException;
}
