package com.example.scrapedemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static java.lang.Long.parseLong;

@Component
public class PropertyReloader {
    Date last;
    StandardEnvironment env;

    @Autowired
    public PropertyReloader(StandardEnvironment env) {
        this.env = env;
    }


    @Scheduled(fixedRate = 10000)
    public void refreshProperties() {
        RestTemplate template = new RestTemplate();
        System.out.println("***running***" + env.getProperty("message"));
        String url = "https://webhook.site/91c523d6-7c9a-4217-83b1-5fd9c1e03102";
        HttpEntity<String> http = new HttpEntity<>(env.getProperty("message"), null);

        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, http, String.class);
    }
}