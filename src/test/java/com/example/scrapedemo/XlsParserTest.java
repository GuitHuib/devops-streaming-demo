package com.example.scrapedemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class XlsParserTest {

    @Autowired
    XlsParser parser;


    @Test
    public void xlsParserShouldParseXlsAndReturnString() throws IOException {
        File file = new File("./file_example_XLS_10.xls");

        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "application/octet-stream", input);

        parser.parse(multipartFile);
//        assertThat(true).isFalse();
    }

}