package sebotics.middleware.device.exception;

public class DeviceAlreadyRegisteredException extends RuntimeException {

	public DeviceAlreadyRegisteredException(String message) {
		super(message);
	}
}
