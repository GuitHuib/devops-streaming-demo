package com.example.scrapedemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


    @Scheduled(fixedRateString = "${interval.seconds}")
    public void refreshProperties() {
        RestTemplate template = new RestTemplate();
        System.out.println("***var_1 from system = " + System.getenv("var_1"));
        System.out.println("***var_1 from env = " + env.getProperty("var_1"));
        System.out.println("***var_2 from system =" + System.getenv("var_2"));
        System.out.println("***var_2 from env =" + env.getProperty("var_2"));
        String url = "https://webhook.site/67a4c4d4-e269-4e6a-a163-97a26309b33c";
        HttpEntity<String> http = new HttpEntity<>(env.getProperty("var_1"), null);

        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, http, String.class);
    }
}