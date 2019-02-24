/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heycar.test.models.Listing;
import com.heycar.test.models.Provider;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@Slf4j
public final class Util {
    
    private final static Pattern CODE_PATTERN = Pattern.compile("(code.*)",Pattern.CASE_INSENSITIVE);
    private final static Pattern YEAR_PATTERN = Pattern.compile("(year.*)",Pattern.CASE_INSENSITIVE);
    
    private Util() {} // prevent this class from being initialized
    
    /**
     * Converts an object to string
     * @param <T>
     * @param object
     * @return 
     */
    public static <T> String convertObjectToJson(T object) {
        try {
            ObjectMapper objMapper = new ObjectMapper();
            return objMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert object to a string", e);
        }
        return null;
    }
    
    /**
     * Convert a string to object
     * @param <T>
     * @param data
     * @param clazz
     * @return 
     */
    public static <T> T convertStringToObject(String data, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            T obj = mapper.readValue(data, clazz);
            return obj;
        } catch (IOException e) {
            log.error("Unable to parse json string {}", data, e);
        }
        return null;
    }
    
    /**
     * Validates the header for a CSV
     * @param record
     * @return 
     */
    public static boolean isHeaderRow(CSVRecord record) {
        return CODE_PATTERN.matcher(record.get(0)).matches() || YEAR_PATTERN.matcher(record.get(3)).matches();
    }
    
    /**
     * Builds a Listing object
     * @param record
     * @return 
     */
    public static Listing getListing(CSVRecord record, Provider provider) {
        String[] makeModel = record.get(1).split("/");
        Listing listing = new Listing.ListingBuilder().setCode(record.get(0))
                                                      .setColor(record.get(4))
                                                      .setKw(Integer.valueOf(record.get(2)))
                                                      .setMake(makeModel[0])
                                                      .setModel(makeModel[1])
                                                      .setPrice(new BigDecimal(record.get(5)))
                                                      .setProvider(provider)
                                                      .setYear(Integer.valueOf(record.get(3))).build();
        return listing;
    }
    
    public static boolean validFileExtension(MultipartFile file) {
        String extension = file.getOriginalFilename().split("\\.")[1];
        return (null != extension && !extension.trim().isEmpty() && extension.trim().equals("csv"));
    }
}
