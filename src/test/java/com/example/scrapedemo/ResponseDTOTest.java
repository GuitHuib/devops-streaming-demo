package com.example.scrapedemo;

import com.thoughtworks.xstream.XStream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ResponseDTOTest {

    @Test
    public void xstreamShouldParseXmlIntoResponseDto(){
        String xml = "<rootElementName>\n" +
                "  <title>new dto</title>\n" +
                "  <url>url</url>\n" +
                " <dob>some data</dob>\n" +
                "</rootElementName>";

        XStream xStream = new XStream();
        xStream.alias("rootElementName", ResponseDTO.class);
        xStream.ignoreUnknownElements();
        xStream.allowTypesByWildcard(new String[] {
                "com.example.scrapedemo.**",
                "test.other.application.**"
        });

        ResponseDTO dto = (ResponseDTO) xStream.fromXML(xml);
        assertThat(dto.getTitle()).isEqualTo("new dto");
    }
    @Test
    public void xStreamShouldConvertObjectToXml(){
        ResponseDTO dto = new ResponseDTO();
        dto.setTitle("new dto");
        dto.setUrl("url");

        XStream xStream = new XStream();
        String data = xStream.toXML(dto);
        System.out.println(data);
        assertTrue(false);
    }
}