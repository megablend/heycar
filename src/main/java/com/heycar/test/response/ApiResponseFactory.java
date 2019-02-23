/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.response;

import lombok.AllArgsConstructor;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
@AllArgsConstructor
public class ApiResponseFactory implements ResponseAbstractFactory {

    private final String responseCode;
    private final Object responseMessage;
    
    @Override
    public Response createResponse() {
        return new ApiResponse(responseCode, responseMessage);
    }
    
}
