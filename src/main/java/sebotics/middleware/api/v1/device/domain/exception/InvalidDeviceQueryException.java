package sebotics.middleware.api.v1.device.domain.exception;

public class InvalidDeviceQueryException extends RuntimeException {

    public InvalidDeviceQueryException(String message) {
        super(message);
    }
}
