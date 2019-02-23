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
public class UnableToPersistDataException extends Exception {
    
    public UnableToPersistDataException(String message, String entity) {
        super(new StringBuilder(message).append(entity).toString());
    }
    
    public UnableToPersistDataException(Throwable t){
        super(t);
    }
}
