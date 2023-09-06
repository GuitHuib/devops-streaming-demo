package com.example.scrapedemo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class XlsParser {

    public void parse(MultipartFile file) throws IOException {
        //todo: use workbook type that matches bbo data
//        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
        try (Workbook workbook = new HSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet

            for (Row row : sheet) {
                for (Cell cell : row) {
                    String cellValue = cell.toString();
                    System.out.print(cellValue + "\t");
                }
                System.out.println(); // Move to the next row
            }
        }
    }
}
