package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.service.InfoService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info")
public class InfoController {

    private final InfoService infoService;

    @GetMapping("/infoDocURL")
    public Response<String> findInfoDocumentURL(){
        String result = infoService.getInfoDocumentURL();
        return Response.success(result);
    }

    @GetMapping("/infoDoc")
    public ResponseEntity<byte[]> getPDF() {
        byte[] contents = new byte[0];
        try {
            contents = Files.readAllBytes(Paths.get("/Conflict of Interest Policy.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "Conflict of Interest Policy.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);

        return response;
    }
}
