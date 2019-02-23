/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.response;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public final class ResponseFactory {
    
    /**
     * This method creates the response object
     * @param factory
     * @return the response object
     */
    public static Response createResponse(ResponseAbstractFactory factory) {
        return factory.createResponse();
    }
}
