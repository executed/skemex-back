package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.service.InfoService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
