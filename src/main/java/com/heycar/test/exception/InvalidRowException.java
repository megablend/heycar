/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heycar.test.exception;

/**
 *
 * @author Megafu Charles <noniboycharsy@gmail.com>
 */
public class InvalidRowException extends Exception {
    
    public InvalidRowException(String message) {
        super(message);
    }
    
    public InvalidRowException(Throwable t){
        super(t);
    }
}
