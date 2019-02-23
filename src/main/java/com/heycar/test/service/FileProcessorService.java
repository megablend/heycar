/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.service;

import com.heycar.test.models.Provider;
import java.io.InputStream;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public interface FileProcessorService {
    /**
     * Process the CSV content
     * @param is
     * @param provider 
     */
    void processFile(InputStream is, Provider provider) throws Exception;
}
