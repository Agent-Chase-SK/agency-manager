package cz.muni.fi.pv168.agencymanager.common;


/**
 * Exception indicates service failure.
 */

public class ServiceException extends RuntimeException {

    /**
     * Constructor for ServiceException
     */
    public ServiceException(){}

    /**
     * Constructor for ServiceException
     * @param message with details
     */
    public ServiceException(String message){
        super(message);
    }

    public ServiceException(Throwable cause){
        super(cause);
    }

    public ServiceException(String message, Throwable cause){
        super(message, cause);
    }
}
