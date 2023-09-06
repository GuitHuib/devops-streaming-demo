package com.example.scrapedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(path = "/")
public class ScraperController {

    @Autowired
    ScraperServiceImpl scraperService;

    @GetMapping(path = "/{vehicleModel}")
    public Set<ResponseDTO> getVehicleByModel(@PathVariable String vehicleModel) {
        return  scraperService.getVehicleByModel(vehicleModel);
    }

    @GetMapping(path = "/timesheet")
    public void getVehicleByModel() throws IOException {
        scraperService.getTimesheets();
    }

    @GetMapping(path = "/selenium")
    public void getTimesheetWithSelenium() throws IOException {
        scraperService.getTimesheetWithSelenium();
    }

    @GetMapping(path = "/satori")
    public void getSatori() throws IOException {
        scraperService.getSatori();
    }
}