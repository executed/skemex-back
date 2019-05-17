package com.devserbyn.skemex.service.schedule;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@PropertySources({
        @PropertySource ("classpath:deployment.properties"), @PropertySource("classpath:schedule.properties")
})
public class DeploymentScheduleService {

    private final Environment env;
    private final ApplicationContext context;

    @Scheduled(cron = "${job.schedule.snoozePrevention.cron}")
    public void preventSnoozing() {

        if ((boolean) context.getBean("isDevMode")) {
            return;
        }
        log.trace("Snoozing prevention job started");

        List<String> targetURLs = new ArrayList<>();
        targetURLs.add(env.getProperty("deployment.frontendBaseUrl"));
        targetURLs.add(env.getProperty("deployment.backendBaseUrl"));

        targetURLs.forEach(x -> {
            try {
                Jsoup.connect(x).ignoreContentType(true).get();
                log.trace(String.format("Snoozing prevention job for host %s succeeded", x));
            } catch (IOException e) {
                log.error("Snoozing prevention went wrong. Problems accessing target URLs", e);
            }
        });
    }
}
