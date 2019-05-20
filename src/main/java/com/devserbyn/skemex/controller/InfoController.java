package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.service.InfoService;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/info")
public class InfoController {

    private final InfoService infoService;

    @GetMapping("/open/infoDocURL")
    public Response<String> findInfoDocumentURL(){
        String result = infoService.getInfoDocumentURL();
        return Response.success(result);
    }

    @GetMapping("/open/infoVideoId")
    public Response<String> findInfoVideoId(){
        String result = infoService.getInfoVideoId();
        return Response.success(result);
    }

    @GetMapping("/open/infoDoc")
    public ResponseEntity<byte[]> getPDF() throws IOException {
        File resultFile = new ClassPathResource("Conflict of Interest Policy.pdf").getFile();
        String resultFileName = resultFile.getName();

        if (!resultFile.exists()) {
            throw new RuntimeException("Info file doesn't exist");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=" + resultFileName);
        headers.setContentDispositionFormData(resultFileName, resultFileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        byte[] resultFileBytes = Files.readAllBytes(resultFile.toPath());

        return new ResponseEntity<>(resultFileBytes, headers, HttpStatus.OK);
    }
}
