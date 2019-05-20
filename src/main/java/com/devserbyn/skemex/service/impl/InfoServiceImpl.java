package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.InfoService;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PropertySources({
        @PropertySource("classpath:info.properties")
})
public class InfoServiceImpl implements InfoService
{

    private final Environment env;

    @Override
    public String getInfoDocumentURL() {
        return env.getProperty("info.doc.infoDocumentURL");
    }

    @Override
    public String getInfoVideoId() {
        return env.getProperty("info.doc.infoVideoId");
    }
}
