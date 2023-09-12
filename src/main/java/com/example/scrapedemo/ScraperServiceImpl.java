package com.example.scrapedemo;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScraperServiceImpl{
    //Reading data from property file to a list
    @Value("#{'${website.urls}'.split(',')}")
    List<String> urls;


    public void getSatori() throws IOException {
        String url = "https://www.satorireader.com/signin";
        String satoriPassword = "Goaway88";

        Connection.Response res = Jsoup.connect(url)
                .data("username", "rwallaceguitar@gmail.com", "password", "Goaway88")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .method(Connection.Method.POST)
                .followRedirects(true)
                .execute();

        Document doc = Jsoup.connect(String.valueOf(res.url())).get();
        Elements series = doc.select(".tile");
        Elements links = doc.select(".tile-ar a");
        for (Element el : links){
            System.out.println(el);
        }
    }
    public void getTimesheets() throws IOException {
        String loginPageUrl = "https://yorksolutions.bbo.bullhornstaffing.com/Login/";

        // Your login credentials
        String username = "rwallaceguitar@gmail.com";
        String password = "Goaw@y88";

        // Create a POST request with the form data
        Connection.Response loginResponse = Jsoup.connect(loginPageUrl)
                .data("username", username, "password", password)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .method(Connection.Method.POST)
                .followRedirects(true)
                .execute();

        System.out.println(loginResponse.cookies());
        System.out.println(loginResponse.url());
    }

    public Set<ResponseDTO> getVehicleByModel(String vehicleModel) {
        //Using a set here to only store unique elements
        Set<ResponseDTO> responseDTOS = new HashSet<>();
        //Traversing through the urls
        for (String url: urls) {

            if (url.contains("ikman")) {
                //method to extract data from Ikman.lk
                extractDataFromIkman(responseDTOS, url + vehicleModel);
            } else if (url.contains("riyasewana")) {
                //method to extract Data from riyasewana.com
                extractDataFromRiyasewana(responseDTOS, url + vehicleModel);
            }

        }

        StandardEnvironment env = new StandardEnvironment();
        RestTemplate template = new RestTemplate();
        System.out.println("***running***" + env.getProperty("message"));
        String url = "https://webhook.site/91c523d6-7c9a-4217-83b1-5fd9c1e03102";
        HttpEntity<String> http = new HttpEntity<>(env.getProperty("message"), null);

        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, http, String.class);

        return responseDTOS;
    }

    private void extractDataFromRiyasewana(Set<ResponseDTO> responseDTOS, String url) {

        try {
            //loading the HTML to a Document Object
            Document document = Jsoup.connect(url).get();
            //Selecting the element which contains the ad list
            Element element = document.getElementById("content");
            //getting all the <a> tag elements inside the content div tag
            Elements elements = element.getElementsByTag("a");
            //traversing through the elements
            for (Element ads: elements) {
                ResponseDTO responseDTO = new ResponseDTO();

                if (!StringUtils.isEmpty(ads.attr("title")) ) {
                    //mapping data to the model class
                    responseDTO.setTitle(ads.attr("title"));
                    responseDTO.setUrl(ads.attr("href"));
                }
                if (responseDTO.getUrl() != null) responseDTOS.add(responseDTO);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void extractDataFromIkman(Set<ResponseDTO> responseDTOS, String url) {
        try {
            //loading the HTML to a Document Object
            Document document = Jsoup.connect(url).get();
            //Selecting the element which contains the ad list
            Element element = document.getElementsByClass("list--3NxGO").first();
            //getting all the <a> tag elements inside the list-       -3NxGO class
            Elements elements = element.getElementsByTag("a");

            for (Element ads: elements) {

                ResponseDTO responseDTO = new ResponseDTO();

                if (StringUtils.isNotEmpty(ads.attr("href"))) {
                    //mapping data to our model class
                    responseDTO.setTitle(ads.attr("title"));
                    responseDTO.setUrl("https://ikman.lk"+ ads.attr("href"));
                }
                if (responseDTO.getUrl() != null) responseDTOS.add(responseDTO);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void getTimesheetWithSelenium() {
        FirefoxDriver driver = new FirefoxDriver();
        String loginUrl = "https://yorksolutions.bbo.bullhornstaffing.com/Login/";
        driver.get(loginUrl);

        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));

        //todo: replace with admin credentials supplied by Robb
        String username = "jiradmin";
        String password = "FalconSpace533$";

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        WebElement loginButton = driver.findElement(By.xpath("//input[@type='image']"));
        loginButton.click();

        try{
            Thread.sleep(5000);
        }
        catch (InterruptedException e){
            System.out.println(e.getMessage());
        }

        WebElement tab = driver.findElement(By.cssSelector("[data-automation-id=tabTimesheet]"));
        tab.click();

        try{
            Thread.sleep(5000);
        }
        catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
        String newPage = driver.getPageSource();
        Document spreadsheet = Jsoup.parse(newPage);
        Elements rows = spreadsheet.select(".novo-data-table-row");
        for (Element row : rows) {
            Elements placementId = row.select(".novo-column-assignmentIntegrationId span");
            Elements name = row.select(".novo-column-employee span");
            Elements hours = row.select(".novo-column-regularHours span");
            Elements overtime = row.select(".novo-column-overtimeHours span");
            Elements updatedAt = row.select(".novo-column-lastSentToAtsFormatted span");
            Elements processed = row.select(".novo-column-processed span");
            System.out.print(placementId.html() + " ");
            System.out.print(name.html() + " ");
            System.out.print(hours.html() + " ");
            System.out.print(overtime.html() + " ");
            System.out.print(updatedAt.html() + " ");
            System.out.println(processed.html());
            driver.quit();
        }
    }
}
