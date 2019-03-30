package cz.muni.fi.pv168.agencymanager.common;

public class ValidationException extends RuntimeException {

    /**
     * Constructor for ValidationException
     */
    public ValidationException(){}

    /**
     * Constructor for  ValidationException
     * @param message with details
     */
    public ValidationException(String message){
        super(message);
    }
}
